package com.tzj.garvel.core.parser.api.visitor.toml;

import com.tzj.garvel.core.parser.api.ast.semver.*;
import com.tzj.garvel.core.parser.api.ast.toml.DependencyPairAst;
import com.tzj.garvel.core.parser.api.visitor.semver.SemverASTVisitor;
import com.tzj.garvel.core.parser.api.visitor.semver.SemverKey;

import java.util.ArrayList;
import java.util.List;

/**
 * The Semver Parser and AST need to be rewritten in a sane way in a
 * future iteration. This is way too ugly and inefficient, but will do
 * for 1.0.
 */
public class SemverASTTOMLVisitor implements SemverASTVisitor {
    private DependencyPairAst dependency;

    public SemverASTTOMLVisitor(final DependencyPairAst dependency) {
        this.dependency = dependency;
    }

    @Override
    public void visit(final Major majorAst) {
        if (dependency.getValue().get(SemverKey.MAJOR) != null) {
            dependency.getValue().get(SemverKey.MAJOR).add(majorAst.getLiteral().getSpelling());
        } else {
            final List<String> majors = new ArrayList<>();
            majors.add(majorAst.getLiteral().getSpelling());
            dependency.getValue().put(SemverKey.MAJOR, majors);
        }
    }

    @Override
    public void visit(final Minor minorAst) {
        if (dependency.getValue().get(SemverKey.MINOR) != null) {
            dependency.getValue().get(SemverKey.MINOR).add(minorAst.getLiteral().getSpelling());
        } else {
            final List<String> minors = new ArrayList<>();
            minors.add(minorAst.getLiteral().getSpelling());
            dependency.getValue().put(SemverKey.MINOR, minors);
        }
    }

    @Override
    public void visit(final Patch patchAst) {
        if (patchAst != null) {
            if (dependency.getValue().get(SemverKey.PATCH) != null) {
                dependency.getValue().get(SemverKey.MAJOR).add(patchAst.getPatch().getSpelling());
            } else {
                final List<String> patches = new ArrayList<>();
                patches.add(patchAst.getPatch().getSpelling());
                dependency.getValue().put(SemverKey.PATCH, patches);
            }
        }
    }

    @Override
    public void visit(final SemverVersion semverVersionAst) {
        semverVersionAst.accept(this);
    }

    @Override
    public void visit(final SemverVersionBuild semverVersionBuildAst) {
        semverVersionBuildAst.accept(this);
    }

    @Override
    public void visit(final SemverVersionPreRelease semverVersionPreReleaseAst) {
        semverVersionPreReleaseAst.accept(this);
    }

    @Override
    public void visit(final SemverVersionPreReleaseBuild semverVersionPreReleaseBuildAst) {
        semverVersionPreReleaseBuildAst.accept(this);
    }

    @Override
    public void visit(final SequentialPreRelease sequentialPreReleaseAst) {
        sequentialPreReleaseAst.accept(this);
    }

    @Override
    public void visit(final SimpleBuild simpleBuildAst) {
        if (dependency.getValue().get(SemverKey.BUILD) != null) {
            dependency.getValue().get(SemverKey.BUILD).add(simpleBuildAst.getId().getSpelling());
        } else {
            final List<String> builds = new ArrayList<>();
            builds.add(simpleBuildAst.getId().getSpelling());
            dependency.getValue().put(SemverKey.BUILD, builds);
        }
    }

    @Override
    public void visit(final Version versionAst) {
        versionAst.accept(this);
    }

    @Override
    public void visit(final SimplePreRelease simplePreReleaseAst) {
        if (dependency.getValue().get(SemverKey.PRERELEASE) != null) {
            dependency.getValue().get(SemverKey.PRERELEASE).add(simplePreReleaseAst.getId().getSpelling());
        } else {
            final List<String> releases = new ArrayList<>();
            releases.add(simplePreReleaseAst.getId().getSpelling());
            dependency.getValue().put(SemverKey.PRERELEASE, releases);
        }
    }

    @Override
    public void visit(final SequentialBuild sequentialBuild) {
        sequentialBuild.accept(this);
    }

    @Override
    public void visit(final Build build) {
        build.accept(this);
    }

    @Override
    public void visit(final PreRelease preRelease) {
        preRelease.accept(this);
    }
}
