package scrapers.lehoi.vietfuntravel;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lehoi.LeHoi;


public class LeHoiScraper {
	
	private static ArrayList<LeHoi> leHois = new ArrayList<>();
	private static Document doc;
	private static BufferedReader reader;
	private static int count = 0;

	public static void scrape() {
		Elements titles = doc.select("div.content-desc h2");
		for(Element title:titles) {
			LeHoi leHoi = new LeHoi();
			String str = title.text();
			String[] parts = str.split("\\.");
		    String ten = parts[1].trim();
			if(ten.length()!=0) leHoi.setTen(ten);
			String mieuTa = doc.select("h2:contains("+str+") ~ p:not(h2:contains("+str+") ~ h2 ~ p, p:has(em))").text().trim();
			if(mieuTa.length()!=0) leHoi.setMieuTa(mieuTa);
			String anh = doc.select("h2:contains("+str+") ~ p:has(img):not(h2:contains("+str+") ~ h2 ~ p)").select("img[src~=(?i)\\.(png|jpe?g|gif)], div.thumbnail img[src~=(?i)\\.(png|jpe?g|gif)]").attr("src");
			if(anh.length()!=0) leHoi.setAnh("https://www.vietfuntravel.com.vn" + anh); 
			
			leHoi.setNguon(9);
			leHois.add(leHoi);
			count++;
		}
	}
	
	public static void main(String[] args) {
		try {
			doc = Jsoup.connect("https://www.vietfuntravel.com.vn/blog/nhung-le-hoi-lon-o-ha-noi.html").get();
		} catch (IOException e) {
			System.out.println("Không thể kết nối tới trang web.");
			return;
		}
		scrape();	
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json;
		json = gson.toJson(leHois);
		String outputFile = "file\\festival-source-9.json";
		try (FileWriter writer = new FileWriter(outputFile)) {
			writer.write(json);
			System.out.println("Dữ liệu đã được ghi vào file " + outputFile);
			System.out.println(count + " bản ghi.");
		} catch (IOException e) {
			System.out.println("Ghi dữ liệu vào file thất bại.");
		}	
	}
}
