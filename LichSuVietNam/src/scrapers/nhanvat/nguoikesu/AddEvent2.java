package scrapers.nhanvat.nguoikesu;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import nhanvat.LanhDaoQuocGia;
import sukien.SuKien;

public class AddEvent2 {
	public static void main(String[] args) {

		List<LanhDaoQuocGia> nhanVatList = readNhanVatFromJson(
				"file\\figure-source-3-2.json"
				);

		// Tạo map<String, ArrayList<String>> từ file event-source-2.json
		Map<String, ArrayList<String>> eventMap = readEventMapFromFile(
				"file\\event-source-3.json");

		// Duyệt qua danh sách nhanVat và kiểm tra sự kiện liên quan
		for (LanhDaoQuocGia nhanVat : nhanVatList) {
			String tenNhanVat = nhanVat.getTen().toLowerCase();
			String s = containAny(tenNhanVat, eventMap.keySet());
			if (s != null) {
				ArrayList<String> suKienLienQuan = eventMap.get(s);
				nhanVat.setSuKienLienQuan(suKienLienQuan);
			}
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(nhanVatList);

		String outputFile = "file\\figure-source-3-2.json";
		try (FileWriter writer = new FileWriter(outputFile)) {
			writer.write(json);
			System.out.println("Dữ liệu đã được ghi vào file " + outputFile);
		} catch (IOException e) {
			System.out.println("Ghi dữ liệu vào file thất bại.");
		}

	}

	// Đọc dữ liệu và chuyển đổi thành List<NhanVat>
	private static List<LanhDaoQuocGia> readNhanVatFromJson(String filename) {
		List<LanhDaoQuocGia> nhanVatList = new ArrayList<>();
		try (FileReader reader = new FileReader(filename)) {
			Gson gson = new Gson();
			Type type = new TypeToken<List<LanhDaoQuocGia>>() {
			}.getType();
			nhanVatList = gson.fromJson(reader, type);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nhanVatList;
	}

	// Tạo map<String, ArrayList<String>> từ file
	private static Map<String, ArrayList<String>> readEventMapFromFile(String filename) {
		Map<String, ArrayList<String>> eventMap = new HashMap<>();
		try (FileReader reader = new FileReader(filename)) {
			Gson gson = new Gson();
			Type type = new TypeToken<List<SuKien>>() {
			}.getType();
			List<SuKien> suKienList = gson.fromJson(reader, type);
			for (SuKien suKien : suKienList) {
				String tenSuKien = suKien.getTen();
				ArrayList<String> nhanVatLienQuan = suKien.getNhanVatLienQuan();
				if (nhanVatLienQuan != null) {
					for (String nhanVat : nhanVatLienQuan) {
						if (!eventMap.containsKey(nhanVat.toLowerCase())) {
							eventMap.put(nhanVat.toLowerCase(), new ArrayList<>());
						}
						eventMap.get(nhanVat.toLowerCase()).add(tenSuKien);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return eventMap;
	}

	private static String containAny(String tenNhanVat, Set<String> set) {

		for (String s : set) {
			if (tenNhanVat.contains(s))
				return s;
		}
		return null;

	}
}

