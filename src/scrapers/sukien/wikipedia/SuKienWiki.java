package scrapers.sukien.wikipedia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.sukien.SuKien;
import scrapers.sukien.EventScraper;

public abstract class SuKienWiki extends EventScraper {
	
	public boolean stringContains(String inputStr, String[] items) {
	    return Arrays.stream(items).anyMatch(inputStr::contains);
	}
	
	public void scrapeWiki(Document doc, HashMap<String, String> nhanVatJson, HashSet<String> suKienJson) {	
		if(isAdded(doc) || suKienJson.contains(doc.baseUri())) {
			return;
		}
		
		SuKien suKien = new SuKien();
		Elements e = doc.select("table.vevent");
		if(e.isEmpty()) {
			return;
		}
		else {
			// Url
			suKien.setUrl(doc.baseUri());
			
			// Ten
			String ten = doc.select("h1#firstHeading").text();
			if(ten.length() != 0) suKien.setTen(ten);
			
			// Thoi Gian
			String thoiGian = e.select("th:contains(Thời gian) + td").html().replaceAll("(?s)<sup.*?</sup>", "");
			String[] thoiGianItems = {"1976", "1977", "1978", "1979", "198", "199", "200", "201", "202"};
			if(stringContains(thoiGian, thoiGianItems)) {
				return;
			}
			if(thoiGian.length() != 0) suKien.setThoiGian(Jsoup.parse(thoiGian).text());
			
			// Dia Diem
			String diaDiem = e.select("th:contains(Địa điểm) + td").html().replaceAll("(?s)<sup.*?</sup>", "");
			if(diaDiem.length() != 0) suKien.setDiaDiem(Jsoup.parse(diaDiem).text());
			
			// Ket Qua
			String ketQua = e.select("th:contains(Kết quả) + td").html().replaceAll("(?s)</li>.*?<li>", ". ").replaceAll("(?s)<sup.*?</sup>", "");
			if(ketQua.length() != 0) suKien.setKetQua(Jsoup.parse(ketQua).text());
			
			// Tham Chien
			ArrayList<ArrayList<String>> thamChienList = new ArrayList<>();
			Elements thamChiens  = e.select("tr:has(th:containsWholeText(Tham chiến)) + tr").select("td");
			for(Element eThamChiens:thamChiens) {
				String html = eThamChiens.html().replaceAll("(?s)<div class=\"NavHead\".*?</div>|(?s)<li .*?</li>", "").replaceAll("(?s)<sup.*?</sup>", "");
				String thamChien = html.replaceAll("(?s)<li>.*?</li>|<p>|</p>", "<br>");
				String[] thamChienSplit =  thamChien.split("<br>|<hr>");
				ArrayList<String> arr = new ArrayList<String>();
				for(int i=0; i<thamChienSplit.length; i++) {
					String str = Jsoup.parse(thamChienSplit[i]).text();
					if(str.length() != 0) arr.add(str);
				}
				if(arr.isEmpty()) {
					String thamChienAdd = html.replaceAll("<li>|</li>|<ul>|</ul>", "<br>");
					String[] thamChienAddSplit =  thamChienAdd.split("<br>|<hr>");
					for(int i=0; i<thamChienAddSplit.length; i++) {
						String str = Jsoup.parse(thamChienAddSplit[i]).text();
						if(str.length() != 0) arr.add(str);
					}
				}
				thamChienList.add(arr);
			}
			if(!thamChienList.isEmpty()) suKien.setThamChien(thamChienList);
			
			// Chi Huy
			ArrayList<ArrayList<String>> chiHuyList = new ArrayList<>();
			Elements chiHuys  = e.select("tr:has(th:containsWholeText(Chỉ huy và lãnh đạo)) + tr").select("td");
			for(Element eChiHuys:chiHuys) {
				ArrayList<String> arr = new ArrayList<String>();
				String html = eChiHuys.html().replaceAll("(?s)</li>.*?<li>", "<br>").replaceAll("(?s)<sup.*?</sup>", "");
				String[] chiHuySplit =  html.split("<br>");
				for(int i=0; i<chiHuySplit.length; i++) {
					String str = Jsoup.parse(chiHuySplit[i]).text().replace(" †","");
					if(str.length() != 0) arr.add(str);
				}
				chiHuyList.add(arr);
			}
			if(!chiHuyList.isEmpty()) suKien.setChiHuy(chiHuyList);
			
			// Nhan Vat Lien Quan
			ArrayList<String> nhanVatLienQuan = new ArrayList<>();
			Elements aTags  = doc.select("div.mw-parser-output > *:not(h2:contains(Chú thích) ~ *, h2:contains(Tham khảo) ~ *, h2:contains(Xem thêm) ~ *)").select("*");	
			for(Element aTag:aTags) {
				String url = "https://vi.wikipedia.org" + aTag.attr("href");
				if(nhanVatJson.containsKey(url)) {
					String nhanVat = nhanVatJson.get(url);
					if(!nhanVatLienQuan.contains(nhanVat)) nhanVatLienQuan.add(nhanVat);
				}			
				if(aTag.text().contains("Những nhà lãnh đạo")) {
					try {
						Document document = Jsoup.connect(url).get();
						Elements as = document.select("a:not(.new)");
						for(Element a:as) {
							String str = "https://vi.wikipedia.org" + a.attr("href");
							if(nhanVatJson.containsKey(str)) {
								if(!nhanVatLienQuan.contains(nhanVatJson.get(str))) nhanVatLienQuan.add(nhanVatJson.get(str));
							}
						}
					} catch (IOException ex) {
						System.out.println("Không thể kết nối tới trang web." + url);
					}
				}
			}
			if(!nhanVatLienQuan.isEmpty()) suKien.setNhanVatLienQuan(nhanVatLienQuan);		
			
			// Luc Luong
			ArrayList<String> lucLuongList = new ArrayList<>();
			Elements lucLuongs  = e.select("tr:has(th:containsWholeText(Lực lượng)) + tr").select("td");
			for(Element lucLuong:lucLuongs) {
				String str = lucLuong.html().replaceAll("(?s)<sup.*?</sup>", "");
				lucLuongList.add(Jsoup.parse(str).text());
			}
			if(!lucLuongList.isEmpty()) suKien.setLucLuong(lucLuongList);
			
			// Ton That
			ArrayList<String> tonThatList = new ArrayList<>();
			Elements tonThats  = e.select("tr:has(th:containsWholeText(Thương vong và tổn thất)) + tr").select("td"); 
			for(Element tonThat:tonThats) {
				String str = tonThat.html().replaceAll("(?s)<sup.*?</sup>", "");
				tonThatList.add(Jsoup.parse(str).text());
			}
			if(!tonThatList.isEmpty()) suKien.setTonThat(tonThatList);
			
			// Mieu ta
			Elements p = doc.select("div.mw-parser-output > p:has(b)");
			if(!p.isEmpty()) {
				String mieuTa = doc.select("div.mw-parser-output > p:has(b)").first().html().replaceAll("(?s)<sup.*?</sup>", "");
				String str = Jsoup.parse(mieuTa).text();
				String[] mieuTaItems = {"phim", "bài hát", "nhạc sĩ", "ca sĩ", "là con đường", "đơn vị nghiên cứu và phát triển", "ca khúc", "chương trình", "hỏa hoạn", "vụ nổ", "câu chuyện", "công ty"};
				if(stringContains(str, mieuTaItems)) {
					return;
				}
				if(str.length() != 0) suKien.setMieuTa(str);
			}
			
			// Nguon
			suKien.setNguon(2);	
			
			// Them Vao List
			addSuKien(suKien);
		}
	}
	
}
