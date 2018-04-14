package com.tzj.garvel.core.parser.api.ast.json;

import java.util.Objects;

public class JsonArray extends JsonArrayElement {
    private JsonValue V;

    public JsonArray(final JsonValue V) {
        this.V = V;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final JsonArray jsonArray = (JsonArray) o;
        return Objects.equals(V, jsonArray.V);
    }

    @Override
    public int hashCode() {
        return Objects.hash(V);
    }

    @Override
    public String toString() {
        return "JsonArray{" +
                "V=" + V +
                '}';
    }

    public JsonValue getV() {
        return V;
    }
}
