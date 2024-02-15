package org.example.metrics.methodlevel;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class LengthOfId {
    public static HashMap<String, Double> getLengthOfIds(String filepath){
        try {
            FileInputStream fileInputStream = new FileInputStream(filepath);
            ParseResult<CompilationUnit> parseResult = new JavaParser().parse(fileInputStream);
            CompilationUnit cu = parseResult.getResult().orElseThrow(FileNotFoundException::new);
            HashMap<String, Double> methodNumOfParamsMap = new HashMap<>();
            cu.findAll(MethodDeclaration.class).forEach(method -> {

                if(!method.getNameAsString().equals("toString") && !method.getNameAsString().equals("compare") && !method.getNameAsString().equals("compareTo")){
                    methodNumOfParamsMap.put(method.getNameAsString(), (double) method.getNameAsString().length());
                }
            });
            return methodNumOfParamsMap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}
