package org.example.metrics.methodlevel;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class NumberOfParams {
    public static HashMap<String, Double> getNumberOfParams(String filepath){

        try {
            FileInputStream fileInputStream = new FileInputStream(filepath);
            ParseResult<CompilationUnit> parseResult = new JavaParser().parse(fileInputStream);
            CompilationUnit cu = parseResult.getResult().orElseThrow(FileNotFoundException::new);
            HashMap<String, Double> methodNumOfParamsMap = new HashMap<>();
            cu.findAll(MethodDeclaration.class).forEach(method -> {
                double parameterCount = method.getParameters().size();
                methodNumOfParamsMap.put(method.getNameAsString(), parameterCount);
            });
            return methodNumOfParamsMap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}
