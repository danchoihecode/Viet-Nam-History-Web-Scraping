package scrapers.sukien.wikipedia;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class WebCrawler {
	
	private static StringBuffer stringBuffer = new StringBuffer();
	private static Document doc;
	private static ArrayList<String> isCrawl = new ArrayList<>();
	
	public static boolean stringContains(String inputStr, String[] items) {
	    return Arrays.stream(items).anyMatch(inputStr::contains);
	}
	
	public static void Crawl(String url) {	
		try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("Không thể kết nối tới trang web " + url);
            return;
        }

    	Elements pages = doc.select("div#mw-pages li:not(:contains(Bản mẫu)) a");
    	for (Element e : pages) {    	
        	String ref = "https://vi.wikipedia.org" + e.attr("href");       
        	if(stringBuffer.indexOf(ref) == -1) stringBuffer.append(ref).append("\n");   	
        } 
    	
    	Elements a = doc.select("div#mw-subcategories a");	
    	for (Element e : a) { 
    		String[] items = {"Chiến tranh Trung–Nhật", "Nội chiến Campuchia", "Tử trận ở Việt Nam‎"};
			if(stringContains(e.text(), items)) continue;
			System.out.println(e.text());
			String ref = "https://vi.wikipedia.org" + e.attr("href"); 
    		if(!isCrawl.contains(ref)) {
    			Crawl(ref);
	        }
    	}   	
        isCrawl.add(url);	
	}

    public static void main(String[] args) {
        
        String url = "https://vi.wikipedia.org/wiki/Th%E1%BB%83_lo%E1%BA%A1i:Chi%E1%BA%BFn_tranh_li%C3%AAn_quan_t%E1%BB%9Bi_Vi%E1%BB%87t_Nam";
        Document doc;
        
        Crawl(url);
        
        try (FileWriter writer = new FileWriter("file\\event-source-2.txt")) {
            writer.write(stringBuffer.toString());
            System.out.println("Đã ghi dữ liệu vào tệp tin");
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi dữ liệu vào tệp tin: " + e.getMessage());
        }
    }
}

