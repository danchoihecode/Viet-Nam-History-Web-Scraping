package scrapers.sukien.nguoikesu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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

import nhanvat.LanhDaoQuocGia;
import nhanvat.NhanVat;
import sukien.SuKien;

public class SuKienScraper {
	
	private static ArrayList<SuKien> suKiens = new ArrayList<>();
	private static Document doc;
	private static BufferedReader reader;
	private static int count = 0;
	private static HashMap<String, String> nhanVatJson = new HashMap<>();
	
	
	public static void scrape(String url) {
		SuKien suKien = new SuKien();
		
		// Url
		suKien.setUrl(url);
		
		// Ten
		String ten = doc.select("div.page-header > h1").text();
		if(ten.length() != 0) suKien.setTen(ten);
		
		// Thoi Gian
		String thoiGian = doc.select("td:contains(Thời gian) + td").text();
		if(thoiGian.length() != 0) suKien.setThoiGian(thoiGian);
		
		// Dia Diem
		String diaDiem = doc.select("td:contains(Địa điểm) + td").text();
		if(diaDiem.length() != 0) suKien.setDiaDiem(diaDiem);
		
		// Ket Qua
		String nguyenNhan = doc.select("td:contains(Nguyên nhân) + td").text();
		if(nguyenNhan.length() != 0) suKien.setNguyenNhan(nguyenNhan);
		
		// Ket Qua
		String ketQua = doc.select("td:contains(Kết quả) + td").text();
		if(ketQua.length() != 0) suKien.setKetQua(ketQua);
		
		// Mieu Ta
		Elements mieuTa  = doc.select("div.infobox ~ p:not(h2:first-of-type ~ p, h2:first-of-type ~ * p)").select("p:not(:has(br))");
		if(!mieuTa.isEmpty()) suKien.setMieuTa(mieuTa.text());
		
		// Tham Chien
		ArrayList<ArrayList<String>> thamChienList = new ArrayList<>();
		Elements thamChien  = doc.select("tr:contains(Tham chiến) + tr[valign=\"top\"]").select("td");
		for(Element e:thamChien) {
			ArrayList<String> arr = new ArrayList<String>();
			String[] thamChienSplit =  e.html().split("<br>");
			for(int i=0; i<thamChienSplit.length; i++) {
				String str = Jsoup.parse(thamChienSplit[i]).text();
				if(str.length() != 0) arr.add(str);
			}
			thamChienList.add(arr);
		}
		if(!thamChienList.isEmpty()) suKien.setThamChien(thamChienList);
	
		// Chi Huy
		ArrayList<ArrayList<String>> chiHuyList = new ArrayList<>();
		Elements chiHuy  = doc.select("tr:contains(Chỉ huy) + tr[valign=\"top\"]").select("td"); 
		for(Element e:chiHuy) {
			ArrayList<String> arr = new ArrayList<String>();
			String[] chiHuySplit =  e.html().split("<br>");
			for(int i=0; i<chiHuySplit.length; i++) {
				String str = Jsoup.parse(chiHuySplit[i]).text().replace(" †","");
				if(str.length() != 0) arr.add(str);
			}
			chiHuyList.add(arr);
		}
		if(!chiHuyList.isEmpty()) suKien.setChiHuy(chiHuyList);
		
		// Luc Luong
		ArrayList<String> lucLuongList = new ArrayList<>();
		Elements lucLuong  = doc.select("tr:contains(Lực lượng) + tr[valign=\"top\"]").select("td"); 
		for(Element e:lucLuong) {
			String str = e.html().replaceAll("(?s)<sup.*?</sup>", "");
			lucLuongList.add(Jsoup.parse(str).text());
		}
		if(!lucLuongList.isEmpty()) suKien.setLucLuong(lucLuongList);
	
		// Ton That
		ArrayList<String> tonThatList = new ArrayList<>();
		Elements tonThat  = doc.select("tr:contains(Tổn thất) + tr[valign=\"top\"]").select("td"); 
		for(Element e:tonThat) {
			String str = e.html().replaceAll("(?s)<sup.*?</sup>", "");
			tonThatList.add(Jsoup.parse(str).text());
		}
		if(!tonThatList.isEmpty()) suKien.setTonThat(tonThatList);
		
		// Nhan Vat Lien Quan
		ArrayList<String> nhanVatLienQuan = new ArrayList<>();
		Elements a1 = doc.select("tr:contains(Chỉ huy) + tr, tr:contains(Tham chiến) + tr").select("a");
		Elements a2 = mieuTa.select("a");
		for(Element a:a1) {
			String str = "https://nguoikesu.com" + a.attr("href");
			if(nhanVatJson.containsKey(str)) {
				if(!nhanVatLienQuan.contains(nhanVatJson.get(str))) nhanVatLienQuan.add(nhanVatJson.get(str));
			}
		}
		for(Element a:a2) {
			String str = "https://nguoikesu.com" + a.attr("href");
			if(nhanVatJson.containsKey(str)) {
				if(!nhanVatLienQuan.contains(nhanVatJson.get(str))) nhanVatLienQuan.add(nhanVatJson.get(str));
			}
		}
		if(!nhanVatLienQuan.isEmpty()) suKien.setNhanVatLienQuan(nhanVatLienQuan);	
		
		// Nguon
		suKien.setNguon(3);	
		
		// Them vao list
		suKiens.add(suKien);
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

		try {
			reader = new BufferedReader(new FileReader("file\\event-source-3.txt"));
			String line = reader.readLine();
			while (line != null) {
				try {
					doc = Jsoup.connect(line).get();
					System.out.println(line);
				} catch (IOException e) {
					System.out.println("Không thể kết nối tới trang web "+line);
				}
				scrape(line);
				
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
//		String url = "https://nguoikesu.com/tu-lieu/quan-su/tran-dinh-tuong-nam-1861";
//		try {
//			doc = Jsoup.connect(url).get();
//		} catch (IOException e) {
//			System.out.println("Không thể kết nối tới trang web.");
//			return;
//		}
//		scrape(url);	
		
		Gson gson1 = new GsonBuilder().setPrettyPrinting().create();
		String json1;
		json1 = gson1.toJson(suKiens);
		String outputFile1 = "file\\event-source-3.json";
		try (FileWriter writer = new FileWriter(outputFile1)) {
			writer.write(json1);
			System.out.println("Dữ liệu đã được ghi vào file " + outputFile1);
			System.out.println(count + " bản ghi.");
		} catch (IOException e) {
			System.out.println("Ghi dữ liệu vào file thất bại.");
		}		
	}
	
}
