package com.tzj.garvel.cli.api.parser.ast;

import com.tzj.garvel.cli.api.parser.visitor.CLIAstVisitor;

import java.util.List;
import java.util.Objects;

public class RunCommandAst extends CommandAst {
    private TargetNameAst target;
    private String[] arguments;

    public RunCommandAst() {
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(final String[] arguments) {
        this.arguments = arguments;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final RunCommandAst that = (RunCommandAst) o;
        return Objects.equals(target, that.target) &&
                Objects.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(target, arguments);
    }

    @Override
    public String toString() {
        return "RunCommandAst{" +
                "target=" + target +
                ", arguments=" + arguments +
                '}';
    }

    public TargetNameAst getTarget() {
        return target;
    }

    public void setTarget(final TargetNameAst target) {
        this.target = target;
    }

    @Override
    public void accept(final CLIAstVisitor visitor) {
        visitor.visit(this);
    }
}
