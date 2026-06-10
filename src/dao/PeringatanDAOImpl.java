/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author hp
 */

import connection.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeringatanDAOImpl {
    public void kirimPeringatan(int managerId, String pesan) {
        String sql = "INSERT INTO peringatan (manager_id, isi_pesan, tanggal) VALUES (?, ?, CURDATE())";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, managerId);
            ps.setString(2, pesan);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<String> getAllPeringatanByManager(int managerId) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT isi_pesan FROM peringatan WHERE manager_id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, managerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(rs.getString("isi_pesan"));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}
