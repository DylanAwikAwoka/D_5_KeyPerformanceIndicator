package dao;


import java.util.List;
import model.User;

public interface IUserDAO {
    // Fungsi CREATE
    void insertUser(User user);
    
    // Fungsi READ
    List<User> getAllUsers();
    
    // Fungsi UPDATE
    void updateUser(User user);
    
    // Fungsi DELETE
    void deleteUser(int idUser);
}