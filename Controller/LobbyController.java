package Controller;

import java.util.Arrays;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LobbyController {
	
    @FXML
    private ColorPicker cP; // Farbauswahl-Element aus der FXML-Datei

    @FXML
    private AnchorPane anchorPane; // Das AnchorPane-Element aus der FXML-Datei
    @FXML
    private Button SinglePlayerButton;
    @FXML
    private Button MultiplayerButton;
    @FXML
    private Button SettingsButton;
    @FXML
    private Button CloseButton;
    @FXML
    private Toggle ToggleButton;
    

    // Methode, die aufgerufen wird, wenn eine neue Farbe ausgewählt wird
    @FXML
    private void colorChange(ActionEvent event) {
        Color color = cP.getValue(); // Die ausgewählte Farbe

        // Setzen der Hintergrundfarbe der Szene auf die ausgewählte Farbe
        anchorPane.setStyle("-fx-background-color: " + toRgbString(color) + ";");
    }

    // Hilfsmethode, um die Farbe in ein RGB-Format umzuwandeln
    private String toRgbString(Color color) {
        return "rgb(" + ((int) (color.getRed() * 255)) + ", " + ((int) (color.getGreen() * 255)) + ", " + ((int) (color.getBlue() * 255)) + ")";
    }
    
    @FXML
    private void FontChange(ActionEvent event) {
        
    	 List<String> fontNames = Arrays.asList("Arial", "Helvetica", "Times New Roman", "Courier New", "Verdana", "Georgia");
    	 String randomFontName = fontNames.get((int) (Math.random() * fontNames.size()));
    	 
    	 for (Node node : anchorPane.getChildren()) {
    	        if (node instanceof Button)  {
    	            ((Button) node).setFont(Font.font(randomFontName, FontWeight.NORMAL, 14));
    	            
    	            
    	             }
    	        if (node instanceof Label) {
    	        	((Label) node).setFont(Font.font(randomFontName, FontWeight.NORMAL, 46));
    	        }
    	    }
    }

}
