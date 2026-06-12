package model;

/**
 * Model entitas Cabang (lokasi outlet/kantor cabang).
 */
public class Cabang {

    private int idCabang;
    private String namaCabang;
    private String lokasi;

    public Cabang() {
    }

    public Cabang(int idCabang, String namaCabang, String lokasi) {
        this.idCabang = idCabang;
        this.namaCabang = namaCabang;
        this.lokasi = lokasi;
    }

    public int getIdCabang() {
        return idCabang;
    }

    public void setIdCabang(int idCabang) {
        this.idCabang = idCabang;
    }

    public String getNamaCabang() {
        return namaCabang;
    }

    public void setNamaCabang(String namaCabang) {
        this.namaCabang = namaCabang;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    @Override
    public String toString() {
        return namaCabang;
    }
}
