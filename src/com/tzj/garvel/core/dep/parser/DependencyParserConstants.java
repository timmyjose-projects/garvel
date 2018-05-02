package com.tzj.garvel.core.dep.parser;

/**
 * Helper class to store used tag names of interest during
 * metadata and POM parsing.
 */
public class DependencyParserConstants {
    // common
    public static final String POM = "POM";

    public static final String METADATA = "metadata";

    // metadata file tags
    public static final String VERSIONING = "versioning";

    public static final String LATEST = "latest";

    public static final String RELEASE = "release";

    public static final String VERSIONS = "versions";

    // pom file tags
    public static final String PROJECT = "project";

    public static final String DEPENDENCIES = "dependencies";

    public static final String GROUPID = "groupId";

    public static final String ARTIFACTID = "artifactId";

    public static final String VERSION = "version";

    public static final String PARENT = "parent";

    public static final String PROPERTIES = "properties";

    public static final String DEPENDENCY_MANAGEMENT = "dependencyManagement";
}
