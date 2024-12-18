package controller;

import model.User;
import util.Database;

import java.util.Vector;

public class UserController {

    private User userModel;
    private Database connect;
    
    public UserController() {
        userModel = new User();
    }

    // Get all users
    public Vector<User> getAllUsers() {
        return userModel.getAllUsers();
    }

    // Validate user credentials (login)
    public User validateUserCredentials(String username, String password) {
        return userModel.validateUserCredentials(username, password);
    }

    // Register a new user
    public void register(String username, String password, String email, String role) {
        String validationMessage = checkRegisterInput(username, password, email, role);

        if (validationMessage.equals("Validation successful")) {
            String result = userModel.register(username, password, email, role);
            System.out.println(result);
        } else {
            System.out.println("User validation failed: " + validationMessage);
        }
    }

    // Login user
    public String login(String username, String password) {
        return userModel.login(username, password);
    }

    // Validate user input for registration
    public String checkRegisterInput(String username, String password, String email, String role) {
        return userModel.checkRegisterInput(username, password, email, role);
    }

    // Validate user input for profile change
    public String checkChangeProfileInput(String email, String name, String oldPassword, String newPassword) {
        return userModel.checkChangeProfileInput(email, name, oldPassword, newPassword);
    }

    // Change user profile information
    public String changeProfile(String email, String name, String oldPassword, String newPassword) {
        String validationMessage = checkChangeProfileInput(email, name, oldPassword, newPassword);

        if (validationMessage.equals("Validation successful")) {
            return userModel.changeProfile(email, name, oldPassword, newPassword);
        } else {
            return "Profile update failed: " + validationMessage;
        }
    }

    // Get user by email
    public User getUserByEmail(String email) {
        return userModel.getUserByEmail(email);
    }

    // Get user by username
    public User getUserByUsername(String username) {
        return userModel.getUserByName(username);
    }
    
 
}
