/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author hp
 */



public class Karyawan extends User {
    private double gajiPokok;
    private String posisi;

    public Karyawan(int idUser, String username, String password, String namaLengkap,
            boolean isActive, int roleId, int cabangId, double gajiPokok, String posisi){
        super(idUser, username, password, namaLengkap, isActive, roleId, cabangId);
        this.gajiPokok = gajiPokok;
        this.posisi = posisi;
    }

    public double getGajiPokok() { return gajiPokok; }
    public void setGajiPokok(double gajiPokok) { this.gajiPokok = gajiPokok; }

    public String getPosisi() { return posisi; }
    public void setPosisi(String posisi) { this.posisi = posisi; }
}