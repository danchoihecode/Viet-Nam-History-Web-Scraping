package scrapers.sukien.wikipedia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SuKienScraper2 extends Scraper implements GetData {
	
	@Override
	public void getData() {
		getSavedUrl();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("file\\event-source-2.txt"));
			String line = reader.readLine();
			while (line != null) {
				try {
					Document doc = Jsoup.connect(line).get();
					System.out.println(line);
					if(!suKienJson.contains(line)) scrape(line, doc);	
				} catch (IOException e) {
					System.out.println("Không thể kết nối tới trang web "+line);
				}		
				
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		String line = "https://vi.wikipedia.org/wiki/Tr%E1%BA%ADn_%C4%90%E1%BB%93_B%C3%A0n_(1377)";
//		try {
//			Document doc = Jsoup.connect(line).get();
//			scrape(line, doc);	
//		} catch (IOException e) {
//			System.out.println("Không thể kết nối tới trang web." + line);
//			return;
//		}	
	}
	
	
	
	public static void main(String[] args) {
		SuKienScraper2 scraper = new SuKienScraper2();
		
		scraper.getData();
		scraper.save("file\\event-source-2-2.json");
	}
	
}
