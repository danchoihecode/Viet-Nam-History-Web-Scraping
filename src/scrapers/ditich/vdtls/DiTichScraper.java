package scrapers.ditich.vdtls;

import java.util.ArrayList;
import java.util.Hashtable;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.ditich.DiTich;
import scrapers.ditich.SiteScraper;

public class DiTichScraper {

	private static Hashtable<String, DiTich> diTichHashtable = new Hashtable<>();

	private static String escapeJsonCharacters(String input) {
		return input.replace("\\", "\\\\") // Escape backslashes
				.replace("\"", "\\\"") // Escape double quotes
				.replace("\b", "\\b") // Escape backspace
				.replace("\f", "\\f") // Escape form feed
				.replace("\n", "\\n") // Escape newline
				.replace("\r", "\\r") // Escape carriage return
				.replace("\t", "\\t"); // Escape tab
	}

	public static void main(String args[]) {

		SiteScraper scraper = new SiteScraper() {

			@Override
			public void scrape(Document doc) {
				DiTich ditich = new DiTich();
				Elements headings = doc.select("h2.hl__comp-heading.hl__comp_heading_custom");
				Elements images = doc.select("img.sp-large");
				Elements locations = doc.select("span.address-line1");
				Elements buildFroms1 = doc.select("span").select(":containsOwn(Niên đại tuyệt đối)");
				Elements buildFroms2 = doc.select("span").select(":containsOwn(Niên đại chủ đạo)");
				String[] wordsToExclude = { "Ngọc hoàng", "Ngọc Hòang", "thành hòang làng", "Thành hòang làng", "Tổ",
						"Tướng có công đánh giặc ngoại xâm", "Tổ tiên", "Đền thờ của xóm", "Thành hoàng", "thành hoàng",
						"Thành Hoàng", "Đối tượng thờ", "Ngọc Hoàng", "Thần", "thần", "Tiên Thiên", "Địa Thiên", "phật",
						"Phật" };
				Elements nhanVatLienQuans = doc.select("span:containsOwn(Đối tượng thờ)");
				ArrayList<String> nvlqList = new ArrayList<>();

				Elements rankingType = doc.select("span").select(":containsOwn(Loại hình xếp hạng)");

				Element image = images.get(0);

				String imageLink = image.attr("src");
				String replacedImageLink = imageLink.replace("\\", "/");

				if (locations.isEmpty()) {
					System.out.println("Empty location");
				} else {
					Element location = locations.get(0);
					String diaDiem = location.ownText();
					ditich.setDiaChi(diaDiem);
				}

				if (headings.isEmpty()) {
					System.out.println("Empty array");
				} else {
					Element heading = headings.get(0);
					String miniHeading = heading.ownText();
					System.out.println(miniHeading);
					ditich.setTen(miniHeading);
				}

				for (Element element : nhanVatLienQuans) {
					String textContent = element.text();
					if (ditich.getTen().equals("Đình Đốc Hậu") || ditich.getTen().equals("Đền Phù Sa")
							|| ditich.getTen().equals("Đình Cổ Lão (Đình Cả)")
							|| ditich.getTen().equals("Đình Cả (Đình Khuân)")) {
						textContent = textContent.substring(14);
						nvlqList.add(textContent);
						ditich.setNhanVatLienQuan(nvlqList);
						continue;
					}
					if (ditich.getTen().equals("Đình Quan Xuyên")) {
						continue;
					}

					textContent = textContent.replaceAll(";", ",");
					String[] values = textContent.split(":");

					for (String value : values) {
						String[] values2 = value.split(",");

						for (String value1 : values2) {
							boolean shouldExclude = false;
							for (String exclude : wordsToExclude) {
								if (value1.contains(exclude)) {
									shouldExclude = true;
									break;
								}
							}

							if (!shouldExclude) {
								nvlqList.add(value1.trim());
							}
						}
					}
				}

				if (!buildFroms1.isEmpty()) {
					Element buildFrom = buildFroms1.get(0);
					String khoiLap = buildFrom.ownText();
					ditich.setKhoiLap(khoiLap);
				} else if (!buildFroms2.isEmpty()) {
					Element buildFrom = buildFroms2.get(0);
					String khoiLap = buildFrom.ownText();
					ditich.setKhoiLap(khoiLap);
				} else {
					System.out.println("Empty khoiLap");
				}

				if (nvlqList.size() == 0) {
					System.out.println("Empty nvlq");
				} else {
					ditich.setNhanVatLienQuan(nvlqList);
				}

				if (rankingType.isEmpty()) {
					System.out.println("Empty ranking");
				} else {
					Element xepHang = rankingType.get(0);
					String xH = xepHang.ownText();
					ditich.setPhanLoai(xH);
				}

				ditich.setNguon(4);
				ditich.setUrl(escapeJsonCharacters(doc.baseUri()));
				ditich.setAnh("http://ditich.vn" + replacedImageLink);

				if (diTichHashtable.get(ditich.getTen().toLowerCase()) != null) {
					System.out.println(
							"Object with the name '" + ditich.getTen() + "' already exists. Modifying object name...");
					if (ditich.getDiaChi().lastIndexOf(',') == ditich.getDiaChi().length() - 1)
						ditich.setDiaChi(ditich.getDiaChi().substring(0, ditich.getDiaChi().lastIndexOf(',')));
					String newName = ditich.getTen() + " " + ditich.getDiaChi()
							.substring(ditich.getDiaChi().lastIndexOf(',') + 2, ditich.getDiaChi().length());
					ditich.setTen(newName);
					System.out.println("New name: " + newName);
					diTichHashtable.put(newName, ditich);
					addDiTich(ditich);
					return;
				}
				diTichHashtable.put(ditich.getTen().toLowerCase(), ditich);
				addDiTich(ditich);

			}
		};
		ArrayList<Document> docs = scraper.connectToUrls("file\\site-source-4.txt");
		for (Document doc:docs) {
			scraper.scrape(doc);
		}
		scraper.getJsonString();
		scraper.saveToFile("file\\site-source-4.json");

		
	}
}
