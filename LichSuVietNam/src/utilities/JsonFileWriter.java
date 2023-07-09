package utilities;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonFileWriter {

	public static void writeJsonToFile(String json, String filePath, boolean append) {
		if (append == false) {
			try (FileWriter writer = new FileWriter(filePath)) {
				writer.write(json);
				System.out.println("Dữ liệu đã được ghi vào file " + filePath);
			} catch (IOException e) {
				System.out.println("Ghi dữ liệu vào file thất bại.");
			}
		}
		else {
			try {

				String existingJson = new String(Files.readAllBytes(Paths.get(filePath)));

				Gson gson2 = new GsonBuilder().setPrettyPrinting().create();

				JsonElement existingJsonElement = JsonParser.parseString(existingJson);
				JsonArray existingJsonArray = existingJsonElement.getAsJsonArray();

				JsonElement newJsonElement = JsonParser.parseString(json);
				JsonArray newJsonArray = newJsonElement.getAsJsonArray();

				existingJsonArray.addAll(newJsonArray);

				FileWriter fileWriter = new FileWriter(filePath);
				gson2.toJson(existingJsonArray, fileWriter);
				fileWriter.close();

				System.out.println("Dữ liệu đã được nối vào file " + filePath);
			} catch (IOException e) {
				System.out.println("Nối dữ liệu vào file thất bại.");
			}
		}
	}
}
