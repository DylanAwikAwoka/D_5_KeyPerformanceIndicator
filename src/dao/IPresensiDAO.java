package dao;

import model.Presensi;

/**
 * Kontrak akses data presensi (kehadiran karyawan).
 * Sebelumnya PresensiDAO tidak memiliki interface (Defect #6).
 */
public interface IPresensiDAO {

    /** Mencatat satu baris presensi. */
    void recordPresensi(Presensi presensi);

    /**
     * Jumlah presensi ber-status 'Alpha' (tidak masuk) milik seorang user
     * pada bulan & tahun tertentu. Dipakai untuk kalkulasi potongan gaji.
     */
    int getTotalAlpha(int userId, int bulan, int tahun);
}
