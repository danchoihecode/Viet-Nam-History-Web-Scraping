package scrapers.thoiky.accgroup;

import thoiky.ThoiKy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ScraperAccGroup {

	public static void main(String[] args) {
		Document doc;
		try {
			doc = Jsoup.connect("https://accgroup.vn/lich-su-viet-nam-qua-cac-thoi-ky/").get();
		} catch (IOException e) {
			System.out.println("Không thể kết nối tới trang web.");
			return;
		}

		ArrayList<ThoiKy> periods = new ArrayList<>();
		Elements headings = doc.select("h2.ftwp-heading");

		for (int i = 0; i < headings.size(); i++) {
			if (i == 8)
				continue;
			Element heading = headings.get(i);

			Element p = heading.nextElementSibling();

			StringBuilder str = new StringBuilder();
			ArrayList<String> lanhDaoQuocGia = new ArrayList<String>();
			ThoiKy period = new ThoiKy();
			boolean firstDes = true;

			if (i == 0) {
				period = new ThoiKy();
				period.setTen(heading.text());
				period.setNguon(1);
			}
			if (i == 3 || i == 4 || i == 6 || i == 7 || i == 9 || i == 10) {
				period = new ThoiKy();
				String tmp = heading.text();
				String ten = tmp.substring(0, tmp.indexOf('(') - 1);
				String thoiGian = tmp.substring(tmp.indexOf('(') + 1, tmp.indexOf(')'));
				period.setTen(ten);
				period.setThoiGian(thoiGian);
				period.setNguon(1);
			}
			if (i == 5)
				p = p.nextElementSibling();

			while (true) {

				if ((i != 5 && i != 11 && p.selectFirst("em") != null)
						|| ((i == 5 || i == 11) && p.selectFirst("strong") != null)) {

					if (!str.isEmpty()) {
						period.setMieuTa(truncate(str.toString()));
						String thuDo = findCapital(str.toString());
						if (thuDo != null) {
							period.setThuDo(thuDo);
						}
						firstDes = true;
						if (!lanhDaoQuocGia.isEmpty()) {
							period.setLanhDaoQuocGia(lanhDaoQuocGia);
							lanhDaoQuocGia = new ArrayList<String>();

						}
						periods.add(period);
					}
					period = new ThoiKy();
					String tmp = p.text();
					String ten, thoiGian;
					if (tmp.indexOf('(') != -1) {
						ten = tmp.substring(0, tmp.indexOf('(') - 1);
						thoiGian = tmp.substring(tmp.indexOf('(') + 1, tmp.indexOf(')'));
					} else {
						ten = tmp;
						thoiGian = tmp.substring(17);
					}

					period.setTen(ten);
					period.setThoiGian(thoiGian);
					period.setNguon(1);

				} else if (i != 0 && p.tagName().equals("ul")) {

					Elements items = p.select("li");

					for (int j = 0; j < items.size(); j++) {
						lanhDaoQuocGia.add(items.get(j).text());
					}

				} else if (firstDes) {
					str = new StringBuilder();
					str.append(p.text());
					firstDes = false;
				} else if (i != 10) {
					str.append("\n" + p.text());
				}

				p = p.nextElementSibling();
				if (p.tagName().equals("h2") || (i == 12 && p.text().contains("xã hội chủ nghĩa"))) {

					if (!str.isEmpty()) {
						period.setMieuTa(truncate(str.toString()));
						String thuDo = findCapital(str.toString());
						if (thuDo != null) {
							period.setThuDo(thuDo);
						}
						firstDes = true;
						if (!lanhDaoQuocGia.isEmpty()) {
							period.setLanhDaoQuocGia(lanhDaoQuocGia);
							lanhDaoQuocGia = new ArrayList<String>();
						}
						periods.add(period);
					}
					break;
				}
			}
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(periods);

		String outputFile = "C:\\Users\\pc\\Documents\\OOPBigProject\\LichSuVietNam\\file\\period-source-1.json";
		try (FileWriter writer = new FileWriter(outputFile)) {
			writer.write(json);
			System.out.println("Dữ liệu đã được ghi vào file " + outputFile);
		} catch (IOException e) {
			System.out.println("Ghi dữ liệu vào file thất bại.");
		}

	}

	public static String truncate(String str) {
		int idx = str.indexOf(':');
		if (idx == -1 || str.charAt(idx - 1) == 'n') {
			return str;
		} else {
			for (int i = idx; i >= 0; i--) {
				if (str.charAt(i) == '.') {
					return str.substring(0, i + 1) + str.substring(idx + 1);
				}
			}
		}
		return str;

	}

	public static String findCapital(String str) {
		String[] patterns = { "đóng đô ở", "kinh đô là", "dời đô về", "đóng đô tại thành", "thủ đô là" };
		for (String pattern : patterns) {
			String result = getNextTwoWords(str, pattern);
			if (result != null) {
				return result.replaceAll("[.]", "");
			}
		}
		return null;
	}

	public static String getNextTwoWords(String input, String phrase) {
		int index = input.indexOf(phrase);
		if (index != -1) {
			String subString = input.substring(index + phrase.length()).trim();
			String[] words = subString.split("\\s+");
			if (words.length >= 2) {
				return words[0] + " " + words[1];
			}
		}
		return null;
	}

}
