package gui;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AppDemo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        TableView<PersonProperty> tableView = new TableView<>();

        TableColumn<PersonProperty, String> propertyColumn = new TableColumn<>("Property");
        TableColumn<PersonProperty, String> valueColumn = new TableColumn<>("Value");

        propertyColumn.setCellValueFactory(new PropertyValueFactory<>("property"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        tableView.getColumns().addAll(propertyColumn, valueColumn);

        ObservableList<PersonProperty> data = FXCollections.observableArrayList(
                new PersonProperty("Name", "John Doe"),
                new PersonProperty("Age", "25"),
                new PersonProperty("Email", "johndoe@example.com")
        );

        tableView.setItems(data);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Scene scene = new Scene(tableView, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
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
}
