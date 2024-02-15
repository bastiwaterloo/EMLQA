package org.example.metrics.classlevel;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NumberOfChildren {
    public static double getNumberOfChildren(String directoryPath, String parentClassName) {
        List<String> subclassFiles = findSubclassesInDirectory(directoryPath, parentClassName);
        return subclassFiles.size();
    }

    public static List<String> findSubclassesInDirectory(String directoryPath, String parentClassName) {
        List<String> subclassFiles = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
            List<String> javaFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .map(Path::toString)
                    .collect(Collectors.toList());

            for (String filePath : javaFiles) {
                try {
                    File file = new File(filePath);
                    ParseResult<CompilationUnit> parseResult = new JavaParser().parse(file);

                    CompilationUnit cu = parseResult.getResult().orElse(null);

                    if (cu != null) {
                        List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class)
                                .stream()
                                .filter(classDecl -> classDecl.getExtendedTypes().stream()
                                        .anyMatch(type -> type.getNameAsString().equals(parentClassName)))
                                .collect(Collectors.toList());

                        if (!classes.isEmpty()) {
                            subclassFiles.add(file.getName());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return subclassFiles;
    }
}
