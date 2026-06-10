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
import exception.InputKosongException;
import java.sql.Date;
import java.util.List;
import model.Penjualan;

public class PenjualanController {
    
    private IPenjualanDAO penjualanDAO;

    public PenjualanController() {
        this.penjualanDAO = new PenjualanDAOImpl();
    }

    /**
     * Method untuk Kasir menginput penjualan harian
     * Throws 2 jenis Exception sekaligus sesuai syarat rubrik dosen!
     */
    public void submitPenjualan(int userId, String tanggalStr, String totalPenjualanStr) 
            throws InputKosongException, NumberFormatException {
        
        // 1. Validasi Input Kosong (User Defined Exception)
        if (tanggalStr == null || tanggalStr.trim().isEmpty() ||
            totalPenjualanStr == null || totalPenjualanStr.trim().isEmpty()) {
            throw new InputKosongException("GAGAL: Tanggal dan Total Penjualan wajib diisi!");
        }

        // 2. Parsing Tipe Data (Memancing Default Exception)
        // Jika kasir menginput "Seratus Ribu" di kolom uang, ini otomatis melempar NumberFormatException
        double total = Double.parseDouble(totalPenjualanStr);
        Date tanggal = Date.valueOf(tanggalStr); // Format wajib YYYY-MM-DD

        // 3. Eksekusi simpan ke DAO
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
     * Method untuk Manager memvalidasi (Menerima/Menolak)
     */
    public void validasiPenjualanManager(int idPenjualan, String status, int managerId) {
        penjualanDAO.validasiPenjualan(idPenjualan, status, managerId);
    }
}