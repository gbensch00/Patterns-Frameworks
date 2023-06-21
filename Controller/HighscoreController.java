package Controller;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Model.DatabaseConnection;

/**
 * Der HighscoreController ist verantwortlich für das Abrufen und Aktualisieren von Highscore-Daten aus der Datenbank.
 */
public class HighscoreController {
    
    private DatabaseConnection dbConnection;
    
    /**
     * Konstruktor für den HighscoreController.
     * Initialisiert die Verbindung zur Datenbank.
     */
    public HighscoreController() {
    	 try {
	            dbConnection = new DatabaseConnection("jdbc:mysql://localhost:3307/TestDB", "root", "");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
    
        /**
     * Gibt den Highscore für den angegebenen Spieler zurück.
     *
     * @param playerName Der Name des Spielers.
     * @return Der Highscore des Spielers.
     * @throws SQLException Wenn ein Fehler bei der Datenbankabfrage auftritt.
     */
    public int getHighscore(String playerName) throws SQLException {
        int highscore = 0;
        String query = "SELECT highscore FROM PLAYER WHERE name = ?";
        try (PreparedStatement statement = dbConnection.getConnection().prepareStatement(query)) {
            statement.setString(1, playerName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                highscore = resultSet.getInt("highscore");
            }
        }
        return highscore;
    }

    /**
     * Aktualisiert den Highscore für den angegebenen Spieler, wenn der neue Highscore höher ist als der aktuelle.
     *
     * @param playerName   Der Name des Spielers.
     * @param newHighscore Der neue Highscore.
     * @throws SQLException Wenn ein Fehler bei der Datenbankabfrage auftritt.
     */
    public void updateHighscore(String playerName, int newHighscore) throws SQLException {
      int oldHighscore =  getHighscore(playerName);
      if (oldHighscore <= newHighscore) {
    	String query = "UPDATE PLAYER SET highscore = ? WHERE name = ?";
        try (PreparedStatement statement = dbConnection.getConnection().prepareStatement(query)) {
            statement.setInt(1, newHighscore);
            statement.setString(2, playerName);
            statement.executeUpdate();}
        return;
        }
    }
    
    /**
     * Gibt eine ResultSet-Objekt zurück, das die Namen und Highscores der besten 10 Spieler enthält.
     *
     * @return Das ResultSet-Objekt mit den Highscore-Daten.
     * @throws SQLException Wenn ein Fehler bei der Datenbankabfrage auftritt.
     */
    public ResultSet getAllHighscores() throws SQLException {
        String query = "SELECT name, highscore FROM PLAYER WHERE name != 'defaultAvatar' ORDER BY highscore DESC LIMIT 10";
        PreparedStatement statement = dbConnection.getConnection().prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

}