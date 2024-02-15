package org.example.metrics.packagelevel;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Instability {
    private static Map<String, Set<String>> afferentMap = new HashMap<>();
    private static Map<String, Set<String>> efferentMap = new HashMap<>();

    public static double countCouplings(String directoryPath) {
        try {
            Files.walk(Path.of(directoryPath))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            CompilationUnit cu = StaticJavaParser.parse(path);
                            String currentClassName = path.getFileName().toString().replace(".java", "");
                            Set<String> imports = new HashSet<>();

                            cu.findAll(ImportDeclaration.class).forEach(importDecl -> {
                                String importedClass = importDecl.getNameAsString();
                                imports.add(importedClass);

                                efferentMap.computeIfAbsent(currentClassName, k -> new HashSet<>()).add(importedClass);
                                afferentMap.computeIfAbsent(importedClass, k -> new HashSet<>()).add(currentClassName);
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            double ce = efferentMap.size();
            double ca = afferentMap.size();

            double rmi = ce / (ca + ce);
            return rmi;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return -9999;
    }

    public static double getInstability(String directoryPath) {
        return countCouplings(directoryPath);
    }

}
