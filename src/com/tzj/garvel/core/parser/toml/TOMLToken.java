package com.tzj.garvel.core.parser.toml;

public class TOMLToken {
    private TOMLTokenType kind;
    private String spelling;
    private int line;
    private int column;

    public TOMLToken(final TOMLTokenType kind, final String spelling, final int line, final int column) {
        this.kind = kind;

        if (TOMLTokenType.isKeyword(spelling)) {
            this.kind = TOMLTokenType.getKeyword(spelling);
        }

        this.spelling = spelling;
        this.line = line;
        this.column = column;
    }

    public TOMLTokenType kind() {
        return kind;
    }

    public String spelling() {
        return spelling;
    }

    public int line() {
        return line;
    }

    public int column() {
        return column;
    }

    @Override
    public String toString() {
        return "< " + kind + ", " + spelling + ", " + line + ", " + column + " >";
    }
}
