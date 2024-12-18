package controller;

import model.Admin;
import model.EventOrganizer;
import model.User;

import java.util.Vector;

public class AdminController {

    private Admin adminModel;

    public AdminController() {
        adminModel = new Admin();
    }

    // Get all events
    public Vector<EventOrganizer> getAllEvents() {
        return adminModel.viewAllEvents();
    }

    // Get all users
    public Vector<User> getAllUsers() {
        return adminModel.viewAllUsers();
    }

    // Delete a user
    public String deleteUser(String userID) {
        return adminModel.deleteUser(userID);
    }
}
