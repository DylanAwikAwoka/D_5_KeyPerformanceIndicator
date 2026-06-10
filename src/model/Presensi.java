package model;

import java.sql.Date;

public class Presensi {
    private int idPresensi;
    private int userId;
    private Date tanggal;
    private String status;

    // Constructor
    public Presensi(int idPresensi, int userId, Date tanggal, String status) {
        this.idPresensi = idPresensi;
        this.userId = userId;
        this.tanggal = tanggal;
        this.status = status;
    }

    // WAJIB ADA GETTER AGAR DAO BISA MENGAKSES DATA
    public int getUserId() {
        return userId;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public String getStatus() {
        return status;
    }

    // Opsional: Tambahkan setter jika diperlukan
    public void setIdPresensi(int idPresensi) { this.idPresensi = idPresensi; }
}