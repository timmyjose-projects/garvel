package com.tzj.garvel.core.parser.toml;

import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.parser.api.SemverParser;
import com.tzj.garvel.core.parser.api.TOMLParser;
import com.tzj.garvel.core.parser.api.ast.semver.Semver;
import com.tzj.garvel.core.parser.api.ast.toml.*;
import com.tzj.garvel.core.parser.api.visitor.semver.SemverASTVisitor;
import com.tzj.garvel.core.parser.api.visitor.toml.SemverASTTOMLVisitor;
import com.tzj.garvel.core.parser.exception.SemverParserException;
import com.tzj.garvel.core.parser.exception.TOMLParserException;
import com.tzj.garvel.core.parser.exception.TOMLScannerException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.tzj.garvel.core.parser.toml.TOMLTokenType.*;

/**
 * Parser for the Garvel.gl configuration file.
 */
public class TOMLParserImpl implements TOMLParser {
    private TOMLScanner scanner;
    private TOMLToken currentToken;

    public TOMLParserImpl(final String filename) throws TOMLParserException {
        try {
            this.scanner = new TOMLScanner(filename);
        } catch (Exception e) {
            throw new TOMLParserException(e.getLocalizedMessage());
        }
    }

    // test
    public static void main(String[] args) {
        final String filename = System.getProperty("user.dir") + "/src/com/tzj/garvel/common/templates/GarvelTemplate.gl";

        try {
            TOMLParser parser = new TOMLParserImpl(filename);
            final ConfigAst config = parser.parse();
            System.out.println(config);
        } catch (TOMLParserException e) {
            e.printStackTrace();
        }
    }

    /**
     * Config ::= Project-Section Dependencies-Section [ Bin-Section ]
     *
     * @return
     */
    private ConfigAst parseConfig() throws TOMLParserException {
        final ProjectSectionAst project = parseProjectSection();
        final DependenciesSectionAst dependencies = parseDependenciesSection();

        ConfigAst config = new ConfigAst(project, dependencies);

        // optional
        if (currentToken.kind() == LEFT_BRACKET) {
            final LibSectionAst libMetadata = parseLibSection();

            if (libMetadata == null) {
                // we have already consumed '[' and BIN
                scanner.backtrack();
                scanner.backtrack();
                try {
                    currentToken = scanner.scan();
                } catch (TOMLScannerException e) {
                    throw new TOMLParserException(String.format("Error while backtracking at line: %d, col: %d", currentToken.line(), currentToken.column()));
                }
                final BinSectionAst targets = parseBinSection();
                config.setBin(targets);
            } else {
                config.setLib(libMetadata);

                // optional
                if (currentToken.kind() == LEFT_BRACKET) {
                    final BinSectionAst targets = parseBinSection();
                    config.setBin(targets);
                }
            }
        }

        return config;
    }

    /**
     * LibSection ::= '[' LIB ']' Main-Class [Fat-Jar]
     *
     * @return
     */
    private LibSectionAst parseLibSection() throws TOMLParserException {
        try {
            accept(TOMLTokenType.LEFT_BRACKET);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"[\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        try {
            accept(TOMLTokenType.LIB);
        } catch (TOMLParserException e) {
            // This means that the `lib` section is probably missing, and we should try for the `bin` section before
            // declaring an error.
            return null;
        }

        try {
            accept(TOMLTokenType.RIGHT_BRACKET);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"]\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        LibSectionAst libMetadata = null;

        // main-class attribute is mandatory if the `lib` section is present
        final MainClassAst mainClass = parseMainClass();

        // `fat-jar` is optional
        if (currentToken.kind() != EOT && currentToken.kind() != LEFT_BRACKET) {
            final boolean fatJar = parseFatJar();
            libMetadata = new LibSectionAst(mainClass, fatJar);
        }

        return libMetadata;
    }

    /**
     * FatJar ::= "true" | "false"
     *
     * @return
     */
    private boolean parseFatJar() throws TOMLParserException {
        boolean isFatJar = false;

        try {
            accept(TOMLTokenType.FAT_JAR);
        } catch (TOMLParserException e) {
            return isFatJar;
        }

        try {
            accept(TOMLTokenType.EQUAL);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected to find \"=\" after \"fat-jar\" key target key, found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        try {
            final Identifier fatJarId = parseIdentifier();
            if (fatJarId.spelling().equalsIgnoreCase("\"true\"")) {
                isFatJar = true;
            }
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected to find \"fat-jar\" target value " +
                    "(\"true\" or \"false\"), found \"%s\"", currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        return isFatJar;
    }

    /**
     * Main-Class ::= MAIN-CLASS '=' Identifier
     *
     * @return
     */
    private MainClassAst parseMainClass() throws TOMLParserException {
        try {
            accept(TOMLTokenType.MAIN_CLASS);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected to find \"main-class\" key, found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        try {
            accept(TOMLTokenType.EQUAL);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected to find \"=\" after \"main-class\" key target key, found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        MainClassAst mainClass = null;
        try {
            mainClass = new MainClassAst(parseIdentifier());
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected to find \"main-class\" target value (Identifier), found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        return mainClass;
    }

    /**
     * Bin-Section ::= '[' BIN ']' Bin-Pair (, Bin-Pair)*
     *
     * @return
     */
    private BinSectionAst parseBinSection() throws TOMLParserException {
        try {
            accept(TOMLTokenType.LEFT_BRACKET);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"[\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        try {
            accept(TOMLTokenType.BIN);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected section \"bin\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        try {
            accept(TOMLTokenType.RIGHT_BRACKET);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"]\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        BinSectionAst bins = new BinSectionAst();

        if (currentToken.kind() != EOT) {
            final Set<BinPairAst> targets = parseBins();
            bins.setTargets(targets);
        }

        return bins;
    }

    /**
     * BinPairs ::= BinPair (BinPair)*
     *
     * @return
     */
    private Set<BinPairAst> parseBins() throws TOMLParserException {
        Set<BinPairAst> targets = new HashSet<>();

        while (currentToken.kind() != EOT) {
            final BinPairAst target = parseBinPair();
            targets.add(target);
        }

        return targets;
    }

    /**
     * BinPair ::= Identifier '=' Identifier
     *
     * @return
     */
    private BinPairAst parseBinPair() throws TOMLParserException {
        Identifier key = null;
        try {
            key = parseIdentifier();
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected to find a bin target key (Identifier), found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        try {
            accept(TOMLTokenType.EQUAL);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected to find \"=\" after bin target key, found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        Identifier value = null;
        try {
            value = parseIdentifier();
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected to find a bin target value (Identifier), found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        return new BinPairAst(key, value);
    }

    /**
     * Dependencies-Section ::= '[' DEPENDENCIES ']' Dependency-Pair (, Dependency-Pair)*
     *
     * @return
     */
    private DependenciesSectionAst parseDependenciesSection() throws TOMLParserException {
        try {
            accept(TOMLTokenType.LEFT_BRACKET);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"[\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        try {
            accept(TOMLTokenType.DEPENDENCIES);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected section \"dependencies\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        try {
            accept(TOMLTokenType.RIGHT_BRACKET);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"]\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        DependenciesSectionAst dependencies = new DependenciesSectionAst();

        if (currentToken.kind() != LEFT_BRACKET) {
            final Set<DependencyPairAst> deps = parseDependencies();
            dependencies.setDependencies(deps);
        }

        return dependencies;
    }

    /**
     * Dependencies ::= DependencyPair (DependencyPair)*
     *
     * @return
     */
    private Set<DependencyPairAst> parseDependencies() throws TOMLParserException {
        Set<DependencyPairAst> deps = new HashSet<>();

        // since the `bin` section is optional, we need to check for EOT as well
        while (currentToken.kind() != LEFT_BRACKET && currentToken.kind() != EOT) {
            final DependencyPairAst dep = parseDependency();
            deps.add(dep);
        }

        return deps;
    }

    /**
     * DependencyPair ::= Identifier'=' Identifier
     * <p>
     * We also need to ensure that the value is a valid semver string.
     *
     * @return
     */
    private DependencyPairAst parseDependency() throws TOMLParserException {
        Identifier key = null;
        try {
            key = parseIdentifier();
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected to find a dependency key (Identifier), found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        try {
            accept(TOMLTokenType.EQUAL);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected to find \"=\" after dependency key, found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        Identifier value = null;
        try {
            value = parseIdentifier();
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected to find a dependency value (Identifier), found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        // invoke the SemverParser and retrieve the Semver AST
        final DependencyPairAst dependency = new DependencyPairAst(key);
        final String valueSpelling = value.spelling();
        final String trimmedSpelling = valueSpelling.substring(1, valueSpelling.length() - 1);
        final SemverParser parser = CoreModuleLoader.INSTANCE.getParserFramework().getSemVerParser(trimmedSpelling);
        try {
            final Semver semver = parser.parse();
            final SemverASTVisitor tomlVisitor = new SemverASTTOMLVisitor(dependency);
            semver.accept(tomlVisitor);
        } catch (SemverParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected to find a valid dependency value (semver string), found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        return dependency;
    }

    /**
     * Project-Section ::= '[' PROJECT ']' (NAME '=' Identifier) (VERSION = Identifier) EXTRA-DEPS
     *
     * @return
     */
    private ProjectSectionAst parseProjectSection() throws TOMLParserException {
        try {
            accept(TOMLTokenType.LEFT_BRACKET);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"[\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        try {
            accept(TOMLTokenType.PROJECT);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected section \"project\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        try {
            accept(TOMLTokenType.RIGHT_BRACKET);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"]\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        final NameAst name = parseName();
        final VersionAst version = parseVersion();
        final ProjectSectionAst section = new ProjectSectionAst(name, version);

        // optionals
        if (currentToken.kind() != LEFT_BRACKET) {
            final Set<ProjectMetadataAst> optionals = parseProjectMetadata(section);
            section.setOptionals(optionals);
        }

        return section;
    }

    /**
     * Extra-Deps ::= [ CLASSPATH = '[' Identifier (, Identifier)* ']') ]
     * [ AUTHORS = '[' Identifier (, Identifier)* ']' ]
     * [ DESCRIPTION = Identifier ]
     * [ HOMEPAGE = Identifier ]
     * [ README = Identifier ]
     * [ KEYWORDS = '[' Identifier (, Identifier)* ']' ]
     * [ CATEGORIES = '[' Identifier (, Identifier)* ']' ]
     * [ LICENCE = Identifier ]
     * [ LICENCE-FILE = Identifier ]
     *
     * @param section
     */
    private Set<ProjectMetadataAst> parseProjectMetadata(final ProjectSectionAst section) throws TOMLParserException {
        Set<ProjectMetadataAst> optionals = new HashSet<>();

        while (currentToken.kind() != LEFT_BRACKET) {
            switch (currentToken.kind()) {
                case CLASSPATH: {
                    final ClassPathAst classpath = parseClassPath();
                    optionals.add(classpath);
                }
                break;

                case AUTHORS: {
                    final AuthorsAst authors = parseAuthors();
                    optionals.add(authors);
                }
                break;

                case DESCRIPTION: {
                    final DescriptionAst description = parseDescription();
                    optionals.add(description);
                }
                break;

                case HOMEPAGE: {
                    final HomepageAst homepage = parseHomepage();
                    optionals.add(homepage);
                }
                break;

                case README: {
                    final ReadmeAst readme = parseReadme();
                    optionals.add(readme);
                }
                break;

                case KEYWORDS: {
                    final KeywordsAst keywords = parseKeywords();
                    optionals.add(keywords);
                }
                break;

                case CATEGORIES: {
                    final CategoriesAst categories = parseCategories();
                    optionals.add(categories);
                }
                break;

                case LICENCE: {
                    final LicenceAst licence = parseLicence();
                    optionals.add(licence);
                }
                break;

                case LICENCE_FILE: {
                    final LicenceFileAst licenceFile = parseLicenceFile();
                    optionals.add(licenceFile);
                }
                break;

                default: {
                    throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, \"%s\" is not a valid key for project metadata",
                            currentToken.line(), currentToken.column(), currentToken.spelling()));
                }
            }
        }

        return optionals;
    }

    /**
     * Licence-File ::= Identifier
     *
     * @return
     */
    private LicenceFileAst parseLicenceFile() throws TOMLParserException {
        try {
            accept(TOMLTokenType.LICENCE_FILE);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"licence-file\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        try {
            accept(TOMLTokenType.EQUAL);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"=\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        LicenceFileAst licenceFile = null;
        try {
            licenceFile = new LicenceFileAst(parseIdentifier());
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected to parse an Identifier after \"licence-file = \", found %s",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        return licenceFile;
    }

    /**
     * Liccence ::= Identifier
     *
     * @return
     */
    private LicenceAst parseLicence() throws TOMLParserException {
        try {
            accept(TOMLTokenType.LICENCE);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"licence\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        try {
            accept(TOMLTokenType.EQUAL);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"=\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        LicenceAst licence = null;
        try {
            licence = new LicenceAst(parseIdentifier());
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected to parse an Identifier after \"licence = \", found %s",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        return licence;
    }

    /**
     * Categories :: = '[' Identifier (Identifier)* ']'
     *
     * @return
     */
    private CategoriesAst parseCategories() throws TOMLParserException {
        try {
            accept(TOMLTokenType.CATEGORIES);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"categories\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        try {
            accept(TOMLTokenType.EQUAL);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"=\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        List<Identifier> cats = new ArrayList<>();
        try {
            accept(TOMLTokenType.LEFT_BRACKET);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"[\" after \"categories = \", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        while (currentToken.kind() != RIGHT_BRACKET) {
            Identifier cat;
            try {
                cat = parseIdentifier();
            } catch (TOMLParserException e) {
                throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected identifier for categories entry, found \"%s\"",
                        currentToken.line(), currentToken.column(), currentToken.spelling()));
            }

            if (currentToken.kind() == COMMA) {
                accept(COMMA);
            }

            cats.add(cat);
        }

        try {
            accept(TOMLTokenType.RIGHT_BRACKET);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"]\" after categories entries, found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        CategoriesAst categories = new CategoriesAst(cats);

        return categories;
    }

    /**
     * Keywords ::= '[' Identifier (Identifier)* ']'
     *
     * @return
     */
    private KeywordsAst parseKeywords() throws TOMLParserException {
        try {
            accept(TOMLTokenType.KEYWORDS);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"keywords\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        try {
            accept(TOMLTokenType.EQUAL);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"=\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        List<Identifier> kws = new ArrayList<>();
        try {
            accept(TOMLTokenType.LEFT_BRACKET);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"[\" after \"keywords = \", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        while (currentToken.kind() != RIGHT_BRACKET) {
            Identifier kw;
            try {
                kw = parseIdentifier();
            } catch (TOMLParserException e) {
                throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected identifier for keywords entry, found \"%s\"",
                        currentToken.line(), currentToken.column(), currentToken.spelling()));
            }

            if (currentToken.kind() == COMMA) {
                accept(COMMA);
            }

            kws.add(kw);
        }

        try {
            accept(TOMLTokenType.RIGHT_BRACKET);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"]\" after keywords entries, found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        KeywordsAst keywords = new KeywordsAst(kws);

        return keywords;
    }

    /**
     * Readme ::= Identifier
     *
     * @return
     */
    private ReadmeAst parseReadme() throws TOMLParserException {
        try {
            accept(TOMLTokenType.README);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"readme\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        try {
            accept(TOMLTokenType.EQUAL);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"=\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        ReadmeAst readme = null;
        try {
            readme = new ReadmeAst(parseIdentifier());
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected to parse an Identifier after \"readme = \", found %s",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        return readme;
    }

    /**
     * Homepage ::= Identifier
     *
     * @return
     */
    private HomepageAst parseHomepage() throws TOMLParserException {
        try {
            accept(TOMLTokenType.HOMEPAGE);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"homepage\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        try {
            accept(TOMLTokenType.EQUAL);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"=\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        HomepageAst homepage = null;
        try {
            homepage = new HomepageAst(parseIdentifier());
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected to parse an Identifier after \"homepage = \", found %s",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        return homepage;
    }

    /**
     * Description ::= Identifier
     *
     * @return
     */
    private DescriptionAst parseDescription() throws TOMLParserException {
        try {
            accept(TOMLTokenType.DESCRIPTION);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"description\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        try {
            accept(TOMLTokenType.EQUAL);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"=\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        DescriptionAst description = null;
        try {
            description = new DescriptionAst(parseIdentifier());
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected to parse an Identifier after \"description = \", found %s",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        return description;
    }

    /**
     * Authors ::= '[' Identifier (Identifier)* ']'
     *
     * @return
     */
    private AuthorsAst parseAuthors() throws TOMLParserException {
        try {
            accept(TOMLTokenType.AUTHORS);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"authors\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        try {
            accept(TOMLTokenType.EQUAL);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"=\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        List<Identifier> auths = new ArrayList<>();
        try {
            accept(TOMLTokenType.LEFT_BRACKET);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"[\" after \"authors = \", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        while (currentToken.kind() != RIGHT_BRACKET) {
            Identifier auth;
            try {
                auth = parseIdentifier();
            } catch (TOMLParserException e) {
                throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected identifier for authors entry, found \"%s\"",
                        currentToken.line(), currentToken.column(), currentToken.spelling()));
            }

            if (currentToken.kind() == COMMA) {
                accept(COMMA);
            }

            auths.add(auth);
        }

        try {
            accept(TOMLTokenType.RIGHT_BRACKET);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"]\" after authors entries, found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        AuthorsAst authors = new AuthorsAst(auths);

        return authors;
    }

    /**
     * ClassPath ::= '[' Identifier (Identifier)* ']'
     *
     * @return
     */
    private ClassPathAst parseClassPath() throws TOMLParserException {
        try {
            accept(TOMLTokenType.CLASSPATH);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"classpath\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        try {
            accept(TOMLTokenType.EQUAL);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"=\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        List<Identifier> paths = new ArrayList<>();
        try {
            accept(TOMLTokenType.LEFT_BRACKET);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"[\" after \"classpath = \", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        while (currentToken.kind() != RIGHT_BRACKET) {
            Identifier path;
            try {
                path = parseIdentifier();
            } catch (TOMLParserException e) {
                throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected identifier for classpath entry, found \"%s\"",
                        currentToken.line(), currentToken.column(), currentToken.spelling()));
            }

            if (currentToken.kind() == COMMA) {
                accept(COMMA);
            }

            paths.add(path);
        }

        try {
            accept(TOMLTokenType.RIGHT_BRACKET);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"]\" after classpath entries, found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        ClassPathAst classpath = new ClassPathAst(paths);

        return classpath;
    }

    /**
     * Version ::= Identifier
     *
     * @return
     */
    private VersionAst parseVersion() throws TOMLParserException {
        try {
            accept(TOMLTokenType.VERSION);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"version\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        try {
            accept(TOMLTokenType.EQUAL);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"=\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        VersionAst version = null;
        try {
            version = new VersionAst(parseIdentifier());
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected to parse an Identifier after \"version = \", found %s",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        return version;
    }

    /**
     * Name ::= Identifier
     *
     * @return
     * @throws TOMLScannerException
     */
    private NameAst parseName() throws TOMLParserException {
        try {
            accept(TOMLTokenType.NAME);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"name\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        try {
            accept(TOMLTokenType.EQUAL);
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected \"=\", found \"%s\"",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        NameAst name = null;
        try {
            name = new NameAst(parseIdentifier());
        } catch (TOMLParserException e) {
            throw new TOMLParserException(String.format("Invalid configuration file: line: %d, col: %d, expected to parse an Identifier after \"name = \", found %s",
                    currentToken.line(), currentToken.column(), currentToken.spelling()));
        }

        return name;
    }

    private Identifier parseIdentifier() throws TOMLParserException {
        if (currentToken.kind() != TOMLTokenType.IDENTIFIER) {
            throw new TOMLParserException();
        }

        final Identifier id = new Identifier(currentToken.spelling());
        try {
            currentToken = scanner.scan();
        } catch (TOMLScannerException e) {
            throw new TOMLParserException(e.getLocalizedMessage());
        }

        return id;
    }


    /**
     * unconditionally accept the token
     *
     * @throws TOMLScannerException
     */
    private void acceptIt() throws TOMLParserException {
        try {
            currentToken = scanner.scan();
        } catch (TOMLScannerException e) {
            throw new TOMLParserException(e.getLocalizedMessage());
        }
    }

    /**
     * accept the token only if it is the right kind.
     *
     * @param expectedKind
     * @throws TOMLScannerException
     */
    private void accept(final TOMLTokenType expectedKind) throws TOMLParserException {
        if (currentToken.kind() != expectedKind) {
            throw new TOMLParserException();
        }

        if (currentToken.kind() == EOT) {
            return;
        }

        try {
            currentToken = scanner.scan();
        } catch (TOMLScannerException e) {
            throw new TOMLParserException(e.getLocalizedMessage());
        }
    }

    /**
     * The entrypoint
     *
     * @return
     * @throws TOMLParserException
     */
    @Override
    public ConfigAst parse() throws TOMLParserException {
        ConfigAst config = null;
        try {
            currentToken = scanner.scan();
            config = parseConfig();
            accept(EOT);
        } catch (Exception e) {
            throw new TOMLParserException(e.getLocalizedMessage());
        }

        return config;
    }
}
