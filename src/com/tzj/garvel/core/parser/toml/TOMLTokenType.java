package com.tzj.garvel.core.parser.toml;

import java.util.HashMap;
import java.util.Map;

public enum TOMLTokenType {
    IDENTIFIER("<identifier>"),
    LEFT_BRACKET("["),
    RIGHT_BRACKET("]"),
    COMMA(","),
    EQUAL("="),
    PROJECT("project"),
    NAME("name"),
    VERSION("version"),
    CLASSPATH("classpath"),
    AUTHORS("authors"),
    DESCRIPTION("description"),
    HOMEPAGE("homepage"),
    README("readme"),
    KEYWORDS("keywords"),
    CATEGORIES("categories"),
    LICENCE("licence"),
    LICENCE_FILE("licence-file"),
    DEPENDENCIES("dependencies"),
    BIN("bin"),
    EOT("<eot>");


    private static final Map<String, TOMLTokenType> keywordMap;

    static {
        keywordMap = new HashMap<>();

        keywordMap.put(PROJECT.description, PROJECT);
        keywordMap.put(NAME.description, NAME);
        keywordMap.put(VERSION.description, VERSION);
        keywordMap.put(CLASSPATH.description, CLASSPATH);
        keywordMap.put(AUTHORS.description, AUTHORS);
        keywordMap.put(DESCRIPTION.description, DESCRIPTION);
        keywordMap.put(HOMEPAGE.description, HOMEPAGE);
        keywordMap.put(README.description, README);
        keywordMap.put(KEYWORDS.description, KEYWORDS);
        keywordMap.put(CATEGORIES.description, CATEGORIES);
        keywordMap.put(LICENCE.description, LICENCE);
        keywordMap.put(LICENCE_FILE.description, LICENCE_FILE);
        keywordMap.put(DEPENDENCIES.description, DEPENDENCIES);
        keywordMap.put(BIN.description, BIN);
    }

    private String description;

    private TOMLTokenType(final String description) {
        this.description = description;
    }

    public static boolean isKeyword(final String spelling) {
        return keywordMap.containsKey(spelling);
    }

    public static TOMLTokenType getKeyword(final String spelling) {
        return keywordMap.get(spelling);
    }

    @Override
    public String toString() {
        return description;
    }
}
