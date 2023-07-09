package gui;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
	public static void main(String[] args) {
		try {
			// Đường dẫn đến tệp JSON
			String filePath = "C:\\Users\\pc\\Documents\\output.json";

			// Đọc tệp JSON
			JsonArray jsonArray = new Gson().fromJson(new FileReader(filePath), JsonArray.class);

			// Tạo Map để lưu trữ thông tin nhân vật
			Map<String, JsonObject> characterMap = createCharacterMap(jsonArray);

			// Nhập tên nhân vật từ người dùng
			Scanner scanner = new Scanner(System.in);
			System.out.print("Nhập tên nhân vật: ");
			String name = scanner.nextLine();
			scanner.close();

			// Tìm thông tin nhân vật trong Map
			JsonObject characterInfo = characterMap.get(name.toLowerCase());
			if (characterInfo != null) {
				String characterName = characterInfo.get("name").getAsString();
				JsonObject birthYear = characterInfo.getAsJsonObject("birthYear");

				// Kiểm tra số lượng năm sinh
				if (birthYear.size() == 1) {
					// Trường hợp chỉ có một năm sinh
					String year = birthYear.keySet().iterator().next();
					System.out.println("Nhân vật: " + characterName);
					System.out.println("Năm sinh: " + year);
				} else {
					// Trường hợp có nhiều năm sinh
					System.out.println("Nhân vật: " + characterName);
					System.out.print("Năm sinh: ");

					// Liệt kê các năm sinh và nguồn
					List<String> years = new ArrayList<>();
					for (Map.Entry<String, JsonElement> entry : birthYear.entrySet()) {
						String year = entry.getKey();
						JsonArray sources = entry.getValue().getAsJsonArray();
						StringBuilder yearInfo = new StringBuilder(year);
						yearInfo.append(" (Nguồn ");
						for (int i = 0; i < sources.size(); i++) {
							yearInfo.append(sources.get(i).getAsInt());
							if (i < sources.size() - 1) {
								yearInfo.append(",");
							}
						}
						yearInfo.append(")");
						years.add(yearInfo.toString());
					}

					// In ra thông tin năm sinh
					for (int i = 0; i < years.size(); i++) {
						System.out.print(years.get(i));
						if (i < years.size() - 1) {
							System.out.print(", ");
						}
					}
					System.out.println();
				}
			} else {
				System.out.println("Không tìm thấy thông tin cho nhân vật: " + name);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Tạo Map để lưu trữ thông tin nhân vật
	private static Map<String, JsonObject> createCharacterMap(JsonArray jsonArray) {
		Map<String, JsonObject> characterMap = new HashMap<>();
		for (JsonElement jsonElement : jsonArray) {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			String characterName = jsonObject.get("name").getAsString().toLowerCase();
			characterMap.put(characterName, jsonObject);
		}
		return characterMap;
	}
}