package org.example.service;

import javafx.scene.control.Alert;
import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.repo.custom.impl.UserRepoIMPL;

import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
//import org.example.util.PasswordUtils;


public class UserService {
    private UserRepoIMPL userRepoIMPL = new UserRepoIMPL();

    public UserService() {
    }

    public UserService(UserRepoIMPL userRepoIMPL) {
        this.userRepoIMPL = userRepoIMPL;
    }


    public String saveUser(UserDTO userDTO) {

        User user = convertDTOtoEntity(userDTO);
        try {
            userRepoIMPL.save(user);
            return "User Saved";
        } catch (SQLException e){
            if(e.getErrorCode()==1062){
                return "Duplicate User";
            };
        } catch (Exception e) {
            return "something went wrong";
        }
        return "error";
    }


//    Handles user login and conversion but should avoid returning null.
    public UserDTO loginUser(String username, String password) {
        try {
            User user = userRepoIMPL.login(username, password);
            if (user != null) {
                return convertEntityToDTO(user);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during login: " + e.getMessage());
        }
        return null;
    }


//    Handles fetching users, converting to DTOs, and exceptions but could improve error differentiation
    public List<UserDTO> getAllUsers() {
        try {
            List<User> all = userRepoIMPL.getAll();
            List<UserDTO> userDTOs = new ArrayList<>();

            if (all != null) {
                for (User user : all) {
                    userDTOs.add(convertEntityToDTO(user));
                }
                return userDTOs;
            } else {
                throw new RuntimeException("No users found");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Contact Develloper", e);
        }

    }

    public boolean deleteUser(int id) {
        try {
            return userRepoIMPL.delete(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public String updateUser(UserDTO userDTO) {
        try {
            userRepoIMPL.update(convertDTOtoEntity(userDTO));
            return "User Updated";
        } catch (SQLException e){
            System.out.printf(e.getMessage());
            if(e.getErrorCode()==1062){
                return "Duplicate User";
            };
        } catch (Exception e) {
            return "something went wrong";
        }
        return "error";
    }

    //convert DTO to Entity
    private User convertDTOtoEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user.setPassword(userDTO.getPassword());
        return user;
    }

    //convert entity to dto
    private UserDTO convertEntityToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                null // Exclude password in DTO
        );
    }
}
