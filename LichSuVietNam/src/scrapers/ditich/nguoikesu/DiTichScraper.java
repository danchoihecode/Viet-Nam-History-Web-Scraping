package scrapers.ditich.nguoikesu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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

import entities.ditich.DiTich;



public class DiTichScraper {
	
	private static ArrayList<DiTich> diTichs = new ArrayList<>();
	private static Document doc;
	private static BufferedReader reader;
	private static int count = 0;
	private static HashMap<String, String> suKienJson = new HashMap<>();
	private static HashMap<String, String> nhanVatJson = new HashMap<>();
	
	
	public static void scrape() {
		DiTich diTich = new DiTich();
		Elements body = doc.select("div.com-content-article__body > *:not(h2:contains(Tham khảo) ~ *)").select("*");
		// Ten
		String ten = doc.select("div.page-header>h2").text();
		diTich.setTen(ten);
		
		// Dia Chi
		String diaChi = doc.select("div.infobox").select("th:containsOwn(Vị trí) + td, th:containsOwn(Địa chỉ) + td, th:containsOwn(Địa điểm) + td, th:containsOwn(Khu vực) + td").html().replaceAll("(?s)<sup.*?</sup>", "");
		if(diaChi.length() != 0) diTich.setDiaChi(Jsoup.parse(diaChi).text());
		
		// Mieu Ta
		Elements p = body.select("p:has(b)");
		if(!p.isEmpty()) {
			String mieuTa = body.select("p:has(b)").first().html().replaceAll("(?s)<sup.*?</sup>", "");
			String str = Jsoup.parse(mieuTa).text();	
			if(mieuTa.length() != 0) diTich.setMieuTa(str);
		}

		// Anh
		String anh = doc.selectFirst("tr img[src~=(?i)\\.(png|jpe?g|gif)], div.thumbnail img[src~=(?i)\\.(png|jpe?g|gif)]").attr("src");
		String image = "https://nguoikesu.com" + anh;
		if(anh.length() != 0) diTich.setAnh(image);
		
		// Nguon
		diTich.setNguon(3);
		
		// Nhan Vat & Su Kien Lien Quan
		ArrayList<String> nhanVatLienQuan = new ArrayList<>();
		ArrayList<String> suKienLienQuan = new ArrayList<>();
		Elements ele = body.select("a");
		for(Element e:ele) {
			String str = "https://nguoikesu.com" + e.attr("href");
			if(suKienJson.containsKey(str)) {
				if(!suKienLienQuan.contains(suKienJson.get(str))) suKienLienQuan.add(suKienJson.get(str));
			}
			if(nhanVatJson.containsKey(str)) {
				if(!nhanVatLienQuan.contains(nhanVatJson.get(str))) nhanVatLienQuan.add(nhanVatJson.get(str));
			}
		}
		if(!suKienLienQuan.isEmpty()) diTich.setSuKienLienQuan(suKienLienQuan);	
		if(!nhanVatLienQuan.isEmpty()) diTich.setNhanVatLienQuan(nhanVatLienQuan);	
		
		diTichs.add(diTich);
		count++;
	}
	

	public static void main(String[] args) {
		
		try(FileReader reader = new FileReader("file\\figure-source-3.json")){
			JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
			for(JsonElement jsonElement:jsonArray) {
				JsonObject jsonObject = jsonElement.getAsJsonObject();
				String url = jsonObject.get("url").getAsString();
				String ten = jsonObject.get("ten").getAsString();
				nhanVatJson.put(url, ten);
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		try(FileReader reader = new FileReader("file\\event-source-3.json")){
			JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
			for(JsonElement jsonElement:jsonArray) {
				JsonObject jsonObject = jsonElement.getAsJsonObject();
				String url = jsonObject.get("url").getAsString();
				String ten = jsonObject.get("ten").getAsString();
				suKienJson.put(url, ten);
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		try {
			reader = new BufferedReader(new FileReader("file\\site-source-3.txt"));
			String line = reader.readLine();
			while (line != null) {
				try {
					doc = Jsoup.connect(line).get();
					System.out.println(line);
				} catch (IOException e) {
					System.out.println("Không thể kết nối tới trang web "+line);
				}
				scrape();
				
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
//		try {
//			doc = Jsoup.connect("https://nguoikesu.com/dia-danh/co-do-hoa-lu").get();
//		} catch (IOException e) {
//			System.out.println("Không thể kết nối tới trang web.");
//			return;
//		}
//		scrape();	
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json;
		json = gson.toJson(diTichs);
		String outputFile = "file\\site-source-3.json";
		try (FileWriter writer = new FileWriter(outputFile)) {
			writer.write(json);
			System.out.println("Dữ liệu đã được ghi vào file " + outputFile);
			System.out.println(count + " bản ghi.");
		} catch (IOException e) {
			System.out.println("Ghi dữ liệu vào file thất bại.");
		}	
	}

}
