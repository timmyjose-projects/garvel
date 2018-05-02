package com.tzj.garvel.core.dep.parser.metadata;

import com.tzj.garvel.core.dep.api.exception.DependencyManagerException;
import com.tzj.garvel.core.dep.api.parser.*;
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
 * Retrieves and parses the metadata for the project.
 */
public class DependencyMetadataParser extends DependencyParser {
    private String metadataUrl;
    private String metadataMD5Url;
    private String metadataSHA1Url;
    private Versions versions;

    public DependencyMetadataParser(final String metadataUrl) {
        this.versions = new Versions();
        this.metadataUrl = metadataUrl;
        this.metadataMD5Url = this.metadataUrl + RepositoryConstants.MD5;
        this.metadataSHA1Url = this.metadataUrl + RepositoryConstants.SHA1;
    }

    /**
     * Parse the contents of the metadata file and retrieve the available versions.
     *
     * @throws DependencyManagerException
     * @param repoLoader
     */
    @Override
    public void parse(final RepositoryLoader repoLoader) throws DependencyManagerException {
        try {
            download(DependencyParserConstants.METADATA, metadataUrl, FilesystemConstants.TMPDIR);
            final Path metadataPath = validate(DependencyParserConstants.METADATA, metadataUrl,
                    metadataMD5Url, metadataSHA1Url, FilesystemConstants.TMPDIR);

            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();

            final Document root = builder.parse(metadataPath.toFile());
            final NodeList versioningTags = root.getElementsByTagName(DependencyParserConstants.VERSIONING);

            if (versioningTags.getLength() != 1) {
                return; // no version information available
            }

            final Node versionTag = versioningTags.item(0);
            populateVersionInfo(versionTag);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new DependencyManagerException(String.format("Failed to parse the metadata file at %s\n", metadataUrl));
        }
    }

    private void populateVersionInfo(final Node versionTag) {
        final NodeList children = versionTag.getChildNodes();

        if (children.getLength() == 0) {
            return;
        }

        // check for latest version
        final Node latestTag = findTagAmongstChildren(children, DependencyParserConstants.LATEST);
        if (latestTag != null) {
            versions.setLatestVersion(latestTag.getTextContent());
        }

        // check for release version
        final Node releaseTag = findTagAmongstChildren(children, DependencyParserConstants.RELEASE);
        if (releaseTag != null) {
            versions.setReleaseVersion(releaseTag.getTextContent());
        }

        // check for the available versions.
        final Node availableVersionsTag = findTagAmongstChildren(children, DependencyParserConstants.VERSIONS);
        if (availableVersionsTag != null) {
            final NodeList versionsTags = availableVersionsTag.getChildNodes();

            if (versionsTags != null) {
                for (int i = 0; i < versionsTags.getLength(); i++) {
                    if (versionsTags.item(i) != null && versionsTags.item(i).getNodeName() != null
                            && versionsTags.item(i).getNodeName().equalsIgnoreCase(DependencyParserConstants.VERSION)) {
                        versions.addAvailableVersion(versionsTags.item(i).getTextContent());
                    }
                }
            }
        }
    }

    @Override
    public Versions getVersions() {
        return versions;
    }

    @Override
    public Dependencies getDependencies() {
        throw new UnsupportedOperationException("getDependencies is not supported by DepMetadataParser");
    }
}
