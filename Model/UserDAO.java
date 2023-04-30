package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class UserDAO {
    private Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO users(username, password, avatarUrl) VALUES(?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getAvatarUrl());
            pstmt.executeUpdate();
        }
    }

    public User getByUsername(String username) throws SQLException {
        User user = null;
        String sql = "SELECT * FROM users WHERE username=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"),
                        rs.getString("avatarUrl"));
            }
        }
        return user;
    }
}
