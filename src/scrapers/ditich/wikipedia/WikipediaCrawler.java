package scrapers.ditich.wikipedia;

import java.util.HashMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import scrapers.Crawler;

public class WikipediaCrawler {
	private static String[] tmp;

	public static void main(String[] args) {

		Crawler crawler = new Crawler() {

			@Override
			public void crawl(Document doc) {
				StringBuilder str;
				String s;
				int y = 0;
				int x;
				if (tmp.length == 3)
					x = Integer.parseInt(tmp[1]);
				else
					x = Integer.parseInt(tmp[0]);
				Elements uls = doc.select("h3 + ul").select("a");
				for (Element ul : uls) {
					y++;
					s = ul.text();

					if (y > x && !s.contains("Cụm đình") && !s.contains("đặc biệt") && !s.equals("Đình")
							&& !s.equals("Kèo") && !s.contains("Việt Nam") && !s.equals("Nhà dài Ê Đê")
							&& !s.equals("Nhà rông") && !s.equals("Cổng làng") && !s.contains("Bẩy")
							&& !s.equals("Phú Riềng Đỏ") && !s.contains("Bản mẫu")) {

						str = new StringBuilder();
						str.append("https://vi.wikipedia.org").append(ul.attr("href"));
						addUrl(str.toString());
					}
				}

			}

		};
		HashMap<Document, String[]> docs = crawler.connectToUrls("file\\url-site-source-2.txt", true);
		for (Document doc : docs.keySet()) {

			tmp = docs.get(doc);
			crawler.crawl(doc);

		}
		crawler.saveToFile("file\\site-source-2.txt");

	}

}
