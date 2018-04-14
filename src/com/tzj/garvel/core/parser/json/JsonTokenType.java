package com.tzj.garvel.core.parser.json;

public enum JsonTokenType {
    STRING("<string>"),
    COMMA(","),
    COLON(":"),
    LEFT_BRACE("{"),
    RIGHT_BRACE("}"),
    LEFT_SQUARE_BRACKETS("["),
    RIGHT_SQUARE_BRACKETS("]"),
    EOT("<eot>");
    
    private String description;

    private JsonTokenType(final String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
