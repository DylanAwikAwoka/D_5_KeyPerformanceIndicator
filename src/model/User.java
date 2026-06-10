/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author hp
 */

public abstract class User {
    private int idUser;
    private String username;
    private String password;
    private String namaLengkap;
    private boolean isActive;
    private int roleId;
    private int cabangId;

    public User(int idUser, String username, String password, String namaLengkap, boolean isActive, int roleId, int cabangId) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.namaLengkap = namaLengkap;
        this.isActive = isActive;
        this.roleId = roleId;
        this.cabangId = cabangId;
    }

    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }

    public int getCabangId() { return cabangId; }
    public void setCabangId(int cabangId) { this.cabangId = cabangId; }
}