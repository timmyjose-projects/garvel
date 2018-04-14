package com.tzj.garvel.core.parser.api.ast;

import java.util.Objects;

public class JsonSequentialPair extends JsonMember {
    JsonMember M1;
    JsonMember M2;

    public JsonSequentialPair(final JsonMember M1, final JsonMember M2) {
        this.M1 = M1;
        this.M2 = M2;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final JsonSequentialPair that = (JsonSequentialPair) o;
        return Objects.equals(M1, that.M1) &&
                Objects.equals(M2, that.M2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(M1, M2);
    }

    @Override
    public String toString() {
        return "JsonSequentialPair{" +
                "M1=" + M1 +
                ", M2=" + M2 +
                '}';
    }

    public JsonMember getM1() {
        return M1;
    }

    public JsonMember getM2() {
        return M2;
    }
}
