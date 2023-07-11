package scrapers.thoiky.leloi;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.thoiky.ThoiKy;
import scrapers.thoiky.PeriodScraper;

public class ScraperLeLoi {
	public static List<String> extractNumbers(String input) {
		List<String> numbers = new ArrayList<>();
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			numbers.add(matcher.group());
		}
		return numbers;
	}

	public static void main(String args[]) {

		PeriodScraper scraper = new PeriodScraper() {

			@Override
			public void scrape(Document doc) {
				Elements cot1 = doc.select("tr td[width=\"223\"]");
				Elements cot3 = doc.select("tr td[width=\"120\"]");
				Elements cot4 = doc.select("tr td[width=\"92\"]");

				Elements cot1exception = doc.select("tr td[width=\"199\"]");
				Elements boxcot1exception = cot1exception.select("p b span span");
				Elements rowcot3exception = cot3.select("p span span");

				System.out.println(boxcot1exception.get(0).ownText());
				Elements rowcot1exception = cot1exception.select("p span span");
				ArrayList<String> leadersException = new ArrayList<>();
				String numberSequence = "";
				for (int i = 1; i < rowcot1exception.size(); i++) {
					leadersException.add(rowcot1exception.get(i).ownText());
					numberSequence += " " + rowcot3exception.get(i + 137).ownText();
				}
				System.out.println(numberSequence);
				List<String> numbers = extractNumbers(numberSequence);
				System.out.println(numbers);

				ThoiKy thoiKyException = new ThoiKy();
				thoiKyException.setThoiGian(numbers.get(0) + "-" + numbers.get(numbers.size() - 1));
				thoiKyException.setTen(boxcot1exception.get(0).ownText());

				for (int m = 0; m < leadersException.size(); m++) {
					leadersException.set(m,
							leadersException.get(m).substring(leadersException.get(m).indexOf(".") + 2));
					leadersException.set(m, leadersException.get(m).trim());
					if (leadersException.get(m).substring(leadersException.get(m).indexOf(".") + 1).equals("")) {
						leadersException.remove(m);
					}
				}
				thoiKyException.setLanhDaoQuocGia(leadersException);
				thoiKyException.setNguon(7);
				addThoiKy(thoiKyException);

				for (int i = 0; i < cot1.size(); i++) {
					ThoiKy thoiky = new ThoiKy();
					Elements boxcot1 = cot1.get(i).select("p span span");
					Element trieuDai = boxcot1.get(0);
					System.out.println(i + ". " + trieuDai.ownText());
					thoiky.setTen(trieuDai.ownText());
					System.out.println(boxcot1.size());
					ArrayList<String> leaders = new ArrayList<>();

					if (i == 3 || i == 4) {
						Elements boxcot3 = cot3.get(i).select("p span span");
						String numberSequence1 = "";
						for (int k = 0; k < boxcot3.size(); k++) {
							numberSequence1 += " " + boxcot3.get(k).ownText();
						}
						System.out.println(numberSequence1);
						List<String> numbers1 = extractNumbers(numberSequence1);
						System.out.println(numbers1);
						if (i < 4)
							thoiky.setThoiGian(numbers1.get(1) + "-" + numbers1.get(numbers1.size() - 1) + " TCN");
						else if (i == 4)
							thoiky.setThoiGian(numbers1.get(1) + "TCN -" + numbers1.get(numbers1.size() - 1));
						else
							thoiky.setThoiGian(numbers1.get(1) + "-" + numbers1.get(numbers1.size() - 1));

						thoiky.setNguon(7);
						addThoiKy(thoiky);
						continue;
					} else if (i == 0 || i == 8 || i == 15 || i == 19 || i == 21 || i == 24) {
						continue;
					}

					if (i != 3 && i != 4 && i != 6 && i != 9 && i != 16 && i != 19 && i != 20 && i != 25 && i != 26) {
						for (int k = 1; k < boxcot1.size(); k++) {
							leaders.add(boxcot1.get(k).ownText());
						}
					} else if (i == 6) {
						leaders.add(boxcot1.get(3).ownText());
					} else if (i == 9) {
						leaders.add(boxcot1.get(3).ownText());
						leaders.add(boxcot1.get(5).ownText());
					} else if (i == 16) {
						for (int k = 1; k < boxcot1.size(); k++) {
							if (k != 2 && k != 5 && k != 6 && k != 11) {
								leaders.add(boxcot1.get(k).ownText());
							}
						}
					} else if (i == 20) {
						for (int k = 1; k < boxcot1.size(); k++) {
							if (k != 4) {
								leaders.add(boxcot1.get(k).ownText());
							}
						}
					} else if (i == 25 || i == 26) {
						for (int k = 1; k < cot1.get(i).select("p span span").size(); k++) {
							leaders.add(cot1.get(i).select("p span span").get(k).ownText());
						}

						Elements boxcot3 = cot3.get(i + 2).select("p span span");
						String numberSequence1 = "";
						for (int j = 0; j < boxcot3.size(); j++) {
							numberSequence1 += " " + boxcot3.get(j).ownText();
						}
						System.out.println(numberSequence1);
						List<String> numbers1 = extractNumbers(numberSequence1);
						System.out.println(numbers1);
						thoiky.setThoiGian(numbers1.get(1) + "-" + numbers1.get(numbers1.size() - 1));

						Elements boxcot4 = cot4.get(i).select("p b span span");
						ArrayList<String> tenNuoc = new ArrayList<>();

						for (int k = 0; k < boxcot4.size(); k++) {
							tenNuoc.add(boxcot4.get(k).ownText());
						}
						thoiky.setTenNuoc(tenNuoc);
					}

					for (int n = 0; n < leaders.size(); n++) {
						leaders.set(n, leaders.get(n).substring(leaders.get(n).indexOf(".") + 2));
						leaders.set(n, leaders.get(n).trim());
						if (leaders.get(n).substring(leaders.get(n).indexOf(".") + 1).equals("")) {
							leaders.remove(n);
						}
					}
					if (i == 11) {
						leaders.removeIf(leader -> leader.equals("12 sứ quân"));
					}
					thoiky.setLanhDaoQuocGia(leaders);

					if (i != 25 && i != 26) {
						Elements boxcot3 = cot3.get(i).select("p span span");
						String numberSequence1 = "";
						for (int k = 0; k < boxcot3.size(); k++) {
							numberSequence1 += " " + boxcot3.get(k).ownText();
						}
						System.out.println(numberSequence1);
						List<String> numbers1 = extractNumbers(numberSequence1);
						System.out.println(numbers1);

						if (i < 4) {
							thoiky.setThoiGian(numbers1.get(1) + "-" + numbers1.get(numbers1.size() - 1) + " TCN");
						} else if (i == 4) {
							thoiky.setThoiGian(numbers1.get(1) + "TCN -" + numbers1.get(numbers1.size() - 1));
						} else {
							thoiky.setThoiGian(numbers1.get(1) + "-" + numbers1.get(numbers1.size() - 1));
						}

						Elements boxcot4 = cot4.get(i).select("p b span span");
						ArrayList<String> tenNuoc = new ArrayList<>();

						for (int k = 0; k < boxcot4.size(); k++) {
							if (!boxcot4.get(k).ownText().replaceAll("Giao Châu", "").equals("")) {
								tenNuoc.add(boxcot4.get(k).ownText().replaceAll("Giao Châu", ""));
							}
						}
						thoiky.setTenNuoc(tenNuoc);
					}

					thoiky.setNguon(7);
					addThoiKy(thoiky);
				}

			}
		};
		Document doc = scraper.connectToUrl(
				"http://leloi.phuyen.edu.vn/sinh-hoat-chuyen-mon/to-su-gdcd/lich-su-viet-nam-qua-cac-trieu-dai-2879-tcn-1976-.html");
		if (doc != null) {
			scraper.scrape(doc);
			scraper.getJsonString();
			scraper.saveToFile("file\\period-source-7.json");
		}

	}
}