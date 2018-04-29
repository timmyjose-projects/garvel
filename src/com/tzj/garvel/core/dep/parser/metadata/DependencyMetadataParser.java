package com.tzj.garvel.core.dep.parser.metadata;

import com.tzj.garvel.common.util.UtilServiceImpl;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.dep.api.exception.DependencyManagerException;
import com.tzj.garvel.core.dep.api.parser.*;
import com.tzj.garvel.core.dep.api.repo.RepositoryConstants;
import com.tzj.garvel.core.dep.api.repo.RepositoryKind;
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

    // test
    public static void main(String[] args) throws DependencyManagerException {
        final String url = RepositoryKind.CENTRAL.getUrl() + File.separator + "junit/junit/maven-metadata.xml";
        DependencyParser parser = DependencyParserFactory.getParser(DependencyParserKind.METADATA, url);
        parser.parse();
    }

    /**
     * Download the MD5 and SHA1 files into the System temporary directory.
     * contents.
     *
     * @throws DependencyManagerException
     */
    private void download() throws DependencyManagerException {
        try {
            CoreModuleLoader.INSTANCE.getNetworkFramework().downloadTextFile(metadataUrl, FilesystemConstants.TMPDIR);
        } catch (NetworkServiceException e) {
            throw new DependencyManagerException(String.format("unable to download the metadata file (%s) for validation\n", metadataUrl));
        }
    }

    /**
     * Validate the MD5 and SHA1 hashses against the downloaded file's generated hashes.
     */
    private Path validate() throws DependencyManagerException {
        final Path metadataFilePath = Paths.get(FilesystemConstants.TMPDIR + File.separator + RepositoryConstants.METADATA);

        if (!UtilServiceImpl.INSTANCE.pathExists(metadataFilePath)) {
            throw new DependencyManagerException(String.format("unable to validate - metadata file %s could not be found\n", metadataFilePath.toFile().getAbsolutePath()));
        }

        final String metadataMD5 = UtilServiceImpl.INSTANCE.getMD5(metadataFilePath);
        final String metadataSHA1 = UtilServiceImpl.INSTANCE.getSHA1(metadataFilePath);

        try {
            final String md5Hash = CoreModuleLoader.INSTANCE.getNetworkFramework().downloadTextFileAsString(metadataMD5Url);
            final String sha1Hash = CoreModuleLoader.INSTANCE.getNetworkFramework().downloadTextFileAsString(metadataSHA1Url);

            if (metadataMD5 == null || metadataSHA1 == null || md5Hash == null || sha1Hash == null) {
                throw new DependencyManagerException("metadata file validation failed: hashes do not match");
            }

            if (!metadataMD5.equalsIgnoreCase(md5Hash) || !sha1Hash.equalsIgnoreCase(sha1Hash)) {
                throw new DependencyManagerException("metadata file validation failed: hashes do not match");
            }
        } catch (NetworkServiceException e) {
            throw new DependencyManagerException("failed to download the MD5 and/or SHA1 hash files for validation\n");
        }

        return metadataFilePath;
    }


    /**
     * Parse the contents of the metadata file and retrieve the available versions.
     *
     * @throws DependencyManagerException
     */
    @Override
    public void parse() throws DependencyManagerException {
        try {
            download();
            final Path metadataPath = validate();

            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();

            final Document root = builder.parse(metadataPath.toFile());
            final NodeList versioningTags = root.getElementsByTagName("versioning");

            if (versioningTags.getLength() != 1) {
                return; // no version information available
            }

            final Node versionTag = versioningTags.item(0);
            populateVersionInfo(versionTag);
        } catch (ParserConfigurationException e) {
            throw new DependencyManagerException(String.format("Failed to parse the metadata file at %s\n", metadataUrl));
        } catch (SAXException | IOException e) {
            throw new DependencyManagerException(String.format("Failed to parse the metadata file at %s\n", metadataUrl));
        }
    }

    private void populateVersionInfo(final Node versionTag) {
        final NodeList children = versionTag.getChildNodes();

        if (children.getLength() == 0) {
            return;
        }

        // check for latest version
        final Node latestTag = findTagAmongstChildren(children, "latest");
        if (latestTag != null) {
            versions.setLatestVersion(latestTag.getTextContent());
        }

        // check for release version
        final Node releaseTag = findTagAmongstChildren(children, "release");
        if (releaseTag != null) {
            versions.setReleaseVersion(releaseTag.getTextContent());
        }

        // check for the available versions.
        final Node availableVersionsTag = findTagAmongstChildren(children, "versions");
        if (availableVersionsTag != null) {
            final NodeList versionsTags = availableVersionsTag.getChildNodes();

            if (versionsTags != null) {
                for (int i = 0; i < versionsTags.getLength(); i++) {
                    if (versionsTags.item(i) != null && versionsTags.item(i).getNodeName() != null && versionsTags.item(i).getNodeName().equalsIgnoreCase("version")) {
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
