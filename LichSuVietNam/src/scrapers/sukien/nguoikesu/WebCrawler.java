package scrapers.sukien.nguoikesu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.FileWriter;
import java.io.IOException;

public class WebCrawler {

    public static void main(String[] args) {
        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i <= 70; i += 5) {
            String url = "https://nguoikesu.com/tu-lieu/quan-su?filter_tag[0]=&start=" + i;
            Document doc;
            try {
                doc = Jsoup.connect(url).get();
                System.out.println(url);
            } catch (IOException e) {
                System.out.println("Không thể kết nối tới trang web " + url);
                return;
            }
            Elements headings = doc.select("h2 a");
            for (Element heading : headings) {
                String ref = heading.attr("href");       
                if(!ref.contains("1979") && !ref.contains("1988")) stringBuffer.append("https://nguoikesu.com").append(ref).append("\n");             
            }
        }

        try (FileWriter writer = new FileWriter("file\\event-source-3.txt")) {
            writer.write(stringBuffer.toString());
            System.out.println("Đã ghi dữ liệu vào tệp tin");
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi dữ liệu vào tệp tin: " + e.getMessage());
        }
    }
}

