package controller;

import java.util.List;

import model.Guest;
import model.Invitation;

public class GuestController {
	private Guest guestModel;

	public GuestController() {
		guestModel = new Guest();
	}

	// Accept invitation
	public boolean acceptInvitation(String invitationId) {
		return guestModel.acceptInvitation(invitationId);
	}

	// Show pending invitations
	public List<Invitation> getPendingInvitations(String userId) {
		return guestModel.viewPendingInvitations(userId);
	}

	// Show accepted events
	public List<String> getAcceptedEvents(String userId) {
		return guestModel.viewAcceptedEvents(userId);
	}

	// Show accepted event details
	public List<String> getAcceptedEventDetails(String userId, String eventId) {
		return guestModel.viewAcceptedEventDetails(userId, eventId);
	}

}
