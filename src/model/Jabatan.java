package model;

/**
 * Model entitas Jabatan (posisi/role karyawan beserta gaji dasarnya).
 */
public class Jabatan {

    private int idJabatan;
    private String namaJabatan;
    private double gajiDasar;

    public Jabatan() {
    }

    public Jabatan(int idJabatan, String namaJabatan, double gajiDasar) {
        this.idJabatan = idJabatan;
        this.namaJabatan = namaJabatan;
        this.gajiDasar = gajiDasar;
    }

    public int getIdJabatan() {
        return idJabatan;
    }

    public void setIdJabatan(int idJabatan) {
        this.idJabatan = idJabatan;
    }

    public String getNamaJabatan() {
        return namaJabatan;
    }

    public void setNamaJabatan(String namaJabatan) {
        this.namaJabatan = namaJabatan;
    }

    public double getGajiDasar() {
        return gajiDasar;
    }

    public void setGajiDasar(double gajiDasar) {
        this.gajiDasar = gajiDasar;
    }

    @Override
    public String toString() {
        return namaJabatan;
    }
}
