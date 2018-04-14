package com.tzj.garvel.core.parser.api.ast;

import java.util.Objects;

public class JsonSequentialArray extends JsonArrayElement {
    private JsonArrayElement A1;
    private JsonArrayElement A2;

    public JsonSequentialArray(final JsonArrayElement A1, final JsonArrayElement A2) {
        this.A1 = A1;
        this.A2 = A2;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final JsonSequentialArray that = (JsonSequentialArray) o;
        return Objects.equals(A1, that.A1) &&
                Objects.equals(A2, that.A2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(A1, A2);
    }

    @Override
    public String toString() {
        return "JsonSequentialArray{" +
                "A1=" + A1 +
                ", A2=" + A2 +
                '}';
    }

    public JsonArrayElement getA1() {
        return A1;
    }

    public JsonArrayElement getA2() {
        return A2;
    }
}
