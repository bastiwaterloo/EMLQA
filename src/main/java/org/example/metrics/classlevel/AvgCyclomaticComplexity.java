package org.example.metrics.classlevel;

import org.example.metrics.methodlevel.CyclomaticComplexity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class AvgCyclomaticComplexity {
    public static double getAvgCyclomaticComplexity(String filepath){
        HashMap<String, Double> ccValues = CyclomaticComplexity.getCyclomaticComplexityForMethod(filepath);
        double totalCC = 0;
        for(Map.Entry<String, Double> entry : ccValues.entrySet()){
            totalCC += entry.getValue();
        }
        if(ccValues.size() == 0){
            return 0;
        }
        return totalCC / ccValues.size();
    }
}
