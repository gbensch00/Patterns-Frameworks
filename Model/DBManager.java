package Model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {

    private static final String DB_PATH = "/Applications/XAMPP/xamppfiles/htdocs/pattern-frameworks/user.db";
    private static final String TABLE_NAME = "user";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_IMAGE = "picture"; 
    private Connection conn;  
    
    //öffnen
    public void open() throws ClassNotFoundException {
        try {
            //JDBC Treiber für SQLite laden + Verbindung zur SQLite-Datenbank
            //Class.forName()-Methode lädt die Treiberklasse
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + DB_PATH;

            conn = DriverManager.getConnection(url);
            createTableIfNotExists();
        } catch (SQLException e) {
            System.out.println("Error opening database: " + e.getMessage());
        }
    }

    //Methode um jeden User eine neue ID zu vergeben
    public int getNewUserId() throws SQLException {
        int newId = 0;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MAX(" + COLUMN_ID + ") FROM " + TABLE_NAME);
    
            if (resultSet.next()) {
                newId = resultSet.getInt(1) + 1;
            } else {
                newId = 1;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw e;
        }
        return newId;
    }
    
    //schließen
    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error closing database: " + e.getMessage());
        }
    }
    
    //Methode falls nichts existiert
    private void createTableIfNotExists() throws SQLException {
        Statement stmt = conn.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (\n"
                + COLUMN_ID + " INTEGER PRIMARY KEY,\n"
                + COLUMN_NAME + " TEXT NOT NULL,\n"
                + COLUMN_PASSWORD + " TEXT NOT NULL,\n"
                + COLUMN_IMAGE + " BLOB\n"
                + ");";
        stmt.execute(sql);
    }
    
    public Connection getConnection() {
        return conn;
    }
}
 
