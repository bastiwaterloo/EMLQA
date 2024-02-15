package org.example.metrics.classlevel;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class NumberOfMethods {

    public static double getNumberOfMethods(String filePath) {
        double methodCount = 0;
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            ParseResult<CompilationUnit> parseResult = new JavaParser().parse(fileInputStream);
            CompilationUnit cu = parseResult.getResult().orElseThrow(FileNotFoundException::new);

            methodCount = countMethods(cu);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return methodCount;
    }

    public static double countMethods(CompilationUnit cu) {
        return cu.findAll(MethodDeclaration.class).size();
    }


    public static double calculateAvgNOM(String projectPath){
        File[] files = new File(projectPath).listFiles();
        double classCounter = 0;
        double totalNom = 0;
        for(File file : files){
            totalNom += getNumberOfMethods(file.getAbsolutePath());
            classCounter++;
        }
        return totalNom / classCounter;
    }
}
