package com.tzj.garvel.core.parser.exception;

public class TOMLParserException extends ParserException {
    private static final long serialVersionUID = 8701422962562586593L;

    public TOMLParserException(final String errorMessage) {
        super(errorMessage);
    }

    public TOMLParserException() {
        super();
    }
}
