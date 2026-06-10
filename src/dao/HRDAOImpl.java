package dao;


import connection.DatabaseConnection;
import java.sql.*;

public class HRDAOImpl {
    public int getStatistikPegawaiAktif() {
        String sql = "SELECT COUNT(*) FROM user WHERE is_active = true";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public void nonaktifkanAkun(int userId) {
        String sql = "UPDATE user SET is_active = false WHERE id_user = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}