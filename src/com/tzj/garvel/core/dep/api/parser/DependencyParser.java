package com.tzj.garvel.core.dep.api.parser;

import com.tzj.garvel.core.dep.api.exception.DependencyManagerException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class DependencyParser {
    public abstract void parse() throws DependencyManagerException;

    public abstract Versions getVersions();

    public abstract Dependencies getDependencies();

    protected final Node findTagAmongstChildren(final NodeList children, final String nodeName) {
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) != null && children.item(i).getNodeName() != null && children.item(i).getNodeName().equalsIgnoreCase(nodeName)) {
                return children.item(i);
            }
        }

        return null;
    }

}
