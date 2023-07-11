package scrapers.sukien.wikipedia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.jsoup.nodes.Document;

import utilities.EntityUrl;

public class WikipediaScraper {

	public static void main(String[] args) {
		HashMap<String, String> nhanVatJson = EntityUrl.getEntityUrlAndName("file\\figure-source-2.json");
		HashSet<String> suKienJson = EntityUrl.getEntityUrl("file\\event-source-2.json");
		SuKienWiki scraper = new SuKienWiki() {
			@Override
			public void scrape(Document doc) {
				scrapeWiki(doc, nhanVatJson, suKienJson);
			}
		};
		ArrayList<Document> docs = scraper.connectToUrls("file\\event-source-2.txt");
		for (Document doc : docs) {
			scraper.scrape(doc);
		}
		scraper.getJsonString();
		scraper.saveToFile("file\\event-source-2.json", true);
	}

}
