package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import util.Database;

public class Event {
    private String event_id;
    private String event_name;
    private String event_date;
    private String event_location;
    private String event_description;
    private String organizer_id; // Foreign key to User ID
    
    private Database connect;
    
    public Event() {
    	connect = Database.getInstance();
    }

    // Constructor
    public Event(String event_id, String event_name, String event_date, String event_location, String event_description, String organizer_id) {
        this.event_id = event_id;
        this.event_name = event_name;
        this.event_date = event_date;
        this.event_location = event_location;
        this.event_description = event_description;
        this.organizer_id = organizer_id;
    }
    
    public String createEvent(String eventName, String date, String location, String description, String organizerID) {
        String validationResult = checkCreateEventInput(eventName, date, location, description);
        if (!validationResult.equals("Validation successful")) {
            return validationResult;
        }

        // Generate a unique event ID using UUID
        String eventId = UUID.randomUUID().toString();  // This generates a unique string for event_id

        String query = "INSERT INTO event (event_id, event_name, event_date, event_location, event_description, organizer_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setString(1, eventId);  // Set the generated UUID as event_id
            ps.setString(2, eventName);
            ps.setString(3, date);
            ps.setString(4, location);
            ps.setString(5, description);
            ps.setString(6, organizerID);
            ps.executeUpdate();
            return "Event created successfully";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Event creation failed: Database error";
        }
    }
    
    public String checkCreateEventInput(String eventName, String date, String location, String description) {
        if (eventName == null || eventName.trim().isEmpty()) {
            return "Event Name cannot be empty";
        }
        if (date == null || date.trim().isEmpty()) {
            return "Event Date cannot be empty";
        }
        if (location == null || location.trim().isEmpty()) {
            return "Event Location cannot be empty";
        }
        if (location.length() < 5) {
            return "Event Location must be at least 5 characters";
        }
        if (description == null || description.trim().isEmpty()) {
            return "Event Description cannot be empty";
        }
        if (description.length() > 200) {
            return "Event Description cannot exceed 200 characters";
        }
        // Event Date must be in the future
        if (isPastDate(date)) {
            return "Event Date must be in the future";
        }
        return "Validation successful";
    }
    
    private boolean isPastDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Assuming the date format is yyyy-MM-dd
        try {
            // Parse the event date as a LocalDate
            LocalDate eventDate = LocalDate.parse(date, formatter);
            // Get the current date
            LocalDate currentDate = LocalDate.now();
            return eventDate.isBefore(currentDate); // Returns true if the event date is in the past
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return true; // Return true for invalid date format, indicating an error
        }
    }
    
    public EventOrganizer viewOrganizedEventDetails(String eventID) {
        String query = "SELECT * FROM events WHERE event_id = ?";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setString(1, eventID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String eventName = rs.getString("event_name");
                String eventDate = rs.getString("event_date");
                String eventLocation = rs.getString("event_location");
                String eventDescription = rs.getString("event_description");
                return new EventOrganizer(eventID, eventName, eventDate, eventLocation, eventDescription, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Getters and setters
    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getEvent_location() {
        return event_location;
    }

    public void setEvent_location(String event_location) {
        this.event_location = event_location;
    }

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public String getOrganizer_id() {
        return organizer_id;
    }

    public void setOrganizer_id(String organizer_id) {
        this.organizer_id = organizer_id;
    }

    @Override
    public String toString() {
        return event_name; // This is what will be shown in the ComboBox for event selection
    }
}
