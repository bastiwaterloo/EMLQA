package org.example.writers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class JsonFileWriter {
    public static void writeDataToJsonFile(String filepath, HashMap<?, ?> data) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(filepath), data);
        System.out.println("Daten erfolgreich in " + filepath + " geschrieben.");
    }

}
