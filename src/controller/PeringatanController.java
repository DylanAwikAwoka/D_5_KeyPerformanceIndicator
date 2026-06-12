/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author hp
 */
import dao.PeringatanDAOImpl;
import exception.InputKosongException;
import java.util.List;

public class PeringatanController {
    private PeringatanDAOImpl dao = new PeringatanDAOImpl();

    public void kirimPeringatan(int managerId, String pesan) throws InputKosongException {
        // Guard clause: isi pesan wajib diisi sebelum dikirim ke DAO.
        if (pesan == null || pesan.trim().isEmpty()) {
            throw new InputKosongException("GAGAL: Isi pesan peringatan wajib diisi!");
        }
        dao.kirimPeringatan(managerId, pesan);
    }

    public List<String> getPeringatan(int managerId) {
        return dao.getAllPeringatanByManager(managerId);
    }
}
