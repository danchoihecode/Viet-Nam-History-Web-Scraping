package scrapers.lehoi.dulichchaovietnam;

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

import entities.lehoi.LeHoi;

public class LeHoiScraper {

	private static ArrayList<LeHoi> leHois = new ArrayList<>();
	private static Document doc;
	private static BufferedReader reader;
	private static int count = 0;

	public static void scrape() {
		Elements titles = doc.select("div.col-sm-12 p:has(strong)");
		for(Element title:titles) {
			LeHoi leHoi = new LeHoi();
			String str = title.text();
			if(str.matches(".*\\d+\\..*")) {
				String[] parts = str.split("\\.");
			    String ten = parts[1].trim();
				if(ten.length()!=0) leHoi.setTen(ten);
				String anh = doc.select("p:contains("+ten+") ~ p:has(img):not(p:contains("+ten+") ~ p:has(strong) ~ p)").select("img[src~=(?i)\\.(png|jpe?g|gif)], div.thumbnail img[src~=(?i)\\.(png|jpe?g|gif)]").attr("src");
				if(anh.length()!=0) leHoi.setAnh(anh); 
				String thoiGian = doc.select("p:contains("+ten+") ~ p:contains(Thời gian:):not(p:contains("+ten+") ~ p:has(strong) ~ p)").text();
				if(thoiGian.length()!=0) {
					parts = thoiGian.split("\\:");
					leHoi.setThoiGian(parts[1].trim()); 
				}
				String diaDiem = doc.select("p:contains("+ten+") ~ p:contains(Địa điểm:):not(p:contains("+ten+") ~ p:has(strong) ~ p)").text();
				if(diaDiem.length()!=0) {
					parts = diaDiem.split("\\:");
					leHoi.setDiaDiem(parts[1].trim()); 
				}
				String mieuTa = doc.select("p:contains("+ten+") ~ p:not(p:contains("+ten+") ~ p:has(strong) ~ p, p:has(strong), p:contains(Địa điểm:), p:contains(Thời gian:), p:has(img))").text().trim();
				if(mieuTa.length()!=0) {					
					leHoi.setMieuTa(mieuTa); 
				}
			}else continue;
			
			leHoi.setNguon(10);
			leHois.add(leHoi);
			count++;
		}
	}
	
	public static void main(String[] args) {
		try {
			doc = Jsoup.connect("https://dulichchaovietnam.com/9-le-hoi-o-dac-sac-o-vinh-phuc.html").get();
		} catch (IOException e) {
			System.out.println("Không thể kết nối tới trang web.");
			return;
		}
		scrape();	
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json;
		json = gson.toJson(leHois);
		String outputFile = "file\\festival-source-10.json";
		try (FileWriter writer = new FileWriter(outputFile)) {
			writer.write(json);
			System.out.println("Dữ liệu đã được ghi vào file " + outputFile);
			System.out.println(count + " bản ghi.");
		} catch (IOException e) {
			System.out.println("Ghi dữ liệu vào file thất bại.");
		}	
	}
}
