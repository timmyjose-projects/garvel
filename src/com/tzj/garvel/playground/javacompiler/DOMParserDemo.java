package com.tzj.garvel.playground.javacompiler;

import com.tzj.garvel.core.parser.api.ast.toml.TOMLAst;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DOMParserDemo {
    private static final String metadataXml = "http://central.maven.org/maven2/junit/junit/maven-metadata.xml";
    private static final String pomXml = "http://central.maven.org/maven2/junit/junit/4.12-beta-3/junit-4.12-beta-3.pom";
    private static final int TIMEOUT = 10000;
    private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    public static void main(String[] args) throws IOException {
        printMetdata();
        printPom();
    }

    private static void printPom() throws MalformedURLException {
        final URL url = new URL(pomXml);
        URLConnection conn = null;
        try {
            conn = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        conn.setReadTimeout(TIMEOUT);

        StringBuffer sb = new StringBuffer();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document root = builder.parse(new InputSource(new StringReader(sb.toString())));

            NodeList projectItems = root.getElementsByTagName("project").item(0).getChildNodes();
            Node dependencies = null;
            for (int i = 0; i < projectItems.getLength(); i++) {
                if (projectItems.item(i).getNodeName().equalsIgnoreCase("dependencies")) {
                    dependencies = projectItems.item(i);
                    break;
                }
            }

            if (dependencies == null) {
                System.err.println("No project dependencies found");
                return;
            }

            final NodeList deps = dependencies.getChildNodes();
            for (int i = 0; i < deps.getLength(); i++) {
                if (deps.item(i) != null) {
                    printDep(deps.item(i));
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void printDep(final Node item) {
        final NodeList items = item.getChildNodes();
        String groupId = null;
        String artifactId = null;
        String version = null;

        for (int i = 0; i < items.getLength(); i++) {
            if (items.item(i).getNodeName().equalsIgnoreCase("groupId")) {
                groupId = items.item(i).getTextContent();
            } else if (items.item(i).getNodeName().equalsIgnoreCase("artifactId")) {
                artifactId = items.item(i).getTextContent();
            } else if (items.item(i).getNodeName().equalsIgnoreCase("version")) {
                version = items.item(i).getTextContent();
            }
        }

        if (groupId != null & artifactId != null && version != null) {
            System.out.printf("%s, %s, %s\n", groupId, artifactId, version);
        }
    }

    private static void printMetdata() throws MalformedURLException {
        final URL url = new URL(metadataXml);

        URLConnection conn = null;
        try {
            conn = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        conn.setReadTimeout(TIMEOUT);

        StringBuffer sb = new StringBuffer();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document metadata = builder.parse(new InputSource(new StringReader(sb.toString())));

            NodeList versions = metadata.getElementsByTagName("versions");
            Node versionTag = versions.item(0);
            NodeList versionList = versionTag.getChildNodes();
            for (int i = 0; i < versionList.getLength(); i++) {
                System.out.println(versionList.item(i).getTextContent());
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
