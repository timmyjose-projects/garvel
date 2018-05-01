package com.tzj.garvel.core.builder.jar;

import com.tzj.garvel.core.builder.api.jar.JarFileCreator;
import com.tzj.garvel.core.builder.api.jar.JarFileCreatorOptions;
import com.tzj.garvel.core.builder.common.JarFileCreatorFileVisitor;
import com.tzj.garvel.core.builder.api.exception.JarFileCreationException;
import org.w3c.dom.Attr;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class NormalJarFileCreator implements JarFileCreator {
    /**
     * Create the JAR File.
     *
     * @param buildDirPath
     * @param options
     * @return
     * @throws JarFileCreationException
     */
    @Override
    public Path createJarFile(final Path buildDirPath, final JarFileCreatorOptions options) throws JarFileCreationException {
        Manifest mf = getManifest(options);

        JarOutputStream jarStream = null;
        try {
            jarStream = new JarOutputStream(new FileOutputStream(options.getJarFileName()), mf);
        } catch (IOException e) {
            throw new JarFileCreationException(String.format("Unable to create the JAR file: %s", e.getLocalizedMessage()));
        }
        final Set<FileVisitOption> opts = new HashSet<>();
        opts.add(FileVisitOption.FOLLOW_LINKS);

        final URI buildDirUri = URI.create(buildDirPath.toFile().getAbsolutePath());
        try {
            Files.walkFileTree(buildDirPath, opts, Integer.MAX_VALUE, new JarFileCreatorFileVisitor(jarStream, buildDirUri));
        } catch (IOException e) {
            throw new JarFileCreationException(String.format("Unable to create the JAR file: %s", e.getLocalizedMessage()));
        }

        try {
            jarStream.close();
        } catch (IOException e) {
            throw new JarFileCreationException(String.format("Unable to create the JAR file: %s", e.getLocalizedMessage()));
        }

        return Paths.get(options.getJarFileName());
    }

    /**
     * Fill in the manifest file with all the specified and required attributes.
     *
     * @param options
     * @return
     */
    private Manifest getManifest(final JarFileCreatorOptions options) {
        Manifest mf = new Manifest();

        mf.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, options.getManifestVersion());

        if (options.getMainClass() != null) {
            mf.getMainAttributes().put(Attributes.Name.MAIN_CLASS, options.getMainClass());
        }

        mf.getMainAttributes().put(Attributes.Name.CLASS_PATH, options.getClassPathString());

        return mf;
    }
}
