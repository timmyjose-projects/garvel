package com.tzj.garvel.core.parser.api.ast;

import java.util.Objects;

public class JsonString extends JsonValue {
    private String spelling;

    public JsonString(final String spelling) {
        this.spelling = spelling;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final JsonString that = (JsonString) o;
        return Objects.equals(spelling, that.spelling);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spelling);
    }

    @Override
    public String toString() {
        return "JsonString{" +
                "spelling='" + spelling + '\'' +
                '}';
    }

    public String spelling() {
        return spelling;
    }
}
