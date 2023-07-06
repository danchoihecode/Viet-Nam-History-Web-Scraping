package scrapers.ditich.vdtls;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.FileWriter;
import java.io.IOException;

public class DiTichWebCrawler{
    public static void main(String[] args) {
        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 1; i <= 336; i += 1) {
            String url = "http://ditich.vn/FrontEnd/DiTich?cpage=" + i + "&rpage=&corder=&torder=&tpage=" + i + "&TEN=&LA_CDT=&LOAI_HINH_XEP_HANG=&XEP_HANG=&DIA_DANH=&TEN_HANG_MUC=&HM_LOAI_HINH_XEP_HANG=&HM_XEP_HANG=&TEN_HIEN_VAT=&HV_LOAI=&namtubo=";
            Document doc;
            try {

                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                System.out.println("Không thể kết nối tới trang web " + url);
                return;
            }
            Elements headings = doc.select("section section section div section a");
            for (Element heading : headings) {
                String ref = heading.attr("href");          
                stringBuffer.append("ditich.vn").append(ref).append("\n");             
            }
        }

        try (FileWriter writer = new FileWriter("file\\ditich.txt")) {
            writer.write(stringBuffer.toString());
            System.out.println("Đã ghi dữ liệu vào tệp tin ditich.txt");
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi dữ liệu vào tệp tin ditich.txt: " + e.getMessage());
        }
    }
}

