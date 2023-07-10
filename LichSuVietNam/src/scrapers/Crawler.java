package scrapers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Document;

public abstract class Crawler extends DataCollector {

	private Set<String> set = new HashSet<String>();

	public void addUrl(String str) {
		set.add(str);
	}

	public boolean containUrl(String str) {
		for (String s : set) {
			if (s.contains(str))
				return true;
		}
		return false;
	}

	@Override
	public void saveToFile(String filePath) {

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			for (String element : set) {
				writer.write(element);
				writer.newLine();
			}
			System.out.println("Ghi thành công vào file " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public abstract void crawl(Document doc);

}
