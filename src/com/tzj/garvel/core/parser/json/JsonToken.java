package com.tzj.garvel.core.parser.json;

public class JsonToken {
    private JsonTokenType kind;
    private String spelling;

    public JsonToken(final JsonTokenType kind, final String spelling) {
        this.kind = kind;
        this.spelling = spelling;
    }

    public JsonTokenType kind() {
        return kind;
    }

    public String spelling() {
        return spelling;
    }

    @Override
    public String toString() {
        return "< " + kind + ", " + spelling + " >";
    }
}