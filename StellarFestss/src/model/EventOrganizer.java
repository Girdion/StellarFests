package model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;
import java.util.Vector;
import util.Database;

public class EventOrganizer extends User{
    private String event_id;
    private String event_name;
    private String event_date;
    private String event_location;
    private String event_description;
    private String organizer_id; // Foreign key to User ID

    private Database connect;

    public EventOrganizer() {
        connect = Database.getInstance();
    }

    public EventOrganizer(String event_id, String event_name, String event_date, String event_location, String event_description, String organizer_id) {
        this.event_id = event_id;
        this.event_name = event_name;
        this.event_date = event_date;
        this.event_location = event_location;
        this.event_description = event_description;
        this.organizer_id = organizer_id;
    }

    // 1. Create Event
    public String createEvent(String eventName, String date, String location, String description, String organizerID) {
        int nextId = getNextEventId();
        if (nextId == -1) {
            return "Error generating event ID";
        }

        String eventId = String.valueOf(nextId); // Convert to string for VARCHAR compatibility

        String query = "INSERT INTO event (event_id, event_name, event_date, event_location, event_description, organizer_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setString(1, eventId);
            ps.setString(2, eventName);
            ps.setString(3, date);
            ps.setString(4, location);
            ps.setString(5, description);
            ps.setString(6, organizerID);
            ps.executeUpdate();
            return "Event created successfully with ID: " + eventId;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Event creation failed: Database error";
        }
    }


    private int getNextEventId() {
        String query = "SELECT COALESCE(MAX(CAST(event_id AS UNSIGNED)), 0) AS max_id FROM event";
        try (PreparedStatement ps = connect.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("max_id") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1; // Start from 1 if no records exist
    }




    // 2. View Organized Events
    public Vector<EventOrganizer> viewOrganizedEvent(String userID) {
        Vector<EventOrganizer> events = new Vector<>();
        String query = "SELECT * FROM event WHERE organizer_id = ?";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setString(1, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String eventID = rs.getString("event_id");
                String eventName = rs.getString("event_name");
                String eventDate = rs.getString("event_date");
                String eventLocation = rs.getString("event_location");
                String eventDescription = rs.getString("event_description");
                events.add(new EventOrganizer(eventID, eventName, eventDate, eventLocation, eventDescription, userID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }



    // 3. View Event Details
    public EventOrganizer viewOrganizedEventDetails(String eventID) {
        String query = "SELECT * FROM event WHERE event_id = ?";
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

    // 4. Get Guests
    public Vector<User> getGuests() {
        Vector<User> guests = new Vector<>();
        String query = "SELECT * FROM users WHERE role = 'Guest'";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String userID = rs.getString("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String role = rs.getString("role");
                guests.add(new User(userID, name, null, email, role));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return guests;
    }

    // 5. Get Vendors
    public Vector<User> getVendors() {
        Vector<User> vendors = new Vector<>();
        String query = "SELECT * FROM users WHERE role = 'Vendor'";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String userID = rs.getString("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String role = rs.getString("role");
                vendors.add(new User(userID, name, null, email, role));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vendors;
    }

    // 6. Get Guests by Transaction ID
    public Vector<User> getGuestsByTransactionID(String eventID) {
        Vector<User> guests = new Vector<>();
        String query = "SELECT u.* FROM users u JOIN event_guests eg ON u.id = eg.user_id WHERE eg.event_id = ?";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setString(1, eventID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String userID = rs.getString("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String role = rs.getString("role");
                guests.add(new User(userID, name, null, email, role));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return guests;
    }

    // 7. Get Vendors by Transaction ID
    public Vector<User> getVendorsByTransactionID(String eventID) {
        Vector<User> vendors = new Vector<>();
        String query = "SELECT u.* FROM users u JOIN event_vendors ev ON u.id = ev.user_id WHERE ev.event_id = ?";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setString(1, eventID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String userID = rs.getString("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String role = rs.getString("role");
                vendors.add(new User(userID, name, null, email, role));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vendors;
    }

    // 8. Check Create Event Input
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

    // 9. Check Add Vendor Input
    public String checkAddVendorInput(String vendorID) {
        if (vendorID == null || vendorID.trim().isEmpty()) {
            return "Vendor ID cannot be empty";
        }
        return "Validation successful";
    }

    // 10. Check Add Guest Input
    public String checkAddGuestInput(String guestID) {
        if (guestID == null || guestID.trim().isEmpty()) {
            return "Guest ID cannot be empty";
        }
        return "Validation successful";
    }

    // 11. Edit Event Name
    public String editEventName(String eventID, String eventName) {
        if (eventName == null || eventName.trim().isEmpty()) {
            return "Event Name cannot be empty";
        }

        String query = "UPDATE events SET event_name = ? WHERE event_id = ?";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setString(1, eventName);
            ps.setString(2, eventID);
            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                return "Event name updated successfully";
            } else {
                return "Update failed: Event not found";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Update failed: Database error";
        }
    }

    // Getters for EventOrganizer fields
    public String getEvent_id() {
        return event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getEvent_date() {
        return event_date;
    }

    public String getEvent_location() {
        return event_location;
    }

    public String getEvent_description() {
        return event_description;
    }

    public String getOrganizer_id() {
        return organizer_id;
    }

 // 12. View All Events (not just organized by the user)
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

 // 13. Send Vendor Invitation
    public String sendVendorInvitation(String vendorID, String eventID) {
        // 1. Check if the event exists
        String eventQuery = "SELECT * FROM event WHERE event_id = ?";
        try (PreparedStatement ps = connect.prepareStatement(eventQuery)) {
            ps.setString(1, eventID);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return "Event not found";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error while checking event";
        }

        // 2. Check if the vendor is already invited (check the invitation table)
        String checkVendorQuery = "SELECT * FROM invitation WHERE event_id = ? AND user_id = ?";
        try (PreparedStatement ps = connect.prepareStatement(checkVendorQuery)) {
            ps.setString(1, eventID);
            ps.setString(2, vendorID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return "Vendor is already invited to this event";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error while checking vendor invitation status";
        }

        // 3. Insert vendor invitation into invitation table
        String insertInvitationQuery = "INSERT INTO invitation (invitation_id, event_id, user_id, invitation_status, invitation_role) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connect.prepareStatement(insertInvitationQuery)) {
            String invitationID = UUID.randomUUID().toString();  // Generate a unique invitation ID
            String invitationStatus = "Pending";  // Default status is "Pending"
            String invitationRole = "Vendor";    // Assuming the role is "Vendor"

            ps.setString(1, invitationID);
            ps.setString(2, eventID);
            ps.setString(3, vendorID);
            ps.setString(4, invitationStatus);
            ps.setString(5, invitationRole);
            ps.executeUpdate();

            return "Invitation sent successfully to vendor " + vendorID;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error while sending invitation";
        }
    }
    
    public boolean updateEvent() {
        String query = "UPDATE event SET event_name = ? WHERE event_id = ?";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setString(1, this.event_name);
            ps.setString(2, this.event_id);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    
}
