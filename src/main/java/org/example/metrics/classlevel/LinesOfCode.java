package org.example.metrics.classlevel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LinesOfCode {
    public static double getLinesOfCode(String filepath) {
        try {
            double lineCount = countLines(filepath);
            return lineCount;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static double countLines(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            int lines = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                // Entferne führende und abschließende Leerzeichen, dann prüfe, ob die Zeile Leerzeichen enthält
                if (!line.trim().isEmpty()) {
                    lines++;
                }
            }
            return lines;
        }
    }
}
