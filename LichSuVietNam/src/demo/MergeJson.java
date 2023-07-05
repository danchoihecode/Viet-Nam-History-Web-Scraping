package demo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MergeJson {
	public static void main(String[] args) {
		// Đường dẫn tới file input.json và output.json
		String inputFile = "C:\\Users\\pc\\Documents\\person.json";
		String outputFile = "C:\\Users\\pc\\Documents\\output.json";

		try {
			// Đọc file input.json
			JsonArray jsonArray = new Gson().fromJson(new FileReader(inputFile), JsonArray.class);

			// Tạo map để lưu trữ thông tin theo tên nhân vật
			Map<String, JsonObject> mergedMap = new HashMap<>();

			// Xử lí dữ liệu từ input.json
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
				String name = jsonObject.get("name").getAsString();
				String birthYear = jsonObject.get("birthYear").getAsString();
				int source = jsonObject.get("source").getAsInt();

				// Kiểm tra xem đã tồn tại thông tin về nhân vật trong map hay chưa
				if (mergedMap.containsKey(name)) {
					// Nếu đã tồn tại, thêm thông tin về birthYear và source vào đối tượng đã có
					JsonObject existingObject = mergedMap.get(name);
					JsonObject birthYearObject = existingObject.getAsJsonObject("birthYear");

					if (birthYearObject.has(birthYear)) {
						// Nếu đã tồn tại thông tin về birthYear, thêm source vào danh sách nguồn dữ
						// liệu
						JsonArray sourceArray = birthYearObject.getAsJsonArray(birthYear);
						sourceArray.add(source);
					} else {
						// Nếu chưa tồn tại thông tin về birthYear, tạo mới danh sách nguồn dữ liệu
						JsonArray sourceArray = new JsonArray();
						sourceArray.add(source);
						birthYearObject.add(birthYear, sourceArray);
					}
				} else {
					// Nếu chưa tồn tại, tạo mới đối tượng và thêm vào map
					JsonObject newObject = new JsonObject();
					JsonObject birthYearObject = new JsonObject();
					JsonArray sourceArray = new JsonArray();

					sourceArray.add(source);
					birthYearObject.add(birthYear, sourceArray);
					newObject.addProperty("name", name);
					newObject.add("birthYear", birthYearObject);

					mergedMap.put(name, newObject);
				}
			}

			// Chuyển đổi map thành list chứa các đối tượng đã xử lí
			List<JsonObject> mergedList = new ArrayList<>(mergedMap.values());

			// Ghi ra file output.json
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			FileWriter fileWriter = new FileWriter(outputFile);
			fileWriter.write(gson.toJson(mergedList));
			fileWriter.close();

			System.out.println("Successfully merged JSON data.");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
