/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author hp
 */


import java.util.List;
import model.Penjualan;

public interface IPenjualanDAO {
    // Fungsi untuk Karyawan (Kasir)
    void insertPenjualan(Penjualan penjualan);
    
    // Fungsi untuk Read Data (Bisa difilter di Controller nanti)
    List<Penjualan> getAllPenjualan();
    
    // Fungsi untuk Manager (Hanya mengupdate status validasi, bukan merubah nominal uang)
    void validasiPenjualan(int idPenjualan, String status, int managerId);

    /**
     * Total omzet penjualan ber-status 'Valid' milik seorang user pada
     * bulan & tahun tertentu. Dipakai untuk kalkulasi bonus gaji.
     */
    double getTotalPenjualanValid(int userId, int bulan, int tahun);
}
