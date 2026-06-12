package controller;

import dao.IAturanGajiDAO;
import dao.IPenjualanDAO;
import dao.IPresensiDAO;
import dao.ISlipGajiDAO;
import dao.IUserDAO;
import dao.AturanGajiDAOImpl;
import dao.PenjualanDAOImpl;
import dao.PresensiDAOImpl;
import dao.SlipGajiDAOImpl;
import dao.UserDAOImpl;
import model.AturanGaji;
import model.SlipGaji;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Controller untuk proses penggajian.
 *
 * Catatan refactor (Defect #3 - MVC contamination):
 * Seluruh akses database (SQL) sudah DIPINDAH ke lapisan DAO. Controller ini
 * kini murni mengatur ALUR/logika bisnis (orchestration) dan tidak lagi
 * menyentuh java.sql / Connection. Setiap DAO bertanggung jawab atas tabelnya
 * masing-masing dan disuntikkan lewat constructor (Dependency Injection).
 */
public class SlipGajiController {

    // Aturan utama yang diatur Owner diasumsikan ber-id 1.
    private static final int ATURAN_ID_UTAMA = 1;
    // TODO: idealnya gaji pokok diambil dari tabel Jabatan; untuk demo dibuat tetap.
    private static final double GAJI_POKOK_DEFAULT = 3000000.0;

    // Controller hanya mengenal Interface, bukan implementasi SQL-nya (Loose Coupling).
    private final ISlipGajiDAO slipDAO;
    private final IAturanGajiDAO aturanDAO;
    private final IUserDAO userDAO;
    private final IPenjualanDAO penjualanDAO;
    private final IPresensiDAO presensiDAO;

    /** Constructor default: memakai implementasi DAO standar. */
    public SlipGajiController() {
        this(new SlipGajiDAOImpl(), new AturanGajiDAOImpl(), new UserDAOImpl(),
             new PenjualanDAOImpl(), new PresensiDAOImpl());
    }

    /** Constructor untuk Dependency Injection (mempermudah pengujian). */
    public SlipGajiController(ISlipGajiDAO slipDAO, IAturanGajiDAO aturanDAO,
            IUserDAO userDAO, IPenjualanDAO penjualanDAO, IPresensiDAO presensiDAO) {
        this.slipDAO = slipDAO;
        this.aturanDAO = aturanDAO;
        this.userDAO = userDAO;
        this.penjualanDAO = penjualanDAO;
        this.presensiDAO = presensiDAO;
    }

    /**
     * Kalkulasi gaji otomatis (KPI) untuk satu periode, lalu menyimpan draft.
     * Tidak ada SQL di sini — semua data diambil melalui DAO.
     */
    public void hitungGajiOtomatis(int bulan, int tahun) {
        System.out.println("Memulai kalkulasi gaji otomatis KPI untuk periode: " + bulan + "/" + tahun);

        // 1. Ambil rumus Aturan Gaji (KPI) yang diatur Owner.
        AturanGaji aturan = aturanDAO.getAturanById(ATURAN_ID_UTAMA);
        if (aturan == null) {
            System.out.println("Kalkulasi dibatalkan: aturan gaji id "
                    + ATURAN_ID_UTAMA + " tidak ditemukan.");
            return;
        }
        double persentaseBonus = aturan.getPersentaseBonus();
        double potonganPerAlpha = aturan.getPotonganWajib();

        // 2. Proses setiap user aktif.
        List<Integer> userIds = userDAO.getActiveUserIds();
        for (int userId : userIds) {
            // a. Bonus dari total penjualan tervalidasi.
            double totalPenjualan = penjualanDAO.getTotalPenjualanValid(userId, bulan, tahun);
            double totalBonus = totalPenjualan * (persentaseBonus / 100);

            // b. Potongan dari jumlah Alpha (tidak masuk).
            int totalAlpha = presensiDAO.getTotalAlpha(userId, bulan, tahun);
            double totalPotongan = totalAlpha * potonganPerAlpha;

            // c. Susun objek draft slip gaji.
            SlipGaji slip = new SlipGaji();
            slip.setUserId(userId);
            slip.setAturanId(ATURAN_ID_UTAMA);
            slip.setPeriodeBulan(bulan);
            slip.setPeriodeTahun(tahun);
            slip.setNominalGajiPokok(GAJI_POKOK_DEFAULT);
            slip.setTotalBonus(totalBonus);
            slip.setTotalPotongan(totalPotongan);

            // d. Simpan sebagai Draft HR.
            slipDAO.insertDraftGaji(slip);
        }
        System.out.println("Kalkulasi Selesai! Semua draft gaji berhasil dibuat.");
    }

    /**
     * Export slip gaji satu periode ke file CSV (untuk HRD).
     */
    public void exportSlipGajiToCSV(int bulan, int tahun, String filePath) {
        List<SlipGaji> list = slipDAO.getSlipGajiByBulan(bulan, tahun);

        try (PrintWriter writer = new PrintWriter(new File(filePath))) {
            writer.println("ID Slip,User ID,Bulan,Tahun,Gaji Pokok,Total Bonus,Total Potongan,Kustom,Gaji Bersih,Status");
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
}
