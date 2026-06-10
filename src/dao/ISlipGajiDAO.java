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
import model.SlipGaji;

public interface ISlipGajiDAO {
    // Fungsi Khusus HR: Memasukkan draft awal
    void insertDraftGaji(SlipGaji slip);
    
    // Fungsi Khusus Owner: Mengubah nominal/memberi bonus manual
    void updateKustomisasiOwner(int idSlip, double kustomisasiGaji);
    
    // Fungsi Khusus Owner: Menyetujui gaji
    void approveGajiFinal(int idSlip);
    
    // Fungsi Read Data
    List<SlipGaji> getAllSlipGaji();
    List<SlipGaji> getSlipGajiByBulan(int bulan, int tahun);
}
