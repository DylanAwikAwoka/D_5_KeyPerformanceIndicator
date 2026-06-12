package controller;


import dao.AturanGajiDAOImpl;
import exception.InputKosongException;
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
    public void perbaruiRumusKPI(int aturanId, String namaAturan, double persenBonus, double dendaAlpha)
            throws InputKosongException {

        // Validasi berurutan (guard clause) — jalan utama hanya lanjut bila semua lolos.

        // 1. Nama aturan wajib diisi.
        if (namaAturan == null || namaAturan.trim().isEmpty()) {
            throw new InputKosongException("GAGAL: Nama aturan wajib diisi!");
        }

        // 2. Persentase bonus harus dalam rentang 0–100.
        if (persenBonus < 0 || persenBonus > 100) {
            throw new IllegalArgumentException("GAGAL: Persentase bonus harus antara 0 dan 100.");
        }

        // 3. Denda alpha tidak boleh negatif.
        if (dendaAlpha < 0) {
            throw new IllegalArgumentException("GAGAL: Denda alpha tidak boleh negatif.");
        }

        // Semua validasi lolos -> bungkus ke Model lalu update via DAO.
        AturanGaji aturanBaru = new AturanGaji(aturanId, namaAturan, persenBonus, dendaAlpha);
        dao.updateAturan(aturanBaru);

        System.out.println("SUKSES: Rumus KPI berhasil diperbarui oleh Owner!");
    }
}