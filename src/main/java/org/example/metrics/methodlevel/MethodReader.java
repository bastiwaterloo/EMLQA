package org.example.metrics.methodlevel;

import org.example.Utils.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodReader {

    public static List<String> getClassMethods(String filepath) {
        try {
            String javaCodeString = FileUtils.readJavaFile(filepath);

            List<String> abstractMethods = extractAbstractMethods(javaCodeString);
            List<String> implementedMethdos = extractImplementedMethods(javaCodeString);
            List<String> allMethods = new ArrayList<>();
            allMethods.addAll(abstractMethods);
            allMethods.addAll(implementedMethdos);
            return allMethods;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Funktion zur Extraktion aller Abstrakten bzw. nicht implementierten Methoden
    private static List<String> extractAbstractMethods(String javaCodeString) {
        // Regex-Pattern für die Extraktion aller Methoden
        String methodRegex = "(public|private|protected|static|final|native|synchronized|abstract|transient)\\s+(abstract|synchronized|transient|native|final)?\\s+[a-zA-Z0-9]+\\[?\\]?\\s+[a-zA-Z0-9]+\\s*\\(\\);";

        Pattern pattern = Pattern.compile(methodRegex);
        Matcher matcher = pattern.matcher(javaCodeString);

        List<String> methods = new ArrayList<>();
        StringBuilder extractedMethods = new StringBuilder();

        while (matcher.find()) {
            String methodContent = matcher.group(); // Kompletter Inhalt der gefundenen Methode
            extractedMethods.append(methodContent).append("\n\n"); // Hinzufügen zum Ergebnis mit Zeilenumbruch
            methods.add(methodContent);
        }

        return methods;
    }

    // Funktion zur Extraktion aller Methoden aus dem Java-Code-String
    private static List<String> extractImplementedMethods(String javaCodeString) {
        // Regex-Pattern für die Extraktion aller Methoden
        String methodRegex = "(?:(?:public|private|protected|static|final|native|synchronized|abstract|transient)+\\s+)+[$_\\w<>\\[\\]\\s]*\\s+[\\$_\\w]+\\([^\\)]*\\)?\\s*\\{?[^\\}]*\\}?";

        Pattern pattern = Pattern.compile(methodRegex);
        Matcher matcher = pattern.matcher(javaCodeString);

        List<String> methods = new ArrayList<>();
        StringBuilder extractedMethods = new StringBuilder();

        while (matcher.find()) {
            String methodContent = matcher.group(); // Kompletter Inhalt der gefundenen Methode
            extractedMethods.append(methodContent).append("\n\n"); // Hinzufügen zum Ergebnis mit Zeilenumbruch
            methods.add(methodContent);
        }

        return methods;
    }
}
