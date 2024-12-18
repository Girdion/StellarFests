package controller;

import java.sql.Connection;
import java.util.Vector;

import model.Vendor;
import util.Database;

public class VendorController {

	private Vendor vendorModel = new Vendor();

	// Add product
    public String manageVendor(String description, String productName, String vendorID) {
        return vendorModel.manageVendor(description, productName, vendorID);
    }

	// View All Products
	public Vector<String> viewProducts(int vendorID) {
		return vendorModel.viewProducts(vendorID);
	}

	// View Product Details
//	public String viewProductDetails(String productID) {
//		return vendorModel.viewProductDetails(productID);
//	}

	// Check Create Product Input
	public String checkManageVendorInput(String productName, String description) {
		return vendorModel.checkManageVendorInput(productName, description);
	}

	// View invitations
	public Vector<String> getInvitations(int userID) {
		return vendorModel.viewInvitations(userID);
	}

	// Accept invitations
	public String acceptInvitation(String eventID, String userID) {
		return vendorModel.acceptInvitation(eventID, userID);
	}
	
	 // View accepted invitations
    public Vector<String> getAcceptedInvitations(int userID) {
        return vendorModel.viewAcceptedInvitations(userID);
    }
}
