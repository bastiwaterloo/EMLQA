package org.example.writers;

import org.example.RecommendationCreator;
import org.example.Utils.FileUtils;
import org.example.metrics.classlevel.*;
import org.example.prompts.ClassRecommendationPrompt;
import org.example.thresholds.Treshholds;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClassMetricsWriter {

    public static void writeClassMetrics(String rootPath) throws IOException {


        File rootDir = new File(rootPath);
        File[] subdirs = rootDir.listFiles();

        HashMap<String, HashMap<String, HashMap<String, Double>>> classMetrics = new HashMap<>();
        HashMap<String, HashMap<String, HashMap<String, String>>> recommendations = new HashMap<>();

        /*
         * Calculate Class Metrics
         */

        for(File dir : subdirs){
            String path = FileUtils.escapePath(dir.getPath());

            for(File clazzFile : dir.listFiles()){
                String filepath = FileUtils.escapePath(clazzFile.getPath());

                HashMap<String, String> metricRecommendationMap = new HashMap<>();

                double loc = LinesOfCode.getLinesOfCode(filepath);
                if (loc > Treshholds.LOC.getValue()){
                    metricRecommendationMap.put("loc", RecommendationCreator.createRecommendation(ClassRecommendationPrompt.LOC_TOO_HIGH.getPrompt()));
                }
                double avgCC = AvgCyclomaticComplexity.getAvgCyclomaticComplexity(filepath);
                if (avgCC > Treshholds.AVGCC.getValue()){
                    metricRecommendationMap.put("avgCC", RecommendationCreator.createRecommendation(ClassRecommendationPrompt.AVGCC_TOO_HIGH.getPrompt()));
                }
                double nom = NumberOfMethods.getNumberOfMethods(filepath);
                if (nom > Treshholds.NOM.getValue()){
                    metricRecommendationMap.put("nom", RecommendationCreator.createRecommendation(ClassRecommendationPrompt.NOM_TOO_HIGH.getPrompt()));
                }
                double noc = NumberOfChildren.getNumberOfChildren(path, FileUtils.getClassNameFromFilepath(filepath));
                if (noc > Treshholds.NOC.getValue()){
                    metricRecommendationMap.put("noc", RecommendationCreator.createRecommendation(ClassRecommendationPrompt.NOC_TOO_HIGH.getPrompt()));
                }
                double nmo = NumberOfOverriddenMethods.getNumberOfOverriddenMethods(filepath);
                if (nmo > Treshholds.NMO.getValue()){
                    metricRecommendationMap.put("nmo", RecommendationCreator.createRecommendation(ClassRecommendationPrompt.NMO_TOO_HIGH.getPrompt()));
                }
                double wmc = WeightedMethodsPerClass.getWeightedMethodsPerClass(filepath);
                if (wmc > Treshholds.WMC.getValue()){
                    metricRecommendationMap.put("wmc", RecommendationCreator.createRecommendation(ClassRecommendationPrompt.WMC_TOO_HIGH.getPrompt()));
                }
                double dit = DepthOfInheritanceTree.calculateDIT(filepath);
                if (dit > Treshholds.DIT.getValue()){
                    metricRecommendationMap.put("dit", RecommendationCreator.createRecommendation(ClassRecommendationPrompt.DIT_TOO_HIGH.getPrompt()));
                }
                double avgLengthOfId = AvgLengthOfId.getAvgLengthOfId(filepath);
                if (avgLengthOfId < Treshholds.AVGLOID.getValue()){
                    metricRecommendationMap.put("avgLengthOfId", RecommendationCreator.createRecommendation(ClassRecommendationPrompt.AVGLOID_TOO_HIGH.getPrompt()));
                }
                double lcom = LackOfCohesionInMethods.getLCOM(filepath);
                if (lcom > Treshholds.LCOM.getValue()){
                    metricRecommendationMap.put("lcom", RecommendationCreator.createRecommendation(ClassRecommendationPrompt.LCOM_TOO_HIGH.getPrompt()));
                }

                if(!classMetrics.containsKey(dir.getName())){
                    classMetrics.put(dir.getName(), new HashMap<>());
                }
                if(!classMetrics.get(dir.getName()).containsKey(clazzFile.getName())){
                    classMetrics.get(dir.getName()).put(clazzFile.getName(), new HashMap<>());
                }
                classMetrics.get(dir.getName()).get(clazzFile.getName()).put("loc", loc);
                classMetrics.get(dir.getName()).get(clazzFile.getName()).put("avgCC", avgCC);
                classMetrics.get(dir.getName()).get(clazzFile.getName()).put("nom", nom);
                classMetrics.get(dir.getName()).get(clazzFile.getName()).put("noc", noc);
                classMetrics.get(dir.getName()).get(clazzFile.getName()).put("nmo", nmo);
                classMetrics.get(dir.getName()).get(clazzFile.getName()).put("wmc", wmc);
                classMetrics.get(dir.getName()).get(clazzFile.getName()).put("dit", dit);
                classMetrics.get(dir.getName()).get(clazzFile.getName()).put("avgLengthOfId", avgLengthOfId);
                classMetrics.get(dir.getName()).get(clazzFile.getName()).put("lcom", lcom);

                recommendations.put(dir.getName(), new HashMap<>());
                recommendations.get(dir.getName()).put(clazzFile.getName(), metricRecommendationMap);


            }
        }
        JsonFileWriter.writeDataToJsonFile("src/reports/metrics/class_metrics.json", classMetrics);
        JsonFileWriter.writeDataToJsonFile("src/reports/recommendations/class_recommendations.json", recommendations);
    }
}
