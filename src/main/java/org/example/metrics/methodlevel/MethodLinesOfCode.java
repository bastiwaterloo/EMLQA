package org.example.metrics.methodlevel;

import com.github.javaparser.JavaParser;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodLinesOfCode {

    public static Map<String, Double> getMethodLinesOfCode(String filePath){
        Map<String, Double> methodLines = countLinesInMethods(filePath);

        return methodLines;
    }
    public static Map<String, Double> countLinesInMethods(String filePath) {
        Map<String, Double> methodLines = new HashMap<>();
        try {
            CompilationUnit cu = StaticJavaParser.parse(new File(filePath));

            new MethodLineVisitor(methodLines).visit(cu, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return methodLines;
    }

    private static class MethodLineVisitor extends VoidVisitorAdapter<Void> {
        private Map<String, Double> methodLines;

        public MethodLineVisitor(Map<String, Double> methodLines) {
            this.methodLines = methodLines;
        }

        @Override
        public void visit(MethodDeclaration md, Void arg) {
            double lines = md.getEnd().get().line - md.getBegin().get().line + 1;
            methodLines.put(md.getNameAsString(), lines);
            super.visit(md, arg);
        }
    }
}
