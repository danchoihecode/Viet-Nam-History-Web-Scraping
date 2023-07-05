package scrapers.nhanvat.wikipedia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import nhanvat.NhanVat;

public class ScraperNhanVat {
	public static void main(String[] args) {
		Document doc;
		BufferedReader reader;
		String[] tk;
//		int i = 0;
		ArrayList<NhanVat> nhanVats = new ArrayList<NhanVat>();
		try {
			reader = new BufferedReader(new FileReader(
					"C:\\Users\\pc\\Documents\\OOPBigProject\\LichSuVietNam\\file\\figure-source-2.txt"));
			String line = reader.readLine();
			while (line != null) {
//				if (i < 3) {
//					i++;
//					line = reader.readLine();
//					continue;
//
//				}
//				System.out.println(i);
				tk = line.split("\\|");
				try {
					if (tk.length == 2)
						doc = Jsoup.connect(tk[1]).get();
					else
						doc = Jsoup.connect(tk[0]).get();
				} catch (IOException e) {
					System.out.println("Không thể kết nối tới trang web ");
					break;
				}

				NhanVat nhanVat = new NhanVat();

				if (tk.length == 2)
					nhanVat.setUrl(tk[1]);
				else
					nhanVat.setUrl(tk[0]);

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
					while (p.tagName() != "p" || p.text().isBlank())
						p = p.nextElementSibling();

				}

				String mieuTa = p.text().replaceAll("\\[[^\\]]*\\]", "");
				nhanVat.setMieuTa(mieuTa);

				if (mieuTa.contains("(?)")) {

					mieuTa = mieuTa.replaceAll("\\(\\?\\)", "?");
				}
				int idx = mieuTa.indexOf('(');
				if (idx != -1) {
					int endidx = mieuTa.indexOf(')');
					if (endidx == -1)
						endidx = mieuTa.length();
					String str = mieuTa.substring(idx + 1, endidx);

					if (str.indexOf('-') == -1 && str.indexOf('–') == -1) {
						String tmp = mieuTa.replaceAll("\\(c.*?\\) ", "");
						int idx1 = tmp.indexOf('(');
						if (idx1 != -1) {
							int idx2 = tmp.indexOf(')');
							str = tmp.substring(idx1 + 1, idx2);
						}

					}
					if (str.indexOf('-') != -1 || str.indexOf('–') != -1) {

						boolean bool = setTimeByHyphen(str, nhanVat);
						if (bool == false) {
							Elements a = doc.selectFirst("p").select("sup ~ a");
							if (a.size() >= 3) {
								nhanVat.setNamSinh(a.get(1).text());
								nhanVat.setNamMat(a.get(2).text());
							}

						}

					} else if (str.contains("sinh") && !str.contains("mất")) {
						String s = str.substring(str.length() - 4, str.length());

						if (s.matches("^\\d+$"))
							nhanVat.setNamSinh(s);
					}

				}

				if (tk.length == 2)
					nhanVat.setThoiKy(tk[0]);
				nhanVat.setNguon(2);

//				i++;
				nhanVats.add(nhanVat);
				line = reader.readLine();

//				if (i == 4)
//					break;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(nhanVats);

		String outputFile = "C:\\Users\\pc\\Documents\\OOPBigProject\\LichSuVietNam\\file\\figure-source-2.json";
		try (FileWriter writer = new FileWriter(outputFile)) {
			writer.write(json);
			System.out.println("Dữ liệu đã được ghi vào file " + outputFile);
		} catch (IOException e) {
			System.out.println("Ghi dữ liệu vào file thất bại.");
		}

	}

	public static boolean setTimeByHyphen(String str, NhanVat nhanVat) {
		int idx1 = str.indexOf('-');
		int a, b, c, d;
		if (idx1 != -1) {
			a = b = idx1 - 1;
			c = d = idx1 + 1;
		} else {
			int idx2 = str.indexOf('–');
			a = b = idx2 - 1;
			c = d = idx2 + 1;
		}

		while (a != -1 && !(Character.isDigit(str.charAt(a)) || str.charAt(a) == '?')) {
			a--;
			b--;
		}
		while (d != str.length() && !(Character.isDigit(str.charAt(d)) || str.charAt(d) == '?')) {
			c++;
			d++;
		}

		if (a == -1 && d == str.length())
			return false;

		while (a != -1 && (Character.isDigit(str.charAt(a)) || str.charAt(a) == '?')) {
			a--;
		}
		while (d != str.length() && (Character.isDigit(str.charAt(d)) || str.charAt(d) == '?')) {
			d++;
		}

		String namSinh = str.substring(a + 1, b + 1);
		String namMat = str.substring(c, d);

		nhanVat.setNamSinh(namSinh);
		if (namMat.length() == 0)
			return true;
		if (namMat.length() > 2 || namMat.charAt(0) == '?') {
			nhanVat.setNamMat(namMat);
			return true;
		} else {
			nhanVat.setNamMat(str.substring(c, str.length()));
			return true;
		}

	}

}
