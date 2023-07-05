package scrapers.lehoi.wikipedia;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
//import java.util.HashSet;
//import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
	public static void main(String[] args) {

		StringBuilder str = new StringBuilder();
		Document doc;

		try {

			doc = Jsoup.connect(
					"https://vi.wikipedia.org/wiki/L%E1%BB%85_h%E1%BB%99i_c%C3%A1c_d%C3%A2n_t%E1%BB%99c_Vi%E1%BB%87t_Nam")
					.get();
		} catch (IOException e) {
			System.out.println("Không thể kết nối tới trang web ");
			return;
		}

		Elements uls = doc.select("div.div-col ul a");
		for (Element ul : uls) {

			if (!ul.attr("title").contains("không tồn tại")) {

				str.append("https://vi.wikipedia.org").append(ul.attr("href")).append("\n");

			}
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(
				"C:\\Users\\pc\\Documents\\OOPBigProject\\LichSuVietNam\\file\\festival-source-2.txt"))) {

			writer.write(str.toString());
			System.out.println("Ghi thành công vào file.");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
