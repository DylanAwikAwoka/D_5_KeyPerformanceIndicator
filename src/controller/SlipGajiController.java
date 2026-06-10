package controller;

import dao.SlipGajiDAOImpl;
import model.SlipGaji;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class SlipGajiController {
    // Inisialisasi DAO
    private dao.ISlipGajiDAO dao = new SlipGajiDAOImpl();

    // Fungsi untuk export ke CSV (HRD)
    public void exportSlipGajiToCSV(int bulan, int tahun, String filePath) {
        // 1. Ambil data dari DAO sesuai periode
        List<SlipGaji> list = dao.getSlipGajiByBulan(bulan, tahun);
        
        // 2. Tulis ke file CSV
        try (PrintWriter writer = new PrintWriter(new File(filePath))) {
            // Header CSV
            writer.println("ID Slip,User ID,Bulan,Tahun,Gaji Pokok,Total Bonus,Total Potongan,Kustom,Gaji Bersih,Status");
            
            // Isi Data
            for (SlipGaji s : list) {
                writer.printf("%d,%d,%d,%d,%.2f,%.2f,%.2f,%.2f,%.2f,%s\n", 
                    s.getIdSlip(), s.getUserId(), s.getPeriodeBulan(), s.getPeriodeTahun(),
                    s.getNominalGajiPokok(), s.getTotalBonus(), s.getTotalPotongan(), 
                    s.getKustomisasiGaji(), s.getGajiBersih(), s.getStatusApproval());
            }
            System.out.println("Export berhasil ke: " + filePath);
        } catch (FileNotFoundException e) {
            System.err.println("Gagal export CSV: " + e.getMessage());
        }
    }

    // Fungsi lain yang Anda perlukan...
    public void hitungGajiOtomatis(int bulan, int tahun) {
        System.out.println("Memulai kalkulasi gaji otomatis KPI untuk periode: " + bulan + "/" + tahun);
        java.sql.Connection conn = connection.DatabaseConnection.getInstance();
        
        // Asumsi aturan_id = 1 adalah aturan utama yang diatur Owner
        int aturanId = 1; 
        double persentaseBonus = 0.0;
        double potonganPerAlpha = 0.0;

        try {
            // 1. Ambil Rumus Aturan Gaji (KPI) dari Owner
            String sqlAturan = "SELECT persentase_bonus, potongan_wajib FROM aturan_gaji WHERE aturan_id = ?";
            try (java.sql.PreparedStatement ps = conn.prepareStatement(sqlAturan)) {
                ps.setInt(1, aturanId);
                java.sql.ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    persentaseBonus = rs.getDouble("persentase_bonus");
                    potonganPerAlpha = rs.getDouble("potongan_wajib");
                }
            }

            // 2. Tarik semua data user yang aktif (Asumsi id role karyawan)
            String sqlUser = "SELECT id_user FROM user WHERE is_active = true";
            try (java.sql.PreparedStatement psUser = conn.prepareStatement(sqlUser);
                 java.sql.ResultSet rsUser = psUser.executeQuery()) {
                
                while (rsUser.next()) {
                    int userId = rsUser.getInt("id_user");
                    
                    // a. Hitung Total Penjualan user ini di bulan tersebut
                    double totalPenjualan = hitungTotalPenjualan(conn, userId, bulan, tahun);
                    double totalBonus = totalPenjualan * (persentaseBonus / 100);

                    // b. Hitung Total Alpha (Tidak Masuk)
                    int totalAlpha = hitungTotalAlpha(conn, userId, bulan, tahun);
                    double totalPotongan = totalAlpha * potonganPerAlpha;

                    // c. Asumsi Gaji Pokok (Bisa disesuaikan jika mengambil dari tabel Jabatan)
                    double gajiPokok = 3000000.0; // Contoh fix 3 Juta, ubah sesuai kebutuhan database Anda

                    // 3. Buat Objek Slip Gaji untuk dijadikan Draft
                    model.SlipGaji slip = new model.SlipGaji();
                    slip.setUserId(userId);
                    slip.setAturanId(aturanId);
                    slip.setPeriodeBulan(bulan);
                    slip.setPeriodeTahun(tahun);
                    slip.setNominalGajiPokok(gajiPokok);
                    slip.setTotalBonus(totalBonus);
                    slip.setTotalPotongan(totalPotongan);
                    
                    // 4. Masukkan ke Database sebagai Draft HR
                    dao.insertDraftGaji(slip);
                }
            }
            System.out.println("Kalkulasi Selesai! Semua draft gaji berhasil dibuat.");
            
        } catch (java.sql.SQLException e) {
            System.out.println("Error saat kalkulasi gaji otomatis: " + e.getMessage());
        }
    }

    // --- METHOD BANTUAN UNTUK KALKULASI ---

    private double hitungTotalPenjualan(java.sql.Connection conn, int userId, int bulan, int tahun) throws java.sql.SQLException {
        String sql = "SELECT SUM(total) as omset FROM penjualan WHERE user_id = ? AND MONTH(tanggal) = ? AND YEAR(tanggal) = ? AND status = 'Approved'";
        try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, bulan);
            ps.setInt(3, tahun);
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("omset");
        }
        return 0.0;
    }

    private int hitungTotalAlpha(java.sql.Connection conn, int userId, int bulan, int tahun) throws java.sql.SQLException {
        String sql = "SELECT COUNT(*) as jumlah_alpha FROM presensi WHERE user_id = ? AND MONTH(tanggal) = ? AND YEAR(tanggal) = ? AND status = 'Alpha'";
        try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, bulan);
            ps.setInt(3, tahun);
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("jumlah_alpha");
        }
        return 0;
    }
}