package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PainterController {

    @FXML
    private Pane drawingAreaPane;
    
    @FXML
    private RadioButton penRadioButton;

    @FXML
    private RadioButton eraserRadioButton;

    @FXML
    void initialize() {
        ToggleGroup toolsToggleGroup = new ToggleGroup();
        this.penRadioButton.setToggleGroup(toolsToggleGroup);
        this.eraserRadioButton.setToggleGroup(toolsToggleGroup);
    }
    
    @FXML
    void clearButtonPressed(ActionEvent event) {
    	drawingAreaPane.getChildren().clear();
    }

    @FXML
    void drawingAreaMouseDragged(MouseEvent event) {
    	Circle newCircle = new Circle(event.getX(), event.getY(), 4);
        if (eraserRadioButton.isSelected()) {
            // Set the fill color to white to simulate erasing
            newCircle.setFill(Color.WHITE);
        } else {
            // Set the fill color to black for drawing
            newCircle.setFill(Color.BLACK);
        }
        drawingAreaPane.getChildren().add(newCircle);
    }

}