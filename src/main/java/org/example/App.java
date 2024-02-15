package org.example;

import org.example.metrics.classlevel.DataAccessingMetric;
import org.example.metrics.classlevel.DepthOfInheritanceTree;
import org.example.metrics.classlevel.NumberOfAttributes;
import org.example.metrics.classlevel.NumberOfMethods;
import org.example.metrics.packagelevel.*;
import org.example.writers.ClassMetricsWriter;
import org.example.writers.MethodMetricsWriter;
import org.example.writers.PackageMetricWriter;

import java.io.IOException;
import java.util.HashMap;


/**
 * EMLQA is a tool to calculate oo-metrics for java code and calculate a tqi.
 * all the results are sent to openAI which return suggestions to improve the code quality
 */
public class App {
    public static void main(String[] args) throws IOException {
        //set information about project location
        String rootPath = "Path/To/Project";
        String rootPath2 = "Path/To/Any/Package";
        //write the metrics to reports folder
        PackageMetricWriter.writePckgMetrics(rootPath);
        ClassMetricsWriter.writeClassMetrics(rootPath);
        MethodMetricsWriter.writeMethodMetrics(rootPath);

        //calculate Design Characteristics
        double dsc = NumberOfClasses.getNumberOfClasses(rootPath2);
        double dit = DepthOfInheritanceTree.calculateAvgDIT(rootPath2);
        double rma = Abstractness.getAbstractness(rootPath2);
        double encapsulation = DataAccessingMetric.getAvgDAM(rootPath2);
        double rmi = Instability.getInstability(rootPath2);
        double cohesion = 1 / AvgNumberOfMethodsOverridden.getAvgNumMethodsOverridden(rootPath2);
        double noa = NumberOfAttributes.calculateAvgNOA(rootPath2);
        double inheritance = 1 - (AvgNumberOfMethodsOverridden.getAvgNumMethodsOverridden(rootPath2) / NumberOfMethods.calculateAvgNOM(rootPath2));
        double nmo = AvgNumberOfMethodsOverridden.getAvgNumMethodsOverridden(rootPath2);
        double nom = NumberOfMethods.calculateAvgNOM(rootPath2);
        double wmc = AvgWeightedMethodsPerClass.getAvgWMC(rootPath2);

        //calculate Quality Attributes
        HashMap<String, Double> qualityAttributes = TQICalculator.calculateTQI(dsc, dit, rma, encapsulation, rmi, cohesion, noa, inheritance, nmo, nom, wmc);
        qualityAttributes.forEach((qualityAttribute, value) -> {
            System.out.println(qualityAttribute + ": " + value);
        });

    }
}

