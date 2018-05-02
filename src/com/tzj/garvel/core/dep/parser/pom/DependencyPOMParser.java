package com.tzj.garvel.core.dep.parser.pom;

import com.tzj.garvel.core.dep.api.Artifact;
import com.tzj.garvel.core.dep.api.exception.DependencyManagerException;
import com.tzj.garvel.core.dep.api.exception.RepositoryLoaderException;
import com.tzj.garvel.core.dep.api.parser.Dependencies;
import com.tzj.garvel.core.dep.api.parser.DependencyParser;
import com.tzj.garvel.core.dep.api.parser.Versions;
import com.tzj.garvel.core.dep.api.repo.RepositoryConstants;
import com.tzj.garvel.core.dep.api.repo.RepositoryLoader;
import com.tzj.garvel.core.dep.parser.DependencyParserConstants;
import com.tzj.garvel.core.filesystem.api.FilesystemConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Retrieve the POM file for a particular dependency, and explore its
 * own transitive dependencies.
 */
public class DependencyPOMParser extends DependencyParser {
    private static final String DOLLAR = "$";

    private String pomUrl;
    private String pomMD5Url;
    private String pomSHA1Url;
    private Dependencies dependencies;

    public DependencyPOMParser(final String pomUrl) {
        this.pomUrl = pomUrl;
        this.pomMD5Url = pomUrl + RepositoryConstants.MD5;
        this.pomSHA1Url = pomUrl + RepositoryConstants.SHA1;
        this.dependencies = new Dependencies();
    }

    /**
     * Parse the contents of the POM file and retrieve the available dependencies.
     * <p>
     * Dependency parsing algorithm:
     * <p>
     * 1. For the given artifact, locate its `dependencies` section (under `project`).
     * 2. Add each `dependency` node to the list of dependencies.
     * 3. If the version is given as a ${var.value} instead, try and locate the
     * `properties` section in the current document. Failing that (of if the variable
     * was not found in this section), try and locate the `parent` section of the
     * current document. Then seek the variable in the parent POM's `properties`
     * section.
     * 4. If, for a given dependency, no `version` tag is available, then query the
     * parent POM's `dependencyManagement` section for the version. Again, if a
     * ${var.value} is encountered, resolve it as in step 3.
     *
     * @param repoLoader
     * @throws DependencyManagerException
     */
    @Override
    public void parse(final RepositoryLoader repoLoader) throws DependencyManagerException {
        try {
            download(DependencyParserConstants.POM, pomUrl, FilesystemConstants.TMPDIR);
            final Path pomPath = validate(DependencyParserConstants.POM, pomUrl, pomMD5Url, pomSHA1Url, FilesystemConstants.TMPDIR);

            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();

            final Document root = builder.parse(pomPath.toFile());
            final Node projectNode = root.getElementsByTagName(DependencyParserConstants.PROJECT).item(0);
            final NodeList projectItems = projectNode.getChildNodes();
            Node dependencies = null;

            for (int i = 0; i < projectItems.getLength(); i++) {
                if (projectItems.item(i).getNodeName().equalsIgnoreCase(DependencyParserConstants.DEPENDENCIES)) {
                    dependencies = projectItems.item(i);
                    break;
                }
            }

            // no dependencies is not an error
            if (dependencies == null) {
                return;
            }

            final NodeList deps = dependencies.getChildNodes();
            for (int i = 0; i < deps.getLength(); i++) {
                if (deps.item(i) != null) {
                    addDep(repoLoader, projectNode, deps.item(i));
                }
            }
        } catch (ParserConfigurationException e) {
            // @TODO remove this with a robust check
            if (e.getLocalizedMessage().contains("SNAPSHOT")) {
                return;
            }

            throw new DependencyManagerException(String.format("Failed to parse the POM file at %s\n", pomUrl));
        } catch (SAXException | IOException e) {
            // @TODO remove this with a robust check
            if (e.getLocalizedMessage().contains("SNAPSHOT")) {
                return;
            }

            throw new DependencyManagerException(String.format("Failed to parse the POM file at %s\n", pomUrl));
        }
    }

    /**
     * Add a dependency to the list of dependencies.
     *
     * @param repoLoader
     * @param projectNode
     * @param item
     */
    private void addDep(final RepositoryLoader repoLoader, final Node projectNode, final Node item) throws DependencyManagerException {
        final NodeList items = item.getChildNodes();
        String groupId = null;
        String artifactId = null;
        String version = null;

        boolean versionTagFound = false;
        for (int i = 0; i < items.getLength(); i++) {
            final Node node = items.item(i);

            if (node.getNodeName() != null && node.getNodeName().equalsIgnoreCase(DependencyParserConstants.GROUPID)) {
                groupId = node.getTextContent();
            } else if (node.getNodeName() != null && node.getNodeName().equalsIgnoreCase(DependencyParserConstants.ARTIFACTID)) {
                artifactId = node.getTextContent();
            } else if (node.getNodeName() != null && node.getNodeName().equalsIgnoreCase(DependencyParserConstants.VERSION)) {
                versionTagFound = true;
                version = node.getTextContent();

                if (version != null && version.startsWith(DOLLAR)) {
                    version = strip(version);
                    version = resolveVersionVariable(repoLoader, projectNode, version);
                }
            }
        }

        if (groupId != null && artifactId != null && !versionTagFound) {
            version = resolveMissingVersion(repoLoader, projectNode, groupId, artifactId);
        }

        if (groupId != null & artifactId != null && version != null) {
            final Artifact newDep = new Artifact(groupId, artifactId, version);
            dependencies.getDependencies().add(newDep);
        }
    }

    /**
     * Convert a ${project.var} to `project.var` to be used for locating
     * the tag.
     *
     * @param version
     * @return
     */
    private String strip(final String version) {
        if (version.startsWith("${")) {
            return version.substring(2, version.length() - 1);
        } else if (version.startsWith("$")) {
            return version.substring(1, version.length() - 1);
        }

        return version;
    }

    /**
     * If the `version` tag itself is missing, it may be due to the version
     * having been specified in one of the parent POM's `dependencyManagement` section.
     * Again, recurse till no parent node is found.
     *
     * @param repoLoader
     * @param projectNode
     * @param depGroupId
     * @param depArtifactId
     * @return
     */
    private String resolveMissingVersion(final RepositoryLoader repoLoader, final Node projectNode, final String depGroupId, final String depArtifactId)
            throws DependencyManagerException {
        // base case
        if (projectNode == null) {
            return null;
        }

        // check parent's dependencyManagement section for version information
        final Node parentNode = findTagAmongstChildren(projectNode.getChildNodes(), DependencyParserConstants.PARENT);
        final Artifact parentArtifact = constructArtifact(parentNode);

        try {
            final String parentPOMUrl = repoLoader.constructPOMUrl(parentArtifact.getGroupId(), parentArtifact.getArtifactId(), parentArtifact.getVersion());
            final String parentPOMMD5Url = parentPOMUrl + RepositoryConstants.MD5;
            final String parentSHA1Url = parentPOMUrl + RepositoryConstants.SHA1;

            download(DependencyParserConstants.POM, parentPOMUrl, FilesystemConstants.TMPDIR);
            final Path parentPOMPath = validate(DependencyParserConstants.POM, parentPOMUrl,
                    parentPOMMD5Url, parentSHA1Url, FilesystemConstants.TMPDIR);

            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();

            final Document root = builder.parse(parentPOMPath.toFile());
            final Node parentProjectNode = root.getElementsByTagName(DependencyParserConstants.PROJECT).item(0);

            // now check in the parent's dependencyManagement section
            final Node dependencyManagementNode = findTagAmongstChildren(parentProjectNode.getChildNodes(),
                    DependencyParserConstants.DEPENDENCY_MANAGEMENT);

            if (dependencyManagementNode != null) {
                final NodeList depMgmtChildNodes = dependencyManagementNode.getChildNodes();
                for (int i = 0; i < depMgmtChildNodes.getLength(); i++) {
                    final Node depMgmtChild = depMgmtChildNodes.item(i);
                    if (depMgmtChild != null) {
                        final String depMgmtChildName = depMgmtChild.getNodeName();
                        if (depMgmtChildName != null && depMgmtChildName.equalsIgnoreCase(DependencyParserConstants.DEPENDENCIES)) {
                            final NodeList dependencies = depMgmtChild.getChildNodes();
                            if (dependencies != null) {
                                for (int j = 0; j < dependencies.getLength(); j++) {
                                    final Node dependency = dependencies.item(j);
                                    if (dependency != null) {
                                        final NodeList depFields = dependency.getChildNodes();
                                        if (depFields != null && depFields.getLength() != 0) {
                                            String groupId = null;
                                            String artifactId = null;
                                            String version = null;
                                            for (int k = 0; k < depFields.getLength(); k++) {
                                                final Node depField = depFields.item(k);
                                                if (depField != null && depField.getNodeName() != null) {
                                                    final String depName = depField.getNodeName();
                                                    if (depName.equalsIgnoreCase(DependencyParserConstants.GROUPID)) {
                                                        groupId = depField.getTextContent();
                                                    } else if (depName.equalsIgnoreCase(DependencyParserConstants.ARTIFACTID)) {
                                                        artifactId = depField.getTextContent();
                                                    } else if (depName.equalsIgnoreCase(DependencyParserConstants.VERSION)) {
                                                        version = depField.getTextContent();
                                                    }
                                                }
                                            }
                                            if (groupId != null && groupId.equalsIgnoreCase(depGroupId) &&
                                                    artifactId != null && artifactId.equalsIgnoreCase(depArtifactId)) {
                                                final String depVersion = version;

                                                if (depVersion != null && depVersion.startsWith(DOLLAR)) {
                                                    version = strip(version);
                                                    return resolveVersionVariable(repoLoader, parentProjectNode, depVersion);
                                                } else {
                                                    return depVersion;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return resolveMissingVersion(repoLoader, parentProjectNode, depGroupId, depArtifactId);
        } catch (RepositoryLoaderException | ParserConfigurationException | SAXException | IOException e) {
            // @TODO remove this with fallback schemes
            if (e.getLocalizedMessage().contains("SNAPSHOT")) {
                return null;
            }

            throw new DependencyManagerException(String.format("Failed to resolve the parent POM file: %s", e.getLocalizedMessage()));
        }
    }

    /**
     * The given version string is a ${project.variable}, and so we need to do the following:
     * <p>
     * 1. Query the current POM for the `properties` section. If found, return.
     * 2. If there is a `parent` tag, download the parent POM, and find its
     * `properties` section and query for the env var there.
     * <p>
     * Note that there can be any number of levels of ancestor nodes that
     * we might have to query to resolve the var.
     *
     * @param repoLoader
     * @param projectNode
     * @param version
     * @return
     */
    private String resolveVersionVariable(final RepositoryLoader repoLoader, final Node projectNode, final String version) throws DependencyManagerException {
        // base case
        if (projectNode == null) {
            return null;
        }

        // check for local properties
        final Node propertiesNode = findTagAmongstChildren(projectNode.getChildNodes(), DependencyParserConstants.PROPERTIES);

        if (propertiesNode != null) {
            final NodeList properties = propertiesNode.getChildNodes();
            if (properties != null) {
                for (int i = 0; i < properties.getLength(); i++) {
                    final Node property = properties.item(i);
                    if (property != null && property.getNodeName() != null) {
                        final String nodeName = property.getNodeName();
                        // found a match
                        if (nodeName != null && nodeName.equalsIgnoreCase(version)) {
                            return property.getTextContent();
                        }
                    }
                }
            }
        }

        // otherwise check for parent properties
        final Node parentNode = findTagAmongstChildren(projectNode.getChildNodes(), DependencyParserConstants.PARENT);
        final Artifact parentArtifact = constructArtifact(parentNode);

        try {
            final String parentPOMUrl = repoLoader.constructPOMUrl(parentArtifact.getGroupId(), parentArtifact.getArtifactId(), parentArtifact.getVersion());
            final String parentPOMMD5Url = parentPOMUrl + RepositoryConstants.MD5;
            final String parentSHA1Url = parentPOMUrl + RepositoryConstants.SHA1;

            download(DependencyParserConstants.POM, parentPOMUrl, FilesystemConstants.TMPDIR);
            final Path parentPOMPath = validate(DependencyParserConstants.POM, parentPOMUrl,
                    parentPOMMD5Url, parentSHA1Url, FilesystemConstants.TMPDIR);

            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();

            final Document root = builder.parse(parentPOMPath.toFile());
            final Node parentProjectNode = root.getElementsByTagName(DependencyParserConstants.PROJECT).item(0);


            return resolveVersionVariable(repoLoader, parentProjectNode, version);
        } catch (RepositoryLoaderException | ParserConfigurationException | SAXException | IOException e) {
            // @TODO remove this with fallback schemes
            if (e.getLocalizedMessage().contains("SNAPSHOT")) {
                return null;
            }
            throw new DependencyManagerException(String.format("Failed to resolve the parent POM file: %s", e.getLocalizedMessage()));
        }
    }

    private Artifact constructArtifact(final Node parentNode) {
        final NodeList items = parentNode.getChildNodes();
        String groupId = null;
        String artifactId = null;
        String version = null;

        for (int i = 0; i < items.getLength(); i++) {
            final Node node = items.item(i);

            if (node.getNodeName() != null && node.getNodeName().equalsIgnoreCase(DependencyParserConstants.GROUPID)) {
                groupId = node.getTextContent();
            } else if (node.getNodeName() != null && node.getNodeName().equalsIgnoreCase(DependencyParserConstants.ARTIFACTID)) {
                artifactId = node.getTextContent();
            } else if (node.getNodeName() != null && node.getNodeName().equalsIgnoreCase(DependencyParserConstants.VERSION)) {
                version = node.getTextContent();
            }
        }

        Artifact artifact = null;
        if (groupId != null & artifactId != null && version != null) {
            artifact = new Artifact(groupId, artifactId, version);
        }

        return artifact;
    }

    @Override
    public Versions getVersions() {
        throw new UnsupportedOperationException("getVersion is not supported by DependencyPOMParser");
    }

    @Override
    public Dependencies getDependencies() {
        return dependencies;
    }

    public void setDependencies(final Dependencies dependencies) {
        this.dependencies = dependencies;
    }
}
