package scrapers.ditich.wikipedia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import ditich.DiTich;

public class Scraper {

	public static void main(String[] args) {
		HashSet<String> nhanVat = new HashSet<>();
		HashMap<String, String> suKien = new HashMap<>();

		try (FileReader reader = new FileReader(
				"C:\\Users\\pc\\Documents\\OOPBigProject\\LichSuVietNam\\file\\figure-source-2.json")) {

			JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

			for (JsonElement jsonElement : jsonArray) {
				String url = jsonElement.getAsJsonObject().get("url").getAsString();
				nhanVat.add(url);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try (FileReader reader = new FileReader(
				"C:\\Users\\pc\\Documents\\OOPBigProject\\LichSuVietNam\\file\\event-source-2.json")) {

			JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

			for (JsonElement jsonElement : jsonArray) {
				if (jsonElement.getAsJsonObject().get("url") != null) {
					String url = jsonElement.getAsJsonObject().get("url").getAsString();
					String ten = jsonElement.getAsJsonObject().get("ten").getAsString();
					suKien.put(url, ten);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		Document doc;
		BufferedReader reader;
//		int i = 0;
		ArrayList<DiTich> diTichs = new ArrayList<DiTich>();
		try {
			reader = new BufferedReader(
					new FileReader("C:\\Users\\pc\\Documents\\OOPBigProject\\LichSuVietNam\\file\\site-source-2.txt"));
			String line = reader.readLine();
			while (line != null) {
//				if (i < 75) {
//					i++;
//					line = reader.readLine();
//					continue;
//
//				}
				try {
					doc = Jsoup.connect(line).get();
				} catch (IOException e) {
					System.out.println("Không thể kết nối tới trang web ");
					break;
				}

				DiTich diTich = new DiTich();

				diTich.setUrl(line);

				Element ten = doc.selectFirst("p b");
				Element p;
				if (ten == null || ten.text().equals("·")) {

					diTich.setTen((doc.title().split(" – "))[0]);
					p = doc.selectFirst("p");
				} else {

					p = ten.parent();
					diTich.setTen(ten.text());
				}

				ArrayList<String> suKienLienQuan = new ArrayList<>();
				ArrayList<String> nhanVatLienQuan = new ArrayList<>();

				// Đoạn văn đầu tiên
				Elements refs = p.select("a");
				String str;
				String url;

				for (Element ref : refs) {
					str = ref.text();

					url = "https://vi.wikipedia.org" + ref.attr("href");
					if (!nhanVatLienQuan.contains(str) && nhanVat.contains(url)) {
						nhanVatLienQuan.add(str);
					}
					if (suKien.containsKey(url) && !suKienLienQuan.contains(suKien.get(url))) {
						suKienLienQuan.add(suKien.get(url));
					}

				}

				// Đoạn văn thứ hai
				Element p2 = p.nextElementSibling();

				if (p2 != null) {
					while (p2 != null && !p2.tagName().equals("p"))
						p2 = p2.nextElementSibling();
					if (p2 != null) {
						refs = p2.select("a");
						for (Element ref : refs) {
							str = ref.text();
							url = "https://vi.wikipedia.org" + ref.attr("href");
							if (!nhanVatLienQuan.contains(str) && nhanVat.contains(url)) {
								nhanVatLienQuan.add(str);
							}
							if (suKien.containsKey(url) && !suKienLienQuan.contains(suKien.get(url))) {
								suKienLienQuan.add(suKien.get(url));
							}
						}
					}
				}

				// Đoạn văn thứ ba
				if (p2 != null) {
					Element p5 = p2.nextElementSibling();

					if (p5 != null) {
						while (p5 != null && !p5.tagName().equals("p"))
							p5 = p5.nextElementSibling();
						if (p5 != null) {
							refs = p5.select("a");
							for (Element ref : refs) {
								str = ref.text();
								url = "https://vi.wikipedia.org" + ref.attr("href");
								if (!nhanVatLienQuan.contains(str) && nhanVat.contains(url)) {
									nhanVatLienQuan.add(str);
								}
								if (suKien.containsKey(url) && !suKienLienQuan.contains(suKien.get(url))) {
									suKienLienQuan.add(suKien.get(url));
								}
							}
						}
					}
				}

				if (nhanVatLienQuan.isEmpty()) {
					Elements p3 = doc.select("h2 span.mw-headline");
					Element p4;

					for (int j = 0; j < p3.size(); j++) {
						p4 = p3.get(j);
						// Tìm đến mục lịch sử hoặc đặc điểm
						if (p4.text().contains("Lịch sử") || p4.text().contains("Đặc điểm")) {
							// Đoạn văn đầu tiên
							p4 = p4.parent();
							while (!p4.tagName().equals("p"))
								p4 = p4.nextElementSibling();

							refs = p4.select("a");
							for (Element ref : refs) {
								str = ref.text();
								url = "https://vi.wikipedia.org" + ref.attr("href");
								if (!nhanVatLienQuan.contains(str) && nhanVat.contains(url)) {
									nhanVatLienQuan.add(str);
								}
								if (suKien.containsKey(url) && !suKienLienQuan.contains(suKien.get(url))) {
									suKienLienQuan.add(suKien.get(url));
								}
							}
							// Đoạn văn thứ hai
							if (p4.nextElementSibling().tagName().equals("p")) {
								refs = p4.nextElementSibling().select("a");
								for (Element ref : refs) {
									str = ref.text();
									url = "https://vi.wikipedia.org" + ref.attr("href");
									if (!nhanVatLienQuan.contains(str) && nhanVat.contains(url)) {
										nhanVatLienQuan.add(str);
									}
									if (suKien.containsKey(url) && !suKienLienQuan.contains(suKien.get(url))) {
										suKienLienQuan.add(suKien.get(url));
									}
								}

							}
							break;

						}

					}

				}

				if (!suKienLienQuan.isEmpty())
					diTich.setSuKienLienQuan(suKienLienQuan);
				if (!nhanVatLienQuan.isEmpty())
					diTich.setNhanVatLienQuan(nhanVatLienQuan);

				String mieuTa = p.text().replaceAll("\\[[^\\]]*\\]", "");
				if (mieuTa.contains("có thể là một trong") || mieuTa.contains("có thể đã đề cập")) {
//					i++;
					line = reader.readLine();
					continue;
				}
				diTich.setMieuTa(mieuTa);

				String diaChi = null;
				String khoiLap = null;
				String phanLoai = null;

				Element infobox = doc.selectFirst("table.infobox");
				if (infobox != null) {
					Elements rows = infobox.select("tr");
					for (int j = 0; j < rows.size(); j++) {

						Element head = rows.get(j).selectFirst("th");

						if (head != null && diaChi == null && (head.text().equals("Địa chỉ")
								|| head.text().equals("Địa điểm") || head.text().equals("Vị trí"))) {
							Element td = rows.get(j).selectFirst("td");
							if (td != null)
								diaChi = td.text();

						}
						if (head != null && khoiLap == null
								&& (head.text().equals("Bắt đầu xây dựng") || head.text().equals("Khởi lập")
										|| head.text().equals("Thành lập") || head.text().equals("Khởi công")
										|| head.text().equals("Xây dựng")
										|| head.text().equals("Quá trình xây dựng"))) {
							Element td = rows.get(j).selectFirst("td");
							if (td != null)
								khoiLap = td.text();

						}
						if (head != null && phanLoai == null && (head.text().equals("Phân loại"))) {
							Element td = rows.get(j).selectFirst("td");
							if (td != null)
								phanLoai = td.text();

						}

						if (diaChi != null && khoiLap != null && phanLoai != null) {
							break;
						}

					}

					if (diaChi == null) {
						Element span = infobox.selectFirst("tr td span");
						if (span != null) {
							diaChi = span.text();
						}
					}
					if (diaChi != null && !diaChi.isBlank())
						diTich.setDiaChi(diaChi);
					if (khoiLap != null && !khoiLap.isBlank())
						diTich.setKhoiLap(khoiLap);
					if (phanLoai != null && !phanLoai.isBlank())
						diTich.setPhanLoai(phanLoai);

				}
				Element link = doc.selectFirst("figure img");
				if (link != null)
					diTich.setAnh(link.attr("src"));
				diTich.setNguon(2);

//				i++;
				diTichs.add(diTich);
				line = reader.readLine();

//				if (i == 60)
//					break;
			}
			reader.close();
		} catch (

		IOException e) {
			e.printStackTrace();
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(diTichs);

		String outputFile = "C:\\Users\\pc\\Documents\\OOPBigProject\\LichSuVietNam\\file\\site-source-2.json";
		try (FileWriter writer = new FileWriter(outputFile)) {
			writer.write(json);
			System.out.println("Dữ liệu đã được ghi vào file " + outputFile);
		} catch (IOException e) {
			System.out.println("Ghi dữ liệu vào file thất bại.");
		}
	}

}
