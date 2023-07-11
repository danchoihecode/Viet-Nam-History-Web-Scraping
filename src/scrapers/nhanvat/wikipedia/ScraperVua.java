package scrapers.nhanvat.wikipedia;

import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.nhanvat.LanhDaoQuocGia;
import scrapers.nhanvat.FigureScraper;

public class ScraperVua {
	public static void main(String[] args) {

		FigureScraper scraper = new FigureScraper() {

			@Override
			public void scrape(Document doc) {

				Elements headings = doc.select("h3");
				Elements boxes = doc.select("table.wikitable[cellpadding], table[cellpadding=\"0\"]");

				for (int i = 0; i < headings.size(); i++) {
					Element box;
					if (i < 10) {
						box = boxes.get(i);
					} else {
						box = boxes.get(i + 1);
					}

					Elements rows = box.select("tr");

					for (int j = 1; j < rows.size(); j++) {

						LanhDaoQuocGia emperor = new LanhDaoQuocGia();

						String thoiKy = headings.get(i).selectFirst("span.mw-headline").text();
						emperor.setThoiKy(thoiKy);

						Elements data = rows.get(j).select("td");

						Element link = data.get(0).selectFirst("img");
						if (link != null)
							emperor.setAnh(link.attr("src"));

						Element ten = data.get(1).selectFirst("a");
						emperor.setTen(ten.text());

						emperor.setUrl("https://vi.wikipedia.org" + ten.attr("href"));

						ArrayList<String> tenKhac = new ArrayList<String>();
						if (i != 3) {
							tenKhac.add("Miếu hiệu/Tôn hiệu:" + data.get(2).text().replaceAll("\\[[^\\]]*\\]", ""));
							tenKhac.add("Thụy hiệu:" + data.get(3).text().replaceAll("\\[[^\\]]*\\]", ""));
						} else {
							tenKhac.add("Thụy hiệu:" + data.get(2).text().replaceAll("\\[[^\\]]*\\]", ""));
							tenKhac.add("Tôn hiệu:" + data.get(3).text().replaceAll("\\[[^\\]]*\\]", ""));
						}

						tenKhac.add("Niên hiệu:" + data.get(4).text().replaceAll("\\[[^\\]]*\\]", ""));
						tenKhac.add("Tên húy:" + data.get(5).text().replaceAll("\\[[^\\]]*\\]", ""));

						emperor.setTenKhac(tenKhac);

						if (i != 17) {
							String theThu = data.get(6).text().replaceAll("\\[[^\\]]*\\]", "");
							if (!theThu.isEmpty())
								emperor.setTheThu(theThu);
						}

						String thoiGianTriVi;
						if (i == 1) {
							thoiGianTriVi = data.get(7).text().replaceAll("\\[[^\\]]*\\]", "");

						} else if (i == 17) {
							StringBuilder str = new StringBuilder();
							str.append(data.get(6).ownText()).append(data.get(7).text()).append(data.get(8).ownText());
							thoiGianTriVi = str.toString();
						} else {
							StringBuilder str = new StringBuilder();
							str.append(data.get(7).ownText()).append(data.get(8).text()).append(data.get(9).ownText());
							thoiGianTriVi = str.toString();
						}
						emperor.setThoiGianTaiVi(thoiGianTriVi);

						emperor.setNguon(2);
						addNhanVat(emperor);

					}

				}
			}
		};
		Document doc = scraper.connectToUrl("https://vi.wikipedia.org/wiki/Vua_Việt_Nam");
		if (doc != null) {
			scraper.scrape(doc);
			scraper.getJsonString();
			scraper.saveToFile("file\\figure-source-2.json", true);
		}

	}
}
