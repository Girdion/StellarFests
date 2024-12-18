package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.Database;

public class Guest {
	private String accepted_invitations;

	private Database connect;

	public Guest() {
		connect = Database.getInstance();
	}

	public Guest(String accepted_invitations) {
		this.accepted_invitations = accepted_invitations;
	}

	public boolean acceptInvitation(String invitationId) {
		String query = "UPDATE invitation SET invitation_status = 'Accepted' WHERE invitation_id = ?";

		try (PreparedStatement ps = connect.prepareStatement(query)) {

			ps.setString(1, invitationId);
			ps.executeUpdate();
			System.out.println("Invitation accepted.");
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// View Invitations (Pending)
		public List<Invitation> viewPendingInvitations(String userId) {
			List<Invitation> invitations = new ArrayList<>();
			String query = "SELECT * FROM invitation WHERE user_id = ? AND invitation_status = 'Pending'";

			try (PreparedStatement ps = connect.prepareStatement(query)) {
				ps.setString(1, userId);
				ResultSet rs = ps.executeQuery();

				while (rs.next()) {
					invitations.add(new Invitation(
							rs.getString("invitation_id"),
							rs.getString("event_id"),
							rs.getString("user_id"),
							rs.getString("invitation_status"),
							rs.getString("invitation_role")
							));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return invitations;
	}

	// View Accepted Events
	public List<String> viewAcceptedEvents(String userId) {
		List<String> acceptedEvents = new ArrayList<>();
		String query = "SELECT e.event_name FROM events e "
				+ "INNER JOIN invitation i ON e.event_id = i.event_id "
				+ "WHERE i.user_id = ? AND i.invitation_status = 'Accepted'";

		try (PreparedStatement ps = connect.prepareStatement(query)) {
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				acceptedEvents.add(rs.getString("event_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return acceptedEvents;
	}

	// View Accepted Event Details
	public List<String> viewAcceptedEventDetails(String userId, String eventId) {
		List<String> eventDetails = new ArrayList<>();
		String query = "SELECT e.event_name, e.event_date, e.event_location, e.event_description "
				+ "FROM events e "
				+ "INNER JOIN invitation i ON e.event_id = i.event_id "
				+ "WHERE i.user_id = ? AND i.event_id = ? AND i.invitation_status = 'Accepted'";

		try (PreparedStatement ps = connect.prepareStatement(query)) {
			ps.setString(1, userId);
			ps.setString(2, eventId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				eventDetails.add("Event Name: " + rs.getString("event_name"));
				eventDetails.add("Event Date: " + rs.getString("event_date"));
				eventDetails.add("Event Location: " + rs.getString("event_location"));
				eventDetails.add("Event Description: " + rs.getString("event_description"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return eventDetails;
	}
}
