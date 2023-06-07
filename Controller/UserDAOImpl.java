package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {
	
	private Connection con;

    public UserDAOImpl(Connection con) {
        this.con = con;
    }
        
        @Override
	public User getUserByName(String name) {
        	
        	User user = null;
            String query = "SELECT * FROM PLAYER WHERE name = ?";
            
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, name);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String username = rs.getString("name");
                    String password = rs.getString("password");
                    int highscore = rs.getInt("highscore");
                    
                    user = new User(id, username, password, highscore);
                }
                
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            return user;
        }
        
        @Override
    	public User getUserByID(String dbid) {
            	
            	User user = null;
                String query = "SELECT * FROM PLAYER WHERE id = ?";
                
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setString(1, dbid);
                    ResultSet rs = stmt.executeQuery();
                    
                    if (rs.next()) {
                        int id = rs.getInt("id");
                        String username = rs.getString("name");
                        String password = rs.getString("password");
                        int highscore = rs.getInt("highscore");
                        
                        user = new User(id, username, password, highscore);
                    }
                    
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                
                return user;
            }
	

	@Override
	public void createUser(User user) {
		try {
	        String query = "INSERT INTO PLAYER (name, password) VALUES (?, ?)";
	        PreparedStatement pstmt = con.prepareStatement(query);
	        pstmt.setString(1, user.getUsername());
	        pstmt.setString(2, user.getPassword());
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		
	}

	@Override
	public void updateUser(User user) {
		// TODO Auto-generated method stub
		
	}

}