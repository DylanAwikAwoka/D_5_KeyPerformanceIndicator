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
import model.Penjualan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PenjualanDAOImpl implements IPenjualanDAO {

    @Override
    public void insertPenjualan(Penjualan p) {
        Connection conn = DatabaseConnection.getInstance();
        // Secara default saat diinput Karyawan/Kasir, statusnya otomatis 'Pending'
        String query = "INSERT INTO penjualan (user_id, tanggal, total_penjualan, status_validasi) VALUES (?, ?, ?, 'Pending')";
        
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, p.getUserId());
            ps.setDate(2, p.getTanggal());
            ps.setDouble(3, p.getTotalPenjualan());
            ps.executeUpdate();
            System.out.println("Data penjualan berhasil disubmit, menunggu validasi Manager.");
        } catch (SQLException e) {
            System.out.println("Error insert penjualan: " + e.getMessage());
        }
    }

    @Override
    public List<Penjualan> getAllPenjualan() {
        List<Penjualan> listPenjualan = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance();
        String query = "SELECT * FROM penjualan ORDER BY tanggal DESC";
        
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                Penjualan p = new Penjualan();
                p.setIdPenjualan(rs.getInt("id_penjualan"));
                p.setUserId(rs.getInt("user_id"));
                p.setTanggal(rs.getDate("tanggal"));
                p.setTotalPenjualan(rs.getDouble("total_penjualan"));
                p.setStatusValidasi(rs.getString("status_validasi"));
                p.setEditedBy(rs.getInt("edited_by"));
                listPenjualan.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error get data penjualan: " + e.getMessage());
        }
        return listPenjualan;
    }

    @Override
    public void validasiPenjualan(int idPenjualan, String status, int managerId) {
        Connection conn = DatabaseConnection.getInstance();
        // Manager hanya mengupdate status (Valid/Ditolak) dan mencatat ID-nya sebagai validator
        String query = "UPDATE penjualan SET status_validasi = ?, edited_by = ? WHERE id_penjualan = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, status); 
            ps.setInt(2, managerId);
            ps.setInt(3, idPenjualan);
            ps.executeUpdate();
            System.out.println("Status penjualan berhasil diupdate menjadi: " + status);
        } catch (SQLException e) {
            System.out.println("Error validasi penjualan: " + e.getMessage());
        }
    }
}