package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton penyedia koneksi database (MySQL).
 *
 * Perbaikan utama dibanding versi sebelumnya:
 *  - Validasi connection.isClosed() agar koneksi yang sudah tertutup / timeout
 *    tidak dikembalikan ke pemanggil (mencegah "No operations allowed after
 *    connection closed").
 *  - Sinkronisasi (thread-safe) saat membuat / membuat ulang koneksi.
 *  - Gagal koneksi dilempar sebagai RuntimeException, bukan ditelan diam-diam,
 *    sehingga pemanggil tahu koneksi tidak valid (tidak lagi mengembalikan null).
 *
 * Catatan arsitektur: method getInstance() dipertahankan agar kompatibel dengan
 * seluruh DAO yang sudah ada. Dependency Injection koneksi ke DAO akan dikerjakan
 * pada tahap refactor lapisan DAO.
 *
 * @author hp
 */
public final class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/d_5_keyperformanceindicator"
            + "?useSSL=false&serverTimezone=Asia/Jakarta&autoReconnect=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    /** Timeout (detik) untuk pengecekan validitas koneksi. */
    private static final int VALIDATION_TIMEOUT = 2;

    private static Connection connection;

    private DatabaseConnection() {
        // Mencegah instansiasi dari luar.
    }

    /**
     * Mengembalikan koneksi tunggal yang dijamin masih terbuka dan valid.
     * Jika koneksi belum ada, sudah tertutup, atau tidak valid lagi,
     * koneksi baru akan dibuat secara otomatis.
     *
     * @return Connection yang aktif dan valid.
     */
    public static synchronized Connection getInstance() {
        try {
            if (connection == null
                    || connection.isClosed()
                    || !connection.isValid(VALIDATION_TIMEOUT)) {
                openConnection();
            }
        } catch (SQLException e) {
            // isClosed()/isValid() sendiri bisa gagal jika koneksi rusak total.
            openConnection();
        }
        return connection;
    }

    /**
     * Membuat (atau membuat ulang) koneksi fisik ke database.
     */
    private static void openConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Koneksi Database Berhasil!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    "Driver MySQL tidak ditemukan di classpath.", e);
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Koneksi Database GAGAL: " + e.getMessage(), e);
        }
    }

    /**
     * Menutup koneksi secara eksplisit (mis. saat aplikasi shutdown).
     * Aman dipanggil walau koneksi sudah null / tertutup.
     */
    public static synchronized void close() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Koneksi Database ditutup.");
                }
            } catch (SQLException e) {
                System.out.println("Gagal menutup koneksi: " + e.getMessage());
            } finally {
                connection = null;
            }
        }
    }
}
