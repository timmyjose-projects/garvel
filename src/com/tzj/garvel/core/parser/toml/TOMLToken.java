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

    public TOMLTokenType getKind() {
        return kind;
    }

    public String getSpelling() {
        return spelling;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "< " + kind + ", " + spelling + ", " + line + ", " + column + " >";
    }
}
