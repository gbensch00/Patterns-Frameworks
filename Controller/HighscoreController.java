package Controller;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.DatabaseConnection;

public class HighscoreController {
	
    
    private DatabaseConnection dbConnection;
    
    public HighscoreController() {
    	 try {
	            dbConnection = new DatabaseConnection("jdbc:mysql://localhost:3307/TestDB", "root", "");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
    

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
    
    public ResultSet getAllHighscores() throws SQLException {
        String query = "SELECT name, highscore FROM PLAYER WHERE name != 'defaultAvatar' ORDER BY highscore DESC LIMIT 10";
        PreparedStatement statement = dbConnection.getConnection().prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

}