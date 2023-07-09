package scrapers.lehoi;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import entities.lehoi.LeHoi;
import scrapers.Scraper;

public abstract class FestivalScraper extends Scraper {
	private ArrayList<LeHoi> leHois = new ArrayList<>();

	public void addLeHoi(LeHoi leHoi) {
		leHois.add(leHoi);
	}

	public void getJsonString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(leHois);
		setJson(json);
	}

}
