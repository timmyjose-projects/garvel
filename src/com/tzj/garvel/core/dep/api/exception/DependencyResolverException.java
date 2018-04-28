package com.tzj.garvel.core.dep.api.exception;

public class DependencyResolverException extends DependencyManagerException {
    private static final long serialVersionUID = -5638420214557905396L;

    public DependencyResolverException(final String errorMessage) {
        super(errorMessage);
    }
}
