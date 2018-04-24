package com.tzj.garvel.core.builder.api;

import java.util.List;

public class CompilationResult {
    private boolean successful;
    private List<String> diagnostics;

    public CompilationResult() {
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(final boolean successful) {
        this.successful = successful;
    }

    public List<String> getDiagnostics() {
        return diagnostics;
    }

    public void setDiagnostics(final List<String> diagnostics) {
        this.diagnostics = diagnostics;
    }
}
