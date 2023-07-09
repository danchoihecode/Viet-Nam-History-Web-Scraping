package scrapers.thoiky.wikipedia;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.thoiky.ThoiKy;
import scrapers.thoiky.PeriodScraper;

public class ScraperWikipedia {

	public static void main(String[] args) {

		PeriodScraper scraper = new PeriodScraper() {

			@Override
			public void scrape(Document doc) {
				Element infobox = doc.select("table.wikitable").get(6);
				Elements rows = infobox.select("tr");
				for (int i = 1; i < rows.size(); i++) {
					Elements data = rows.get(i).select("td");
					String ten = data.get(0).text();
					String nguoiSangLap = data.get(1).text();
					String thuDo = data.get(3).text().replaceAll("\\[[^\\]]*\\]", "");
					ThoiKy period = new ThoiKy();
					period.setTen(ten);
					period.setNguoiSangLap(nguoiSangLap);
					period.setThuDo(thuDo);
					period.setNguon(2);
					addThoiKy(period);
				}

			}
		};
		Document doc = scraper.connectToUrl("https://vi.wikipedia.org/wiki/Vua_Việt_Nam");
		if (doc != null) {
			scraper.scrape(doc);
			scraper.getJsonString();
			scraper.saveToFile("file\\period-source-2.json");
		}

	}

}
