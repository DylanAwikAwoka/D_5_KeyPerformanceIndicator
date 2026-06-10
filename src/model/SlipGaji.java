/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author hp
 */

public class SlipGaji {
    private int idSlip;
    private int userId;
    private int aturanId;
    private int periodeBulan;
    private int periodeTahun;
    private double nominalGajiPokok;
    private double totalBonus;
    private double totalPotongan;
    private double kustomisasiGaji; // Tambahan/potongan manual dari Owner
    private double gajiBersih;
    private String statusApproval;  // "Draft_HR", "Approved_Owner", "Ditransfer"

    public SlipGaji() {}

    public SlipGaji(int idSlip, int userId, int aturanId, int periodeBulan, int periodeTahun, double nominalGajiPokok, double totalBonus, double totalPotongan, double kustomisasiGaji, double gajiBersih, String statusApproval) {
        this.idSlip = idSlip;
        this.userId = userId;
        this.aturanId = aturanId;
        this.periodeBulan = periodeBulan;
        this.periodeTahun = periodeTahun;
        this.nominalGajiPokok = nominalGajiPokok;
        this.totalBonus = totalBonus;
        this.totalPotongan = totalPotongan;
        this.kustomisasiGaji = kustomisasiGaji;
        this.gajiBersih = gajiBersih;
        this.statusApproval = statusApproval;
    }

    // --- GETTER & SETTER ---
    public int getIdSlip() { return idSlip; }
    public void setIdSlip(int idSlip) { this.idSlip = idSlip; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getAturanId() { return aturanId; }
    public void setAturanId(int aturanId) { this.aturanId = aturanId; }

    public int getPeriodeBulan() { return periodeBulan; }
    public void setPeriodeBulan(int periodeBulan) { this.periodeBulan = periodeBulan; }

    public int getPeriodeTahun() { return periodeTahun; }
    public void setPeriodeTahun(int periodeTahun) { this.periodeTahun = periodeTahun; }

    public double getNominalGajiPokok() { return nominalGajiPokok; }
    public void setNominalGajiPokok(double nominalGajiPokok) { this.nominalGajiPokok = nominalGajiPokok; }

    public double getTotalBonus() { return totalBonus; }
    public void setTotalBonus(double totalBonus) { this.totalBonus = totalBonus; }

    public double getTotalPotongan() { return totalPotongan; }
    public void setTotalPotongan(double totalPotongan) { this.totalPotongan = totalPotongan; }

    public double getKustomisasiGaji() { return kustomisasiGaji; }
    public void setKustomisasiGaji(double kustomisasiGaji) { this.kustomisasiGaji = kustomisasiGaji; }

    public double getGajiBersih() { return gajiBersih; }
    public void setGajiBersih(double gajiBersih) { this.gajiBersih = gajiBersih; }

    public String getStatusApproval() { return statusApproval; }
    public void setStatusApproval(String statusApproval) { this.statusApproval = statusApproval; }
}