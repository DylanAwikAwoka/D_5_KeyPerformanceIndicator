package dao;

import model.User;

/**
 * Kontrak untuk proses autentikasi (login).
 * Controller cukup mengenal interface ini, bukan implementasi SQL-nya
 * (Loose Coupling / Dependency Inversion).
 */
public interface IAuthDAO {

    /**
     * Memverifikasi kredensial dan mengembalikan objek User konkret
     * (Manager / Karyawan) bila berhasil.
     *
     * @param username username yang dimasukkan.
     * @param password password yang dimasukkan (plaintext untuk saat ini).
     * @return objek User yang sesuai bila valid & aktif; null bila gagal.
     */
    User login(String username, String password);
}
