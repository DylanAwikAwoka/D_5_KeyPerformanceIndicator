package controller;


import dao.AturanGajiDAOImpl;
import model.AturanGaji;
import java.util.List;

public class AturanGajiController {
    
    private AturanGajiDAOImpl dao = new AturanGajiDAOImpl();

    // 1. Fungsi untuk menampilkan Rumus KPI saat ini ke layar Owner
    public AturanGaji getAturanAktif() {
        List<AturanGaji> list = dao.getAllAturan();
        if (!list.isEmpty()) {
            return list.get(0); // Mengambil aturan utama (Asumsi aturan_id = 1)
        }
        return null;
    }

    // 2. Fungsi saat Owner menekan tombol "Simpan Rumus Baru"
    public void perbaruiRumusKPI(int aturanId, String namaAturan, double persenBonus, double dendaAlpha) {
        // Bungkus data yang diinput Owner ke dalam objek Model
        AturanGaji aturanBaru = new AturanGaji(aturanId, namaAturan, persenBonus, dendaAlpha);
        
        // Kirim ke DAO untuk di-update di Database
        dao.updateAturan(aturanBaru);
        
        System.out.println("SUKSES: Rumus KPI berhasil diperbarui oleh Owner!");
    }
}