package Model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class UserRegistration {
    private String username;
    private String password;
    private final Image picture;
    private int id;

    //Konstruktor
    public UserRegistration(String username, String password, Image picture, int id) throws SQLException {
        this.username = username;
        this.password = password;
        this.picture = new Image("player.png");
    } 

    // Speicherung der Daten
    public void saveToDatabase() {
        try {
            //DB 
            DBManager dbManager = new DBManager();
            dbManager.open();
            Connection connection = dbManager.getConnection();

            // Neuen User erstellen
            PreparedStatement statement = connection.prepareStatement("INSERT INTO user (id, username, password, picture) VALUES (?, ?, ?, ?)");
            statement.setInt(1, dbManager.getNewUserId());
            statement.setString(2, username);
            statement.setString(3, password);
            statement.setBytes(4, convertImageToByteArray(picture));

            // Execute und schließen der Verbindung
            statement.executeUpdate();
            statement.close();
            connection.close();
            //Nachträglich eingefügt
            dbManager.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    //Methode - Bild in Array
    public static byte[] convertImageToByteArray(Image image) throws IOException {
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "png", bos);
        return bos.toByteArray();
    }
    
    // Setter-Methoden
    public void setName(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter-Methoden
    public String getName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Image getDefaultImage() {
        return picture;
    }

    public int getId() {
        return id;
    }
}
