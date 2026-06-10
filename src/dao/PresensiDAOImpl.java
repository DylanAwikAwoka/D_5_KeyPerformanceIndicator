package dao;

import connection.DatabaseConnection;
import model.Presensi;
import java.sql.*;

public class PresensiDAOImpl {
    public void recordPresensi(Presensi p) {
        String sql = "INSERT INTO presensi (user_id, tanggal, status) VALUES (?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, p.getUserId());
            ps.setDate(2, p.getTanggal());
            ps.setString(3, p.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}