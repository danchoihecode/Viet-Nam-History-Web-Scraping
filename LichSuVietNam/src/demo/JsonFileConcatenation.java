package demo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonFileConcatenation {

    public static void main(String[] args) {
        List<String> inputFiles = new ArrayList<>();
        inputFiles.add("file\\site-source-2.json");
        inputFiles.add("file\\site-source-3.json");
        inputFiles.add("file\\site-source-4.json");
        
        String outputFile = "file\\site-concatenated.json";
        jsonFileConcatenation(inputFiles, outputFile);
    }

    public static void jsonFileConcatenation(List<String> inputFiles, String outputFile) {
        JsonArray concatenatedArray = new JsonArray();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        for (String inputFile : inputFiles) {
            try {
                JsonElement jsonElement = JsonParser.parseReader(new FileReader(inputFile));

                if (jsonElement.isJsonArray()) {
                    JsonArray jsonArray = jsonElement.getAsJsonArray();
                    concatenatedArray.addAll(jsonArray);
                }
            } catch (FileNotFoundException e) {
                System.out.println("Không tìm thấy file: " + inputFile);
            }
        }

        try (FileWriter fileWriter = new FileWriter(outputFile)) {
            gson.toJson(concatenatedArray, fileWriter);
            System.out.println("Nối các tệp JSON thành công vào file: " + outputFile);
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi vào file: " + outputFile);
        }
    }
}
