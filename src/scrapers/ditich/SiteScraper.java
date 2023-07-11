package scrapers.ditich;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import entities.ditich.DiTich;
import scrapers.Scraper;

public abstract class SiteScraper extends Scraper {
	private ArrayList<DiTich> diTichs = new ArrayList<>();

	public void addDiTich(DiTich diTich) {
		diTichs.add(diTich);
	}

	public void getJsonString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(diTichs);
		setJson(json);
	}

}
