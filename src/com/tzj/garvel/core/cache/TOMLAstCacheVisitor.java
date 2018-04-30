package com.tzj.garvel.core.cache;

import com.tzj.garvel.core.cache.api.*;
import com.tzj.garvel.core.parser.api.ast.toml.*;
import com.tzj.garvel.core.parser.api.visitor.toml.TOMLAstVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Populate the cache by traversing the entire AST and offloading
 * the population to the relevant entries.
 */
public class TOMLAstCacheVisitor implements TOMLAstVisitor {
    private Map<CacheKey, CacheEntry> cache;

    public TOMLAstCacheVisitor(final Map<CacheKey, CacheEntry> cache) {
        this.cache = cache;
    }

    @Override
    public void visit(final AuthorsAst authorsAst) {
        final List<String> auths = new ArrayList<>();

        final List<Identifier> astAuths = authorsAst.getAuthors();
        for (Identifier author : astAuths) {
            auths.add(strip(author.spelling()));
        }

        final AuthorsEntry authors = new AuthorsEntry(auths);
        cache.put(CacheKey.AUTHORS, authors);
    }

    @Override
    public void visit(final BinPairAst targetAst) {
        final BinSectionEntry targets = (BinSectionEntry) cache.get(CacheKey.TARGETS);
        targets.getTargets().put(strip(targetAst.getKey().spelling()), strip(targetAst.getValue().spelling()));
    }

    @Override
    public void visit(final BinSectionAst targetsAst) {
        cache.put(CacheKey.TARGETS, new BinSectionEntry());
    }

    @Override
    public void visit(final CategoriesAst categoriesAst) {
        final List<String> cats = new ArrayList<>();

        final List<Identifier> astCats = categoriesAst.getCategories();
        for (Identifier cat : astCats) {
            cats.add(strip(cat.spelling()));
        }

        final CategoriesEntry categories = new CategoriesEntry(cats);
        cache.put(CacheKey.CATEGORIES, categories);
    }

    @Override
    public void visit(final ClassPathAst classpathAst) {
        final List<String> paths = new ArrayList<>();

        final List<Identifier> astPaths = classpathAst.getClassPath();
        for (Identifier path : astPaths) {
            paths.add(strip(path.spelling()));
        }

        final ClassPathEntry classpath = new ClassPathEntry(paths);
        cache.put(CacheKey.CLASSPATH, classpath);
    }


    @Override
    public void visit(final DependenciesSectionAst dependencies) {
        cache.put(CacheKey.DEPENDENCIES, new DependenciesEntry());
    }

    @Override
    public void visit(final DependencyPairAst dependencyAst) {
        final DependenciesEntry deps = (DependenciesEntry) cache.get(CacheKey.DEPENDENCIES);
        deps.getDependencies().put(dependencyAst.getKey().spelling(), dependencyAst.getValue());
    }

    @Override
    public void visit(final DescriptionAst descriptionAst) {
        final DescriptionEntry description = new DescriptionEntry(strip(descriptionAst.getDescription().spelling()));
        cache.put(CacheKey.DESCRIPTION, description);
    }


    @Override
    public void visit(final HomepageAst homepageAst) {
        final HomepageEntry homepage = new HomepageEntry(strip(homepageAst.getHomepage().spelling()));
        cache.put(CacheKey.HOMEPAGE, homepage);
    }

    @Override
    public void visit(final KeywordsAst keywordsAst) {
        final List<String> kws = new ArrayList<>();

        final List<Identifier> astKeywords = keywordsAst.getKeywords();
        for (Identifier kw : astKeywords) {
            kws.add(strip(kw.spelling()));
        }

        final KeywordsEntry keywords = new KeywordsEntry(kws);
        cache.put(CacheKey.KEYWORDS, keywords);
    }

    @Override
    public void visit(final LicenceAst licenceAst) {
        final LicenceEntry licence = new LicenceEntry(strip(licenceAst.getLicence().spelling()));
        cache.put(CacheKey.LICENCE, licence);
    }

    @Override
    public void visit(final LicenceFileAst licenceFileAst) {
        final LicenceFileEntry licenceFile = new LicenceFileEntry(strip(licenceFileAst.getLicenceFile().spelling()));
        cache.put(CacheKey.LICENCE_FILE, licenceFile);
    }

    @Override
    public void visit(final NameAst nameAst) {
        final NameEntry name = new NameEntry(strip(nameAst.getName().spelling()));
        cache.put(CacheKey.NAME, name);
    }

    @Override
    public void visit(final ReadmeAst readmeAst) {
        final ReadmeEntry readme = new ReadmeEntry(strip(readmeAst.getReadme().spelling()));
        cache.put(CacheKey.README, readme);
    }

    @Override
    public void visit(final VersionAst versionAst) {
        final VersionEntry version = new VersionEntry(strip(versionAst.getVersion().spelling()));
        cache.put(CacheKey.VERSION, version);
    }

    @Override
    public void visit(final MainClassAst mainClassAst) {
        final MainClassEntry mainClass = new MainClassEntry(strip(mainClassAst.getMainClass().spelling()));
        cache.put(CacheKey.MAIN_CLASS, mainClass);
    }

    @Override
    public void visit(final LibSectionAst libSectionAst) {
        final FatJarEntry fatJar = new FatJarEntry(libSectionAst.isFatJar());
        cache.put(CacheKey.FAT_JAR, fatJar);
    }

    /**
     * Strip any extra quotes at the beginning and end of the string.
     *
     * @param spelling
     * @return
     */
    private String strip(final String spelling) {
        if (spelling == null || spelling.isEmpty() || !spelling.startsWith("\"")) {
            return spelling;
        }

        StringBuffer sb = new StringBuffer();
        if (spelling.startsWith("\"")) {
            for (int i = 1; i < spelling.length() - 1; i++) {
                sb.append(spelling.charAt(i));
            }
        }

        return sb.toString();
    }
}
