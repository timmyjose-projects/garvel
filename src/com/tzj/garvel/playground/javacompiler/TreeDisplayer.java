package com.tzj.garvel.playground.javacompiler;

import java.util.ArrayList;
import java.util.List;

public class TreeDisplayer {
    public static void main(String[] args) {
        final Node root = new Node("Root");

        final Node first = new Node("First");
        first.addChild(new Node("FirstFirst"));
        first.addChild(new Node("FirstSecond"));

        final Node second = new Node("Second");
        second.addChild(new Node("SecondFirst"));
        second.addChild(new Node("SecondSecond"));
        second.addChild(new Node("SecondThird"));

        root.addChild(first);
        root.addChild(second);

        root.prettyPrint("", true);
    }

    static class Node {
        private String name;
        private List<Node> children;

        public Node(final String name) {
            this.name = name;
            this.children = new ArrayList<>();
        }

        public void addChild(Node node) {
            children.add(node);
        }

        public void prettyPrint(String indent, boolean last) {
            System.out.print(indent);

            if (last) {
                System.out.printf("\\- ");
                indent += " ";
            } else {
                System.out.printf("|- ");
                indent += "| ";
            }
            System.out.println(name);

            for (int i = 0; i < children.size(); i++) {
                children.get(i).prettyPrint(indent, i == children.size() - 1);
            }
        }
    }
}

