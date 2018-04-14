package com.tzj.garvel.core.parser.api.ast;

import java.util.Objects;

public class JsonObject extends JsonValue {
    private JsonMember M;

    public JsonObject(final JsonMember M) {
        this.M = M;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final JsonObject that = (JsonObject) o;
        return Objects.equals(M, that.M);
    }

    @Override
    public int hashCode() {
        return Objects.hash(M);
    }

    @Override
    public String toString() {
        return "JsonObject{" +
                "M=" + M +
                '}';
    }

    public JsonMember getM() {
        return M;
    }
}
