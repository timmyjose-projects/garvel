package com.tzj.garvel.core.dep.api.parser;

import com.tzj.garvel.common.util.UtilServiceImpl;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.dep.api.exception.DependencyManagerException;
import com.tzj.garvel.core.dep.api.repo.RepositoryLoader;
import com.tzj.garvel.core.net.api.exception.NetworkServiceException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class DependencyParser {
    public abstract void parse(final RepositoryLoader repoLoader) throws DependencyManagerException;

    public abstract Versions getVersions();

    public abstract Dependencies getDependencies();

    /**
     * Download the MD5 and SHA1 files into the given directory.
     * contents.
     *
     * @throws DependencyManagerException
     */
    protected void download(final String kind, final String url, final String dir) throws DependencyManagerException {
        try {
            CoreModuleLoader.INSTANCE.getNetworkFramework().downloadTextFile(url, dir);
        } catch (NetworkServiceException e) {
            // @TODO remove this with a robust check
            if (url.contains("SNAPSHOT")) {
                return;
            }
            throw new DependencyManagerException(String.format("unable to download the %s file (%s) for validation.\n", kind, url));
        }
    }

    /**
     * Validate the MD5 and SHA1 hashes against the downloaded file's generated hashes.
     */
    protected Path validate(final String kind, final String url, final String md5Url, final String sha1Url, final String dir) throws DependencyManagerException {
        final String filename = url.substring(url.lastIndexOf("/"), url.length());
        final Path filePath = Paths.get(dir + File.separator + filename);

        if (!UtilServiceImpl.INSTANCE.pathExists(filePath)) {
            throw new DependencyManagerException(String.format("unable to validate - %s file %s could not be found\n",
                    kind, filePath.toFile().getAbsolutePath()));
        }

        final String fileMD5 = UtilServiceImpl.INSTANCE.getMD5(filePath);
        final String fileSHA1 = UtilServiceImpl.INSTANCE.getSHA1(filePath);

        try {
            final String md5HashFull = CoreModuleLoader.INSTANCE.getNetworkFramework().downloadTextFileAsString(md5Url);
            final String sha1HashFull = CoreModuleLoader.INSTANCE.getNetworkFramework().downloadTextFileAsString(sha1Url);

            // some Hash files tend to contain extra text. so split at the first whitespace
            final String md5Hash = md5HashFull.split(" ")[0];
            final String sha1Hash = sha1HashFull.split(" ")[0];

            if (fileMD5 == null || fileSHA1 == null || md5Hash == null || sha1Hash == null) {
                throw new DependencyManagerException(String.format("%s file validation failed: hashes do not match for url: %s",
                        kind, url));
            }

            if (!fileMD5.equalsIgnoreCase(md5Hash) || !sha1Hash.equalsIgnoreCase(sha1Hash)) {
                throw new DependencyManagerException(String.format("%s file validation failed: hashes do not match for url: %s",
                        kind, url));
            }
        } catch (NetworkServiceException e) {
            throw new DependencyManagerException(String.format("failed to download the MD5 and/or SHA1 hash files for validation for url: %s", url));
        }

        return filePath;
    }


    /**
     * Given the list of child nodes, try and locate a specific tag using its name.
     *
     * @param children
     * @param nodeName
     * @return
     */
    protected final Node findTagAmongstChildren(final NodeList children, final String nodeName) {
        if (children == null || children.getLength() == 0) {
            return null;
        }

        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) != null && children.item(i).getNodeName() != null && children.item(i).getNodeName().equalsIgnoreCase(nodeName)) {
                return children.item(i);
            }
        }

        return null;
    }
}
