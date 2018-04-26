package com.tzj.garvel.playground.javacompiler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RetrieveMavenCentralRepoMetadata {
    private static final String CENTRAL_URL = "http://central.maven.org/maven2/";
    private static final String PATTERN = "<a href=.*>([a-zA-Z0-9_/-]+)</a>";
    private static final Pattern COMPILED_PATTERN = Pattern.compile(PATTERN);
    private static final int BUF_SIZE = 8192;

    private static final ExecutorService executors = Executors.newFixedThreadPool(1000);//Executors.newCachedThreadPool();

    public static void main(String[] args) {
        URL centralUrl = null;

        try {
            centralUrl = new URL(CENTRAL_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        URLConnection conn = null;
        try {
            conn = centralUrl.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] buffer = new byte[BUF_SIZE];
        StringBuffer sb = new StringBuffer();

        try (BufferedInputStream in = new BufferedInputStream(conn.getInputStream())) {
            while (in.read(buffer) != -1) {
                sb.append(new String(buffer, Charset.forName("UTF-8")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Repo> repos = new ArrayList<>();
        Matcher m = COMPILED_PATTERN.matcher(sb.toString());

        List<Callable<Void>> tasks = new ArrayList<>();
        while (m.find()) {
            if (m.groupCount() == 1) {
                final Repo repo = new Repo(m.group(1).substring(0, m.group(1).lastIndexOf("/")));
                tasks.add(new RepoFetcher(repos, repo, CENTRAL_URL, COMPILED_PATTERN));
            }
        }

        try {
            List<Future<Void>> futures = executors.invokeAll(tasks);
            boolean allDone = true;
            do {
                allDone = true;
                for (Future<?> f : futures) {
                    if (!f.isDone()) {
                        allDone = false;
                        break;
                    }

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        //
                    }
                }
            } while (!allDone);

            for (Repo repo : repos) {
                System.out.println(repo);
            }

            executors.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

class Repo {
    private String name;
    private List<String> subDirs;

    public Repo(final String name) {
        this.name = name;
        subDirs = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<String> getSubDirs() {
        return subDirs;
    }

    @Override
    public String toString() {
        return "Repo{" +
                "name='" + name + '\'' +
                ", subDirs=" + subDirs +
                '}';
    }

    public void addItem(final String dir) {
        subDirs.add(dir);
    }
}

class RepoFetcher implements Callable<Void> {
    private static final int BUF_SIZE = 8192;
    private static final int TIMEOUT = 10000;
    private final String centralUrl;
    private final Pattern pattern;
    private List<Repo> repos;
    private Repo repo;

    public RepoFetcher(final List<Repo> repos, final Repo repo, final String centralUrl, final Pattern pattern) {
        this.repos = repos;
        this.repo = repo;
        this.centralUrl = centralUrl;
        this.pattern = pattern;
    }

    @Override
    public Void call() {
        populateSubDirs(repo);
        repos.add(repo);
        System.out.printf("Finished fetching sub directories for %s\n\n", repo.getName());

        return null;
    }

    private void populateSubDirs(final Repo repo) {
        URL url = null;
        try {
            url = new URL(centralUrl + repo.getName());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(TIMEOUT);
            byte[] buffer = new byte[BUF_SIZE];
            StringBuffer sb = new StringBuffer();

            try (BufferedInputStream in = new BufferedInputStream(conn.getInputStream())) {
                while (in.read(buffer) != -1) {
                    sb.append(new String(buffer, Charset.forName("UTF-8")));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Matcher m = pattern.matcher(sb.toString());

            while (m.find()) {
                if (m.groupCount() == 1) {
                    repo.addItem(m.group(1));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
