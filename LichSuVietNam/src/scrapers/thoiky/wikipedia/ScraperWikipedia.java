package scrapers.thoiky.wikipedia;

import thoiky.ThoiKy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ScraperWikipedia {

	public static void main(String[] args) {
		Document doc;
		try {
			doc = Jsoup.connect("https://vi.wikipedia.org/wiki/Vua_Việt_Nam").get();
		} catch (IOException e) {
			System.out.println("Không thể kết nối tới trang web.");
			return;
		}

		ArrayList<ThoiKy> periods = new ArrayList<>();
		Element infobox = doc.select("table.wikitable").get(6);
		Elements rows = infobox.select("tr");
		for (int i = 1; i < rows.size(); i++) {
			Elements data = rows.get(i).select("td");
			String ten = data.get(0).text();
			String nguoiSangLap = data.get(1).text();
			String thuDo = data.get(3).text().replaceAll("\\[[^\\]]*\\]", "");
			ThoiKy period = new ThoiKy();
			period.setTen(ten);
			period.setNguoiSangLap(nguoiSangLap);
			period.setThuDo(thuDo);
			period.setNguon(2);
			periods.add(period);
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(periods);

		String outputFile = "C:\\Users\\pc\\Documents\\OOPBigProject\\LichSuVietNam\\file\\period-source-2.json";
		try (FileWriter writer = new FileWriter(outputFile)) {
			writer.write(json);
			System.out.println("Dữ liệu đã được ghi vào file " + outputFile);
		} catch (IOException e) {
			System.out.println("Ghi dữ liệu vào file thất bại.");
		}

	}

}
