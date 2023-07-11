package gui;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import gui.AppDemo2.PersonProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class Controller {
	int state = 1;
	@FXML
    private TableColumn<PersonProperty, String> col1;

    @FXML
    private TableColumn<PersonProperty, String> col2;

    @FXML
    private TextField searchText;

    @FXML
    private TableView<PersonProperty> tableView;

    @FXML
    void diTich(ActionEvent event) {
    	initialize("file\\site.json");
    	state = 4;
    }

    @FXML
    void leHoi(ActionEvent event) {
    	initialize("file\\festival.json");
    	state = 5;
    }

    @FXML
    void nhanVat(ActionEvent event) {
    	initialize("file\\figure.json");
    	state = 2;
    }

    @FXML
    void suKien(ActionEvent event) {
    	initialize("file\\event.json");
    	state = 3;
    }
	
	@FXML
	public void thoiKy(ActionEvent event) {
		initialize("file\\period.json");
		state = 1;
	}
	
	public void initialize(String inputFile) {    
        ObservableList<PersonProperty> data = FXCollections.observableArrayList();
    
        JsonArray jsonArray;
		try {
			jsonArray = new Gson().fromJson(new FileReader(inputFile), JsonArray.class);
			for (int i = 0; i < jsonArray.size(); i++) {
	        	JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
	        	String name = jsonObject.get("ten").getAsString();
	        	String desc = "";
	        	if(jsonObject.has("mieuTa")) {
	                JsonObject attr = jsonObject.get("mieuTa").getAsJsonObject();
	                String mieuTaValue = attr.toString();
	                for (String key : attr.keySet()) {
	                    int value = attr.get(key).getAsJsonArray().get(0).getAsInt();
	                    String formattedString = key + "(" + value + ")";
	                    desc += (formattedString + ". ");
	                }
	        	}
	        	data.add(new PersonProperty(name, desc));
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
  
        col1.setCellValueFactory(new PropertyValueFactory<>("property"));  
        col2.setCellValueFactory(new PropertyValueFactory<>("value"));
        tableView.setItems(data);
    } 
	
	@FXML
	public void search(ActionEvent event) {
	    String text = searchText.getText().trim().toLowerCase();
	    String input = "file\\period.json";
	    switch (state) {
	    case 1:
	    	input = "file\\period.json";
	    	break;
	    case 2:
	    	input = "file\\figure.json";
	    	break;
	    case 3:
	    	input = "file\\event.json";
	    	break;
	    case 4:
	    	input = "file\\site.json";
	    	break;
	    case 5:
	    	input = "file\\festival.json";
	    	break;
//	    default:
	    	
	    }
	    	
	    ObservableList<PersonProperty> data = FXCollections.observableArrayList();
	    JsonArray jsonArray;
		try {
			jsonArray = new Gson().fromJson(new FileReader(input), JsonArray.class);
			for (int i = 0; i < jsonArray.size(); i++) {
	        	JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
	        	String name = jsonObject.get("ten").getAsString();
	        	if (compareString(name, text)) {
	        		String desc = "";
		        	if(jsonObject.has("mieuTa")) {
		                JsonObject attr = jsonObject.get("mieuTa").getAsJsonObject();
		                String mieuTaValue = attr.toString();
		                for (String key : attr.keySet()) {
		                    int value = attr.get(key).getAsJsonArray().get(0).getAsInt();
		                    String formattedString = key + "(" + value + ")";
		                    desc += (formattedString + ". ");
		                }
		        	}
		        	data.add(new PersonProperty(name, desc));
		        }
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
	    if(text.isEmpty() || data.size()==0) {
	    	System.out.println("0 result");
	    	tableView.getItems().clear();
	        return;
	    }
	    else {
			col1.setCellValueFactory(new PropertyValueFactory<>("property"));  
	        col2.setCellValueFactory(new PropertyValueFactory<>("value"));
	        tableView.setItems(data);
	    }
	}
	
	public static class PersonProperty {
        private SimpleStringProperty property;
        private SimpleStringProperty value;

        public PersonProperty(String property, String value) {
            this.property = new SimpleStringProperty(property);
            this.value = new SimpleStringProperty(value);
        }

        public String getProperty() {
            return property.get();
        }

        public void setProperty(String property) {
            this.property.set(property);
        }

        public String getValue() {
            return value.get();
        }

        public void setValue(String value) {
            this.value.set(value);
        }
    }
    
	public static boolean compareString(String str1, String str2) {
        // Chuẩn hóa cả hai chuỗi về dạng không dấu
        String str1Normalized = Normalizer.normalize(str1, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        String str2Normalized = Normalizer.normalize(str2, Normalizer.Form.NFD).replaceAll("\\p{M}", "");

        // Chuyển đổi hai chuỗi thành chữ thường
        String str1Lower = str1Normalized.toLowerCase();
        String str2Lower = str2Normalized.toLowerCase();

        // Kiểm tra xem chuỗi thứ nhất có chứa chuỗi thứ hai hay không
        return str1Lower.contains(str2Lower);
    }
}
