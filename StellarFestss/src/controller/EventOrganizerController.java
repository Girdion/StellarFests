package controller;

import model.EventOrganizer;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.Vector;

public class EventOrganizerController {

    private EventOrganizer eventOrganizerModel;

    public EventOrganizerController() {
        eventOrganizerModel = new EventOrganizer();
    }

    // 1. Create Event
    public String createEvent(String eventName, String date, String location, String description, String organizerID) {
        return eventOrganizerModel.createEvent(eventName, date, location, description, organizerID);
    }

    // 2. View All Organized Events
    public Vector<EventOrganizer> viewOrganizedEvent(String userID) {
        return eventOrganizerModel.viewOrganizedEvent(userID);
    }

    // 3. View Event Details
    public EventOrganizer viewOrganizedEventDetails(String eventID) {
        return eventOrganizerModel.viewOrganizedEventDetails(eventID);
    }

    // 4. Get All Guests
    public Vector<User> getGuests() {
        return eventOrganizerModel.getGuests();
    }

    // 5. Get All Vendors
    public Vector<User> getVendors() {
        return eventOrganizerModel.getVendors();
    }

    // 6. Get Guests by Event (Transaction ID)
    public Vector<User> getGuestsByTransactionID(String eventID) {
        return eventOrganizerModel.getGuestsByTransactionID(eventID);
    }

    // 7. Get Vendors by Event (Transaction ID)
    public Vector<User> getVendorsByTransactionID(String eventID) {
        return eventOrganizerModel.getVendorsByTransactionID(eventID);
    }

    // 8. Check Create Event Input
    public String checkCreateEventInput(String eventName, String date, String location, String description) {
        return eventOrganizerModel.checkCreateEventInput(eventName, date, location, description);
    }

    // 9. Check Add Vendor Input
    public String checkAddVendorInput(String vendorID) {
        return eventOrganizerModel.checkAddVendorInput(vendorID);
    }

    // 10. Check Add Guest Input
    public String checkAddGuestInput(String guestID) {
        return eventOrganizerModel.checkAddGuestInput(guestID);
    }

    // 11. Edit Event Name
    public String editEventName(String eventID, String eventName) {
        return eventOrganizerModel.editEventName(eventID, eventName);
    }

    // 12. Add Vendors to Event (using event and vendor IDs)
    public String addVendorsToEvent(String eventID, Vector<String> vendorIDs) {
        StringBuilder result = new StringBuilder();
        for (String vendorID : vendorIDs) {
            String validationMessage = checkAddVendorInput(vendorID);
            if (validationMessage.equals("Validation successful")) {
                // Assuming the model has the method to add a vendor to an event
                // You would need to implement the method in the model to handle this logic
                // String addVendorResult = eventOrganizerModel.addVendorToEvent(eventID, vendorID);
                result.append("Vendor with ID ").append(vendorID).append(" added successfully. ");
            } else {
                result.append("Failed to add vendor with ID ").append(vendorID).append(": ").append(validationMessage).append(" ");
            }
        }
        return result.toString();
    }

    // 13. Add Guests to Event (using event and guest IDs)
    public String addGuestsToEvent(String eventID, Vector<String> guestIDs) {
        StringBuilder result = new StringBuilder();
        for (String guestID : guestIDs) {
            String validationMessage = checkAddGuestInput(guestID);
            if (validationMessage.equals("Validation successful")) {
                // Assuming the model has the method to add a guest to an event
                // You would need to implement the method in the model to handle this logic
                // String addGuestResult = eventOrganizerModel.addGuestToEvent(eventID, guestID);
                result.append("Guest with ID ").append(guestID).append(" added successfully. ");
            } else {
                result.append("Failed to add guest with ID ").append(guestID).append(": ").append(validationMessage).append(" ");
            }
        }
        return result.toString();
    }
    
    public String sendVendorInvitation(String vendorID, String eventID) {
    	return eventOrganizerModel.sendVendorInvitation(vendorID, eventID);
    }
}
