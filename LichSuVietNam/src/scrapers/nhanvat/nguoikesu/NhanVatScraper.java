package scrapers.nhanvat.nguoikesu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

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

import entities.nhanvat.LanhDaoQuocGia;
import entities.nhanvat.NhanVat;
import entities.thoiky.ThoiKy;


public class NhanVatScraper {
	
	private static ArrayList<NhanVat> nhanVats = new ArrayList<>();
	private static ArrayList<LanhDaoQuocGia> lanhDaos = new ArrayList<>();
	private static Document doc;
	private static BufferedReader reader;
	private static int count = 0;
	
	
	public static boolean isNull(NhanVat nhanVat) {
		if(nhanVat.getMieuTa() == null) return true;
		return false;
	}
	
	public static boolean stringContains(String inputStr, String[] items) {
		
	    return Arrays.stream(items).anyMatch(inputStr::contains);
	}
	
	public static void scrapeDetail(NhanVat nhanVat, Document doc) {
		
		// Mieu ta
		String text = doc.select("div#toc ~ p:first-of-type").html().replaceAll("(?s)<sup.*?</sup>", "");
		String mieuTa = Jsoup.parse(text).text();
		String[] mieuTaItems = {"thụy hiệu của một số vị quân chủ", "Triều Tiên", "Xiêm", "có thể là", "Đài Loan", "là một triều đại", "là triều đại", "là một vọng tộc", "là nơi ở", 
				"là các tên gọi", "diễn viên", "một trong các dân tộc", "là kết hợp thứ", "là một giai đoạn", "văn bản lịch sử", "là một huyện", "là một thành phố", "một trong mười nước",
				"có thể đề cập", "là thủ phủ"};
		if(stringContains(mieuTa, mieuTaItems)) {
			return;
		}
		if(mieuTa.length() != 0) nhanVat.setMieuTa(mieuTa);
		
		// Que quan
		String queQuan = doc.select("div.infobox").select("th:containsOwn(Quê quán) + td").html().replaceAll("(?s)<sup.*?</sup>", "");
		if(queQuan.length() != 0) nhanVat.setQueQuan(Jsoup.parse(queQuan).text());
		
		// Ten
		String ten = doc.select("div.page-header>h2").text();
		nhanVat.setTen(ten);
		
		// Nam Sinh & Noi Sinh
		String namSinh = doc.select("div.infobox").select("th:containsOwn(Sinh) + td").html().replaceAll("(?s)<sup.*?</sup>", "");
		if(namSinh.length() != 0) nhanVat.setNamSinh(Jsoup.parse(namSinh).text());
		
		// Nam Mat & Noi Mat
		String namMat = doc.select("div.infobox").select("th:containsOwn(Mất) + td").html().replaceAll("(?s)<sup.*?</sup>", "");
		if(namMat.length() != 0) nhanVat.setNamMat(Jsoup.parse(namMat).text());
		
		// Ten Khac	
		String tenKhac = doc.select("div.infobox").select("th:containsOwn(Tên húy) + td, th:containsOwn(Tên thật) + td, th:containsOwn(Bút danh) + td, th:containsOwn(Tên khác) + td").html().replaceAll("(?s)<sup.*?</sup>", "");
		if(tenKhac.length() != 0) {
			String[] tenKhacSplit = tenKhac.split("<br>");
			ArrayList<String> tenKhacFinal = new ArrayList<>();
			for(int i=0; i<tenKhacSplit.length; i++) {
				tenKhacFinal.add(Jsoup.parse(tenKhacSplit[i]).text());
			}
			nhanVat.setTenKhac(tenKhacFinal);
		}
				
		// Thoi Ky
		String thoiKy = doc.select("div.infobox").select("th:containsOwn(Triều đại) + td, th:containsOwn(Kỷ nguyên) + td").html().replaceAll("(?s)<sup.*?</sup>", "");
		if(thoiKy.length() != 0) nhanVat.setThoiKy(Jsoup.parse(thoiKy).text());

		// Anh
		String anh = doc.select("tr img[src~=(?i)\\.(png|jpe?g|gif)], div.thumbnail img[src~=(?i)\\.(png|jpe?g|gif)]").attr("src");
		String images = "https://nguoikesu.com" + anh;
		if(!images.equals("https://nguoikesu.com")) nhanVat.setAnh(images);
		
		// Nguon
		nhanVat.setNguon(3);
		
		// Tac Pham
		ArrayList<String> tacPhams = new ArrayList<>();
		String tacPham1 = doc.select("div.infobox").select("tr:containsWholeText(Tác phẩm)").select("td:not(td:contains(Xem mục Tác phẩm))").html().replaceAll("(?s)<sup.*?</sup>", "");
		if(tacPham1.length() != 0) tacPhams.add(Jsoup.parse(tacPham1).text());
		
		String tacPham2 = doc.select("h2:containsOwn(Tác phẩm) ~ ul:not(h2:containsOwn(Tác phẩm) ~ h2 ~ ul, h2:containsOwn(Tác phẩm) ~ h2 ~ * ul)").html().replace("\'", "`").replaceAll("(?s)<sup.*?</sup>", "");
		String[] tacPham2Split = tacPham2.split("<li>");
		for(int i=1; i<tacPham2Split.length; i++) {
			tacPhams.add(Jsoup.parse(tacPham2Split[i]).text());
		}
		if(!tacPhams.isEmpty()) nhanVat.setTacPham(tacPhams);

	}
	
	public static void scrape(String url) {
		
//		Elements isChinese1 = doc.select("tr:containsWholeText(Sinh), tr:containsWholeText(Mất), tr:containsWholeText(An táng), tr:containsWholeText(Quê quán), tr:containsWholeText(Hoàng đế Trung Hoa), tr:containsWholeText(Vua Trung Hoa)").select("td:containsOwn(Trung Hoa), td:containsOwn(Trung Quốc)");
//		Elements isChinese2 = doc.select("div#toc ~ p:first-of-type").select("p:contains(trong lịch sử Trung Quốc)");
		Elements diaDanh= doc.select("tr:containsWholeText(Địa lý), tr:containsWholeText(Tọa độ), tr:containsWholeText(Địa chỉ), tr:containsWholeText(Diện tích)");
		Elements thuBac = doc.select("table.infobox > caption:containsOwn(Thứ bậc Hoàng tộc, Quý tộc và Hiệp sĩ)");
		if(diaDanh.isEmpty() && thuBac.isEmpty()) {
			Elements isVua = doc.select("td:containsOwn(Vua Việt Nam)");
			if(!isVua.isEmpty()) {
				LanhDaoQuocGia lanhDao = new LanhDaoQuocGia();
				lanhDao.setUrl(url);
				// Thoi Gian Tai Vi
				Elements thoiGianTaiVi = doc.select("div.infobox").select("th:containsOwn(Trị vì) + td, th:containsOwn(Tại vị) + td");
				if(!thoiGianTaiVi.isEmpty()) {
					String thoiGianTaiViFirst = thoiGianTaiVi.first().html().replaceAll("(?s)<sup.*?</sup>", "");
					lanhDao.setThoiGianTaiVi(Jsoup.parse(thoiGianTaiViFirst).text());
				}
				scrapeDetail(lanhDao, doc);
				if(!isNull(lanhDao) && !lanhDaos.contains(lanhDao)) {
					lanhDaos.add(lanhDao);
					count++;
				}
			}
			else {
				NhanVat nhanVat = new NhanVat();
				nhanVat.setUrl(url);
				scrapeDetail(nhanVat, doc);
				if(!isNull(nhanVat) && !nhanVats.contains(nhanVat)) {
					nhanVats.add(nhanVat);
					count++;
				}
			}	
		}
	}
	

	public static void main(String[] args) {

		try {
			reader = new BufferedReader(new FileReader("file\\figure-source-3.txt"));
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
	
//		String url = "https://nguoikesu.com/nhan-vat/nguyen-trai";
//		try {
//			doc = Jsoup.connect(url).get();
//			scrape(url);
//		} catch (IOException e) {
//			System.out.println("Không thể kết nối tới trang web.");
//			return;
//		}
		
		// Luu Nhan Vat
		Gson gson1 = new GsonBuilder().setPrettyPrinting().create();
		String json1;
		json1 = gson1.toJson(nhanVats);
		String outputFile1 = "file\\figure-source-3-1.json";
		try (FileWriter writer = new FileWriter(outputFile1)) {
			writer.write(json1);
			System.out.println("Dữ liệu đã được ghi vào file " + outputFile1);
			System.out.println(count + " bản ghi.");
		} catch (IOException e) {
			System.out.println("Ghi dữ liệu vào file thất bại.");
		}	
		
		// Luu Lanh Dao
		Gson gson2 = new GsonBuilder().setPrettyPrinting().create();
		String json2;
		json2 = gson2.toJson(lanhDaos);
		String outputFile2 = "file\\figure-source-3-2.json";
		try (FileWriter writer = new FileWriter(outputFile2)) {
			writer.write(json2);
			System.out.println("Dữ liệu đã được ghi vào file " + outputFile2);
			System.out.println(count + " bản ghi.");
		} catch (IOException e) {
			System.out.println("Ghi dữ liệu vào file thất bại.");
		}	
	}

}
