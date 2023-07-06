package demo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Concatenation {
	public static void main(String[] args) {
		try {
			JsonArray resultArray = new JsonArray();

			// Đường dẫn các tệp JSON cần nối lại

			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			for (int i = 0; i <= 31; i++) {
				// Đọc dữ liệu từ tệp JSON
				String content = new String(Files.readAllBytes(
						Paths.get("file\\figure-source-2-" + i
								+ ".json")));

				// Phân tích dữ liệu JSON thành JsonArray
				JsonElement jsonElement = JsonParser.parseString(content);
				JsonArray jsonArray = jsonElement.getAsJsonArray();

				// Nối các phần tử của JsonArray vào kết quả
				for (JsonElement element : jsonArray) {
					resultArray.add(element);
				}
			}

			// Ghi kết quả vào tệp mới
			FileWriter fileWriter = new FileWriter(
					"file\\figure-source-2.json");
			gson.toJson(resultArray, fileWriter);
			fileWriter.close();

			System.out.println("Đã nối các tệp JSON thành công!");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
