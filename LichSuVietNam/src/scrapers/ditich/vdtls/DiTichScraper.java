package scrapers.ditich.vdtls;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ditich.DiTich;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
public class DiTichScraper {
	
	private static BufferedReader reader;
	private static Document doc;
	static ArrayList<DiTich> DiTichs = new ArrayList<>();
	static int count = 0;
	private static String escapeJsonCharacters(String input) {
	    return input
	        .replace("\\", "\\\\")  // Escape backslashes
	        .replace("\"", "\\\"")  // Escape double quotes
	        .replace("\b", "\\b")   // Escape backspace
	        .replace("\f", "\\f")   // Escape form feed
	        .replace("\n", "\\n")   // Escape newline
	        .replace("\r", "\\r")   // Escape carriage return
	        .replace("\t", "\\t");  // Escape tab
	}
	
	public static void scrape(String line) {
		DiTich ditich = new DiTich();
		Elements headings = doc.select("h2.hl__comp-heading.hl__comp_heading_custom");
		Elements images = doc.select("img.sp-large");
		Elements locations = doc.select("span.address-line1");
		Elements buildFroms1 = doc.select("span").select(":containsOwn(Niên đại tuyệt đối)");
		Elements buildFroms2 = doc.select("span").select(":containsOwn(Niên đại chủ đạo)");
		String[] wordsToExclude = {"thành hòang làng", "Thành hòang làng", "Tổ", "Tướng có công đánh giặc ngoại xâm", "Tổ tiên", "Đền thờ của xóm", "Thành hoàng", "thành hoàng", "Thành Hoàng", "Đối tượng thờ", "Ngọc Hoàng", "Thần", "thần", "Tiên Thiên", "Địa Thiên", "phật", "Phật"};
		Elements nhanVatLienQuans = doc.select("span:containsOwn(Đối tượng thờ)");
		ArrayList<String> nvlqList = new ArrayList<>();

	    Elements rankingType = doc.select("span").select(":containsOwn(Loại hình xếp hạng)");
	    
	        
		Element image = images.get(0);
		
		String imageLink = image.attr("src");
		String replacedImageLink = imageLink.replace("\\", "/");
		// line = line.replace("\u003d", "=").replace("\u0026", "&");
		
		
		if (locations.isEmpty()) System.out.println("Empty location"); else {
			Element location = locations.get(0);
			String diaDiem = location.ownText();
			ditich.setDiaChi(diaDiem);
		}
		
		if (headings.isEmpty()) System.out.println("Empty array"); else {
			Element heading = headings.get(0);	
			String miniHeading = heading.ownText();
			System.out.println(miniHeading);
			ditich.setTen(miniHeading);
		}
		
		if (!buildFroms1.isEmpty()) {
			Element buildFrom = buildFroms1.get(0);
			String khoiLap = buildFrom.ownText();
			ditich.setKhoiLap(khoiLap);
		} else if (!buildFroms2.isEmpty()) {
			Element buildFrom = buildFroms2.get(0);
			String khoiLap = buildFrom.ownText();
			ditich.setKhoiLap(khoiLap);
		} else System.out.println("Empty khoiLap");
		
		if (nvlqList.size() == 0) { System.out.println("Empty nvlq");} else {
			ditich.setNhanVatLienQuan(nvlqList);	
		}
			
		if (rankingType.isEmpty()) System.out.println("Empty ranking"); else {
			Element xepHang = rankingType.get(0);
			String xH = xepHang.ownText();
			ditich.setPhanLoai(xH);
		}
		

		
		ditich.setNguon(4);
			ditich.setUrl(escapeJsonCharacters(line));
		ditich.setAnh("http://ditich.vn"+ replacedImageLink);
	    if (ditich.getTen().equals("Đình Đốc Hậu") || ditich.getTen().equals(
	    		"Đền Phù Sa")|| ditich.getTen().equals("Đình Cổ Lão (Đình Cả)") || ditich.getTen().equals("Đình Cả (Đình Khuân)")|| ditich.getTen().equals("Đình Quan Xuyên")){
	    						DiTichs.add(ditich);
	    				    	return;
	    				    } ;
		for (Element element : nhanVatLienQuans) {
			
		    String textContent = element.text();
		    if (ditich.getTen().equals("Đình Đốc Hậu") || ditich.getTen().equals(
		    		"Đền Phù Sa")|| ditich.getTen().equals("Đình Cổ Lão (Đình Cả)") || ditich.getTen().equals("Đình Cả (Đình Khuân)")){
		    				    	nvlqList.add(textContent);
		    				    	ditich.setNhanVatLienQuan(nvlqList);
		    				    	continue;
		    				    } ;
		    if (ditich.getTen().equals("Đình Quan Xuyên")) {
		    				    	continue;
		    				    }
		    

		    textContent = textContent.replaceAll(";", ",");
		    String[] values = textContent.split(":");
		    
		    
		    for (String value : values) {
		        String[] values2 = value.split(",");

		        for (String value1 : values2) {
		            boolean shouldExclude = false;
		            for (String exclude : wordsToExclude) {
		                if (value1.contains(exclude)) {
		                    shouldExclude = true;
		                    break;
		                }
		            }

		            if (!shouldExclude) {
		                nvlqList.add(value1.trim());
		            }
		        }
		    }
		}
		DiTichs.add(ditich);
	}
	
	

	public static void main(String args[]) {
		try {
			reader = new BufferedReader(new FileReader("file\\ditich.txt"));
			String line = reader.readLine();
			while (line != null) {
				try {
					line = "http://" + line;
					doc = Jsoup.connect(line).get();
					System.out.println(line);
					count++;
				} catch (IOException e) {
					System.out.println("Không thể kết nối tới trang web "+line);
					line = reader.readLine();
					continue;
				}
				scrape(line);
				
				line = reader.readLine();
			}
			reader.close();
			count++;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Save di tich Info
		Gson gson1 = new GsonBuilder().setPrettyPrinting().create();
		String json1;
		json1 = gson1.toJson(DiTichs);
		String outputFile1 = "C:\\Users\\admin\\Desktop\\OOPBigProject\\LichSuVietNam\\file\\figure-source-4.json";
		
		try (FileWriter writer = new FileWriter(outputFile1)){
			writer.write(json1);
			System.out.println("Dữ liệu đã được ghi vào file " + outputFile1);
			System.out.println(count + " bản ghi.");
		} catch (IOException e) {
			System.out.println("Ghi dữ liệu vào file thất bại.");
		}	
		
		
	}

}
