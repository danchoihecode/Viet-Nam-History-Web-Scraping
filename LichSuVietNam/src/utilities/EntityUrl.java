package utilities;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class EntityUrl {
	public static HashSet<String> getEntityUrl(String filePath) {
		HashSet<String> entity = new HashSet<>();
		try (FileReader reader = new FileReader(filePath)) {

			JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

			for (JsonElement jsonElement : jsonArray) {
				JsonObject jsonObject = jsonElement.getAsJsonObject();
				if(jsonObject.has("url")) {
					String url = jsonObject.get("url").getAsString();
					entity.add(url);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return entity;
	}

	public static HashMap<String, String> getEntityUrlAndName(String filePath) {
		HashMap<String, String> entity = new HashMap<>();
		try (FileReader reader = new FileReader(filePath)) {

			JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

			for (JsonElement jsonElement : jsonArray) {
				if (jsonElement.getAsJsonObject().get("url") != null) {
					String url = jsonElement.getAsJsonObject().get("url").getAsString();
					String ten = jsonElement.getAsJsonObject().get("ten").getAsString();
					entity.put(url, ten);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return entity;
	}
}
