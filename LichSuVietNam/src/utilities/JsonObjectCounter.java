package utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;

public class JsonObjectCounter {
	public static void main(String[] args) {
		try {
			// Đường dẫn tới tệp JSON
			String filePath = "file\\site-source-2.json";

			// Đọc tệp JSON
			JsonElement jsonElement = JsonParser.parseReader(new FileReader(filePath));

			// Kiểm tra nếu tệp JSON là một mảng
			if (jsonElement.isJsonArray()) {
				JsonArray jsonArray = jsonElement.getAsJsonArray();
				int count = jsonArray.size();
				System.out.println("Số lượng đối tượng JSON: " + count);
			} else {
				System.out.println("Tệp JSON không phải là một mảng.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
