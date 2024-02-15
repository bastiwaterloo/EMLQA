package org.example.metrics.classlevel;

import org.example.metrics.methodlevel.CyclomaticComplexity;

import java.util.HashMap;
import java.util.Map;

public class WeightedMethodsPerClass {
    public static double getWeightedMethodsPerClass(String filepath){
        double totalCC = 0;
        HashMap<String, Double> ccValues = CyclomaticComplexity.getCyclomaticComplexityForMethod(filepath);
        for(Map.Entry<String, Double> entry : ccValues.entrySet()){
            totalCC += entry.getValue();
        }
        return totalCC;
    }
}
