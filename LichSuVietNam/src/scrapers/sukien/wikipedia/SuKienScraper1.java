package scrapers.sukien.wikipedia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
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

import entities.sukien.SuKien;

public class SuKienScraper1 extends Scraper implements GetData {

	@Override
	public void getData() {
		getSavedUrl();
		try {
			Document doc = Jsoup.connect("https://vi.wikipedia.org/wiki/C%C3%A1c_cu%E1%BB%99c_chi%E1%BA%BFn_tranh_Vi%E1%BB%87t_Nam_tham_gia").get();
			scrapeDetail(doc);
		} catch (IOException e) {
			System.out.println("Không thể kết nối tới trang web.");
			return;
		}
		
//		try {
//			doc = Jsoup.connect("https://vi.wikipedia.org/wiki/Chi%E1%BA%BFn_tranh_Vi%E1%BB%87t_Nam").get();
//		} catch (IOException e) {
//			System.out.println("Không thể kết nối tới trang web.");
//			return;
//		}
//		SuKien suKien = new SuKien();
//		Elements event = doc.select("table.vevent");
//		if(!event.isEmpty()) {
//			scrapeDetail(suKien, event, doc);
//			suKiens.add(suKien);
//			count++;
//		}
	}

	public void scrapeTable (SuKien suKien, Element ele) {
		// Ten
		String ten1 = ele.select("td:nth-child(1)").text();
		if(ten1.length() != 0) suKien.setTen(ten1);
		
		// Tham Chien
		ArrayList<ArrayList<String>> thamChienList = new ArrayList<>();
		Elements thamChiens  = ele.select("td:nth-child(2), td:nth-child(3)");
		for(Element eThamChiens:thamChiens) {
			String thamChien = eThamChiens.html().replaceAll("(?s)<li>.*?</li>|<p>|</p>", "<br>").replaceAll("Viện trợ|Hỗ trợ|Ủng hộ|:", "");
			String[] thamChienSplit =  thamChien.split("<br>|<hr>");
			ArrayList<String> arr = new ArrayList<String>();
			for(int i=0; i<thamChienSplit.length; i++) {
				String str = Jsoup.parse(thamChienSplit[i]).text();
				if(str.length() != 0) arr.add(str);
			}
			thamChienList.add(arr);
		}
		if(!thamChienList.isEmpty()) suKien.setThamChien(thamChienList);
		
		// Ket Qua
		String ketQua = ele.select("td:nth-child(4)").html().replaceAll("(?s)</li>.*?<li>", ". ").replaceAll("<ul>", ":");
		if(ketQua.length() != 0) suKien.setKetQua(Jsoup.parse(ketQua).text().replaceAll(" :", ":"));
		
		// Nhan Vat Lien Quan
		ArrayList<String> nhanVatLienQuan = new ArrayList<>();
		Elements hrefs  = ele.select("td:nth-child(2), td:nth-child(3)").select("a:not(.new)");
		for(Element href:hrefs) {
			String url = "https://vi.wikipedia.org" + href.attr("href");
			if(nhanVatJson.containsKey(url)) {
				String nhanVat = nhanVatJson.get(url);
				if(!nhanVatLienQuan.contains(nhanVat)) nhanVatLienQuan.add(nhanVat);
			}
		}
		if(!nhanVatLienQuan.isEmpty()) suKien.setNhanVatLienQuan(nhanVatLienQuan);
		
		// Them Thong Tin
		String url = "https://vi.wikipedia.org" + ele.select("td:first-child a:first-child").attr("href");
		try {
			Document document = Jsoup.connect(url).get();
			Elements event = document.select("table.vevent");
			if(!event.isEmpty()) {
				scrape(url, document);
				return;
			}
		} catch (IOException e) {
			System.out.println("Không thể kết nối tới trang web.");
		}	
		
		// Them Vao List
		suKiens.add(suKien);
		count++;
	}
	
	
	public void scrapeDetail(Document doc) {
		
		Elements eH3s = doc.select("h3");
		for(Element eH3:eH3s) {
			String thoiKy = eH3.select("span.mw-headline").text();	
			Elements eTables = doc.select("h3:contains("+thoiKy+") ~ table:not(h3:contains("+thoiKy+") ~ h3 ~ table, h3:contains("+thoiKy+") ~ h2 ~ table)").select("tr:nth-child(n+2)");
			for(Element eTable:eTables) {
				SuKien suKien = new SuKien();
				// Thoi Ky
				if(thoiKy.length() != 0) suKien.setThoiKy(thoiKy);
				// Nguon
				suKien.setNguon(2);	
				scrapeTable(suKien, eTable);
			}
			System.out.println(thoiKy);
		}
		Elements eH2 = doc.select("h2:contains(Thời kỳ Bắc thuộc lần 4)");
		String thoiKy = eH2.select("span.mw-headline").text();
		Elements eTables = doc.select("h2:contains(Thời kỳ Bắc thuộc lần 4) + table").select("tr:nth-child(n+2)");
		for(Element eTable:eTables) {
			SuKien suKien = new SuKien();
			// Thoi Ky
			if(thoiKy.length() != 0) suKien.setThoiKy(thoiKy);
			// Nguon
			suKien.setNguon(2);
			scrapeTable(suKien, eTable);
		}
		System.out.println(thoiKy);
	}

	
	public static void main(String[] args) {
		
		
		SuKienScraper1 scraper = new SuKienScraper1();
	
		scraper.getData();	
		scraper.save("file\\event-source-2-1.json");
	}
}
