package scrapers.nhanvat.cantho;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import nhanvat.LanhDaoQuocGia;

public class ScraperCanTho {

	public static void main(String args[]) {

		Document doc;
		try {
			doc = Jsoup.connect(
					"https://se.ctu.edu.vn/hoat-dong-su/tu-lieu-lich-su/677-the-thu-cac-trieu-vua-viet-nam.html?fbclid=IwAR32dSmbtzxk6zqNDPSmek8TBgtbO16rO_NR2mGPm-k7sk4SuUsbwrcB2_E")
					.get();
		} catch (IOException e) {
			System.out.println("Không thể kết nối tới trang web.");
			return;
		}

		Elements headings = doc.select("strong");

		ArrayList<LanhDaoQuocGia> emperors = new ArrayList<>();

		for (int i = 38; i < headings.size(); i++) {
			Element heading = headings.get(i);
			LanhDaoQuocGia emperor = new LanhDaoQuocGia();
			if (i == 45 || i == 49 || i == 50 || i == 52 || i == 53 || i == 58 || i == 61 || i == 65
					|| (i >= 66 && i <= 70) || (i >= 80 && i <= 85) || i == 99 || (i >= 102 && i <= 104)
					|| (i >= 106 && i <= 114) || (i >= 141 && i <= 143) || i == 149 || (i >= 155 && i <= 157)
					|| i == 172 || (i >= 182 && i <= 184) || i == 189 || i == 190) {
				continue;
			}

			if (i == 40) {

				Elements miniHeadings = heading.nextElementSiblings();
				miniHeadings = doc.select("em");

				for (int j = 12; j < 15; j++) {
					Element miniHeading = miniHeadings.get(j);

					String miniOutput = miniHeading.ownText();
					miniOutput = miniOutput.substring(4);
					System.out.println(i + "." + j + ": " + miniOutput);

					emperor = new LanhDaoQuocGia();

					int exist = miniOutput.indexOf('(') == -1 ? 0 : miniOutput.indexOf('(');
					emperor.setTen(miniOutput.substring(0, exist));

					int existBracket = miniOutput.indexOf('(') == -1 ? 0 : miniOutput.indexOf('(') - 1;
					emperor.setThoiGianTaiVi(miniOutput.substring(existBracket + 2, miniOutput.indexOf(')')));

					if (j == 13) {
						miniHeading = miniHeading.parent();
						for (int k = 1; k <= 4; k++)
							miniHeading = miniHeading.nextElementSibling();
						emperor.setTheThu(miniHeading.ownText());

					} else
						emperor.setTheThu(null);
					emperor.setNguon(5);
					emperors.add(emperor);
				}
				continue;
			}

			String input = heading.text();
			String output = input.replaceAll("Chính quyền ", "");
			output = output.replaceAll("CHÍNH QUYỀN ", "");
			output = output.replaceAll("của ", "");
			output = output.replaceAll("họ ", "");

			if (i == 48) {
				output = output.substring(2);
			}

			while (output.charAt(0) != '–' && i != 48) {
				output = output.substring(1);
			}
			;
			if (i != 48)
				output = output.substring(2);
			if (i == 51) {
				Element strongElement = heading.selectFirst("strong");

				Element parentElement = strongElement.parent();
				output = parentElement.ownText();

				int exist = output.indexOf('(') == -1 ? 0 : output.indexOf('(') - 1;
				emperor.setTen(output.substring(0, exist));

				emperor.setTheThu(parentElement.ownText());
				emperor.setNguon(5);
				emperors.add(emperor);
				continue;
			}

			int existBracket = output.indexOf('(') == -1 ? 0 : output.indexOf('(') - 1;

			System.out.println(i + ": " + output);

			if (existBracket != 0) {

				emperor.setTen(output.substring(0, existBracket));
				emperor.setThoiGianTaiVi(output.substring(existBracket + 2, output.indexOf(')')));
				Element parentElement = heading.parent();

				int repeat;
				if (i == 38)
					repeat = 4;
				else if (i == 41 || i == 46 || i == 144 || i == 150 || i == 152 || i == 153 || i == 158)
					repeat = 0;
				else if (i == 42 || i == 71)
					repeat = 4;
				else if (i == 47 || i == 48 || i == 64 || i == 93 || i == 119 || i == 128 || i == 151 || i == 154
						|| (i >= 159 && i <= 181) || i == 188)
					repeat = 1;
				else if (i == 55 || i == 56 || i == 63 || i == 72 || i == 75 || i == 77 || i == 78 || i == 79
						|| (i >= 87 && i <= 101) || (i >= 115 & i <= 117) || i == 120 || i == 121
						|| (i >= 123 && i <= 127) || (i >= 129 && i <= 140) || (i >= 145 && i <= 148)
						|| (i >= 191 && i <= 202))
					repeat = 2;
				else
					repeat = 3;
				for (int j = 1; j <= repeat; j++) {

					parentElement = parentElement.nextElementSibling();

				}

				output = parentElement.ownText();
				if (output != "")
					emperor.setTheThu(output);

				emperor.setNguon(5);
				emperors.add(emperor);
				continue;
			}

		}
		String outputFile = "file\\figure-source-5.json";

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(emperors);

		try (FileWriter writer = new FileWriter(outputFile)) {
			writer.write(json);
			System.out.println("Dữ liệu đã được ghi vào file " + outputFile);
		} catch (IOException e) {
			System.out.println("Ghi dữ liệu vào file thất bại.");
		}
	}

}