package com.tzj.garvel.core.parser.api.ast.json;

import java.util.Objects;

public class JsonStringValue extends JsonValue {
    private JsonString S;

    public JsonStringValue(final JsonString S) {
        this.S = S;
    }

    @Override
    public String toString() {
        return "JsonStringValue{" +
                "S=" + S +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final JsonStringValue that = (JsonStringValue) o;
        return Objects.equals(S, that.S);
    }

    @Override
    public int hashCode() {
        return Objects.hash(S);
    }

    public JsonString getS() {
        return S;
    }
}
