package com.tzj.garvel.core.parser.json;

public class JsonToken {
    private JsonTokenType kind;
    private String spelling;
    private int lineNumber;
    private int columnNumber;

    public JsonToken(final JsonTokenType kind, final String spelling, final int lineNumber, final int columnNumber) {
        this.kind = kind;
        this.spelling = spelling;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    public JsonTokenType kind() {
        return kind;
    }

    public String spelling() {
        return spelling;
    }

    public int line() {
        return lineNumber;
    }

    public int column() {
        return columnNumber;
    }

    @Override
    public String toString() {
        return "< " + kind + ", " + spelling + ", " + lineNumber + ", " + columnNumber + " >";
    }
}
