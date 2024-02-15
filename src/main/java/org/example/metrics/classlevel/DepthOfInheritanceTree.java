package org.example.metrics.classlevel;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.example.Utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;

public class DepthOfInheritanceTree {

    public static int calculateDIT(String filePath) {
        try {
            String basePath = FileUtils.getPathFromFilePath(filePath);
            File file = new File(filePath);
            ParseResult<CompilationUnit> parseResult = new JavaParser().parse(file);

            CompilationUnit cu = parseResult.getResult().orElseThrow(FileNotFoundException::new);

            ClassOrInterfaceDeclaration mainClass = cu.getClassByName(FileUtils.getClassNameFromFilepath(filePath)).orElse(null); // Passe den Klassennamen an

            if (mainClass != null) {
                return calculateDepth(mainClass, basePath);
            } else {
                return 0; // Wenn die Klasse nicht gefunden wurde, beträgt die DIT 0
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static int calculateDepth(ClassOrInterfaceDeclaration classDecl, String basePath) {
        int maxDepth = 0;

        for (var extendedType : classDecl.getExtendedTypes()) {
            try {
                String extendedClassName = extendedType.getNameAsString();
                File extendedFile = new File(basePath + extendedClassName + ".java"); // Passe den Pfad zum Verzeichnis an

                ParseResult<CompilationUnit> parseResult = new JavaParser().parse(extendedFile);
                CompilationUnit cu = parseResult.getResult().orElse(null);

                if (cu != null) {
                    ClassOrInterfaceDeclaration extendedClass = cu.getClassByName(extendedClassName).orElse(null);

                    if (extendedClass != null) {
                        int depth = calculateDepth(extendedClass, basePath);
                        maxDepth = Math.max(maxDepth, depth);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return maxDepth + 1;
    }

    public static double calculateAvgDIT(String projectPath){
        File[] files = new File(projectPath).listFiles();
        double classCounter = 0;
        double totalDit = 0;
        for(File file : files){
            totalDit += calculateDIT(file.getAbsolutePath());
            classCounter++;
        }
        return totalDit / classCounter;
    }
}
