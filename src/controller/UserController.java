/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author hp
 */
import dao.IUserDAO;
import dao.UserDAOImpl;
import exception.InputKosongException;
import java.util.List;
import model.Karyawan;
import model.Manager;
import model.User;

public class UserController {
    
    // Loose Coupling: Controller hanya mengenal Interface, bukan cara kerja SQL-nya
    private IUserDAO userDAO;

    public UserController() {
        this.userDAO = new UserDAOImpl();
    }

    /**
     * Method untuk memproses penambahan Karyawan dari Layar/GUI
     */
    public void tambahKaryawan(String username, String password, String nama, int cabangId, double gaji, String posisi) throws InputKosongException {
        // 1. Panggil validasi (akan melempar Exception jika kosong)
        validasiInputDasar(username, password, nama);

        // 2. Buat objek Karyawan (Role ID 4 = Karyawan)
        Karyawan karyawanBaru = new Karyawan(0, username, password, nama, true, 4, cabangId, gaji, posisi);
        
        // 3. Lempar ke DAO untuk disimpan ke MySQL
        userDAO.insertUser(karyawanBaru);
    }

    /**
     * Method untuk memproses penambahan Manager dari Layar/GUI
     */
    public void tambahManager(String username, String password, String nama, int cabangId, double gaji, String departemen) throws InputKosongException {
        // 1. Panggil validasi
        validasiInputDasar(username, password, nama);

        // 2. Buat objek Manager (Role ID 3 = Manager)
        Manager managerBaru = new Manager(0, username, password, nama, true, 3, cabangId, gaji, departemen);
        
        // 3. Lempar ke DAO untuk disimpan ke MySQL
        userDAO.insertUser(managerBaru);
    }

    /**
     * Method untuk mengambil semua data user untuk ditampilkan di JTable (Layar)
     */
    public List<User> ambilSemuaDataUser() {
        return userDAO.getAllUsers();
    }

    /**
     * Method untuk menghapus data user dari Layar/GUI
     */
    public void hapusUser(int idUser) {
        userDAO.deleteUser(idUser);
    }

    /**
     * PRIVATE HELPER: Memenuhi syarat rubrik (User Defined Exception)
     * Fungsi ini mengecek apakah ada input teks yang kosong
     */
    private void validasiInputDasar(String username, String password, String nama) throws InputKosongException {
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            nama == null || nama.trim().isEmpty()) {
            
            // Melempar Exception yang kita buat di package exception!
            throw new InputKosongException("GAGAL: Username, Password, dan Nama Lengkap wajib diisi!");
        }
    }
}