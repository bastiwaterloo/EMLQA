package org.example.metrics.classlevel;

import org.example.metrics.methodlevel.LengthOfId;

import java.util.HashMap;
import java.util.Map;

public class AvgLengthOfId {
    public static double getAvgLengthOfId(String filepath){
        HashMap<String, Double> methodIdLengthMap = LengthOfId.getLengthOfIds(filepath);
        double totalLengthOfIds = 0;
        for(Map.Entry<String,Double> entry : methodIdLengthMap.entrySet()){
            totalLengthOfIds += entry.getValue();
        }
        if(methodIdLengthMap.size() == 0){
            return 8;
        }
        return totalLengthOfIds / methodIdLengthMap.size();
    }
}
