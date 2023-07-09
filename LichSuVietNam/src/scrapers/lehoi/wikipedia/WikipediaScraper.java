package scrapers.lehoi.wikipedia;

import java.util.ArrayList;
import java.util.HashSet;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.lehoi.LeHoi;
import scrapers.lehoi.FestivalScraper;
import utilities.EntityUrl;

public class WikipediaScraper {

	public static void main(String[] args) {

		HashSet<String> nhanVat = EntityUrl.getEntityUrl("file\\figure-source-2.json");
		HashSet<String> diTich = EntityUrl.getEntityUrl("file\\site-source-2.json");

		FestivalScraper scraper = new FestivalScraper() {

			@Override
			public void scrape(Document doc) {

				LeHoi leHoi = new LeHoi();

				leHoi.setUrl(doc.baseUri());

				Element name = doc.selectFirst("p b");
				Element p;
				String ten = (doc.title().split(" – "))[0];
				leHoi.setTen(ten);
				if (name == null || name.text().equals("·")) {

					p = doc.selectFirst("p");
				} else {

					p = name.parent();
				}

				String suKienLienQuan = null;
				ArrayList<String> nhanVatLienQuan = new ArrayList<>();
				ArrayList<String> diTichLienQuan = new ArrayList<>();

				// Đoạn văn đầu tiên
				Elements refs = p.select("a");
				String str;
				String url;
				String tmp;
				for (Element ref : refs) {
					str = ref.text();
					tmp = str.toLowerCase();
					if ((tmp.contains("trận") && !tmp.contains("mặt trận")) || tmp.contains("chiến tranh")
							|| tmp.contains("chiến dịch") || tmp.contains("khởi nghĩa")) {
						suKienLienQuan = str;
					}
					url = "https://vi.wikipedia.org" + ref.attr("href");
					if (!nhanVatLienQuan.contains(str) && nhanVat.contains(url)) {
						nhanVatLienQuan.add(str);
					}
					if (!diTichLienQuan.contains(str) && diTich.contains(url)) {
						diTichLienQuan.add(str);
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
							if (!diTichLienQuan.contains(str) && diTich.contains(url)) {
								diTichLienQuan.add(str);
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
								if (!diTichLienQuan.contains(str) && diTich.contains(url)) {
									diTichLienQuan.add(str);
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
						// Tìm đến mục lịch sử
						if (p4.text().contains("Lịch sử") || p4.text().contains("Ý nghĩa")) {
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
								if (!diTichLienQuan.contains(str) && diTich.contains(url)) {
									diTichLienQuan.add(str);
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
									if (!diTichLienQuan.contains(str) && diTich.contains(url)) {
										diTichLienQuan.add(str);
									}
								}

							}
							break;

						}

					}

				}

				Elements heads = doc.select("h2 span.mw-headline");

				for (Element head : heads) {
					String s = head.text().toLowerCase();
					if (s.contains("thời gian")) {
						head = head.parent();
						while (!head.tagName().equals("p"))
							head = head.nextElementSibling();
						leHoi.setThoiGian(head.text());

					}
					if (s.contains("nơi tổ chức")) {
						head = head.parent();
						while (!head.tagName().equals("p"))
							head = head.nextElementSibling();
						leHoi.setDiaDiem(head.text());

					}
				}

				if (suKienLienQuan != null)
					leHoi.setSuKienLienQuan(suKienLienQuan);
				if (!nhanVatLienQuan.isEmpty())
					leHoi.setNhanVatLienQuan(nhanVatLienQuan);
				if (!diTichLienQuan.isEmpty())
					leHoi.setDiTichLienQuan(diTichLienQuan);

				String mieuTa = p.text().replaceAll("\\[[^\\]]*\\]", "");
				if (!mieuTa.contains("có thể là một trong") && !mieuTa.contains("có thể đã đề cập")) {
					leHoi.setMieuTa(mieuTa);
				} else if (ten.indexOf("Đền") != -1) {
					diTichLienQuan.add(ten.substring(ten.indexOf("Đền")));
					leHoi.setDiTichLienQuan(diTichLienQuan);
				}
				int idx;
				if ((idx = mieuTa.indexOf("tổ chức ở")) != -1 || (idx = mieuTa.indexOf("tại")) != -1) {
					for (int j = idx; j < mieuTa.length(); j++) {
						if (mieuTa.charAt(j) == '.' || mieuTa.charAt(j) == ',' || mieuTa.charAt(j) == '(') {
							if (!mieuTa.substring(idx, j).equals("tại đây")) {
								leHoi.setDiaDiem(mieuTa.substring(idx, j));
							}
							break;
						}
					}
				}

				Element link = doc.selectFirst("figure img");
				if (link != null)
					leHoi.setAnh(link.attr("src"));

				leHoi.setNguon(2);

				addLeHoi(leHoi);
			}
		};

		ArrayList<Document> docs = scraper.connectToUrls("file\\festival-source-2.txt");
		for (Document doc : docs) {
			if (doc != null)
				scraper.scrape(doc);
		}
		scraper.getJsonString();
		scraper.saveToFile("file\\festival-source-2.json");

	}

}
