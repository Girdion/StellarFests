package util;

import model.User;

public class UserSession {
    private static User loggedInUser;

    // Prevents instantiation of the class
    private UserSession() {}

    // Set the logged-in user
    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    // Get the logged-in user
    public static User getLoggedInUser() {
        return loggedInUser;
    }
}

