/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author hp
 */

import java.sql.Date;

public class Penjualan {
    private int idPenjualan;
    private int userId; // ID Karyawan (Kasir) yang menginput
    private Date tanggal;
    private double totalPenjualan;
    private String statusValidasi; // 'Pending', 'Valid', 'Ditolak'
    private int editedBy; // ID Manager yang melakukan validasi

    public Penjualan() {}

    public Penjualan(int idPenjualan, int userId, Date tanggal, double totalPenjualan, String statusValidasi, int editedBy) {
        this.idPenjualan = idPenjualan;
        this.userId = userId;
        this.tanggal = tanggal;
        this.totalPenjualan = totalPenjualan;
        this.statusValidasi = statusValidasi;
        this.editedBy = editedBy;
    }

    // Getters and Setters (Encapsulation)
    public int getIdPenjualan() { return idPenjualan; }
    public void setIdPenjualan(int idPenjualan) { this.idPenjualan = idPenjualan; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Date getTanggal() { return tanggal; }
    public void setTanggal(Date tanggal) { this.tanggal = tanggal; }

    public double getTotalPenjualan() { return totalPenjualan; }
    public void setTotalPenjualan(double totalPenjualan) { this.totalPenjualan = totalPenjualan; }

    public String getStatusValidasi() { return statusValidasi; }
    public void setStatusValidasi(String statusValidasi) { this.statusValidasi = statusValidasi; }

    public int getEditedBy() { return editedBy; }
    public void setEditedBy(int editedBy) { this.editedBy = editedBy; }
}