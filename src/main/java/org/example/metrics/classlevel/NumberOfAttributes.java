package org.example.metrics.classlevel;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.example.Utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class NumberOfAttributes {

    public static int countAttributes(String filePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            String className = FileUtils.getClassNameFromFilepath(filePath);
            // Parse the file
            CompilationUnit cu = StaticJavaParser.parse(fileInputStream);

            // Find the class by its name
            ClassOrInterfaceDeclaration targetClass = cu.getClassByName(className).orElse(null);

            if (targetClass != null) {
                // Get the fields (attributes) of the class
                return targetClass.getFields().size();
            } else {
                System.out.println("Class not found.");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static double calculateAvgNOA(String projectPath){
        File[] files = new File(projectPath).listFiles();
        double classCounter = 0;
        double totalAttributes = 0;
        for(File file : files){
            totalAttributes += countAttributes(file.getAbsolutePath());
            classCounter++;
        }
        return totalAttributes / classCounter;
    }

}
