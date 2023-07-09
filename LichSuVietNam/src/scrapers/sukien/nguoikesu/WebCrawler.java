package scrapers.sukien.nguoikesu;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import scrapers.Crawler;

public class WebCrawler {

	public static void main(String[] args) {

		Crawler crawler = new Crawler() {

			@Override
			public void crawl(Document doc) {
				for (int i = 0; i <= 70; i += 5) {
					String url = "https://nguoikesu.com/tu-lieu/quan-su?filter_tag[0]=&start=" + i;
					try {
						doc = Jsoup.connect(url).get();
						System.out.println(url);
					} catch (IOException e) {
						System.out.println("Không thể kết nối tới trang web " + url);
						return;
					}
					Elements headings = doc.select("h2 a");
					for (Element heading : headings) {
						String ref = heading.attr("href");
						if (!ref.contains("1979") && !ref.contains("1988"))
							addUrl("https://nguoikesu.com" + ref);
					}
				}

			}

		};

		crawler.crawl(null);
		crawler.saveToFile("file\\event-source-3.txt");
	}
}
