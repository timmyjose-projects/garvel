package com.tzj.garvel.core.parser.api.ast;

import java.util.Objects;

public class JsonPair extends JsonMember {
    private JsonString S;
    private JsonValue V;

    public JsonPair(final JsonString S, final JsonValue V) {
        this.S = S;
        this.V = V;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final JsonPair jsonPair = (JsonPair) o;
        return Objects.equals(S, jsonPair.S) &&
                Objects.equals(V, jsonPair.V);
    }

    @Override
    public int hashCode() {
        return Objects.hash(S, V);
    }

    @Override
    public String toString() {
        return "JsonPair{" +
                "S=" + S +
                ", V=" + V +
                '}';
    }

    public JsonString getS() {
        return S;
    }

    public JsonValue getV() {
        return V;
    }
}
