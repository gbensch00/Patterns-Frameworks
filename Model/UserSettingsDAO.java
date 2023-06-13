package Model;

import java.sql.SQLException;

public interface UserSettingsDAO {
	  	void createUserSettings(UserSettings userSettings) throws SQLException;
	    void updateUserSettings(UserSettings userSettings) throws SQLException;
		UserSettings getUserSettingsByUserId(String userId) throws SQLException;
}

