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
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line = reader.readLine();
			while (line != null) {
				docs.add(connectToUrl(line));
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return docs;
	}

	public HashMap<Document, String[]> connectToUrls(String filePath, boolean b) {
		BufferedReader reader;
		HashMap<Document, String[]> docs = new HashMap<>();
		String[] tmp;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line = reader.readLine();
			while (line != null) {
				tmp = line.split("\\|");
				if (tmp.length == 3)
					docs.put(connectToUrl(tmp[2]), tmp);
				else if (tmp.length == 2)
					docs.put(connectToUrl(tmp[1]), tmp);
				else 
					docs.put(connectToUrl(tmp[0]), tmp);
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
