package com.tzj.garvel.core.dep.api.graph;

/**
 * This adds custom behaviour to the Graph library.
 */
public interface GraphCallback<T> {
    void pre();

    void invoke(T t);

    void post();
}
