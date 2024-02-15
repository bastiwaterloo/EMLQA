package org.example.metrics.packagelevel;

import org.example.metrics.classlevel.WeightedMethodsPerClass;

import java.io.File;

public class AvgWeightedMethodsPerClass {
    public static double getAvgWMC(String projectPath){
        File[] files = new File(projectPath).listFiles();
        double totalWMC = 0;
        for(File file: files){
            totalWMC += WeightedMethodsPerClass.getWeightedMethodsPerClass(file.getAbsolutePath());
        }
        return totalWMC / files.length;
    }
}
