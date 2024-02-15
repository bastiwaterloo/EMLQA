package org.example.Utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    // Funktion zum Lesen des Inhalts einer Java-Datei als String
    public static String readJavaFile(String filePath) throws Exception {
        Path path = Paths.get(filePath);
        return Files.readString(path);
    }

    public static String getClassNameFromFilepath(String input) {
        int lastSlashIndex = input.lastIndexOf('\\');

        if (lastSlashIndex != -1 && lastSlashIndex < input.length() - 1) {
            String fileName = input.substring(lastSlashIndex + 1);
            int lastDotIndex = fileName.lastIndexOf('.');
            String className = fileName.substring(0, lastDotIndex);
            return className;
        } else {
            // Falls kein Punkt gefunden wird oder der Punkt am Ende des Strings steht
            int lsi = input.lastIndexOf('/');
            if (lsi != -1 && lsi < input.length() - 1) {
                String fileName = input.substring(lsi + 1);
                int lastDotIndex = fileName.lastIndexOf('.');
                if (lastDotIndex <= fileName.length() && lastDotIndex >= 0){
                    return fileName.substring(0, lastDotIndex);
                }
                return fileName;
            } else {
                return input;
            }
        }
    }

    public static String getPathFromFilePath(String filepath){
        String escPath = escapePath(filepath);
        int lio = escPath.lastIndexOf("/");
        String path = escPath.substring(0, lio + 1);
        return path;
    }

    public static String escapePath(String path){
        return path.replace("\\", "/");
    }
}
