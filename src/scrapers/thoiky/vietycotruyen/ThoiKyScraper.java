package scrapers.thoiky.vietycotruyen;

import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.thoiky.ThoiKy;
import utilities.PeriodScraper;

public class ThoiKyScraper {

	public static void main(String[] args) {

		PeriodScraper scraper = new PeriodScraper() {

			@Override
			public void scrape(Document doc) {

				Elements rows = doc.select("table tr");
				int i = 1;
				while (i < rows.size() - 1) {
					ThoiKy thoiKy = new ThoiKy();
					ArrayList<String> lanhDaoQuocGia = new ArrayList<>();
					Element row = rows.get(i);
					Elements row1 = row.select("tr:has(td:nth-of-type(5)):not(:has(td:nth-of-type(6)))");
					Elements row2 = row.select("tr:has(td:nth-of-type(4)):not(:has(td:nth-of-type(5)))");
					if (!row1.isEmpty()) {
						String str = row.select("td").get(1).text();
						if (str.length() == 0) {
							i++;
							continue;
						} else if (str.contains("(") && str.indexOf(")") == str.length() - 1) {
							String[] parts = str.split("\\(");
							String part1 = parts[0].trim();
							String part2 = parts[1].trim().replace(")", "");
							if (part1.length() != 0)
								thoiKy.setTen(part1);
							if (part2.length() != 0)
								thoiKy.setThoiGian(part2);
						} else {
							if (str.length() != 0)
								thoiKy.setTen(str);
						}

						String lanhDao = row.select("td").get(2).text();
						String triVi = row.select("td").get(3).text();
						if (lanhDao.length() != 0 && triVi.length() != 0)
							lanhDaoQuocGia.add(lanhDao + " (" + triVi + ")");
						else {
							String time = row.select("td").get(3).text();
							if (time.length() != 0)
								thoiKy.setThoiGian(time);
						}
						while (true) {
							i++;
							Elements row3 = rows.get(i)
									.select("tr:has(td:nth-of-type(3)):not(:has(td:nth-of-type(4)))");
							if (row3.isEmpty()) {
								break;
							} else {
								lanhDao = row3.select("td").get(0).text();
								triVi = row3.select("td").get(1).text();
								if (lanhDao.length() != 0 && triVi.length() != 0)
									lanhDaoQuocGia.add(lanhDao + " (" + triVi + ")");
							}
						}
						if (!lanhDaoQuocGia.isEmpty())
							thoiKy.setLanhDaoQuocGia(lanhDaoQuocGia);
					} else if (!row2.isEmpty()) {
						String str = row.select("td").get(0).text();
						if (str.length() == 0) {
							i++;
							continue;
						} else if (str.contains("(") && str.indexOf(")") == str.length() - 1) {
							String[] parts = str.split("\\(");
							String part1 = parts[0].trim();
							String part2 = parts[1].trim().replace(")", "");
							if (part1.length() != 0)
								thoiKy.setTen(part1);
							if (part2.length() != 0)
								thoiKy.setThoiGian(part2);
						} else {
							if (str.length() != 0)
								thoiKy.setTen(str);
						}

						String lanhDao = row.select("td").get(1).text();
						String triVi = row.select("td").get(2).text();
						if (lanhDao.length() != 0 && triVi.length() != 0)
							lanhDaoQuocGia.add(lanhDao + " (" + triVi + ")");
						else {
							String time = row.select("td").get(2).text();
							if (time.length() != 0)
								thoiKy.setThoiGian(time);
						}
						while (true) {
							i++;
							Elements row3 = rows.get(i)
									.select("tr:has(td:nth-of-type(3)):not(:has(td:nth-of-type(4)))");
							if (row3.isEmpty()) {
								break;
							} else {
								lanhDao = row3.select("td").get(0).text();
								triVi = row3.select("td").get(1).text();
								if (lanhDao.length() != 0 && triVi.length() != 0)
									lanhDaoQuocGia.add(lanhDao + " (" + triVi + ")");
							}
						}
						if (!lanhDaoQuocGia.isEmpty())
							thoiKy.setLanhDaoQuocGia(lanhDaoQuocGia);
					}
					thoiKy.setNguon(6);
					addThoiKy(thoiKy);
				}
			}
		};
		Document doc = scraper.connectToUrl(
				"http://vietycotruyen.com.vn/cac-trieu-dai-viet-nam-qua-tung-thoi-ky-lich-su?fbclid=IwAR1Ipoo1lU0ZVcOCgcWcS7eYWkbVQaRpcCLBoXH_BkXpouCIAr_63JH3ffs");

		if (doc !=null) {
		scraper.scrape(doc);
		scraper.getJsonString();
		scraper.saveToFile("file\\period-source-6.json");
		}

	}

}
