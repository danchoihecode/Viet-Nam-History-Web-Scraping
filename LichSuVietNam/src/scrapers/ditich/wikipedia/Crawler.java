package scrapers.ditich.wikipedia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//import java.util.HashSet;
//import java.util.Set;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
	public static void main(String[] args) {

		Set<String> set = new HashSet<String>();
		BufferedReader reader;
		StringBuilder str;
		Document doc;
		String[] tmp;
		String s;
		try {
			reader = new BufferedReader(new FileReader(
					"C:\\Users\\pc\\Documents\\OOPProject\\LichSuVietNam\\file\\url-site-source-2.txt"));
			String line = reader.readLine();
			while (line != null) {

				int x;
				int y = 0;
				tmp = line.split("\\|");
				x = Integer.parseInt(tmp[0]);
				try {

					doc = Jsoup.connect(tmp[1]).get();
				} catch (IOException e) {
					System.out.println("Không thể kết nối tới trang web ");
					break;
				}
				Elements uls = doc.select("h3 + ul").select("a");
				for (Element ul : uls) {
					y++;
					s = ul.text();

					if (y > x && !s.contains("Cụm đình") && !s.contains("đặc biệt") && !s.equals("Đình")
							&& !s.equals("Kèo") && !s.contains("Việt Nam") && !s.equals("Nhà dài Ê Đê")
							&& !s.equals("Nhà rông") && !s.equals("Cổng làng") && !s.contains("Bẩy")
							&& !s.equals("Phú Riềng Đỏ") && !s.contains("Bản mẫu")) {

						str = new StringBuilder();
						str.append("https://vi.wikipedia.org").append(ul.attr("href"));
						set.add(str.toString());
					}
				}
				line = reader.readLine();
			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		try (BufferedWriter writer = new BufferedWriter(
				new FileWriter("C:\\Users\\pc\\Documents\\OOPProject\\LichSuVietNam\\file\\site-source-2.txt"))) {
			for (String element : set) {
				writer.write(element);
				writer.newLine();
			}
			System.out.println("Ghi thành công vào file.");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
