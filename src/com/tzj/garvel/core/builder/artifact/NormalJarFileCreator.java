package com.tzj.garvel.core.builder.artifact;

import com.tzj.garvel.core.builder.api.JarFileCreatorService;
import com.tzj.garvel.core.builder.api.JarFileFields;
import com.tzj.garvel.core.builder.common.JarFileCreatorFileVisitor;
import com.tzj.garvel.core.builder.exception.JarFileCreationException;

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

public class NormalJarFileCreator implements JarFileCreatorService {
    private Path buildDirPath;
    private JarFileFields fields;

    public NormalJarFileCreator(final Path buildDirPath, final JarFileFields fields) {
        this.buildDirPath = buildDirPath;
        this.fields = fields;
    }

    @Override
    public Path createJarFile() throws JarFileCreationException {
        Manifest mf = getManifest();

        JarOutputStream jarStream = null;
        try {
            jarStream = new JarOutputStream(new FileOutputStream(fields.getJarFileName()), mf);
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

        return Paths.get(fields.getJarFileName());
    }

    private Manifest getManifest() {
        Manifest mf = new Manifest();
        mf.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, fields.getManifestVersion());
        mf.getMainAttributes().put(Attributes.Name.MAIN_CLASS, fields.getMainClass());

        return mf;
    }
}
