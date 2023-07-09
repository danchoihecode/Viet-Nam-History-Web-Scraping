package scrapers.lehoi.baonghean;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.lehoi.LeHoi;
import scrapers.lehoi.FestivalScraper;

public class LeHoiScraper {

	public static void main(String[] args) {

		FestivalScraper scraper = new FestivalScraper() {

			@Override
			public void scrape(Document doc) {
				Elements titles = doc.select("div.notebox p:has(strong):not(p:contains(:))");
				for (Element title : titles) {
					LeHoi leHoi = new LeHoi();
					String str = title.text();
					if (str.matches(".*\\d+\\..*")) {
						String[] parts = str.split("\\.");
						String ten = parts[1].trim();
						if (ten.length() != 0)
							leHoi.setTen(ten);
						String anh = doc
								.select("p:contains(" + ten + ") ~ p:has(img):not(p:contains(" + ten
										+ ") ~ p:has(strong) ~ p)")
								.select("img[src~=(?i)\\.(png|jpe?g|gif)], div.thumbnail img[src~=(?i)\\.(png|jpe?g|gif)]")
								.attr("src");
						if (anh.length() != 0)
							leHoi.setAnh(anh);
						String thoiGian = doc.select("p:contains(" + ten + ") ~ p:contains(Thời gian:):not(p:contains("
								+ ten + ") ~ p:has(strong) ~ p)").text();
						if (thoiGian.length() != 0) {
							parts = thoiGian.split("\\:");
							leHoi.setThoiGian(parts[1].trim());
						}
						String diaDiem = doc.select("p:contains(" + ten + ") ~ p:contains(Địa điểm:):not(p:contains("
								+ ten + ") ~ p:has(strong) ~ p)").text();
						if (diaDiem.length() != 0) {
							parts = diaDiem.split("\\:");
							leHoi.setDiaDiem(parts[1].trim());
						}
//						String mieuTa = doc.select("p:contains("+ten+") ~ p:not(p:contains("+ten+") ~ p:has(strong) ~ p, p:has(strong), p:contains(Địa điểm:), p:contains(Thời gian:), p:has(img))").text().trim();
//						if(mieuTa.length()!=0) {					
//							leHoi.setMieuTa(mieuTa); 
//						}
					} else
						continue;

					leHoi.setNguon(8);
					addLeHoi(leHoi);
				}

			}
		};
		Document doc = scraper
				.connectToUrl("https://baonghean.vn/nghe-an-se-to-chuc-29-le-hoi-trong-nam-2023-post263070.html");
		if (doc !=null) {
		scraper.scrape(doc);
		scraper.getJsonString();
		scraper.saveToFile("file\\festival-source-8.json");
		}

	}
}
