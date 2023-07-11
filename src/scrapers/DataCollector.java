package scrapers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class DataCollector {

	public Document connectToUrl(String url) {
		Document doc;
		try {

			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			System.out.println("Không thể kết nối tới trang web " + url);
			return null;
		}
		return doc;
	}

	public ArrayList<Document> connectToUrls(String filePath) {
		BufferedReader reader;
		ArrayList<Document> docs = new ArrayList<>();
		Document doc;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line = reader.readLine();
			while (line != null) {
				doc = connectToUrl(line);
				if (doc == null) {
					line = reader.readLine();
					continue;
				} else
					docs.add(doc);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return docs;
	}

	public HashMap<Document, String[]> connectToUrls(String filePath, boolean bool) {
		if (bool = false)
			return null;
		BufferedReader reader;
		HashMap<Document, String[]> docs = new HashMap<>();
		String[] tmp;
		Document doc;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line = reader.readLine();
			while (line != null) {
				tmp = line.split("\\|");
				if (tmp.length == 3)
					doc = connectToUrl(tmp[2]);
				else if (tmp.length == 2)
					doc = connectToUrl(tmp[1]);
				else
					doc = connectToUrl(tmp[0]);
				if (doc == null) {
					line = reader.readLine();
					continue;
				} else
					docs.put(doc, tmp);

				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return docs;
	}

	public abstract void saveToFile(String filePath);

}
