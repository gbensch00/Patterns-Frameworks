package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserSettingsDAOImpl implements UserSettingsDAO {
	
	
    private Connection connection;

    public UserSettingsDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public UserSettings getUserSettingsByUserId(String userId) throws SQLException {
    	
    	
    	UserSettings userSettings = null;
    	
        String query = "SELECT * FROM UserSettings WHERE UserID = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userId);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                int id = rs.getInt("ID");
                int userid = rs.getInt("UserID");
                String fonttype = rs.getString("FontType");
                String fontsize = rs.getString("FontSize");
                String backgroundcolor = rs.getString("Backgroundcolor");
                byte[] avatar = rs.getBytes("Avatar");
                String resolution = rs.getString("Resolution");
                		
                userSettings = new UserSettings(id, userid, fontsize, fonttype, backgroundcolor,resolution,avatar);
            }
            
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
            
            
                return userSettings;
            }
       

    @Override
    public void updateUserSettings(UserSettings userSettings) throws SQLException {
        String query = "UPDATE UserSettings SET FontType = ?, FontSize = ?, Backgroundcolor = ?, Avatar = ?, Resolution = ? WHERE UserID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userSettings.getFontType());
            statement.setString(2, userSettings.getFontSize());
            statement.setString(3, userSettings.getBackgroundColor());
            statement.setBytes(4, userSettings.getSavedAvatar());
            statement.setString(5, userSettings.getResolution());
            statement.setInt(6, userSettings.getUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error updating user settings", e);
        }
    }

    
    public void close() throws SQLException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new SQLException("Error closing connection", e);
        }
    }

	@Override
	public void createUserSettings(UserSettings userSettings) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	
}
