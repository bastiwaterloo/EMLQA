package org.example.metrics.classlevel;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class NumberOfOverriddenMethods {
    public static double getNumberOfOverriddenMethods(String filePath) {
        List<String> overriddenMethods = findOverriddenMethods(filePath);
        return overriddenMethods.size();
    }

    public static List<String> findOverriddenMethods(String filePath) {
        List<String> overriddenMethods = new ArrayList<>();

        try {
            File file = new File(filePath);
            ParseResult<CompilationUnit> parseResult = new JavaParser().parse(file);

            CompilationUnit cu = parseResult.getResult().orElseThrow(FileNotFoundException::new);

            cu.findAll(ClassOrInterfaceDeclaration.class).forEach(classDecl -> {
                classDecl.findAll(MethodDeclaration.class).forEach(method -> {
                    if (method.isAnnotationPresent("Override")) {
                        overriddenMethods.add(method.getNameAsString());
                    }
                });
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return overriddenMethods;
    }

}
