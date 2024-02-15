package org.example.writers;

import org.example.RecommendationCreator;
import org.example.Utils.FileUtils;
import org.example.metrics.packagelevel.*;
import org.example.prompts.PackageRecommendationPromt;
import org.example.thresholds.Treshholds;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PackageMetricWriter {

    private static HashMap<String, HashMap<String, String>> generateRecommendationsForPackageTest(HashMap<String, HashMap<String, Double>> data) {

        HashMap<String, HashMap<String, String>> recommendations = new HashMap<>();

        //foreach package
        for(Map.Entry<String, HashMap<String, Double>> pckg : data.entrySet()){
            HashMap<String, String> metricMap = new HashMap<>();

            //foreach packagemetric
            for(Map.Entry<String, Double> metric : pckg.getValue().entrySet()){
                switch (metric.getKey()){
                    case "avgNumMethodsOverridden":
                        metricMap.put("avgNMO", RecommendationCreator.createRecommendation(PackageRecommendationPromt.AVG_MUM_METHODS_OVERRIDDEN_TOO_HIGH.getPrompt()));
                        break;
                    case "rma":
                        metricMap.put("rma", RecommendationCreator.createRecommendation(PackageRecommendationPromt.RMA_TOO_HIGH.getPrompt()));
                        break;
                    case "dn":
                        metricMap.put("dn", RecommendationCreator.createRecommendation(PackageRecommendationPromt.DN_TOO_HIGH.getPrompt()));
                        break;
                    case "avgWMC":
                        metricMap.put("avgWMC", RecommendationCreator.createRecommendation(PackageRecommendationPromt.AVG_WMC_TOO_HIGH.getPrompt()));
                        break;
                    case "rmi":
                        metricMap.put("rmi", RecommendationCreator.createRecommendation(PackageRecommendationPromt.RMI_TOO_HIGH.getPrompt()));
                        break;
                    default:
                        break;

                }
            }
            recommendations.put(pckg.getKey(), metricMap);
        }
        return recommendations;
    }
    private static HashMap<String, HashMap<String, String>> generateRecommendationsForPackage(HashMap<String, HashMap<String, Double>> data) {

        HashMap<String, HashMap<String, String>> recommendations = new HashMap<>();

        //foreach package
        for(Map.Entry<String, HashMap<String, Double>> pckg : data.entrySet()){
            HashMap<String, String> metricMap = new HashMap<>();

            //foreach packagemetric
            for(Map.Entry<String, Double> metric : pckg.getValue().entrySet()){
                switch (metric.getKey()){
                    case "avgNumMethodsOverridden":
                        if (metric.getValue() > Treshholds.AVGNMO.getValue()){
                            metricMap.put("avgNMO", RecommendationCreator.createRecommendation(PackageRecommendationPromt.AVG_MUM_METHODS_OVERRIDDEN_TOO_HIGH.getPrompt()));
                        }
                        break;
                    case "rma":
                        if (metric.getValue() > Treshholds.RMA.getValue()){
                            metricMap.put("rma", RecommendationCreator.createRecommendation(PackageRecommendationPromt.RMA_TOO_HIGH.getPrompt()));
                        }
                        break;
                    case "dn":
                        if (metric.getValue() > Treshholds.DN.getValue()){
                            metricMap.put("dn", RecommendationCreator.createRecommendation(PackageRecommendationPromt.DN_TOO_HIGH.getPrompt()));
                        }
                        break;
                    case "avgWMC":
                        if (metric.getValue() > Treshholds.AVGWMC.getValue()){
                            metricMap.put("avgWMC", RecommendationCreator.createRecommendation(PackageRecommendationPromt.AVG_WMC_TOO_HIGH.getPrompt()));
                        }
                        break;
                    case "rmi":
                        if (metric.getValue() > Treshholds.RMI.getValue()){
                            metricMap.put("rmi", RecommendationCreator.createRecommendation(PackageRecommendationPromt.RMI_TOO_HIGH.getPrompt()));
                        }
                        break;
                    default:
                        break;

                }
            }
            recommendations.put(pckg.getKey(), metricMap);
        }
        return recommendations;
    }
    public static void writePckgMetrics(String rootPath) throws IOException {
        File rootDir = new File(rootPath);
        File[] subdirs = rootDir.listFiles();

        HashMap<String, HashMap<String, Double>> packageMetrics = new HashMap<>();

        for(File dir : subdirs){
            String path = FileUtils.escapePath(dir.getPath());
            try {
                String outputFilePath = "package_metrics.json";
                System.out.println(dir.getName());
                double avgWMC = AvgWeightedMethodsPerClass.getAvgWMC(path);
                double avgNumMethodsOverridden = AvgNumberOfMethodsOverridden.getAvgNumMethodsOverridden(path);
                double rma = Abstractness.getAbstractness(path);
                //double rma = 1;
                double dn = NormalizedDistanceFromMainSequence.calculateDistanceFromMainSequence(path);
                double rmi = Instability.getInstability(path);
                double numberOfClasses = NumberOfClasses.getNumberOfClasses(path);



                packageMetrics.put(dir.getName(), new HashMap<>());
                packageMetrics.get(dir.getName()).put("avgWMC", avgWMC);
                packageMetrics.get(dir.getName()).put("avgNumMethodsOverridden", avgNumMethodsOverridden);
                packageMetrics.get(dir.getName()).put("rma", rma);
                packageMetrics.get(dir.getName()).put("dn", dn);
                packageMetrics.get(dir.getName()).put("rmi", rmi);
                packageMetrics.get(dir.getName()).put("numberOfClasses", numberOfClasses);


            } catch (Exception e){
                e.printStackTrace();
            }
        }
        JsonFileWriter.writeDataToJsonFile("src/reports/metrics/package_metrics.json", packageMetrics);
        JsonFileWriter.writeDataToJsonFile("src/reports/recommendations/package_recommendations.json", generateRecommendationsForPackage(packageMetrics));
    }
}
