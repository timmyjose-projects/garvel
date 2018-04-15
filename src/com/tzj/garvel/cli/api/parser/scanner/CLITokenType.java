package com.tzj.garvel.cli.api.parser.scanner;

import java.util.HashMap;
import java.util.Map;

public enum CLITokenType {
    IDENTIFIER("<identifier>"),
    VERBOSE("--verbose"),
    GARVEL("garvel"),
    VCS("--vcs"),
    QUIET("--quiet"),
    BIN("--bin"),
    LIB("--lib"),
    HELP("help"),
    VERSION("version"),
    LIST("list"),
    NEW("new"),
    BUILD("build"),
    CLEAN("clean"),
    RUN("run"),
    TEST("test"),
    EOT("<eot>"),
    UNKNOWN("<unknown>");

    private static final Map<String, CLITokenType> keywordMap;

    static {
        keywordMap = new HashMap<>();

        keywordMap.put(GARVEL.description, GARVEL);
        keywordMap.put("-v", VERBOSE);
        keywordMap.put(VERBOSE.description, VERBOSE);
        keywordMap.put("-q", QUIET);
        keywordMap.put(QUIET.description, QUIET);
        keywordMap.put(VCS.description, VCS);
        keywordMap.put(BIN.description, BIN);
        keywordMap.put(LIB.description, LIB);
        keywordMap.put(HELP.description, HELP);
        keywordMap.put(VERSION.description, VERSION);
        keywordMap.put(LIST.description, LIST);
        keywordMap.put(NEW.description, NEW);
        keywordMap.put(BUILD.description, BUILD);
        keywordMap.put(CLEAN.description, CLEAN);
        keywordMap.put(RUN.description, RUN);
        keywordMap.put(TEST.description, TEST);
    }

    private String description;

    private CLITokenType(final String description) {
        this.description = description;
    }

    public static boolean isKeyword(final String spelling) {
        return keywordMap.containsKey(spelling);
    }

    public static CLITokenType getKeyword(final String spelling) {
        if (!keywordMap.containsKey(spelling)) {
            return UNKNOWN;
        }
        return keywordMap.get(spelling);
    }

    @Override
    public String toString() {
        return description;
    }
}
