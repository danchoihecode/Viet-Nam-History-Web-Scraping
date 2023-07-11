package scrapers.thoiky;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import entities.thoiky.ThoiKy;
import scrapers.Scraper;

public abstract class PeriodScraper extends Scraper {
	private ArrayList<ThoiKy> thoiKys = new ArrayList<>();

	public void addThoiKy(ThoiKy thoiKy) {
		thoiKys.add(thoiKy);
	}

	public void getJsonString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(thoiKys);
		setJson(json);
	}
}
