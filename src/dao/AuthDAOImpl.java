package dao;

import connection.DatabaseConnection;
import model.Karyawan;
import model.Manager;
import model.User;
import util.PasswordHasher;
import java.sql.*;

/**
 * Implementasi autentikasi user.
 *
 * Perbaikan dibanding versi sebelumnya:
 *  - Tidak lagi mengembalikan null saat user ditemukan; kini membangun objek
 *    konkret (Manager / Karyawan) berdasarkan role_id, mengikuti pola
 *    polymorphism di UserDAOImpl.getAllUsers().
 *  - Pencarian dilakukan berdasarkan username saja, lalu password dicocokkan
 *    di sisi Java. Ini mempermudah migrasi ke password ter-hash nanti
 *    (cukup mengganti satu baris perbandingan, SQL tidak berubah).
 *  - Menambahkan validasi is_active: user nonaktif tidak boleh login.
 *
 * Catatan: password masih plaintext untuk tahap ini. Hashing akan dikerjakan
 * pada langkah keamanan tersendiri.
 */
public class AuthDAOImpl implements IAuthDAO {

    private static final int ROLE_MANAGER = 3;
    private static final int ROLE_KARYAWAN = 4;

    @Override
    public User login(String username, String password) {
        // LEFT JOIN agar data turunan (gaji, posisi/departemen) ikut terambil,
        // konsisten dengan UserDAOImpl.getAllUsers().
        String sql = "SELECT u.*, m.gaji_pokok AS mgr_gaji, m.departemen, "
                   + "k.gaji_pokok AS kry_gaji, k.posisi "
                   + "FROM user u "
                   + "LEFT JOIN manager m ON u.id_user = m.id_user "
                   + "LEFT JOIN karyawan k ON u.id_user = k.id_user "
                   + "WHERE u.username = ?";

        Connection conn = DatabaseConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null; // username tidak ditemukan
                }

                // Cocokkan password via hash. matches() menerima nilai tersimpan
                // berupa hash maupun plaintext lama (transisi), sehingga data
                // lama tetap bisa login sampai password di-update ulang.
                String storedPassword = rs.getString("password");
                if (!PasswordHasher.matches(password, storedPassword)) {
                    return null; // password salah
                }

                // Tolak user nonaktif.
                if (!rs.getBoolean("is_active")) {
                    return null;
                }

                return buildUser(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error saat login: " + e.getMessage());
            return null;
        }
    }

    /**
     * Membangun objek User konkret dari baris ResultSet berdasarkan role_id.
     */
    private User buildUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_user");
        String uname = rs.getString("username");
        String pass = rs.getString("password");
        String nama = rs.getString("nama_lengkap");
        boolean aktif = rs.getBoolean("is_active");
        int role = rs.getInt("role_id");
        int cabang = rs.getInt("cabang_id");

        if (role == ROLE_MANAGER) {
            return new Manager(id, uname, pass, nama, aktif, role, cabang,
                    rs.getDouble("mgr_gaji"), rs.getString("departemen"));
        } else if (role == ROLE_KARYAWAN) {
            return new Karyawan(id, uname, pass, nama, aktif, role, cabang,
                    rs.getDouble("kry_gaji"), rs.getString("posisi"));
        }

        // Role tanpa kelas konkret (mis. admin/owner) belum didukung di tahap ini.
        System.out.println("Login gagal: role_id " + role + " belum didukung.");
        return null;
    }
}
