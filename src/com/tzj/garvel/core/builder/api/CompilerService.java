package com.tzj.garvel.core.builder.api;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public interface CompilerService {
    Compiler getCompiler(final CompilerType type, final Path buildDirPath, final List<File> srcFiles);
}
