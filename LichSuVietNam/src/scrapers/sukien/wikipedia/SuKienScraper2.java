package scrapers.sukien.wikipedia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import sukien.SuKien;

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
		
//		String line = "https://vi.wikipedia.org/wiki/Hi%E1%BB%87p_%C4%91%E1%BB%8Bnh_Paris_1973";
//		try {
//			doc = Jsoup.connect(line).get();
//			if(!suKienJson.contains(line)) scrape(line, doc);	
//		} catch (IOException e) {
//			System.out.println("Không thể kết nối tới trang web.");
//			return;
//		}	
	}
	
	
	
	public static void main(String[] args) {
		SuKienScraper2 scraper = new SuKienScraper2();
		
		scraper.getData();
		scraper.save("file\\event-source-2-2.json");
	}
	
}
