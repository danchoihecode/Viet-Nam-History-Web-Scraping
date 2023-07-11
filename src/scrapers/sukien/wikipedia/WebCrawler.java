package scrapers.sukien.wikipedia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import scrapers.Crawler;

public class WebCrawler {
	
	public static boolean stringContains(String inputStr, String[] items) {
	    return Arrays.stream(items).anyMatch(inputStr::contains);
	}

    public static void main(String[] args) {
    	ArrayList<String> isCrawl = new ArrayList<>();
    	Crawler crawler = new Crawler() {
    		@Override
			public void crawl(Document doc) {
    			Elements pages = doc.select("div#mw-pages li:not(:contains(Bản mẫu)) a");
    	    	for (Element e : pages) {    	
    	        	String ref = "https://vi.wikipedia.org" + e.attr("href");       
    	        	addUrl(ref); 	
    	        } 
    	    	
    	    	Elements a = doc.select("div#mw-subcategories a");	
    	    	for (Element e : a) { 
    	    		String[] items = {"Chiến tranh Trung–Nhật", "Nội chiến Campuchia", "Tử trận ở Việt Nam‎"};
    				if(stringContains(e.text(), items)) continue;
    				System.out.println(e.text());
    				String ref = "https://vi.wikipedia.org" + e.attr("href"); 
    	    		if(!isCrawl.contains(ref)) {
    	    			try {
    	    	            doc = Jsoup.connect(ref).get();
    	    	            crawl(doc);
    	    	        } catch (IOException e1) {
    	    	            System.out.println("Không thể kết nối tới trang web " + ref);
    	    	            continue;
    	    	        }
    		        }
    	    	}   	
    	        isCrawl.add(doc.baseUri());	
    		}
    	};
    	String url = "https://vi.wikipedia.org/wiki/Th%E1%BB%83_lo%E1%BA%A1i:Chi%E1%BA%BFn_tranh_li%C3%AAn_quan_t%E1%BB%9Bi_Vi%E1%BB%87t_Nam";
		try {
            Document doc = Jsoup.connect(url).get();
        	crawler.crawl(doc);
        } catch (IOException e) {
            System.out.println("Không thể kết nối tới trang web " + url);
            return;
        };
		crawler.saveToFile("file\\event-source-2.txt");
        
    }
}

