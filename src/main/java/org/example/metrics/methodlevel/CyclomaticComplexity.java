package org.example.metrics.methodlevel;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.JavaParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class CyclomaticComplexity {


    public static HashMap<String, Double> getCyclomaticComplexityForMethod(String filepath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filepath);
            ParseResult<CompilationUnit> parseResult = new JavaParser().parse(fileInputStream);
            CompilationUnit cu = parseResult.getResult().orElseThrow(FileNotFoundException::new);
            HashMap<String, Double> methodCCMap = new HashMap<>();
            cu.findAll(MethodDeclaration.class).forEach(method -> {
                double complexity = calculateCyclomaticComplexity(method);
                methodCCMap.put(method.getNameAsString(), complexity);
            });
            return methodCCMap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    private static double calculateCyclomaticComplexity(MethodDeclaration method) {
        double complexity = 1; // Starte mit einer Grundkomplexität von 1

        // Zähle die Anzahl der Verzweigungen in der Methode
        complexity += method.findAll(IfStmt.class).size();
        complexity += method.findAll(WhileStmt.class).size();
        complexity += method.findAll(ForStmt.class).size();
        complexity += method.findAll(SwitchStmt.class).size();
        complexity += method.findAll(TryStmt.class).size();
        complexity += method.findAll(ConditionalExpr.class).size();

        return complexity;
    }

    private static class MethodVisitor extends VoidVisitorAdapter<Void> {

        @Override
        public void visit(MethodDeclaration method, Void arg) {
            double complexity = 1; // Starte mit einer Grundkomplexität von 1

            // Zähle die Anzahl der Verzweigungen in der Methode
            complexity += method.findAll(IfStmt.class).size();
            complexity += method.findAll(WhileStmt.class).size();
            complexity += method.findAll(ForStmt.class).size();
            complexity += method.findAll(SwitchStmt.class).size();
            complexity += method.findAll(TryStmt.class).size();
            complexity += method.findAll(ConditionalExpr.class).size();

            super.visit(method, arg);
        }
    }

}
