package model;

public class AturanGaji {
    private int aturanId;
    private String namaAturan;
    private double persentaseBonus;
    private double potonganWajib;

    public AturanGaji() {
    }

    public AturanGaji(int aturanId, String namaAturan, double persentaseBonus, double potonganWajib) {
        this.aturanId = aturanId;
        this.namaAturan = namaAturan;
        this.persentaseBonus = persentaseBonus;
        this.potonganWajib = potonganWajib;
    }

    public int getAturanId() {
        return aturanId;
    }

    public void setAturanId(int aturanId) {
        this.aturanId = aturanId;
    }

    public String getNamaAturan() {
        return namaAturan;
    }

    public void setNamaAturan(String namaAturan) {
        this.namaAturan = namaAturan;
    }

    public double getPersentaseBonus() {
        return persentaseBonus;
    }

    public void setPersentaseBonus(double persentaseBonus) {
        this.persentaseBonus = persentaseBonus;
    }

    public double getPotonganWajib() {
        return potonganWajib;
    }

    public void setPotonganWajib(double potonganWajib) {
        this.potonganWajib = potonganWajib;
    }
}
