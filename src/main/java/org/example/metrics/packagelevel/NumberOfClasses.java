package org.example.metrics.packagelevel;

import java.io.File;

public class NumberOfClasses {
    public static double countJavaClasses(String directoryPath) {
        File dir = new File(directoryPath);
        File[] files = dir.listFiles();

        if (files == null) {
            return 0;
        }

        double javaClassCount = 0;
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".java")) {
                // Überprüfen, ob die Datei eine Java-Klasse ist
                if (containsClassDeclaration(file)) {
                    javaClassCount++;
                }
            }
        }

        return javaClassCount;
    }

    // Überprüfen, ob die Datei eine Java-Klasse enthält
    private static boolean containsClassDeclaration(File file) {
        // Hier können Sie eine Logik hinzufügen, um festzustellen, ob die Datei eine Java-Klasse ist.
        // Zum Beispiel: Überprüfen Sie, ob sie eine Klasse oder ein Interface deklariert.
        // In diesem Beispiel wird eine einfache Logik verwendet, um den Text "class" im Inhalt zu suchen.

        try {
            java.util.Scanner scanner = new java.util.Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains("class ")) {
                    scanner.close();
                    return true;
                }
            }
            scanner.close();
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static double getNumberOfClasses(String directoryPath) {
        return countJavaClasses(directoryPath);
    }
}
