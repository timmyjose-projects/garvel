package com.tzj.garvel.core.dep.api.repo;

/**
 * The various kinds of supported repositories. In later iterations,
 * this should probably be moved out into an external configuration file,
 * or at least the URLs. The hierarchy of the repositories themselves
 * can remain the same since it is meant to be opaque to the user (at
 * least for now).
 * <p>
 * The repositories are defined in the order that they will be tried for a
 * particular dependency.
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
    },

    JBOSS_RELEASES {
        @Override
        public String getUrl() {
            return "https://repository.jboss.org/nexus/content/repositories/releases/";
        }
    },

    NUXEO_RELEASES {
        @Override
        public String getUrl() {
            return
                    "https://maven-eu.nuxeo.org/nexus/content/repositories/public-releases/";
        }
    },

    XWIKI_RELEASES {
        @Override
        public String getUrl() {
            return "http://maven.xwiki.org/releases/";
        }
    },

    APACHE_RELEASES {
        @Override
        public String getUrl() {
            return "https://repository.apache.org/content/repositories/releases/";
        }
    },

    CLOJARS {
        @Override
        public String getUrl() {
            return "http://clojars.org/repo/";
        }
    };


    public abstract String getUrl();
}
