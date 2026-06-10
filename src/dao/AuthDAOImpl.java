package dao;

import connection.DatabaseConnection;
import model.User;
import java.sql.*;

public class AuthDAOImpl {
    public User login(String username, String password) {
        String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Return objek user yang sesuai (bisa dikembangkan dengan factory pattern)
                return null; // Logic inisialisasi object user ada di sini
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}