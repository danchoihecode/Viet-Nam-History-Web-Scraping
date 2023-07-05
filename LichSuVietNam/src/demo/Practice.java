package demo;

import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.LinkedHashMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Practice {
	public static void main(String[] args) throws IOException {

		Document doc = Jsoup.parse(new File("C:\\Users\\pc\\Documents\\emperor.html"), "UTF-8");

		Element infobox = doc.select("table.infobox").first();
		Elements rows = infobox.select("tr");

		LinkedHashMap<String, String> vuaHung = new LinkedHashMap<String, String>();
		for (Element row : rows) {
			String key = row.select("th").text();
			String value = row.select("td").text();
			vuaHung.put(key, value);
		}
		Element lichSuSection = doc.select("h2").first();
		Elements paragraphs = lichSuSection.nextElementSiblings();

		StringBuffer s = new StringBuffer();
		for (Element p : paragraphs) {
			s.append(p.text());
		}
		vuaHung.put("Lịch sử", s.toString());
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(vuaHung);

        try (FileWriter file = new FileWriter("C:\\Users\\pc\\Documents\\emperor.json")) {
            file.write(json);
            System.out.println("Ghi JSON vào file thành công");
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}
}
