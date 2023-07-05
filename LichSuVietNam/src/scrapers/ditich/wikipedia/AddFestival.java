package scrapers.ditich.wikipedia;

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

import ditich.DiTich;
import lehoi.LeHoi;

public class AddFestival {
	public static void main(String[] args) {
		// Đọc dữ liệu từ file site-source-2.json
		List<DiTich> diTichList = readDiTichFromJson(
				"C:\\Users\\pc\\Documents\\OOPProject\\LichSuVietNam\\file\\site-source-2.json");

		// Tạo map<String, ArrayList<String>> từ file festival-source-2.json
		Map<String, ArrayList<String>> festivalMap = readFestivalMapFromFile(
				"C:\\Users\\pc\\Documents\\OOPProject\\LichSuVietNam\\file\\festival-source-2.json");

		// Duyệt qua danh sách DiTich và kiểm tra lễ hội liên quan
		for (DiTich diTich : diTichList) {
			String tenDiTich = diTich.getTen().toLowerCase();
			String s = containAny(tenDiTich, festivalMap.keySet());
			if (s != null) {
				ArrayList<String> leHoiLienQuan = festivalMap.get(s);
				diTich.setLeHoiLienQuan(leHoiLienQuan);
			}
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(diTichList);

		String outputFile = "C:\\Users\\pc\\Documents\\OOPProject\\LichSuVietNam\\file\\site-source-2.json";
		try (FileWriter writer = new FileWriter(outputFile)) {
			writer.write(json);
			System.out.println("Dữ liệu đã được ghi vào file " + outputFile);
		} catch (IOException e) {
			System.out.println("Ghi dữ liệu vào file thất bại.");
		}

	}

	// Đọc dữ liệu từ file site-source-2.json và chuyển đổi thành List<DiTich>
	private static List<DiTich> readDiTichFromJson(String filename) {
		List<DiTich> diTichList = new ArrayList<>();
		try (FileReader reader = new FileReader(filename)) {
			Gson gson = new Gson();
			Type type = new TypeToken<List<DiTich>>() {
			}.getType();
			diTichList = gson.fromJson(reader, type);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return diTichList;
	}

	// Tạo map<String, ArrayList<String>> từ file festival-source-2.json
	private static Map<String, ArrayList<String>> readFestivalMapFromFile(String filename) {
		Map<String, ArrayList<String>> festivalMap = new HashMap<>();
		try (FileReader reader = new FileReader(filename)) {
			Gson gson = new Gson();
			Type type = new TypeToken<List<LeHoi>>() {
			}.getType();
			List<LeHoi> leHoiList = gson.fromJson(reader, type);
			for (LeHoi leHoi : leHoiList) {
				String tenLeHoi = leHoi.getTen();
				ArrayList<String> diTichLienQuan = leHoi.getDiTichLienQuan();
				if (diTichLienQuan != null) {
					for (String diTich : diTichLienQuan) {
						if (!festivalMap.containsKey(diTich.toLowerCase())) {
							festivalMap.put(diTich.toLowerCase(), new ArrayList<>());
						}
						festivalMap.get(diTich.toLowerCase()).add(tenLeHoi);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return festivalMap;
	}

	private static String containAny(String tenDiTich, Set<String> set) {

		for (String s : set) {
			if (tenDiTich.contains(s))
				return s;
		}
		return null;

	}
}
