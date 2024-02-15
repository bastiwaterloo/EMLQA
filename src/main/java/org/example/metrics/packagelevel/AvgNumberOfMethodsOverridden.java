package org.example.metrics.packagelevel;

import org.example.metrics.classlevel.NumberOfOverriddenMethods;

import java.io.File;

public class AvgNumberOfMethodsOverridden {
    public static double getAvgNumMethodsOverridden(String projectPath){
        File[] files = new File(projectPath).listFiles();
        double totalNMO = 0;
        for (File file : files){
            totalNMO += NumberOfOverriddenMethods.getNumberOfOverriddenMethods(file.getAbsolutePath());
        }
        return totalNMO / files.length;
    }
}
