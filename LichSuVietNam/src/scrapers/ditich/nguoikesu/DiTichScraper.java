package scrapers.ditich.nguoikesu;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.ditich.DiTich;
import scrapers.ditich.SiteScraper;
import utilities.EntityUrl;

public class DiTichScraper {

	public static void main(String[] args) {

		HashMap<String, String> nhanVatJson = EntityUrl.getEntityUrlAndName("file\\figure-source-3.json");
		HashMap<String, String> suKienJson = EntityUrl.getEntityUrlAndName("file\\event-source-3.json");

		SiteScraper scraper = new SiteScraper() {

			@Override
			public void scrape(Document doc) {
				DiTich diTich = new DiTich();
				Elements body = doc.select("div.com-content-article__body > *:not(h2:contains(Tham khảo) ~ *)")
						.select("*");
				// Ten
				String ten = doc.select("div.page-header>h2").text();
				diTich.setTen(ten);

				// Dia Chi
				String diaChi = doc.select("div.infobox").select(
						"th:containsOwn(Vị trí) + td, th:containsOwn(Địa chỉ) + td, th:containsOwn(Địa điểm) + td, th:containsOwn(Khu vực) + td")
						.html().replaceAll("(?s)<sup.*?</sup>", "");
				if (diaChi.length() != 0)
					diTich.setDiaChi(Jsoup.parse(diaChi).text());

				// Mieu Ta
				Elements p = body.select("p:has(b)");
				if (!p.isEmpty()) {
					String mieuTa = body.select("p:has(b)").first().html().replaceAll("(?s)<sup.*?</sup>", "");
					String str = Jsoup.parse(mieuTa).text();
					if (mieuTa.length() != 0)
						diTich.setMieuTa(str);
				}

				// Anh
				String anh = doc
						.selectFirst(
								"tr img[src~=(?i)\\.(png|jpe?g|gif)], div.thumbnail img[src~=(?i)\\.(png|jpe?g|gif)]")
						.attr("src");
				String image = "https://nguoikesu.com" + anh;
				if (anh.length() != 0)
					diTich.setAnh(image);

				// Nguon
				diTich.setNguon(3);

				// Nhan Vat & Su Kien Lien Quan
				ArrayList<String> nhanVatLienQuan = new ArrayList<>();
				ArrayList<String> suKienLienQuan = new ArrayList<>();
				Elements ele = body.select("a");
				for (Element e : ele) {
					String str = "https://nguoikesu.com" + e.attr("href");
					if (suKienJson.containsKey(str)) {
						if (!suKienLienQuan.contains(suKienJson.get(str)))
							suKienLienQuan.add(suKienJson.get(str));
					}
					if (nhanVatJson.containsKey(str)) {
						if (!nhanVatLienQuan.contains(nhanVatJson.get(str)))
							nhanVatLienQuan.add(nhanVatJson.get(str));
					}
				}
				if (!suKienLienQuan.isEmpty())
					diTich.setSuKienLienQuan(suKienLienQuan);
				if (!nhanVatLienQuan.isEmpty())
					diTich.setNhanVatLienQuan(nhanVatLienQuan);

				addDiTich(diTich);

			}
		};
		ArrayList<Document> docs = scraper.connectToUrls("file\\site-source-3.txt");
		for (Document doc : docs) {
			scraper.scrape(doc);
		}
		scraper.getJsonString();
		scraper.saveToFile("file\\site-source-3.json");

	}

}
