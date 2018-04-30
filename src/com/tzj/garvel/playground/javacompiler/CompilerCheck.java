package com.tzj.garvel.playground.javacompiler;

public class CompilerCheck {
    public static void main(String[] args) {
        try {
            Class.forName("javax.tools.ToolProvider");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
