package com.tzj.garvel.core.dep.parser.pom;

import com.tzj.garvel.common.util.UtilServiceImpl;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.dep.api.Artifact;
import com.tzj.garvel.core.dep.api.exception.DependencyManagerException;
import com.tzj.garvel.core.dep.api.parser.Dependencies;
import com.tzj.garvel.core.dep.api.parser.DependencyParser;
import com.tzj.garvel.core.dep.api.parser.Versions;
import com.tzj.garvel.core.dep.api.repo.RepositoryConstants;
import com.tzj.garvel.core.filesystem.api.FilesystemConstants;
import com.tzj.garvel.core.net.api.exception.NetworkServiceException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Retrieve the POM file for a particular dependency, and explore its
 * own transitive dependencies.
 */
public class DependencyPOMParser extends DependencyParser {
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
     * Download the MD5 and SHA1 files into the System temporary directory.
     * contents.
     *
     * @throws DependencyManagerException
     */
    private void download() throws DependencyManagerException {
        try {
            CoreModuleLoader.INSTANCE.getNetworkFramework().downloadTextFile(pomUrl, FilesystemConstants.TMPDIR);
        } catch (NetworkServiceException e) {
            throw new DependencyManagerException(String.format("unable to download the POM file (%s) for validation\n", pomUrl));
        }
    }

    /**
     * Validate the MD5 and SHA1 hashes against the downloaded file's generated hashes.
     */
    private Path validate() throws DependencyManagerException {
        final String pomFileName = pomUrl.substring(pomUrl.lastIndexOf("/"), pomUrl.length());
        final Path pomFilePath = Paths.get(FilesystemConstants.TMPDIR + File.separator + pomFileName);

        if (!UtilServiceImpl.INSTANCE.pathExists(pomFilePath)) {
            throw new DependencyManagerException(String.format("unable to validate - POM file %s could not be found\n", pomFilePath.toFile().getAbsolutePath()));
        }

        final String metadataMD5 = UtilServiceImpl.INSTANCE.getMD5(pomFilePath);
        final String metadataSHA1 = UtilServiceImpl.INSTANCE.getSHA1(pomFilePath);

        try {
            final String md5HashFull = CoreModuleLoader.INSTANCE.getNetworkFramework().downloadTextFileAsString(pomMD5Url);
            final String sha1HashFull = CoreModuleLoader.INSTANCE.getNetworkFramework().downloadTextFileAsString(pomSHA1Url);

            // some Hash files tend to contain extra text. so split at the first whitespace
            final String md5Hash = md5HashFull.split(" ")[0];
            final String sha1Hash = sha1HashFull.split(" ")[0];

            if (metadataMD5 == null || metadataSHA1 == null || md5Hash == null || sha1Hash == null) {
                throw new DependencyManagerException(String.format("POM file validation failed: hashes do not match for url: %s", pomUrl));
            }

            if (!metadataMD5.equalsIgnoreCase(md5Hash) || !sha1Hash.equalsIgnoreCase(sha1Hash)) {
                throw new DependencyManagerException(String.format("POM file validation failed: hashes do not match for url: %s", pomUrl));
            }
        } catch (NetworkServiceException e) {
            throw new DependencyManagerException(String.format("failed to download the MD5 and/or SHA1 hash files for validation for url: %s", pomUrl));
        }

        return pomFilePath;
    }


    /**
     * Parse the contents of the POM file and retrieve the available dependencies.
     *
     * @throws DependencyManagerException
     */
    @Override
    public void parse() throws DependencyManagerException {
        try {
            download();
            final Path pomPath = validate();

            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();

            final Document root = builder.parse(pomPath.toFile());
            final NodeList projectItems = root.getElementsByTagName("project").item(0).getChildNodes();
            Node dependencies = null;

            for (int i = 0; i < projectItems.getLength(); i++) {
                if (projectItems.item(i).getNodeName().equalsIgnoreCase("dependencies")) {
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
                    addDep(deps.item(i));
                }
            }
        } catch (ParserConfigurationException e) {
            throw new DependencyManagerException(String.format("Failed to parse the POM file at %s\n", pomUrl));
        } catch (SAXException | IOException e) {
            throw new DependencyManagerException(String.format("Failed to parse the POM file at %s\n", pomUrl));
        }
    }

    /**
     * Add a dependency to the list of dependencies.
     *
     * @param item
     */
    private void addDep(final Node item) {
        final NodeList items = item.getChildNodes();
        String groupId = null;
        String artifactId = null;
        String version = null;

        for (int i = 0; i < items.getLength(); i++) {
            if (items.item(i).getNodeName().equalsIgnoreCase("groupId")) {
                groupId = items.item(i).getTextContent();
            } else if (items.item(i).getNodeName().equalsIgnoreCase("artifactId")) {
                artifactId = items.item(i).getTextContent();
            } else if (items.item(i).getNodeName().equalsIgnoreCase("version")) {
                version = items.item(i).getTextContent();
            }
        }

        if (groupId != null & artifactId != null && version != null) {
            final Artifact newDep = new Artifact(groupId, artifactId, version);
            dependencies.getDependencies().add(newDep);
        }
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
