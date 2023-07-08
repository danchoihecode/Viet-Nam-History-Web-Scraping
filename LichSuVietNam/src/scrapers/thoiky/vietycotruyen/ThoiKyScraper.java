package scrapers.thoiky.vietycotruyen;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import entities.thoiky.ThoiKy;

public class ThoiKyScraper {
	private static ArrayList<ThoiKy> thoiKys = new ArrayList<>();
	private static Document doc;

	private static int count = 0;

	public static void scrape() {
		Elements rows = doc.select("table tr");
		int i = 1;
		while (i < rows.size() - 1) {
			ThoiKy thoiKy = new ThoiKy();
			ArrayList<String> lanhDaoQuocGia = new ArrayList<>();
			Element row = rows.get(i);
			Elements row1 = row.select("tr:has(td:nth-of-type(5)):not(:has(td:nth-of-type(6)))");
			Elements row2 = row.select("tr:has(td:nth-of-type(4)):not(:has(td:nth-of-type(5)))");
			if (!row1.isEmpty()) {
				String str = row.select("td").get(1).text();
				if (str.length() == 0) {
					i++;
					continue;
				} else if (str.contains("(") && str.indexOf(")") == str.length() - 1) {
					String[] parts = str.split("\\(");
					String part1 = parts[0].trim();
					String part2 = parts[1].trim().replace(")", "");
					if (part1.length() != 0)
						thoiKy.setTen(part1);
					if (part2.length() != 0)
						thoiKy.setThoiGian(part2);
				} else {
					if (str.length() != 0)
						thoiKy.setTen(str);
				}

				String lanhDao = row.select("td").get(2).text();
				String triVi = row.select("td").get(3).text();
				if (lanhDao.length() != 0 && triVi.length() != 0)
					lanhDaoQuocGia.add(lanhDao + " (" + triVi + ")");
				else {
					String time = row.select("td").get(3).text();
					if (time.length() != 0)
						thoiKy.setThoiGian(time);
				}
				while (true) {
					i++;
					Elements row3 = rows.get(i).select("tr:has(td:nth-of-type(3)):not(:has(td:nth-of-type(4)))");
					if (row3.isEmpty()) {
						break;
					} else {
						lanhDao = row3.select("td").get(0).text();
						triVi = row3.select("td").get(1).text();
						if (lanhDao.length() != 0 && triVi.length() != 0)
							lanhDaoQuocGia.add(lanhDao + " (" + triVi + ")");
					}
				}
				if (!lanhDaoQuocGia.isEmpty())
					thoiKy.setLanhDaoQuocGia(lanhDaoQuocGia);
			} else if (!row2.isEmpty()) {
				String str = row.select("td").get(0).text();
				if (str.length() == 0) {
					i++;
					continue;
				} else if (str.contains("(") && str.indexOf(")") == str.length() - 1) {
					String[] parts = str.split("\\(");
					String part1 = parts[0].trim();
					String part2 = parts[1].trim().replace(")", "");
					if (part1.length() != 0)
						thoiKy.setTen(part1);
					if (part2.length() != 0)
						thoiKy.setThoiGian(part2);
				} else {
					if (str.length() != 0)
						thoiKy.setTen(str);
				}

				String lanhDao = row.select("td").get(1).text();
				String triVi = row.select("td").get(2).text();
				if (lanhDao.length() != 0 && triVi.length() != 0)
					lanhDaoQuocGia.add(lanhDao + " (" + triVi + ")");
				else {
					String time = row.select("td").get(2).text();
					if (time.length() != 0)
						thoiKy.setThoiGian(time);
				}
				while (true) {
					i++;
					Elements row3 = rows.get(i).select("tr:has(td:nth-of-type(3)):not(:has(td:nth-of-type(4)))");
					if (row3.isEmpty()) {
						break;
					} else {
						lanhDao = row3.select("td").get(0).text();
						triVi = row3.select("td").get(1).text();
						if (lanhDao.length() != 0 && triVi.length() != 0)
							lanhDaoQuocGia.add(lanhDao + " (" + triVi + ")");
					}
				}
				if (!lanhDaoQuocGia.isEmpty())
					thoiKy.setLanhDaoQuocGia(lanhDaoQuocGia);
			}
			thoiKy.setNguon(6);
			thoiKys.add(thoiKy);
			count++;
		}
	}

	public static void main(String[] args) {

		try {
			doc = Jsoup.connect(
					"http://vietycotruyen.com.vn/cac-trieu-dai-viet-nam-qua-tung-thoi-ky-lich-su?fbclid=IwAR1Ipoo1lU0ZVcOCgcWcS7eYWkbVQaRpcCLBoXH_BkXpouCIAr_63JH3ffs")
					.get();
		} catch (IOException e) {
			System.out.println("Không thể kết nối tới trang web.");
			return;
		}
		scrape();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json;
		json = gson.toJson(thoiKys);
		String outputFile = "file\\period-source-6.json";
		try (FileWriter writer = new FileWriter(outputFile)) {
			writer.write(json);
			System.out.println("Dữ liệu đã được ghi vào file " + outputFile);
			System.out.println(count + " bản ghi.");
		} catch (IOException e) {
			System.out.println("Ghi dữ liệu vào file thất bại.");
		}
	}

}
