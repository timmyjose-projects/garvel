package com.tzj.garvel.cli.api.parser.ast;

import java.util.Objects;

public class RunCommandAst extends CommandAst {
    private TargetNameAst target;

    public RunCommandAst(final TargetNameAst target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "RunCommandAst{" +
                "target=" + target +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final RunCommandAst that = (RunCommandAst) o;
        return Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(target);
    }

    public TargetNameAst getTarget() {
        return target;
    }
}
