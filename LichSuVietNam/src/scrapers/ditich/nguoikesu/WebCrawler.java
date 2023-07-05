package scrapers.ditich.nguoikesu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.FileWriter;
import java.io.IOException;

public class WebCrawler {

    public static void main(String[] args) {
        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i <= 20; i += 10) {
            String url = "https://nguoikesu.com/di-tich-lich-su?types[0]=1&start=" + i;
            Document doc;
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                System.out.println("Không thể kết nối tới trang web " + url);
                return;
            }
            Elements headings = doc.select("h3 a");
            for (Element heading : headings) {
                String ref = heading.attr("href");          
                stringBuffer.append("https://nguoikesu.com").append(ref).append("\n");             
            }
        }

        try (FileWriter writer = new FileWriter("file\\site-source-3.txt")) {
            writer.write(stringBuffer.toString());
            System.out.println("Đã ghi dữ liệu vào tệp");
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi dữ liệu vào tệp: " + e.getMessage());
        }
    }
}

