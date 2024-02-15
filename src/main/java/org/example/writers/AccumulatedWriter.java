package org.example.writers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.RecommendationCreator;
import org.example.Utils.FileUtils;
import org.example.metrics.classlevel.*;
import org.example.metrics.methodlevel.CyclomaticComplexity;
import org.example.metrics.methodlevel.LengthOfId;
import org.example.metrics.methodlevel.MethodLinesOfCode;
import org.example.metrics.methodlevel.NumberOfParams;
import org.example.metrics.packagelevel.*;
import org.example.prompts.ClassRecommendationPrompt;
import org.example.prompts.MethodRecommendationPrompt;
import org.example.prompts.PackageRecommendationPromt;
import org.example.thresholds.Treshholds;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class AccumulatedWriter {

    private static void writeDataToJsonFile(String filepath, HashMap<?, ?> data) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(filepath), data);
        System.out.println("Daten erfolgreich in " + filepath + " geschrieben.");
    }

    public static void writeMetricsAccumulated(String rootPath) throws IOException {
        File rootDir = new File(rootPath);
        File[] subdirs = rootDir.listFiles();

        HashMap<String, HashMap<String, Double>> problematicPckg = new HashMap<>();
        HashMap<String, HashMap<String, Double>> problematicClass = new HashMap<>();
        HashMap<String, HashMap<String, Double>> problematicMethod = new HashMap<>();

        HashMap<String, HashMap<String, String>> pckgRecommendations = new HashMap<>();
        HashMap<String, HashMap<String, String>> classRecommendations = new HashMap<>();
        HashMap<String, HashMap<String, String>> methodRecommendations = new HashMap<>();

        HashMap<String, HashMap<String, Double>> packageMetrics = new HashMap<>();
        HashMap<String, HashMap<String, HashMap<String, Double>>> classMetrics = new HashMap<>();
        HashMap<String, HashMap<String, HashMap<String, HashMap<String, Double>>>> methodMetrics = new HashMap<>();
        int counter = 1;
        for(File dir : subdirs){
            String path = FileUtils.escapePath(dir.getPath());
            classMetrics.put(dir.getName(), new HashMap<>());
            methodMetrics.put(dir.getName(), new HashMap<>());
            System.out.println("Processing " + dir.getName() + " (" + counter + "/" + subdirs.length + ")");
            counter++;
            problematicPckg.put(dir.getName(), new HashMap<>());
            pckgRecommendations.put(dir.getName(), new HashMap<>());


            try {
                double avgWMC = AvgWeightedMethodsPerClass.getAvgWMC(path);
                if(avgWMC > Treshholds.AVGWMC.getValue()){
                    problematicPckg.get(dir.getName()).put("AVGWMC", avgWMC);
                    pckgRecommendations.get(dir.getName()).put("AVGWMC",RecommendationCreator.createRecommendation(PackageRecommendationPromt.AVG_WMC_TOO_HIGH.getPrompt()));
                }
                double avgNumMethodsOverridden = AvgNumberOfMethodsOverridden.getAvgNumMethodsOverridden(path);
                if(avgNumMethodsOverridden > Treshholds.AVGNMO.getValue()){
                    problematicPckg.get(dir.getName()).put("AVGNMO", avgNumMethodsOverridden);
                    pckgRecommendations.get(dir.getName()).put("AVGNMO", RecommendationCreator.createRecommendation(PackageRecommendationPromt.AVG_MUM_METHODS_OVERRIDDEN_TOO_HIGH.getPrompt()));
                }
                double rma = Abstractness.getAbstractness(path);
                if(rma > Treshholds.RMA.getValue()){
                    problematicPckg.get(dir.getName()).put("RMA", rma);
                    pckgRecommendations.get(dir.getName()).put("RMA", RecommendationCreator.createRecommendation(PackageRecommendationPromt.RMA_TOO_HIGH.getPrompt()));
                }
                double dn = NormalizedDistanceFromMainSequence.calculateDistanceFromMainSequence(path);
                if(dn > Treshholds.DN.getValue()){
                    problematicPckg.get(dir.getName()).put("DN", dn);
                    pckgRecommendations.get(dir.getName()).put("DN", RecommendationCreator.createRecommendation(PackageRecommendationPromt.DN_TOO_HIGH.getPrompt()));
                }
                double rmi = Instability.getInstability(path);
                if(rmi > Treshholds.RMI.getValue()){
                    problematicPckg.get(dir.getName()).put("RMI", rmi);
                    pckgRecommendations.get(dir.getName()).put("RMI", RecommendationCreator.createRecommendation(PackageRecommendationPromt.RMI_TOO_HIGH.getPrompt()));
                }
                double numberOfClasses = NumberOfClasses.getNumberOfClasses(path);

                packageMetrics.put(dir.getName(), new HashMap<>());
                packageMetrics.get(dir.getName()).put("avgWMC", avgWMC);
                packageMetrics.get(dir.getName()).put("avgNumMethodsOverridden", avgNumMethodsOverridden);
                packageMetrics.get(dir.getName()).put("rma", rma);
                packageMetrics.get(dir.getName()).put("dn", dn);
                packageMetrics.get(dir.getName()).put("rmi", rmi);
                packageMetrics.get(dir.getName()).put("numberOfClasses", numberOfClasses);

                int filecounter = 1;
                for(File clazzFile : dir.listFiles()){
                    problematicClass.put(clazzFile.getName(), new HashMap<>());
                    System.out.println("File: " + clazzFile.getName() + " " + filecounter + "/" + dir.listFiles().length);
                    filecounter++;
                    String filepath = FileUtils.escapePath(clazzFile.getPath());
                    classRecommendations.put(clazzFile.getName(), new HashMap<>());

                    double loc = LinesOfCode.getLinesOfCode(filepath);
                    if (loc > Treshholds.LOC.getValue()){
                        problematicClass.get(clazzFile.getName()).put("LOC", loc);
                        classRecommendations.get(clazzFile.getName()).put("LOC", RecommendationCreator.createRecommendation(ClassRecommendationPrompt.LOC_TOO_HIGH.getPrompt()));
                    }
                    double avgCC = AvgCyclomaticComplexity.getAvgCyclomaticComplexity(filepath);
                    if (avgCC > Treshholds.AVGCC.getValue()){
                        problematicClass.get(clazzFile.getName()).put("AVGCC", avgCC);
                        classRecommendations.get(clazzFile.getName()).put("AVGCC", RecommendationCreator.createRecommendation(ClassRecommendationPrompt.AVGCC_TOO_HIGH.getPrompt()));

                    }
                    double nom = NumberOfMethods.getNumberOfMethods(filepath);
                    if (nom > Treshholds.NOM.getValue()){
                        problematicClass.get(clazzFile.getName()).put("NOM", nom);
                        classRecommendations.get(clazzFile.getName()).put("NOM", RecommendationCreator.createRecommendation(ClassRecommendationPrompt.NOM_TOO_HIGH.getPrompt()));

                    }
                    double noc = NumberOfChildren.getNumberOfChildren(path, FileUtils.getClassNameFromFilepath(filepath));
                    if (noc > Treshholds.NOC.getValue()){
                        problematicClass.get(clazzFile.getName()).put("NOC", noc);
                        classRecommendations.get(clazzFile.getName()).put("NOC", RecommendationCreator.createRecommendation(ClassRecommendationPrompt.NOC_TOO_HIGH.getPrompt()));

                    }
                    double nmo = NumberOfOverriddenMethods.getNumberOfOverriddenMethods(filepath);
                    if (nmo > Treshholds.NMO.getValue()){
                        problematicClass.get(clazzFile.getName()).put("NMO", nmo);
                        classRecommendations.get(clazzFile.getName()).put("NMO", RecommendationCreator.createRecommendation(ClassRecommendationPrompt.NMO_TOO_HIGH.getPrompt()));

                    }
                    double wmc = WeightedMethodsPerClass.getWeightedMethodsPerClass(filepath);
                    if (wmc > Treshholds.WMC.getValue()){
                        problematicClass.get(clazzFile.getName()).put("WMC", wmc);
                        classRecommendations.get(clazzFile.getName()).put("WMC", RecommendationCreator.createRecommendation(ClassRecommendationPrompt.WMC_TOO_HIGH.getPrompt()));

                    }
                    double dit = DepthOfInheritanceTree.calculateDIT(filepath);
                    if (dit > Treshholds.DIT.getValue()){
                        problematicClass.get(clazzFile.getName()).put("DIT", dit);
                        classRecommendations.get(clazzFile.getName()).put("DIT", RecommendationCreator.createRecommendation(ClassRecommendationPrompt.DIT_TOO_HIGH.getPrompt()));

                    }
                    double avgLengthOfId = AvgLengthOfId.getAvgLengthOfId(filepath);
                    if (avgLengthOfId < Treshholds.AVGLOID.getValue()){
                        problematicClass.get(clazzFile.getName()).put("AVGLOID", avgLengthOfId);
                        classRecommendations.get(clazzFile.getName()).put("AVGLOID", RecommendationCreator.createRecommendation(ClassRecommendationPrompt.AVGLOID_TOO_HIGH.getPrompt()));

                    }
                    double lcom = LackOfCohesionInMethods.getLCOM(filepath);
                    if (lcom > Treshholds.LCOM.getValue()){
                        problematicClass.get(clazzFile.getName()).put("LCOM", lcom);
                        classRecommendations.get(clazzFile.getName()).put("LCOM", RecommendationCreator.createRecommendation(ClassRecommendationPrompt.LCOM_TOO_HIGH.getPrompt()));

                    }

                    classMetrics.get(dir.getName()).put(clazzFile.getName(), new HashMap<>());
                    classMetrics.get(dir.getName()).get(clazzFile.getName()).put("loc", loc);
                    classMetrics.get(dir.getName()).get(clazzFile.getName()).put("avgCC", avgCC);
                    classMetrics.get(dir.getName()).get(clazzFile.getName()).put("nom", nom);
                    classMetrics.get(dir.getName()).get(clazzFile.getName()).put("noc", noc);
                    classMetrics.get(dir.getName()).get(clazzFile.getName()).put("nmo", nmo);
                    classMetrics.get(dir.getName()).get(clazzFile.getName()).put("wmc", wmc);
                    classMetrics.get(dir.getName()).get(clazzFile.getName()).put("dit", dit);
                    classMetrics.get(dir.getName()).get(clazzFile.getName()).put("avgLengthOfId", avgLengthOfId);
                    classMetrics.get(dir.getName()).get(clazzFile.getName()).put("lcom", lcom);

                    methodMetrics.get(dir.getName()).put(clazzFile.getName(), new HashMap<>());
                    HashMap<String, Double> methodCCValues = CyclomaticComplexity.getCyclomaticComplexityForMethod(filepath);
                    HashMap<String, Double> loid = LengthOfId.getLengthOfIds(filepath);
                    Map<String, Double> mloc = MethodLinesOfCode.getMethodLinesOfCode(filepath);
                    HashMap<String, Double> nop = NumberOfParams.getNumberOfParams(filepath);

                    for(Map.Entry<String, Double> ccValue : methodCCValues.entrySet()){
                        methodMetrics.get(dir.getName()).get(clazzFile.getName()).put(ccValue.getKey(), new HashMap<>());
                        methodMetrics.get(dir.getName()).get(clazzFile.getName()).get(ccValue.getKey()).put("cc", ccValue.getValue());
                        if (ccValue.getValue() > Treshholds.CC.getValue()){
                            if (!problematicMethod.containsKey(ccValue.getKey())){
                                problematicMethod.put(ccValue.getKey(), new HashMap<>());
                            }
                            if (!methodRecommendations.containsKey(ccValue.getKey())){
                                methodRecommendations.put(ccValue.getKey(), new HashMap<>());
                            }

                            problematicMethod.get(ccValue.getKey()).put("CC", ccValue.getValue());
                            methodRecommendations.get(ccValue.getKey()).put("CC", RecommendationCreator.createRecommendation(MethodRecommendationPrompt.CC_TOO_HIGH.getPrompt()));
                        }
                    }

                    for(Map.Entry<String, Double> loidValue : loid.entrySet()){
                        methodMetrics.get(dir.getName()).get(clazzFile.getName()).get(loidValue.getKey()).put("loid", loidValue.getValue());
                        if (loidValue.getValue() < Treshholds.LOID.getValue()){
                            if (!problematicMethod.containsKey(loidValue.getKey())) {
                                problematicMethod.put(loidValue.getKey(), new HashMap<>());
                            }
                            if (!methodRecommendations.containsKey(loidValue.getKey())) {
                                methodRecommendations.put(loidValue.getKey(), new HashMap<>());
                            }
                            problematicMethod.get(loidValue.getKey()).put("LOID", loidValue.getValue());
                            methodRecommendations.get(loidValue.getKey()).put("LOID", RecommendationCreator.createRecommendation(MethodRecommendationPrompt.LOID_TOO_LOW.getPrompt()));

                        }
                    }

                    for(Map.Entry<String, Double> mlocValue : mloc.entrySet()){
                        methodMetrics.get(dir.getName()).get(clazzFile.getName()).get(mlocValue.getKey()).put("mloc", mlocValue.getValue());
                        if (mlocValue.getValue() > Treshholds.MLOC.getValue()){
                            if (!problematicMethod.containsKey(mlocValue.getKey())) {
                                problematicMethod.put(mlocValue.getKey(), new HashMap<>());
                            }
                            if (!methodRecommendations.containsKey(mlocValue.getKey())) {
                                methodRecommendations.put(mlocValue.getKey(), new HashMap<>());
                            }
                            problematicMethod.get(mlocValue.getKey()).put("MLOC", mlocValue.getValue());
                            methodRecommendations.get(mlocValue.getKey()).put("MLOC", RecommendationCreator.createRecommendation(MethodRecommendationPrompt.MLOC_TOO_HIGH.getPrompt()));

                        }
                    }

                    for(Map.Entry<String, Double> nopValue : nop.entrySet()){
                        methodMetrics.get(dir.getName()).get(clazzFile.getName()).get(nopValue.getKey()).put("nop", nopValue.getValue());
                        if (nopValue.getValue() > Treshholds.NOP.getValue()){
                            if (!problematicMethod.containsKey(nopValue.getKey())) {
                                problematicMethod.put(nopValue.getKey(), new HashMap<>());
                            }
                            if (!methodRecommendations.containsKey(nopValue.getKey())) {
                                methodRecommendations.put(nopValue.getKey(), new HashMap<>());
                            }
                            problematicMethod.get(nopValue.getKey()).put("NOP", nopValue.getValue());
                            methodRecommendations.get(nopValue.getKey()).put("NOP", RecommendationCreator.createRecommendation(MethodRecommendationPrompt.NOP_TOO_HIGH.getPrompt()));
                        }
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        String outputFilePathPackage = "src/reports/metrics/package_metrics.json";
        String outputFilePathClass = "src/reports/metrics/class_metrics.json";
        String outputFilePathMethod = "src/reports/metrics/method_metrics.json";

        String ouputProblemsPathPckg = "src/reports/problems/problems_package.json";
        String ouputProblemsPathClass = "src/reports/problems/problems_classes.json";
        String ouputProblemsPathMethod = "src/reports/problems/problems_methods.json";

        String outputRecommendationsPckgPath = "src/reports/recommendations/recommendationss_package.json";
        String outputRecommendationsClassPath = "src/reports/recommendations/recommendations_class.json";
        String outputRecommendationsMethodPath = "src/reports/recommendations/recommendations_method.json";

        writeDataToJsonFile(outputFilePathPackage, packageMetrics);
        writeDataToJsonFile(outputFilePathClass, classMetrics);
        writeDataToJsonFile(outputFilePathMethod, methodMetrics);

        writeDataToJsonFile(ouputProblemsPathPckg, problematicPckg);
        writeDataToJsonFile(ouputProblemsPathClass, problematicClass);
        writeDataToJsonFile(ouputProblemsPathMethod, problematicMethod);

        writeDataToJsonFile(outputRecommendationsPckgPath, pckgRecommendations);
        writeDataToJsonFile(outputRecommendationsClassPath, classRecommendations);
        writeDataToJsonFile(outputRecommendationsMethodPath, methodRecommendations);

    }


}
