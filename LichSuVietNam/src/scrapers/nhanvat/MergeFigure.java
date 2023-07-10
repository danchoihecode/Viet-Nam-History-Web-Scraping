
package scrapers.nhanvat;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonPrimitive;

public class MergeFigure {

	private static final String[] ATTRIBUTES = { "namSinh", "namMat", "tenKhac", "queQuan", "thoiKy", "mieuTa",
			"suKienLienQuan", "tacPham", "anh", "nguon", "url", "theThu", "thoiGianTaiVi" };

	public static void main(String args[]) {

		String inputFile = "file\\figure-concatenated.json";
		String outputFile = "file\\figure.json";

		try {
			JsonArray jsonArray = new Gson().fromJson(new FileReader(inputFile), JsonArray.class);
			Map<String, JsonObject> firstMergedMap = new HashMap<>();
			Map<String, JsonObject> finalMergedList = new HashMap<>();
			ArrayList<JsonObject> firstMergedList = new ArrayList<>();

			// Create the firstMergedMap
			// int count = 0;
			for (int i = 0; i < jsonArray.size(); i++) {
				// count++;
				// System.out.println(count);

				try {
					JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
					String ten = jsonObject.get("ten").getAsString();
					int nguon = jsonObject.get("nguon").getAsInt();
					// System.out.print(ten);
					JsonArray tenKhacArray = null;
					if (jsonObject.has("tenKhac")) {
						tenKhacArray = jsonObject.getAsJsonArray("tenKhac");

						ArrayList<String> tenKhac = new ArrayList<>();
						tenKhac.add(ten);
						firstMergedMap.put(ten, jsonObject);
						firstMergedList.add(jsonObject);
						System.out.print(firstMergedMap.get(ten).get("ten"));
						// Store the JSON object directly using "ten" as the key

						for (int j = 0; j < tenKhacArray.size(); j++) {
							String value = tenKhacArray.get(j).getAsString();
							tenKhac.add(value);
							jsonObject.addProperty("ten", value);
							firstMergedMap.put(value, jsonObject);
							System.out.printf(value);
							firstMergedList.add(jsonObject);// Store the same JSON object for each "tenKhac" value
						}
					} else {
	                    String ten1 = jsonObject.get("ten").getAsString();
	                    int nguon1 = jsonObject.get("nguon").getAsInt();

	                    if (!firstMergedMap.containsKey(ten1)) {
	                        firstMergedMap.put(ten1, jsonObject);
	                        firstMergedList.add(jsonObject);
	                    } else {
	                        JsonObject existingObject = firstMergedMap.get(ten1);
	                        mergeAttributes(existingObject, jsonObject, nguon1);
	                    }
					}
				} catch (NullPointerException e) {
					System.out.printf("There is a NullPointerException at id" /* + count */);

				}
				;
				/*
				 * if (tenKhacArray != null) { for (int j = 0; j < tenKhacArray.size(); j++) {
				 * String value = tenKhacArray.get(j).getAsString(); tenKhac.add(value); } }
				 * ArrayList<String> tenKhac = new ArrayList<>(); tenKhac.add(ten);
				 * firstMergedMap.put(tenKhac.get(0), jsonObject); for (int j = 0; j <
				 * tenKhacArray.size(); j++) { String value = tenKhacArray.get(j).getAsString();
				 * tenKhac.add(value); firstMergedMap.put(value, jsonObject); }
				 */
				// use the firstMergedMap to create the finalMergedMap
			}

			for (int i = 0; i < firstMergedList.size(); i++) {
				System.out.printf("i = " + i);
				// get the object from the data
				JsonObject finalJsonObject = firstMergedList.get(i);
				System.out.println(firstMergedList.get(i));
				// get the ten and nguon from the data
				String ten = finalJsonObject.get("ten").getAsString();
				int nguon = finalJsonObject.get("nguon").getAsInt();
				// get the tenKhac from the object and make an array consisting of
				// both the ten and the tenKhac for checking.
				ArrayList<String> finalTenKhac = new ArrayList<>();
				try {
					JsonArray finalTenKhacArray = finalJsonObject.get("tenKhac").getAsJsonArray();
					System.out.println(finalTenKhacArray);
					finalTenKhac.add(ten);
					System.out.println(finalTenKhac);

					for (int j = 0; j < finalTenKhacArray.size(); j++) {
						String value = finalTenKhacArray.get(j).getAsString();
						finalTenKhac.add(value);
					}
					System.out.println(finalTenKhac);
					ArrayList<String> tenKhacHash = finalTenKhac;
					tenKhacHash.add(ten);
					// loop through the array to check if it exists in the finalMergedMap
					for (int j = 0; j < tenKhacHash.size(); j++)
						// if there is one gets the hash element and merge it,
						if (finalMergedList.containsKey(tenKhacHash.get(j))) {
							// first get the hash element
							JsonObject existingObject = finalMergedList.get(tenKhacHash.get(j));
							// then gets its tenKhac elements
							for (int k = 0; k < existingObject.getAsJsonArray("tenKhac").size(); k++) {
								// if unique TenKhac existing, add to finalTenKhac array
								if (finalTenKhac
										.contains(existingObject.getAsJsonArray("tenKhac").get(k).getAsString())) {
									// add that unique TenKhac into finalTenKhac
									// finalTenKhac.add(existingObject.getAsJsonArray("tenKhac").get(k).getAsString());
									// add the hash elements TenKhac into the firstTenKhac file for extra checking
									// firstMergedMap.put(existingObject.getAsJsonArray("tenKhac").get(k).getAsString(),
									// existingObject);
								}
							}
							// merge the objects so it becomes one.
							mergeAttributes(existingObject, finalJsonObject, nguon);

						}
						// else add everything of it into the finalMergedMap table including the tenKhac
						else {
							JsonObject newObject = new JsonObject();
							for (int k = 0; k < finalTenKhac.size(); k++) {
								System.out.println(finalTenKhac + " " + k);
								JsonObject newObjectKhac = new JsonObject();
								newObjectKhac.addProperty("ten", finalTenKhac.get(k));
								System.out.println("FINAL:" + finalJsonObject);
								mergeAttributes(newObjectKhac, finalJsonObject, nguon);
								// firstMergedMap.put(finalTenKhac.get(k), newObjectKhac);
							}
							newObject.addProperty("ten", ten);
							nguon = finalJsonObject.get("nguon").getAsInt();
							mergeAttributes(newObject, finalJsonObject, nguon);
							finalMergedList.put(ten, newObject);
							System.out.println(finalMergedList);
						}

				} catch (NullPointerException e) {
					System.out.println("This one doesn't have tenKhac");
					JsonObject newObject = new JsonObject();
					newObject.addProperty("ten", ten);
					mergeAttributes(newObject, finalJsonObject, nguon);
					finalMergedList.put(ten, newObject);
					continue;
				}

				// Chuyển đổi map thành list chứa các đối tượng đã xử lý
				List<JsonObject> mergedList = new ArrayList<>(finalMergedList.values());

				// Ghi ra file output
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				FileWriter fileWriter;
				try {
					fileWriter = new FileWriter(outputFile);
					fileWriter.write(gson.toJson(mergedList));
					fileWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("Hợp nhất dữ liệu thành công.");

			}

		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void mergeAttributes(JsonObject targetObject, JsonObject sourceObject, int nguon) {
		for (String attribute : ATTRIBUTES) {
			if (sourceObject.has(attribute)) {
				JsonElement attributeValue = sourceObject.get(attribute);
				System.out.println(targetObject);
				System.out.println(attributeValue);
				if (attribute.equals("tenKhac") || attribute.equals("tacPham") || attribute.equals("suKienLienQuan")) {
					mergeArrayListAttribute(targetObject, attribute, attributeValue);
				} else {

					mergeAttributeValue(targetObject, attribute, attributeValue, nguon);
				}
			}
		}
	}

	private static void mergeArrayListAttribute(JsonObject targetObject, String attribute, JsonElement attributeValue) {
		JsonArray targetArray = targetObject.has(attribute) ? targetObject.get(attribute).getAsJsonArray()
				: new JsonArray();
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

	private static void mergeAttributeValue(JsonObject targetObject, String attribute, JsonElement attributeValue,
			int nguon) {
		int count = 0;
		if (targetObject.has(attribute)) {
			count++;
			System.out.println(count);
			JsonElement existingValue = targetObject.get(attribute);
			if (existingValue.isJsonObject()) {
				JsonObject attributeObject = existingValue.getAsJsonObject();
				System.out.println(attributeObject);
				mergeAttributeValue(attributeObject, attributeValue, nguon);
			} else {
				// Handle case where the attribute value is a JsonPrimitive
				// For example, you can replace it with a JsonObject containing both values
				targetObject.addProperty(attribute, attributeValue.getAsString());
			}
		} else {
			JsonObject attributeObject = new JsonObject();
			mergeAttributeValue(attributeObject, attributeValue, nguon);
			targetObject.add(attribute, attributeObject);
		}
	}

	private static void mergeAttributeValue(JsonObject attributeObject, JsonElement attributeValue, int nguon) {
	    if (attributeValue.isJsonPrimitive()) {
	        // Handle JsonPrimitive attribute value
	        JsonPrimitive primitiveValue = attributeValue.getAsJsonPrimitive();
	        if (primitiveValue.isString()) {
	            String attributeValueAsString = primitiveValue.getAsString();
	            JsonArray nguonArray;

	            if (attributeObject.has(attributeValueAsString)) {
	                nguonArray = attributeObject.getAsJsonArray(attributeValueAsString);
	                // Check if nguon value already exists in the array
	                if (!nguonArray.contains(new JsonPrimitive(nguon))) {
	                    nguonArray.add(nguon);
	                }
	            } else {
	                nguonArray = new JsonArray();
	                nguonArray.add(nguon);
	                attributeObject.add(attributeValueAsString, nguonArray);
	            }
	        }
	    } else if (attributeValue.isJsonObject()) {
	        // Handle JsonObject attribute value
	        JsonObject attributeValueObject = attributeValue.getAsJsonObject();
	        for (Map.Entry<String, JsonElement> entry : attributeValueObject.entrySet()) {
	            String key = entry.getKey();
	            JsonElement value = entry.getValue();
	            mergeAttributeValue(attributeObject, new JsonPrimitive(key), nguon);
	            mergeAttributeValue(attributeObject, value, nguon);
	        }
	    } else {
	        // Handle other types of attribute values (arrays, objects, etc.)
	        // You can customize the behavior based on your specific requirements
	        // For example, you can replace the existing attribute value with the new one
	        attributeObject.add(attributeValue.toString(), new JsonPrimitive(nguon));
	    }
	}
	







}
