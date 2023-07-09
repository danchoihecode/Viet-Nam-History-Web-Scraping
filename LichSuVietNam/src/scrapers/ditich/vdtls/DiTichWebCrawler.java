package scrapers.ditich.vdtls;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import scrapers.Crawler;

public class DiTichWebCrawler {
	public static void main(String[] args) {

		Crawler crawler = new Crawler() {

			@Override
			public void crawl(Document doc) {
				for (int i = 1; i <= 336; i += 1) {
					String url = "http://ditich.vn/FrontEnd/DiTich?cpage=" + i + "&rpage=&corder=&torder=&tpage=" + i
							+ "&TEN=&LA_CDT=&LOAI_HINH_XEP_HANG=&XEP_HANG=&DIA_DANH=&TEN_HANG_MUC=&HM_LOAI_HINH_XEP_HANG=&HM_XEP_HANG=&TEN_HIEN_VAT=&HV_LOAI=&namtubo=";

					try {

						doc = Jsoup.connect(url).get();
					} catch (IOException e) {
						System.out.println("Không thể kết nối tới trang web " + url);
						continue;
					}
					Elements headings = doc.select("section section section div section a");
					for (Element heading : headings) {
						String ref = heading.attr("href");
						addUrl("http://ditich.vn" + ref);
					}
				}

			}
		};

		crawler.crawl(null);
		crawler.saveToFile("file\\site-source-4.txt");

	}
}
