package org.example.metrics.classlevel;

import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class LackOfCohesionInMethods {
    public static double getLCOM(String filepath) {
        List<String> allFields = getAllAttributes(filepath);

        List<String> methods = getAllMethods(filepath);

        double connectedFields = 0;
        double disconnectedFields = 0;
        List<String> sharedFieldsList = new ArrayList<>();
        for(String m1 : methods){
            for(String m2 : methods){
                if (!m1.equals(m2)){
                    for (String field : allFields){
                        if(doesMethodAccessField(filepath, m1, field) && doesMethodAccessField(filepath, m2, field)){
                            if(!sharedFieldsList.contains(field)){
                                sharedFieldsList.add(field);
                            }
                        }
                    }
                }
            }
        }

        double con = sharedFieldsList.size();
        double disc = allFields.size() - sharedFieldsList.size();

        double lcom = 0;

        if (con + disc > 0){
            lcom = (disc - con) / (con + disc);
        }
        return 0;
    }

    public static List<String> getAllMethods(String filePath) {
        try {
            FileInputStream file = new FileInputStream(filePath);
            CompilationUnit cu = StaticJavaParser.parse(file);

            MethodVisitor visitor = new MethodVisitor();
            visitor.visit(cu, null);

            return visitor.getMethodNames();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Rückgabe einer leeren Liste im Fehlerfall
        }
    }

    public static boolean doesMethodAccessField(String filePath, String methodName, String fieldName) {
        try {
            FileInputStream file = new FileInputStream(filePath);
            CompilationUnit cu = StaticJavaParser.parse(file);

            FieldAccessVisitor visitor = new FieldAccessVisitor(methodName, fieldName);
            visitor.visit(cu, null);

            return visitor.doesAccessField();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false; // Rückgabe im Fehlerfall
        }
    }

    public static List<String> getAllAttributes(String filePath) {
        try {
            FileInputStream file = new FileInputStream(filePath);
            CompilationUnit cu = StaticJavaParser.parse(file);

            FieldVisitor fieldVisitor = new FieldVisitor();
            fieldVisitor.visit(cu, null);

            return fieldVisitor.getFieldNames();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Rückgabe einer leeren Liste im Fehlerfall
        }
    }

    private static class MethodAttributeVisitor extends VoidVisitorAdapter<Void> {
        private List<String> attributeList;
        private Set<String> methodsAccessingAttributes = new HashSet<>();

        public MethodAttributeVisitor(List<String> attributeList) {
            this.attributeList = attributeList;
        }

        public Set<String> getMethodsAccessingAttributes() {
            return methodsAccessingAttributes;
        }

        @Override
        public void visit(MethodDeclaration md, Void arg) {
            md.findAll(NameExpr.class).forEach(expr -> {
                String expression = expr.toString();
                if (attributeList.contains(expression)) {
                    methodsAccessingAttributes.add(md.getNameAsString());
                }
            });
        }

        @Override
        public void visit(FieldDeclaration fd, Void arg) {
            // Ignorieren der Felder, da nur Methoden analysiert werden sollen
        }
    }

    private static class FieldVisitor extends VoidVisitorAdapter<Void> {
        private List<String> fieldNames = new ArrayList<>();

        @Override
        public void visit(FieldDeclaration fd, Void arg) {
            fd.getVariables().forEach(variable -> fieldNames.add(variable.getNameAsString()));
        }

        public List<String> getFieldNames() {
            return fieldNames;
        }
    }

    private static class MethodVisitor extends com.github.javaparser.ast.visitor.VoidVisitorAdapter<Void> {
        private List<String> methodNames = new ArrayList<>();

        @Override
        public void visit(MethodDeclaration md, Void arg) {
            methodNames.add(md.getNameAsString());
        }

        public List<String> getMethodNames(){
            return methodNames;
        }
    }

    private static class FieldAccessVisitor extends VoidVisitorAdapter<Void> {
        private String methodName;
        private String fieldName;
        private boolean accessesField = false;

        public FieldAccessVisitor(String methodName, String fieldName) {
            this.methodName = methodName;
            this.fieldName = fieldName;
        }

        public boolean doesAccessField() {
            return accessesField;
        }

        @Override
        public void visit(MethodDeclaration md, Void arg) {
            if (md.getNameAsString().equals(methodName)) {
                md.findAll(NameExpr.class).forEach(expr -> {
                    if (expr.toString().equals(fieldName)) {
                        accessesField = true;
                    }
                });
            }
        }

        @Override
        public void visit(FieldDeclaration fd, Void arg) {
            // Ignorieren der Felder, da nur die Methoden analysiert werden sollen
        }
    }
}
