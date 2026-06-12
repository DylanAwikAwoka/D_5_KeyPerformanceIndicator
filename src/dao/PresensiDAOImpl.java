package dao;

import connection.DatabaseConnection;
import model.Presensi;
import java.sql.*;

public class PresensiDAOImpl implements IPresensiDAO {

    @Override
    public void recordPresensi(Presensi p) {
        String sql = "INSERT INTO presensi (user_id, tanggal, status) VALUES (?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, p.getUserId());
            ps.setDate(2, p.getTanggal());
            ps.setString(3, p.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error record presensi: " + e.getMessage());
        }
    }

    @Override
    public int getTotalAlpha(int userId, int bulan, int tahun) {
        Connection conn = DatabaseConnection.getInstance();
        String sql = "SELECT COUNT(*) AS jumlah_alpha FROM presensi "
                   + "WHERE user_id = ? AND MONTH(tanggal) = ? AND YEAR(tanggal) = ? "
                   + "AND status = 'Alpha'";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, bulan);
            ps.setInt(3, tahun);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("jumlah_alpha");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error hitung total alpha: " + e.getMessage());
        }
        return 0;
    }
}