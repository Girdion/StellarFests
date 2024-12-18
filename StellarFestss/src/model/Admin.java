package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import util.Database;

public class Admin extends User{
	
	private Database connect;

	public Admin() {
		connect = Database.getInstance();
	}
	
	 public Vector<EventOrganizer> viewAllEvents() {
	        Vector<EventOrganizer> events = new Vector<>();
	        String query = "SELECT * FROM event"; // Get all events, regardless of organizer
	        try (PreparedStatement ps = connect.prepareStatement(query)) {
	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	                String eventID = rs.getString("event_id");
	                String eventName = rs.getString("event_name");
	                String eventDate = rs.getString("event_date");
	                String eventLocation = rs.getString("event_location");
	                String eventDescription = rs.getString("event_description");
	                String organizerID = rs.getString("organizer_id");
	                events.add(new EventOrganizer(eventID, eventName, eventDate, eventLocation, eventDescription, organizerID));
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return events;
	    }
    
    public Vector<User> viewAllUsers() {
        Vector<User> users = new Vector<>();
        String query = "SELECT * FROM users"; // Get all users from the users table
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String userID = rs.getString("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String role = rs.getString("role");
                // Create a new User object for each record
                users.add(new User(userID, name, null, email, role));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    public String deleteUser(String userID) {
        // Step 1: Delete user-related data from other tables (if applicable)
        try {
            // Example: Delete events associated with the user if they exist
            String deleteEventsQuery = "DELETE FROM event WHERE organizer_id = ?";
            try (PreparedStatement ps1 = connect.prepareStatement(deleteEventsQuery)) {
                ps1.setString(1, userID);
                ps1.executeUpdate();
            }

            // Example: Delete transactions associated with the user if applicable
            // Add any other related tables here (e.g., event_guests, event_vendors, etc.)

            // Step 2: Delete user from the users table
            String deleteUserQuery = "DELETE FROM users WHERE id = ?";
            try (PreparedStatement ps2 = connect.prepareStatement(deleteUserQuery)) {
                ps2.setString(1, userID);
                int rowsAffected = ps2.executeUpdate();

                if (rowsAffected > 0) {
                    return "User deleted successfully.";
                } else {
                    return "User not found.";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error deleting user: " + e.getMessage();
        }
    }
    
    public String deleteEvent(String eventID) {
        try {
            // Step 1: Delete related records if necessary (e.g., event_guests, event_vendors, etc.)
            String deleteRelatedQuery = "DELETE FROM event_guests WHERE event_id = ?";
            try (PreparedStatement ps1 = connect.prepareStatement(deleteRelatedQuery)) {
                ps1.setString(1, eventID);
                ps1.executeUpdate();
            }

            // Step 2: Delete the event from the event table
            String deleteEventQuery = "DELETE FROM event WHERE event_id = ?";
            try (PreparedStatement ps2 = connect.prepareStatement(deleteEventQuery)) {
                ps2.setString(1, eventID);
                int rowsAffected = ps2.executeUpdate();

                if (rowsAffected > 0) {
                    return "Event deleted successfully.";
                } else {
                    return "Event not found.";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error deleting event: " + e.getMessage();
        }
    }


}
