package com.tzj.garvel.core.dep.api.repo;

/**
 * The various kinds of supported repositories.
 */
public enum RepositoryKind {
    CENTRAL {
        @Override
        public String getUrl() {
            return "http://central.maven.org/maven2/";
        }
    },

    SONATYPE {
        @Override
        public String getUrl() {
            return "https://oss.sonatype.org/content/repositories/releases/";
        }
    },

    SPRING_PLUGINS {
        @Override
        public String getUrl() {
            return "http://repo.spring.io/plugins-release/";
        }
    },

    SPRING_LIBS {
        @Override
        public String getUrl() {
            return "http://repo.spring.io/libs-milestone/";
        }
    },

    ATLASSIAN {
        @Override
        public String getUrl() {
            return "\n" +
                    "https://maven.atlassian.com/content/repositories/atlassian-public/";
        }
    };

    abstract String getUrl();
}

