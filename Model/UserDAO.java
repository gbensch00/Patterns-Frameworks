package Model;

import java.sql.SQLException;

public interface UserDAO {
	 	User getUserByName(String name);
	 	User getUserByID(String dbid);
	    boolean createUser(User user) ;
	    void updateUser(User user);
}