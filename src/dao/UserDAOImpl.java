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
import model.Karyawan;
import model.Manager;
import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements IUserDAO {

    @Override
    public void insertUser(User user) {
        Connection conn = DatabaseConnection.getInstance();
        String queryUser = "INSERT INTO user (username, password, nama_lengkap, is_active, role_id, cabang_id) VALUES (?, ?, ?, ?, ?, ?)";
        
        try {
            // Matikan auto-commit (Transaction Management) agar data aman jika terjadi error sebagian
            conn.setAutoCommit(false); 
            
            // 1. Insert ke tabel induk (User)
            PreparedStatement psUser = conn.prepareStatement(queryUser, Statement.RETURN_GENERATED_KEYS);
            psUser.setString(1, user.getUsername());
            psUser.setString(2, user.getPassword()); 
            psUser.setString(3, user.getNamaLengkap());
            psUser.setBoolean(4, user.isActive());
            psUser.setInt(5, user.getRoleId());
            psUser.setInt(6, user.getCabangId());
            psUser.executeUpdate();
            
            // Ambil ID User yang baru saja dibuat otomatis oleh database
            ResultSet rs = psUser.getGeneratedKeys();
            int newUserId = 0;
            if (rs.next()) {
                newUserId = rs.getInt(1);
            }
            
            // 2. Insert ke tabel turunan berdasarkan Polymorphism
            if (user instanceof Manager) {
                Manager mgr = (Manager) user;
                String queryMgr = "INSERT INTO manager (id_user, gaji_pokok, departemen) VALUES (?, ?, ?)";
                PreparedStatement psMgr = conn.prepareStatement(queryMgr);
                psMgr.setInt(1, newUserId);
                psMgr.setDouble(2, mgr.getGajiPokok());
                psMgr.setString(3, mgr.getDepartemen());
                psMgr.executeUpdate();
            } else if (user instanceof Karyawan) {
                Karyawan kry = (Karyawan) user;
                String queryKry = "INSERT INTO karyawan (id_user, gaji_pokok, posisi) VALUES (?, ?, ?)";
                PreparedStatement psKry = conn.prepareStatement(queryKry);
                psKry.setInt(1, newUserId);
                psKry.setDouble(2, kry.getGajiPokok());
                psKry.setString(3, kry.getPosisi());
                psKry.executeUpdate();
            }
            
            conn.commit(); // Simpan permanen jika semua operasi sukses
            System.out.println("Data user berhasil disimpan ke database!");
            
        } catch (SQLException e) {
            try {
                conn.rollback(); // Batalkan semua jika ada error agar data tidak setengah masuk
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("Error saat insert data: " + e.getMessage());
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) {}
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> listUser = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance();
        
        // LEFT JOIN untuk mengambil data dari tabel user digabung dengan tabel manager/karyawan
        String query = "SELECT u.*, m.gaji_pokok as mgr_gaji, m.departemen, " +
                       "k.gaji_pokok as kry_gaji, k.posisi " +
                       "FROM user u " +
                       "LEFT JOIN manager m ON u.id_user = m.id_user " +
                       "LEFT JOIN karyawan k ON u.id_user = k.id_user";
                       
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                int id = rs.getInt("id_user");
                String uname = rs.getString("username");
                String pass = rs.getString("password");
                String nama = rs.getString("nama_lengkap");
                boolean aktif = rs.getBoolean("is_active");
                int role = rs.getInt("role_id");
                int cabang = rs.getInt("cabang_id");
                
                // Polymorphism: Instansiasi objek yang berbeda berdasarkan Role ID
                if (role == 3) { // 3 = Manager
                    double gaji = rs.getDouble("mgr_gaji");
                    String dept = rs.getString("departemen");
                    listUser.add(new Manager(id, uname, pass, nama, aktif, role, cabang, gaji, dept));
                } else if (role == 4) { // 4 = Karyawan
                    double gaji = rs.getDouble("kry_gaji");
                    String pos = rs.getString("posisi");
                    listUser.add(new Karyawan(id, uname, pass, nama, aktif, role, cabang, gaji, pos));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error get all users: " + e.getMessage());
        }
        return listUser;
    }

    @Override
    public void updateUser(User user) {
        Connection conn = DatabaseConnection.getInstance();
        String queryUser = "UPDATE user SET username=?, password=?, nama_lengkap=?, is_active=?, role_id=?, cabang_id=? WHERE id_user=?";
        
        try {
            conn.setAutoCommit(false);
            
            // 1. Update tabel Induk
            PreparedStatement psUser = conn.prepareStatement(queryUser);
            psUser.setString(1, user.getUsername());
            psUser.setString(2, user.getPassword());
            psUser.setString(3, user.getNamaLengkap());
            psUser.setBoolean(4, user.isActive());
            psUser.setInt(5, user.getRoleId());
            psUser.setInt(6, user.getCabangId());
            psUser.setInt(7, user.getIdUser());
            psUser.executeUpdate();
            
            // 2. Update tabel Turunan
            if (user instanceof Manager) {
                Manager mgr = (Manager) user;
                String queryMgr = "UPDATE manager SET gaji_pokok=?, departemen=? WHERE id_user=?";
                PreparedStatement psMgr = conn.prepareStatement(queryMgr);
                psMgr.setDouble(1, mgr.getGajiPokok());
                psMgr.setString(2, mgr.getDepartemen());
                psMgr.setInt(3, mgr.getIdUser());
                psMgr.executeUpdate();
            } else if (user instanceof Karyawan) {
                Karyawan kry = (Karyawan) user;
                String queryKry = "UPDATE karyawan SET gaji_pokok=?, posisi=? WHERE id_user=?";
                PreparedStatement psKry = conn.prepareStatement(queryKry);
                psKry.setDouble(1, kry.getGajiPokok());
                psKry.setString(2, kry.getPosisi());
                psKry.setInt(3, kry.getIdUser());
                psKry.executeUpdate();
            }
            
            conn.commit();
            System.out.println("Data user berhasil diupdate!");
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) {}
            System.out.println("Error update user: " + e.getMessage());
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) {}
        }
    }

    @Override
    public void deleteUser(int idUser) {
        Connection conn = DatabaseConnection.getInstance();
        // Cukup hapus di tabel user, MySQL akan otomatis menghapus data di tabel manager/karyawan (ON DELETE CASCADE)
        String query = "DELETE FROM user WHERE id_user = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idUser);
            ps.executeUpdate();
            System.out.println("User berhasil dihapus!");
        } catch (SQLException e) {
            System.out.println("Error delete user: " + e.getMessage());
        }
    }
}
