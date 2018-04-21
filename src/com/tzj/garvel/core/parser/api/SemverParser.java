package com.tzj.garvel.core.parser.api;

import com.tzj.garvel.core.parser.api.ast.semver.Semver;
import com.tzj.garvel.core.parser.exception.SemverParserException;

public interface SemverParser {
    Semver parse() throws SemverParserException;
}
