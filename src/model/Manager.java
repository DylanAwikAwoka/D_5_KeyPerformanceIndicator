/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author hp
 */


public class Manager extends User {
    private double gajiPokok;
    private String departemen;

    public Manager(int idUser, String username, String password, String namaLengkap, 
            boolean isActive, int roleId, int cabangId, double gajiPokok, String departemen){
        
        super(idUser, username, password, namaLengkap, isActive, roleId, cabangId);
        this.gajiPokok = gajiPokok;
        this.departemen = departemen;
    }

    public double getGajiPokok() { return gajiPokok; }
    public void setGajiPokok(double gajiPokok) { this.gajiPokok = gajiPokok; }

    public String getDepartemen() { return departemen; }
    public void setDepartemen(String departemen) { this.departemen = departemen; }
}