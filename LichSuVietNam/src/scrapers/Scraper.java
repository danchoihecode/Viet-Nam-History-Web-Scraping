package scrapers;

import org.jsoup.nodes.Document;

import utilities.JsonFileWriter;

public abstract class Scraper extends DataCollector {

	private String json;

	public void setJson(String json) {
		this.json = json;
	}

	@Override
	public void saveToFile(String filePath) {
		JsonFileWriter.writeJsonToFile(json, filePath, false);
	}

	public void saveToFile(String filePath, boolean append) {

		JsonFileWriter.writeJsonToFile(json, filePath, append);
	}

	public abstract void scrape(Document doc);

}
