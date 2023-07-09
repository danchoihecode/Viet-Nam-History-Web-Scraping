package scrapers.sukien.nguoikesu;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.sukien.SuKien;
import scrapers.sukien.EventScraper;
import utilities.EntityUrl;

public class SuKienScraper {

	public static void main(String[] args) {

		HashMap<String, String> nhanVatJson = EntityUrl.getEntityUrlAndName("file\\figure-source-3.json");

		EventScraper scraper = new EventScraper() {

			@Override
			public void scrape(Document doc) {
				SuKien suKien = new SuKien();

				// Url
				suKien.setUrl(doc.baseUri());

				// Ten
				String ten = doc.select("div.page-header > h1").text();
				if (ten.length() != 0)
					suKien.setTen(ten);

				// Thoi Gian
				String thoiGian = doc.select("td:contains(Thời gian) + td").text();
				if (thoiGian.length() != 0)
					suKien.setThoiGian(thoiGian);

				// Dia Diem
				String diaDiem = doc.select("td:contains(Địa điểm) + td").text();
				if (diaDiem.length() != 0)
					suKien.setDiaDiem(diaDiem);

				// Ket Qua
				String nguyenNhan = doc.select("td:contains(Nguyên nhân) + td").text();
				if (nguyenNhan.length() != 0)
					suKien.setNguyenNhan(nguyenNhan);

				// Ket Qua
				String ketQua = doc.select("td:contains(Kết quả) + td").text();
				if (ketQua.length() != 0)
					suKien.setKetQua(ketQua);

				// Mieu Ta
				Elements mieuTa = doc.select("div.infobox ~ p:not(h2:first-of-type ~ p, h2:first-of-type ~ * p)")
						.select("p:not(:has(br))");
				if (!mieuTa.isEmpty())
					suKien.setMieuTa(mieuTa.text());

				// Tham Chien
				ArrayList<ArrayList<String>> thamChienList = new ArrayList<>();
				Elements thamChien = doc.select("tr:contains(Tham chiến) + tr[valign=\"top\"]").select("td");
				for (Element e : thamChien) {
					ArrayList<String> arr = new ArrayList<String>();
					String[] thamChienSplit = e.html().split("<br>");
					for (int i = 0; i < thamChienSplit.length; i++) {
						String str = Jsoup.parse(thamChienSplit[i]).text();
						if (str.length() != 0)
							arr.add(str);
					}
					thamChienList.add(arr);
				}
				if (!thamChienList.isEmpty())
					suKien.setThamChien(thamChienList);

				// Chi Huy
				ArrayList<ArrayList<String>> chiHuyList = new ArrayList<>();
				Elements chiHuy = doc.select("tr:contains(Chỉ huy) + tr[valign=\"top\"]").select("td");
				for (Element e : chiHuy) {
					ArrayList<String> arr = new ArrayList<String>();
					String[] chiHuySplit = e.html().split("<br>");
					for (int i = 0; i < chiHuySplit.length; i++) {
						String str = Jsoup.parse(chiHuySplit[i]).text().replace(" †", "");
						if (str.length() != 0)
							arr.add(str);
					}
					chiHuyList.add(arr);
				}
				if (!chiHuyList.isEmpty())
					suKien.setChiHuy(chiHuyList);

				// Luc Luong
				ArrayList<String> lucLuongList = new ArrayList<>();
				Elements lucLuong = doc.select("tr:contains(Lực lượng) + tr[valign=\"top\"]").select("td");
				for (Element e : lucLuong) {
					String str = e.html().replaceAll("(?s)<sup.*?</sup>", "");
					lucLuongList.add(Jsoup.parse(str).text());
				}
				if (!lucLuongList.isEmpty())
					suKien.setLucLuong(lucLuongList);

				// Ton That
				ArrayList<String> tonThatList = new ArrayList<>();
				Elements tonThat = doc.select("tr:contains(Tổn thất) + tr[valign=\"top\"]").select("td");
				for (Element e : tonThat) {
					String str = e.html().replaceAll("(?s)<sup.*?</sup>", "");
					tonThatList.add(Jsoup.parse(str).text());
				}
				if (!tonThatList.isEmpty())
					suKien.setTonThat(tonThatList);

				// Nhan Vat Lien Quan
				ArrayList<String> nhanVatLienQuan = new ArrayList<>();
				Elements a1 = doc.select("tr:contains(Chỉ huy) + tr, tr:contains(Tham chiến) + tr").select("a");
				Elements a2 = mieuTa.select("a");
				for (Element a : a1) {
					String str = "https://nguoikesu.com" + a.attr("href");
					if (nhanVatJson.containsKey(str)) {
						if (!nhanVatLienQuan.contains(nhanVatJson.get(str)))
							nhanVatLienQuan.add(nhanVatJson.get(str));
					}
				}
				for (Element a : a2) {
					String str = "https://nguoikesu.com" + a.attr("href");
					if (nhanVatJson.containsKey(str)) {
						if (!nhanVatLienQuan.contains(nhanVatJson.get(str)))
							nhanVatLienQuan.add(nhanVatJson.get(str));
					}
				}
				if (!nhanVatLienQuan.isEmpty())
					suKien.setNhanVatLienQuan(nhanVatLienQuan);

				// Nguon
				suKien.setNguon(3);

				// Them vao list
				addSuKien(suKien);

			}
		};
		ArrayList<Document> docs = scraper.connectToUrls("file\\event-source-3.txt");
		for (Document doc : docs) {
				scraper.scrape(doc);
		}
		scraper.getJsonString();
		scraper.saveToFile("file\\event-source-3.json");

	}

}
