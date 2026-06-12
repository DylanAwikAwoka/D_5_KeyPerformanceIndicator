/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author hp
 */

import dao.IPenjualanDAO;
import dao.PenjualanDAOImpl;
import exception.BatasWaktuException;
import exception.InputKosongException;
import java.sql.Date;
import java.time.LocalTime;
import java.util.List;
import model.Penjualan;

public class PenjualanController {

    // Tutup Kasir hanya boleh dilakukan SEBELUM pukul 17:00.
    private static final LocalTime BATAS_TUTUP_KASIR = LocalTime.of(17, 0);

    // Status validasi yang sah dari Manager.
    private static final String STATUS_VALID = "Valid";
    private static final String STATUS_DITOLAK = "Ditolak";

    private IPenjualanDAO penjualanDAO;

    public PenjualanController() {
        this.penjualanDAO = new PenjualanDAOImpl();
    }

    /**
     * Method untuk Kasir menginput penjualan harian (Tutup Kasir).
     * Throws 3 jenis Exception: batas waktu, input kosong, dan parsing angka.
     */
    public void submitPenjualan(int userId, String tanggalStr, String totalPenjualanStr)
            throws BatasWaktuException, InputKosongException, NumberFormatException {

        // 1. Validasi Batas Waktu: Tutup Kasir wajib sebelum 17:00.
        //    Diletakkan paling awal agar transaksi lewat batas langsung gagal.
        if (!LocalTime.now().isBefore(BATAS_TUTUP_KASIR)) {
            throw new BatasWaktuException(
                "GAGAL: Tutup Kasir hanya bisa dilakukan sebelum pukul "
                + BATAS_TUTUP_KASIR + ".");
        }

        // 2. Validasi Input Kosong (User Defined Exception)
        if (tanggalStr == null || tanggalStr.trim().isEmpty() ||
            totalPenjualanStr == null || totalPenjualanStr.trim().isEmpty()) {
            throw new InputKosongException("GAGAL: Tanggal dan Total Penjualan wajib diisi!");
        }

        // 3. Parsing Tipe Data (Memancing Default Exception)
        // Jika kasir menginput "Seratus Ribu" di kolom uang, ini otomatis melempar NumberFormatException
        double total = Double.parseDouble(totalPenjualanStr);
        Date tanggal = Date.valueOf(tanggalStr); // Format wajib YYYY-MM-DD

        // 4. Eksekusi simpan ke DAO
        Penjualan p = new Penjualan(0, userId, tanggal, total, "Pending", 0);
        penjualanDAO.insertPenjualan(p);
    }

    /**
     * Method untuk menampilkan riwayat ke layar (JTable)
     */
    public List<Penjualan> ambilSemuaPenjualan() {
        return penjualanDAO.getAllPenjualan();
    }

    /**
     * Method untuk Manager memvalidasi (Menerima/Menolak).
     * Status hanya boleh 'Valid' atau 'Ditolak'.
     */
    public void validasiPenjualanManager(int idPenjualan, String status, int managerId) {
        // Guard clause: tolak status yang tidak sah sebelum menyentuh DAO.
        if (!STATUS_VALID.equals(status) && !STATUS_DITOLAK.equals(status)) {
            throw new IllegalArgumentException(
                "GAGAL: Status hanya boleh '" + STATUS_VALID + "' atau '" + STATUS_DITOLAK + "'.");
        }
        penjualanDAO.validasiPenjualan(idPenjualan, status, managerId);
    }
}