package scrapers.nhanvat;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import entities.nhanvat.NhanVat;
import scrapers.Scraper;

public abstract class FigureScraper extends Scraper {
	private ArrayList<NhanVat> nhanVats = new ArrayList<>();

	public void addNhanVat(NhanVat nhanVat) {
		nhanVats.add(nhanVat);
	}

	public void getJsonString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(nhanVats);
		setJson(json);
	}

}
