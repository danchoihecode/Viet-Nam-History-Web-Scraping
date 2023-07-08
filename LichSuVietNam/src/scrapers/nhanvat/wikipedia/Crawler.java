package scrapers.nhanvat.wikipedia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
	public static void main(String[] args) {

		Set<String> set = new HashSet<String>();
		BufferedReader reader;
		StringBuilder str;
		Document doc;
		String[] tmp;
		try {
			reader = new BufferedReader(new FileReader("file\\url-figure-source-2.txt"));
			String line = reader.readLine();
			while (line != null) {

				int x;
				int y = 0;
				tmp = line.split("\\|");
				if (tmp.length == 3)
					x = Integer.parseInt(tmp[1]);
				else
					x = Integer.parseInt(tmp[0]);
				try {
					if (tmp.length == 3)
						doc = Jsoup.connect(tmp[2]).get();
					else
						doc = Jsoup.connect(tmp[1]).get();
				} catch (IOException e) {
					System.out.println("Không thể kết nối tới trang web ");
					break;
				}

				Elements uls = doc.select("h3 + ul").select("a");
				for (Element ul : uls) {
					y++;

					if (y <= x || ul.text().contains("Phong trào") || ul.text().contains("đoàn")
							|| ul.text().contains("chủng") || ul.text().contains("lệnh") || ul.text().contains("viện")
							|| ul.text().contains("Quân đội") || ul.text().contains("Biệt động")
							|| ul.text().contains("Cảnh sát") || ul.text().contains("Công an")
							|| ul.text().contains("Công ty") || ul.text().contains("Cục")
							|| ul.text().contains("Danh sách") || ul.text().contains("Thành viên")
							|| ul.text().contains("Nhà lao") || ul.text().contains("Tổng cục")
							|| ul.text().contains("Trường Đại học") || ul.text().contains("Trường Sa")
							|| ul.text().contains("không quân") || ul.text().contains("Ý Yên")
							|| ul.text().equals("Đông Ngạc") || ul.text().equals("Hậu Lộc")
							|| ul.text().equals("Hồng Việt, Đông Hưng") || ul.text().equals("Khánh Cường")
							|| ul.text().equals("Khánh Lợi") || ul.text().equals("Khánh Tiên")
							|| ul.text().equals("May 10") || ul.text().equals("Quang Lộc, Can Lộc")
							|| ul.text().equals("Quỳnh Đôi") || ul.text().equals("Tân Phú Tây")
							|| ul.text().equals("Tân Thành Bình") || ul.text().equals("Thượng Kiệm")
							|| ul.text().equals("Vũ Thắng (xã)")

					) {
						continue;
					} else {
						str = new StringBuilder();
						if (tmp.length == 3) {

							str.append(tmp[0]).append("|");
						}
						str.append("https://vi.wikipedia.org").append(ul.attr("href"));
						set.add(str.toString());
					}
				}
				line = reader.readLine();
			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		// Crawl phi hau
		StringBuilder string;

		try {
			doc = Jsoup.connect("https://vi.wikipedia.org/wiki/H%E1%BA%ADu_phi_Vi%E1%BB%87t_Nam").get();
		} catch (IOException e) {
			System.out.println("Không thể kết nối tới trang web ");
			return;
		}

		Elements tables = doc.select(".wikitable");
		for (int i = 3; i < tables.size(); i++) {

			Elements rows = tables.get(i).select("tr");

			for (int j = 1; j < rows.size(); j++) {

				Element data = rows.get(j).select("td").get(1);
				if (data.selectFirst("a") == null)
					data = rows.get(j).select("td").get(2);
				if (data.selectFirst("a") == null)
					data = rows.get(j).select("td").get(3);
				Element ref = data.selectFirst("a");
				if (ref != null && ref.parent().tagName().equals("td") && !(ref.attr("title").contains("không"))) {

					String s = ref.attr("href");
					if (!(s.contains("Gia") || s.contains("Vợ"))) {
						string = new StringBuilder();
						string.append("https://vi.wikipedia.org").append(s);
						set.add(string.toString());
					}
				}
			}

		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter("file\\figure-source-2.txt"))) {
			for (String element : set) {
				writer.write(element);
				writer.newLine();
			}
			System.out.println("Ghi thành công vào file.");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
