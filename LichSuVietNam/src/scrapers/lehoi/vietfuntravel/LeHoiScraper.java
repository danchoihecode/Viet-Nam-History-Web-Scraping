package scrapers.lehoi.vietfuntravel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.lehoi.LeHoi;
import scrapers.lehoi.FestivalScraper;

public class LeHoiScraper {

	public static void main(String[] args) {

		FestivalScraper scraper = new FestivalScraper() {

			@Override
			public void scrape(Document doc) {
				Elements titles = doc.select("div.content-desc h2");
				for (Element title : titles) {
					LeHoi leHoi = new LeHoi();
					String str = title.text();
					String[] parts = str.split("\\.");
					String ten = parts[1].trim();
					if (ten.length() != 0)
						leHoi.setTen(ten);
					String text = doc
							.select("h2:contains(" + str + ") ~ p:not(h2:contains(" + str + ") ~ h2 ~ p, p:has(em))")
							.text().trim();

					Pattern pattern = Pattern.compile("(?<=diễn ra|Diễn ra\\s).+?(?=\\. |, hội đình|tại)");
					Matcher matcher = pattern.matcher(text);
					if (matcher.find()) {
						String result = matcher.group();
						leHoi.setThoiGian("Diễn ra " + result.trim());
					}
					pattern = Pattern
							.compile("(?<=Địa điểm diễn ra|Địa điểm tổ chức|tại|diễn ra ở\\s).+?(?=\\.|, hội)");
					matcher = pattern.matcher(text);
					if (matcher.find()) {
						String result = matcher.group();
						leHoi.setDiaDiem(result.trim());
					}

					pattern = Pattern.compile("([^.?!]+[.?!])");
					matcher = pattern.matcher(text);
					String lastSentence = "";
					while (matcher.find()) {
						lastSentence = matcher.group();
						leHoi.setMieuTa(lastSentence.trim());
					}

					String anh = doc
							.select("h2:contains(" + str + ") ~ p:has(img):not(h2:contains(" + str + ") ~ h2 ~ p)")
							.select("img[src~=(?i)\\.(png|jpe?g|gif)], div.thumbnail img[src~=(?i)\\.(png|jpe?g|gif)]")
							.attr("src");
					if (anh.length() != 0)
						leHoi.setAnh("https://www.vietfuntravel.com.vn" + anh);
					leHoi.setNguon(9);
					addLeHoi(leHoi);

				}

			}
		};
		Document doc = scraper.connectToUrl("https://www.vietfuntravel.com.vn/blog/nhung-le-hoi-lon-o-ha-noi.html");
		if (doc != null) {
			scraper.scrape(doc);
			scraper.getJsonString();
			scraper.saveToFile("file\\festival-source-9.json");
		}
	}
}
