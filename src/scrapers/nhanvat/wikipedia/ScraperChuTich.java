package scrapers.nhanvat.wikipedia;

import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.nhanvat.LanhDaoQuocGia;
import scrapers.nhanvat.FigureScraper;

public class ScraperChuTich {

	private static int k = 0;

	public static void main(String[] args) {

		FigureScraper scraper = new FigureScraper() {

			@Override
			public void scrape(Document doc) {
				FigureScraper supScraper = new FigureScraper() {

					@Override
					public void scrape(Document doc) {

						LanhDaoQuocGia nhanVat = new LanhDaoQuocGia();

						nhanVat.setUrl(doc.baseUri());
						Element ten = doc.selectFirst("p b");
						Element p;
						if (ten != null) {
							p = ten.parent();
							nhanVat.setTen(ten.text());
							Elements tens = p.select("b");
							ArrayList<String> tenKhac = new ArrayList<String>();
							for (int k = 1; k < tens.size(); k++) {
								tenKhac.add(tens.get(k).text());
							}
							if (!tenKhac.isEmpty())
								nhanVat.setTenKhac(tenKhac);

						} else {
							nhanVat.setTen((doc.title().split(" – "))[0]);
							p = doc.selectFirst("p");
							while (p.tagName() != "p" || p.text().isEmpty())
								p = p.nextElementSibling();

						}

						String mieuTa = p.text().replaceAll("\\[[^\\]]*\\]", "");
						nhanVat.setMieuTa(mieuTa);

						if (mieuTa.contains("(?)")) {

							mieuTa = mieuTa.replaceAll("\\(\\?\\)", "?");
						}

						StringBuilder stringBuilder = new StringBuilder();
						Element infobox = doc.selectFirst("table.infobox");
						Elements refs = infobox.select("tr th a");
						for (Element ref : refs) {

							String str = ref.text();
							if ((str.contains("Chủ tịch nước") || str.contains("Quyền Chủ tịch")
									|| str.contains("Chủ tịch Hội đồng Nhà nước")) && !ref.text().contains("Phó")) {
								while (!ref.tagName().equals("tr")) {
									ref = ref.parent();
								}
								ref = ref.nextElementSibling();
								String thoiGianTaiVi = ref.text();
								stringBuilder.append(thoiGianTaiVi).append(".");

							}
						}
						nhanVat.setThoiGianTaiVi(stringBuilder.toString().replaceAll("Vị trí Việt Nam.", "")
								.replaceAll("Nhiệm kỳ ", "").replaceAll("Tiền nhiệm.*?\\.", ""));

						Elements infs = infobox.select("tr th");
						for (Element inf : infs) {
							String str = inf.text();
							if (str.contains("Thông tin")) {
								inf = inf.parent();
								for (int j = 0; j < 3; j++) {

									inf = inf.nextElementSibling();
									if (inf.selectFirst("th").text().contains("Sinh")) {

										truncate(inf.selectFirst("td").text(), nhanVat);
									}
									if (inf.selectFirst("th").text().contains("Mất")) {

										nhanVat.setNamMat(inf.selectFirst("td").ownText().replaceAll(" ,", ""));

									}

								}
								break;
							}
						}

						if (k < 3) {
							nhanVat.setThoiKy("Nước Việt Nam từ 1945 – 1976");
						}
						nhanVat.setNguon(2);

						k++;
						addNhanVat(nhanVat);

					}

				};
				ArrayList<String> tmp = new ArrayList<>();

				Elements tables = doc.select(".wikitable");
				for (int i = 0; i < tables.size(); i++) {

					Elements rows = tables.get(i).select("tr");

					for (int j = 3; j < rows.size(); j++) {
						if (rows.get(j).selectFirst("td") == null)
							continue;

						Element data = rows.get(j).select("td").get(2);
						if (data.selectFirst("a") == null)
							data = rows.get(j).select("td").get(1);
						Element ref = data.selectFirst("a");
						String str = ref.attr("href");
						if (!tmp.contains(str)) {
							tmp.add(str);
							Document supDoc = supScraper.connectToUrl("https://vi.wikipedia.org" + str);
							supScraper.scrape(supDoc);

						}
					}

				}
				supScraper.getJsonString();
				supScraper.saveToFile("file\\figure-source-2.json", true);
			}

		};

		Document doc = scraper.connectToUrl(
				"https://vi.wikipedia.org/wiki/Danh_s%C3%A1ch_Ch%E1%BB%A7_t%E1%BB%8Bch_n%C6%B0%E1%BB%9Bc_Vi%E1%BB%87t_Nam");
		if (doc != null) {
			scraper.scrape(doc);
		}
	}

	public static void truncate(String str, LanhDaoQuocGia nhanVat) {
		int idx1 = -1;
		int idx2 = -1;
		boolean firstDigit = true;
		str = str.replaceAll("\\(.*?\\)", "").replaceAll("\\[[^\\]]*\\]", "");
		for (int i = 0; i < str.length(); i++) {
			if (firstDigit && '0' <= str.charAt(i) && str.charAt(i) <= '9') {
				firstDigit = false;
				idx1 = i;
			}
			if ('0' <= str.charAt(i) && str.charAt(i) <= '9') {
				idx2 = i;
			}
		}
		nhanVat.setNamSinh(str.substring(idx1, idx2 + 1));
		nhanVat.setQueQuan(str.substring(idx2 + 2));

	}
}