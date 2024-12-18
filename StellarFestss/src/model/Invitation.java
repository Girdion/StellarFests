	package model;
	
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	
	import util.Database;
	import util.UserSession;
	
	public class Invitation {
	    private String invitation_id;
	    private String event_id;
	    private String user_id;  // Keep as String
	    private String invitation_status;
	    private String invitation_role;
	
	    private Database connect;
	
	    public Invitation() {
	        connect = Database.getInstance();
	        if (connect == null) {
	            System.err.println("Database connection is not established.");
	        }
	    }
	
	    public Invitation(String invitation_id, String event_id, String user_id, String invitation_status, String invitation_role) {
	        this.invitation_id = invitation_id;
	        this.event_id = event_id;
	        this.user_id = user_id;
	        this.invitation_status = invitation_status;
	        this.invitation_role = invitation_role;
	    }
	
	    public void sendInvitation(String email) {
	        // Implement sending invitation logic here
	        // You can add additional checks and logging
	    }
	
	    public String acceptInvitation(String eventID) {
	        if (connect == null) {
	            return "Database connection is null";
	        }
	
	        String query = "UPDATE invitation SET invitation_status = 'Accepted' WHERE event_id = ?";
	
	        try (PreparedStatement ps = connect.prepareStatement(query)) {
	            ps.setString(1, eventID);
	            ps.executeUpdate();
	            return "Event Accepted";
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return "Acceptance failed: Database error";
	        }
	    }
	
	    public Invitation getInvitations(String email) {
	        String query = "SELECT * FROM invitation WHERE user_id = ? AND invitation_status = 'Pending'";
	        User currentUser = UserSession.getLoggedInUser();
	
	        if (currentUser == null) {
	            System.err.println("No logged in user found.");
	            return null;
	        }
	
	        String userId = currentUser.getUser_id();
	        try (PreparedStatement ps = connect.prepareStatement(query)) {
	            ps.setString(1, userId);  // Keep it as String when passing to the SQL query
	            ResultSet rs = ps.executeQuery();
	            if (rs.next()) {
	                return new Invitation(
	                        rs.getString("invitation_id"),
	                        rs.getString("event_id"),
	                        rs.getString("user_id"),  // Keep user_id as String in the object
	                        rs.getString("invitation_status"),
	                        rs.getString("invitation_role")
	                );
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	
	        return null;
	    }
	
	    public boolean save() {
	        // Check for null or invalid fields
	        if (this.invitation_id == null || this.event_id == null || this.user_id == null || this.invitation_status == null || this.invitation_role == null) {
	            System.err.println("Missing fields: invitation_id, event_id, user_id, invitation_status, or invitation_role.");
	            return false;
	        }

	        // Check if database connection is valid
	        if (connect == null) {
	            System.err.println("Database connection is null");
	            return false;
	        }

	        // Log the values being set for the query
	        System.out.println("Setting parameters for query: ");
	        System.out.println("Invitation ID: " + this.invitation_id);
	        System.out.println("Event ID: " + this.event_id);
	        System.out.println("User ID: " + this.user_id); // Log the user_id as String
	        System.out.println("Status: " + this.invitation_status);
	        System.out.println("Role: " + this.invitation_role);

	        // Prepare and execute the query
	        String query = "INSERT INTO invitation (invitation_id, event_id, user_id, invitation_status, invitation_role) VALUES (?, ?, ?, ?, ?)";
	        try (PreparedStatement ps = connect.prepareStatement(query)) {
	            ps.setString(1, this.invitation_id);  // Assuming invitation_id is a String
	            ps.setString(2, this.event_id);
	            ps.setString(3, this.user_id);  // Keep as String for user_id
	            ps.setString(4, this.invitation_status);
	            ps.setString(5, this.invitation_role);

	            // Execute the query and check if the insertion was successful
	            int rowsAffected = ps.executeUpdate();
	            return rowsAffected > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	
	    // Getter and setter methods with user_id as String
	    public String getInvitation_id() {
	        return invitation_id;
	    }
	
	    public void setInvitation_id(String invitation_id) {
	        this.invitation_id = invitation_id;
	    }
	
	    public String getEvent_id() {
	        return event_id;
	    }
	
	    public void setEvent_id(String event_id) {
	        this.event_id = event_id;
	    }
	
	    public String getUser_id() {
	        return user_id;  // Return user_id as String
	    }
	
	    public void setUser_id(String user_id) {  // Keep user_id as String
	        this.user_id = user_id;
	    }
	
	    public String getInvitation_status() {
	        return invitation_status;
	    }
	
	    public void setInvitation_status(String invitation_status) {
	        this.invitation_status = invitation_status;
	    }
	
	    public String getInvitation_role() {
	        return invitation_role;
	    }
	
	    public void setInvitation_role(String invitation_role) {
	        this.invitation_role = invitation_role;
	    }
	}
