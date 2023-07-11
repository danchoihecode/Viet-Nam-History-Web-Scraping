package utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MergeEvent {

	private static final String[] ATTRIBUTES = {"thoiGian", "thoiKy", "diaDiem", "mieuTa", "nguyenNhan", "ketQua",
			"nhanVatLienQuan", "thamChien", "chiHuy", "lucLuong", "tonThat", "url"};

	public static void main(String[] args) {
		// Đường dẫn tới file input và output
		String inputFile = "file\\event-concatenated.json";
		String outputFile = "file\\event.json";

		try {
			// Đọc file input
			JsonArray jsonArray = new Gson().fromJson(new FileReader(inputFile), JsonArray.class);

			// Tạo map để lưu trữ thông tin, lấy tên lễ hội làm khóa
			// Map<Tên lễ hội, đối tượng lễ hội>
			Map<String, JsonObject> mergedMap = new HashMap<>();

			// Xử lý dữ liệu từ input
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
				String ten = jsonObject.get("ten").getAsString();
				int nguon = jsonObject.get("nguon").getAsInt();

				if (mergedMap.containsKey(ten)) {
					JsonObject existingObject = mergedMap.get(ten);
					mergeAttributes(existingObject, jsonObject, nguon);
					System.out.println(ten);
				} else {
					JsonObject newObject = new JsonObject();
					newObject.addProperty("ten", ten);
					mergeAttributes(newObject, jsonObject, nguon);
					mergedMap.put(ten, newObject);
				}
			}

			// Chuyển đổi map thành list chứa các đối tượng đã xử lý
			List<JsonObject> mergedList = new ArrayList<>(mergedMap.values());

			// Ghi ra file output
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			FileWriter fileWriter = new FileWriter(outputFile);
			fileWriter.write(gson.toJson(mergedList));
			fileWriter.close();

			System.out.println("Hợp nhất dữ liệu thành công.");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void mergeAttributes(JsonObject targetObject, JsonObject sourceObject, int nguon) {
		for (String attribute : ATTRIBUTES) {
			if (sourceObject.has(attribute)) {
				JsonElement attributeValue = sourceObject.get(attribute);
				if(attribute.equals("thamChien") || attribute.equals("chiHuy")) {
					merge2ArrayListAttribute(targetObject, attribute, attributeValue);
				}
				else if (attribute.equals("nhanVatLienQuan") || attribute.equals("lucLuong")|| attribute.equals("tonThat")) {
					mergeArrayListAttribute(targetObject, attribute, attributeValue);
				} else {
					mergeAttributeValue(targetObject, attribute, attributeValue, nguon);
				}
			}
		}
	}

	private static void merge2ArrayListAttribute(JsonObject targetObject, String attribute, JsonElement attributeValue) {
		JsonArray targetArray = targetObject.has(attribute) ? targetObject.get(attribute).getAsJsonArray() : new JsonArray();
		Set<Set<String>> mergedArray = new HashSet<>();
		
		for (JsonElement element : targetArray) {
			JsonArray ele = element.getAsJsonArray();
			Set<String> mergedSet = new HashSet<>();
			for(JsonElement e:ele) {
				mergedSet.add(e.getAsString());
			}
			mergedArray.add(mergedSet);
		}

		JsonArray sourceArray = attributeValue.getAsJsonArray();
		for (JsonElement element : sourceArray) {
			JsonArray ele = element.getAsJsonArray();
			Set<String> mergedSet = new HashSet<>();
			for(JsonElement e:ele) {
				mergedSet.add(e.getAsString());
			}
			mergedArray.add(mergedSet);
		}

		JsonArray mergedArr = new JsonArray();
		for (Set<String> arr : mergedArray) {
			JsonArray eleArray = new JsonArray();
			for(String value:arr) {
				eleArray.add(value);
			}
			mergedArr.add(eleArray);
		}

		targetObject.add(attribute, mergedArr);
	}
	
	private static void mergeArrayListAttribute(JsonObject targetObject, String attribute, JsonElement attributeValue) {
		JsonArray targetArray = targetObject.has(attribute) ? targetObject.get(attribute).getAsJsonArray() : new JsonArray();
		Set<String> mergedSet = new HashSet<String>();

		for (JsonElement element : targetArray) {
			mergedSet.add(element.getAsString());
		}

		JsonArray sourceArray = attributeValue.getAsJsonArray();
		for (JsonElement element : sourceArray) {
			mergedSet.add(element.getAsString());
		}

		JsonArray mergedArray = new JsonArray();
		for (String value : mergedSet) {
			mergedArray.add(value);
		}

		targetObject.add(attribute, mergedArray);
	}

	private static void mergeAttributeValue(JsonObject targetObject, String attribute, JsonElement attributeValue, int nguon) {
		if (targetObject.has(attribute)) {
			JsonObject attributeObject = targetObject.getAsJsonObject(attribute);
			mergeAttributeValue(attributeObject, attributeValue, nguon);
		} else {
			JsonObject attributeObject = new JsonObject();
			mergeAttributeValue(attributeObject, attributeValue, nguon);
			targetObject.add(attribute, attributeObject);
		}
	}

	private static void mergeAttributeValue(JsonObject attributeObject, JsonElement attributeValue, int nguon) {
		String attributeValueAsString = attributeValue.getAsString();
		JsonArray nguonArray;

		if (attributeObject.has(attributeValueAsString)) {
			nguonArray = attributeObject.getAsJsonArray(attributeValueAsString);
		} else {
			nguonArray = new JsonArray();
			attributeObject.add(attributeValueAsString, nguonArray);
		}

		nguonArray.add(nguon);
	}
}
