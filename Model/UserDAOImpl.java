package Model;

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
	public boolean createUser(User user) {
	    try {
	        String query1 = "SELECT * FROM PLAYER WHERE name = ?";
	        PreparedStatement pstmt = con.prepareStatement(query1);
	        pstmt.setString(1, user.getUsername());
	        ResultSet resultSet = pstmt.executeQuery();

	        if (!resultSet.next()) {
	            String query2 = "INSERT INTO PLAYER (name, password) VALUES (?, ?)";
	            PreparedStatement insertStatement = con.prepareStatement(query2);
	            insertStatement.setString(1, user.getUsername());
	            insertStatement.setString(2, user.getPassword());
	            insertStatement.executeUpdate();
	            return true;
	        } else {
	            // User with the same username already exists
	            System.out.println("User with the same username already exists");
	            return false;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return false;
	}
		
	

	@Override
	public void updateUser(User user) {
		// TODO Auto-generated method stub
		
	}

}