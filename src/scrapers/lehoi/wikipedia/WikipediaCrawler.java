package scrapers.lehoi.wikipedia;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import scrapers.Crawler;

public class WikipediaCrawler {
	public static void main(String[] args) {

		Crawler crawler = new Crawler() {

			@Override
			public void crawl(Document doc) {
				StringBuilder str;
				Elements uls = doc.select("div.div-col ul a");
				for (Element ul : uls) {

					if (!ul.attr("title").contains("không tồn tại")) {

						str = new StringBuilder();
						str.append("https://vi.wikipedia.org").append(ul.attr("href"));
						addUrl(str.toString());
					}
				}
			}

		};
		Document doc = crawler.connectToUrl(
				"https://vi.wikipedia.org/wiki/L%E1%BB%85_h%E1%BB%99i_c%C3%A1c_d%C3%A2n_t%E1%BB%99c_Vi%E1%BB%87t_Nam");
		if (doc != null) {
			crawler.crawl(doc);
			crawler.saveToFile("file\\festival-source-2.txt");
		}

	}

}
