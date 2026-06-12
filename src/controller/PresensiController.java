package controller;

import dao.IPresensiDAO;
import dao.PresensiDAOImpl;
import model.Presensi;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Controller untuk presensi (kehadiran) karyawan.
 *
 * Menampung logika clock-in yang sebelumnya tidak ada: status kehadiran
 * ditentukan dari jam saat ini. Clock-in setelah pukul 09:00 otomatis
 * berstatus 'Telat'.
 */
public class PresensiController {

    // Batas jam masuk; clock-in setelah jam ini dianggap Telat.
    private static final LocalTime BATAS_TELAT = LocalTime.of(9, 0);

    public static final String STATUS_HADIR = "Hadir";
    public static final String STATUS_TELAT = "Telat";

    private final IPresensiDAO presensiDAO;

    /** Constructor default: memakai implementasi DAO standar. */
    public PresensiController() {
        this(new PresensiDAOImpl());
    }

    /** Constructor untuk Dependency Injection (mempermudah pengujian). */
    public PresensiController(IPresensiDAO presensiDAO) {
        this.presensiDAO = presensiDAO;
    }

    /**
     * Mencatat clock-in karyawan hari ini. Status ditentukan otomatis
     * berdasarkan jam saat ini (Telat bila lewat pukul 09:00).
     *
     * @param userId id karyawan yang absen.
     * @return status presensi yang tercatat ("Hadir" / "Telat").
     */
    public String clockIn(int userId) {
        String status = LocalTime.now().isAfter(BATAS_TELAT)
                ? STATUS_TELAT
                : STATUS_HADIR;

        Date hariIni = Date.valueOf(LocalDate.now());
        Presensi presensi = new Presensi(0, userId, hariIni, status);
        presensiDAO.recordPresensi(presensi);

        System.out.println("Clock-in tercatat untuk user " + userId + " dengan status: " + status);
        return status;
    }
}
