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
import java.util.List;

public class PeringatanController {
    private PeringatanDAOImpl dao = new PeringatanDAOImpl();

    public void kirimPeringatan(int managerId, String pesan) {
        dao.kirimPeringatan(managerId, pesan);
    }

    public List<String> getPeringatan(int managerId) {
        return dao.getAllPeringatanByManager(managerId);
    }
}
