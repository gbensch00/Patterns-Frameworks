package Controller;

public interface UserDAO {
	 	User getUserByName(String name);
	 	User getUserByID(String dbid);
	    void createUser(User user);
	    void updateUser(User user);
}