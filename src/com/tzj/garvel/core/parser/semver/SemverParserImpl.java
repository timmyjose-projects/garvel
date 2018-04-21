package com.tzj.garvel.core.parser.semver;

import com.tzj.garvel.core.parser.api.SemverParser;
import com.tzj.garvel.core.parser.api.ast.semver.*;
import com.tzj.garvel.core.parser.exception.SemverParserException;

import static com.tzj.garvel.core.parser.semver.SemverTokenType.PERIOD;

public class SemverParserImpl implements SemverParser {
    private SemverScanner scanner;

    private SemverToken currentToken;

    public SemverParserImpl(final String semverString) {
        this.scanner = new SemverScanner(semverString);
    }

    // test
    public static void main(String[] args) throws SemverParserException {
        final String semverString = "0.1.0-nightly+beta";

        SemverParser parser = new SemverParserImpl(semverString);
        Semver semver = parser.parse();

        System.out.println(semver);

    }

    private void acceptIt() {
        currentToken = scanner.scan();
    }

    private void accept(final SemverTokenType expectedKind) throws SemverParserException {
        if (currentToken.kind() != expectedKind) {
            throw new SemverParserException(String.format("Semver Parser error at col %d. Expected to accept token of kind %s, found token of kind %s",
                    currentToken.columnNumber(), expectedKind, currentToken.kind()));
        }

        if (currentToken.kind() == SemverTokenType.EOT) {
            return;
        }

        currentToken = scanner.scan();
    }

    /**
     * Semver ::= Version ("-" PreRelease | "+" Build | "-" PreRelease "+" Build )
     *
     * @return
     */
    private Semver parseSemver() throws SemverParserException {
        Semver semver = null;

        final Version version = parseVersion();

        switch (currentToken.kind()) {
            case DASH: {
                acceptIt();
                final PreRelease preRelease = parsePreRelease();

                if (currentToken.kind() == SemverTokenType.PLUS) {
                    acceptIt();
                    final Build build = parseBuild();
                    semver = new SemverVersionPreReleaseBuild(version, preRelease, build);
                } else {
                    semver = new SemverVersionPreRelease(version, preRelease);
                }
            }
            break;

            case PLUS: {
                acceptIt();
                final Build build = parseBuild();
                semver = new SemverVersionBuild(version, build);
            }
            break;

            default:
                throw new SemverParserException(String.format("Semver Parser Error at col %d. %s cannot start a valid semver string",
                        currentToken.columnNumber(), currentToken.kind()));
        }

        return semver;
    }

    /**
     * Build ::= Identifier ("." Identifier)*
     *
     * @return
     */
    private Build parseBuild() throws SemverParserException {
        Build build1 = parseSimpleBuild();

        while (currentToken.kind() == PERIOD) {
            acceptIt();
            final Build build2 = parseSimpleBuild();
            build1 = new SequentialBuild(build1, build2);
        }

        return build1;
    }

    private Build parseSimpleBuild() throws SemverParserException {
        return new SimpleBuild(parseIdentifier());
    }

    private Identifier parseIdentifier() throws SemverParserException {
        if (currentToken.kind() != SemverTokenType.IDENTIFIER) {
            throw new SemverParserException(String.format("Semver Parser Error at col %d. Expected a %s, found %s",
                    currentToken.columnNumber(), SemverTokenType.IDENTIFIER, currentToken.kind()));
        }

        final Identifier id = new Identifier(currentToken.spelling());
        currentToken = scanner.scan();

        return id;
    }

    private IntegerLiteral parseIntegerLiteral() throws SemverParserException {
        if (currentToken.kind() != SemverTokenType.INTLITERAL) {
            throw new SemverParserException(String.format("Semver Parser Error at col %d. Expected a %s, found %s",
                    currentToken.columnNumber(), SemverTokenType.INTLITERAL, currentToken.kind()));
        }

        final IntegerLiteral il = new IntegerLiteral(currentToken.spelling());
        currentToken = scanner.scan();

        return il;
    }

    /**
     * PreRelease ::= Identififier ("." Identifier)*
     *
     * @return
     */
    private PreRelease parsePreRelease() throws SemverParserException {
        PreRelease preRelease1 = parseSimplePreRelease();

        while (currentToken.kind() == PERIOD) {
            acceptIt();
            final PreRelease preRelease2;
            preRelease2 = parseSimplePreRelease();
            preRelease1 = new SequentialPreRelease(preRelease1, preRelease2);
        }

        return preRelease1;
    }

    private PreRelease parseSimplePreRelease() throws SemverParserException {
        return new SimplePreRelease(parseIdentifier());
    }

    /**
     * Version ::= MAJOR "." MINOR "." PATCH
     *
     * @return
     */
    private Version parseVersion() throws SemverParserException {
        final Major major = parseMajor();
        accept(PERIOD);
        final Minor minor = parseMinor();
        accept(PERIOD);
        final Patch patch = parsePatch();

        return new Version(major, minor, patch);
    }

    private Patch parsePatch() throws SemverParserException {
        return new Patch(parseIntegerLiteral());
    }

    private Minor parseMinor() throws SemverParserException {
        return new Minor(parseIntegerLiteral());
    }

    private Major parseMajor() throws SemverParserException {
        return new Major(parseIntegerLiteral());
    }

    @Override
    public Semver parse() throws SemverParserException {
        currentToken = scanner.scan();
        Semver semver = parseSemver();
        accept(SemverTokenType.EOT);

        return semver;
    }
}
