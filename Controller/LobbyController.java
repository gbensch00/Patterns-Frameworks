package Controller;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.scene.image.*;

public class LobbyController {
	
	String url = "jdbc:mysql://localhost:3306/TestDB";
	String user = "root";
	String password = "";
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	String loggedInUserName;
	String dbID;
	
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
    @FXML
    private Label UserName;		// Begrüßungslabel
    @FXML 
    private ImageView Avatar;	//Avatar für den User
    

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
    	        	((Label) node).setFont(Font.font(randomFontName, FontWeight.NORMAL, 14));
    	        }
    	    }
    }
    @FXML
    public void setLoggedInUserName(String userName) {
        
    	//Begrüßungstext für den Nutzer der sich einloggt
    	UserName.setText("Hallo " + userName + "!");
       
        //Baut Datenbankverbindung auf
        try {
            con = DriverManager.getConnection(this.url, this.user, this.password);
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM PLAYER WHERE name = '" + userName +"'" );
           if (rs.next()) {
             dbID = rs.getString("id");
           
           }
           //Hier werden die Parameter aus der Tabelle UserSettings in der mySQL DB geladen
           // FontType, FontSize und der Avatar
            rs = stmt.executeQuery("SELECT * FROM UserSettings WHERE UserID = '" + dbID + "'");
            if (rs.next()) {
                String fontType = rs.getString("FontType");
                int fontSize = rs.getInt("FontSize");
                //Bei neuen Einträgen in die DB ist der Avatar noch NULL daher wird hier eine
                //Überprüfung gemacht ob der Wert NULL ist wenn ja lade ein Standardbild
                if (rs.getBlob("Avatar") == null) {
                	 InputStream inputStream = getClass().getResourceAsStream("/Bilder/UserAvatar/PlayerIcon1.png");
                	 Image image = new Image(inputStream);
                	 Avatar.setImage(image);
                }
                else {
                Blob avatar = rs.getBlob("Avatar");
                InputStream inputStream = avatar.getBinaryStream();
            	Image image = new Image(inputStream);
                
            	// Weist das Bild der ImageView zu
               
                Avatar.setImage(image);
                }
                          
                SinglePlayerButton.setFont(Font.font(fontType, FontWeight.NORMAL, fontSize));
                System.out.println("Habe gesetzt "+ fontType + fontSize);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (con != null)
                    con.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
    }
    }


