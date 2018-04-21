package com.tzj.garvel.core.parser.toml;

import com.tzj.garvel.core.parser.api.TOMLParser;
import com.tzj.garvel.core.parser.api.ast.toml.BinPairsAst;
import com.tzj.garvel.core.parser.api.ast.toml.ConfigAst;
import com.tzj.garvel.core.parser.api.ast.toml.DependencyPairsAst;
import com.tzj.garvel.core.parser.api.ast.toml.ProjectSectionAst;
import com.tzj.garvel.core.parser.exception.TOMLParserException;
import com.tzj.garvel.core.parser.exception.TOMLScannerException;

import static com.tzj.garvel.core.parser.toml.TOMLTokenType.EOT;
import static com.tzj.garvel.core.parser.toml.TOMLTokenType.LEFT_BRACKET;

public class TOMLParserImpl implements TOMLParser {
    private TOMLScanner scanner;
    private TOMLToken currentToken;

    public TOMLParserImpl(final String filename) {
        try {
            this.scanner = new TOMLScanner(filename);
        } catch (Exception e) {
            throw new TOMLParserException(e.getLocalizedMessage());
        }
    }

    /**
     * Config ::= Project-Section Dependencies-Section [ Bin-Section ]
     *
     * @return
     */
    private ConfigAst parseConfig() {
        final ProjectSectionAst project = parseProjectSection();
        final DependencyPairsAst dependencies = parseDependenciesSection();

        ConfigAst config = new ConfigAst(project, dependencies);

        if (currentToken.kind() == LEFT_BRACKET) {
            final BinPairsAst targets = parseBinSection();
            config.setBin(targets);
        }

        return config;
    }

    /**
     * Bin-Section ::= '[' BIN ']' Bin-Pair (, Bin-Pair)*
     *
     * @return
     */
    private BinPairsAst parseBinSection() {
        return null;
    }

    /**
     * Dependencies-Section ::= '[' DEPENDENCIES ']' Dependency-Pair (, Dependency-Pair)*
     *
     * @return
     */
    private DependencyPairsAst parseDependenciesSection() {
        return null;
    }

    /**
     * Project-Section ::= '[' PROJECT ']' (NAME '=' Identifier) (VERSION = Identifier)
     * [ CLASSPATH = '[' Identifier (, Identifier)* ']') ]
     * [ AUTHORS = '[' Identifier (, Identifier)* ']' ]
     * [ DESCRIPTION = Identifier ]
     * [ HOMEPAGE = Identifier ]
     * [ README = Identifier ]
     * [ KEYWORDS = '[' Identifier (, Identifier)* ']' ]
     * [ CATEGORIES = '[' Identifier (, Identifier)* ']' ]
     * [ LICENCE = Identifier ]
     * [ LICENCE-FILE = Identifier ]
     *
     * @return
     */
    private ProjectSectionAst parseProjectSection() {
        return null;
    }

    private void acceptIt() throws TOMLScannerException {
        currentToken = scanner.scan();
    }

    private void accept(final TOMLTokenType expectedKind) throws TOMLScannerException {
        if (currentToken.kind() != expectedKind) {
            throw new TOMLParserException(String.format("line: %d, col: %d, expected to accept %s, found %s",
                    currentToken.line(), currentToken.column(), expectedKind, currentToken.kind()));
        }

        if (currentToken.kind() == EOT) {
            return;
        }

        currentToken = scanner.scan();
    }

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
