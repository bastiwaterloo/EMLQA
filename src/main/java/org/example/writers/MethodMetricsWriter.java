package org.example.writers;

import org.example.RecommendationCreator;
import org.example.Utils.FileUtils;
import org.example.metrics.methodlevel.CyclomaticComplexity;
import org.example.metrics.methodlevel.LengthOfId;
import org.example.metrics.methodlevel.MethodLinesOfCode;
import org.example.metrics.methodlevel.NumberOfParams;
import org.example.prompts.MethodRecommendationPrompt;
import org.example.thresholds.Treshholds;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MethodMetricsWriter {

    public static void writeMethodMetrics(String rootPath) throws IOException {
        File rootDir = new File(rootPath);
        File[] subdirs = rootDir.listFiles();
        HashMap<String, HashMap<String, HashMap<String, HashMap<String, Double>>>> methodMetrics = new HashMap<>();
        HashMap<String, HashMap<String, HashMap<String, String>>> methodRecommendations = new HashMap<>();

        for(File dir : subdirs) {
            String path = FileUtils.escapePath(dir.getPath());
            methodMetrics.put(dir.getName(), new HashMap<>());


            for (File clazzFile : dir.listFiles()) {
                String filepath = FileUtils.escapePath(clazzFile.getPath());

                methodMetrics.get(dir.getName()).put(clazzFile.getName(), new HashMap<>());
                HashMap<String, Double> methodCCValues = CyclomaticComplexity.getCyclomaticComplexityForMethod(filepath);
                HashMap<String, Double> loid = LengthOfId.getLengthOfIds(filepath);
                Map<String, Double> mloc = MethodLinesOfCode.getMethodLinesOfCode(filepath);
                HashMap<String, Double> nop = NumberOfParams.getNumberOfParams(filepath);

                for(Map.Entry<String, Double> ccValue : methodCCValues.entrySet()){
                    methodMetrics.get(dir.getName()).get(clazzFile.getName()).put(ccValue.getKey(), new HashMap<>());
                    methodMetrics.get(dir.getName()).get(clazzFile.getName()).get(ccValue.getKey()).put("cc", ccValue.getValue());
                    if (ccValue.getValue() > Treshholds.CC.getValue()){

                        if(!methodRecommendations.containsKey(clazzFile.getName())){
                            methodRecommendations.put(clazzFile.getName(), new HashMap<>());
                        }
                        if (!methodRecommendations.get(clazzFile.getName()).containsKey(ccValue.getKey())){
                            methodRecommendations.get(clazzFile.getName()).put(ccValue.getKey(), new HashMap<>());
                        }
                        methodRecommendations.get(clazzFile.getName()).get(ccValue.getKey()).put("CC", RecommendationCreator.createRecommendation(MethodRecommendationPrompt.CC_TOO_HIGH.getPrompt()));
                    }
                }

                for(Map.Entry<String, Double> loidValue : loid.entrySet()){
                    methodMetrics.get(dir.getName()).get(clazzFile.getName()).get(loidValue.getKey()).put("loid", loidValue.getValue());
                    if (loidValue.getValue() < Treshholds.LOID.getValue()){
                        if(!methodRecommendations.containsKey(clazzFile.getName())){
                            methodRecommendations.put(clazzFile.getName(), new HashMap<>());
                        }
                        if (!methodRecommendations.get(clazzFile.getName()).containsKey(loidValue.getKey())) {
                            methodRecommendations.get(clazzFile.getName()).put(loidValue.getKey(), new HashMap<>());
                        }
                        methodRecommendations.get(clazzFile.getName()).get(loidValue.getKey()).put("LOID", RecommendationCreator.createRecommendation(MethodRecommendationPrompt.LOID_TOO_LOW.getPrompt()));

                    }
                }

                for(Map.Entry<String, Double> mlocValue : mloc.entrySet()){
                    methodMetrics.get(dir.getName()).get(clazzFile.getName()).get(mlocValue.getKey()).put("mloc", mlocValue.getValue());
                    if (mlocValue.getValue() > Treshholds.MLOC.getValue()){
                        if(!methodRecommendations.containsKey(clazzFile.getName())){
                            methodRecommendations.put(clazzFile.getName(), new HashMap<>());
                        }
                        if (!methodRecommendations.get(clazzFile.getName()).containsKey(mlocValue.getKey())) {
                            methodRecommendations.get(clazzFile.getName()).put(mlocValue.getKey(), new HashMap<>());
                        }
                        methodRecommendations.get(clazzFile.getName()).get(mlocValue.getKey()).put("MLOC", RecommendationCreator.createRecommendation(MethodRecommendationPrompt.MLOC_TOO_HIGH.getPrompt()));

                    }
                }

                for(Map.Entry<String, Double> nopValue : nop.entrySet()){
                    methodMetrics.get(dir.getName()).get(clazzFile.getName()).get(nopValue.getKey()).put("nop", nopValue.getValue());
                    if (nopValue.getValue() > Treshholds.NOP.getValue()){
                        if(!methodRecommendations.containsKey(clazzFile.getName())){
                            methodRecommendations.put(clazzFile.getName(), new HashMap<>());
                        }
                        if (!methodRecommendations.get(clazzFile.getName()).containsKey(nopValue.getKey())) {
                            methodRecommendations.get(clazzFile.getName()).put(nopValue.getKey(), new HashMap<>());
                        }
                        methodRecommendations.get(clazzFile.getName()).get(nopValue.getKey()).put("NOP", RecommendationCreator.createRecommendation(MethodRecommendationPrompt.NOP_TOO_HIGH.getPrompt()));
                    }
                }

            }
        }
        JsonFileWriter.writeDataToJsonFile("src/reports/metrics/method_metrics.json", methodMetrics);
        JsonFileWriter.writeDataToJsonFile("src/reports/recommendations/method_recommendations.json", methodRecommendations);

    }

}
