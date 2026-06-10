package dao;

import connection.DatabaseConnection;
import model.AturanGaji;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AturanGajiDAOImpl implements IAturanGajiDAO {

    @Override
    public void insertAturan(AturanGaji aturan) {
        Connection conn = DatabaseConnection.getInstance();
        String query = "INSERT INTO aturan_gaji (nama_aturan, persentase_bonus, potongan_wajib) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, aturan.getNamaAturan());
            ps.setDouble(2, aturan.getPersentaseBonus());
            ps.setDouble(3, aturan.getPotonganWajib());
            ps.executeUpdate();
            System.out.println("Aturan gaji berhasil ditambahkan.");
        } catch (SQLException e) {
            System.out.println("Error insert aturan gaji: " + e.getMessage());
        }
    }

    @Override
    public List<AturanGaji> getAllAturan() {
        List<AturanGaji> list = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance();
        String query = "SELECT * FROM aturan_gaji";

        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                AturanGaji aturan = new AturanGaji(
                    rs.getInt("aturan_id"),
                    rs.getString("nama_aturan"),
                    rs.getDouble("persentase_bonus"),
                    rs.getDouble("potongan_wajib")
                );
                list.add(aturan);
            }
        } catch (SQLException e) {
            System.out.println("Error get aturan gaji: " + e.getMessage());
        }

        return list;
    }

    @Override
    public void updateAturan(AturanGaji aturan) {
        Connection conn = DatabaseConnection.getInstance();
        String query = "UPDATE aturan_gaji SET nama_aturan = ?, persentase_bonus = ?, potongan_wajib = ? WHERE aturan_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, aturan.getNamaAturan());
            ps.setDouble(2, aturan.getPersentaseBonus());
            ps.setDouble(3, aturan.getPotonganWajib());
            ps.setInt(4, aturan.getAturanId());
            ps.executeUpdate();
            System.out.println("Aturan gaji berhasil diupdate.");
        } catch (SQLException e) {
            System.out.println("Error update aturan gaji: " + e.getMessage());
        }
    }

    @Override
    public void deleteAturan(int aturanId) {
        Connection conn = DatabaseConnection.getInstance();
        String query = "DELETE FROM aturan_gaji WHERE aturan_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, aturanId);
            ps.executeUpdate();
            System.out.println("Aturan gaji berhasil dihapus.");
        } catch (SQLException e) {
            System.out.println("Error delete aturan gaji: " + e.getMessage());
        }
    }
}
