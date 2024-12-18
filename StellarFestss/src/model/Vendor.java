package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import util.Database;
import util.UserSession;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Vendor extends User {
    private String product_id;
    private String product_name;
    private String product_description;
    private int vendor_id;
    private String accepted_invitations;
    private Database connect;

    private StringProperty vendorName = new SimpleStringProperty();
    private StringProperty productName = new SimpleStringProperty();
    private StringProperty productDescription = new SimpleStringProperty();

    public Vendor() {
        connect = Database.getInstance();
    }

    // Constructor to initialize Vendor with name and product name
    public Vendor(String vendorName, String productName) {
        this.vendorName.set(vendorName);
        this.productName.set(productName);
        connect = Database.getInstance();
    }

    // Getters and Setters for properties (JavaFX binding)
    public StringProperty nameProperty() {
        return vendorName;
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public StringProperty productDescriptionProperty() {
        return productDescription;
    }

    public String getVendorName() {
        return vendorName.get();
    }

    public String getProductName() {
        return productName.get();
    }

    public String getProductDescription() {
        return productDescription.get();
    }

    public void setVendorName(String vendorName) {
        this.vendorName.set(vendorName);
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public void setProductDescription(String productDescription) {
        this.productDescription.set(productDescription);
    }

    // View Invitations for a specific user ID
    public Vector<String> viewInvitations(int userID) {
        Vector<String> invitations = new Vector<>();
        String query =
                "SELECT invitation_id, event_id, invitation_status, invitation_role " +
                "FROM invitation " +
                "WHERE user_id = ? AND invitation_status = 'Pending'";

        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String invitationId = rs.getString("invitation_id");
                String eventId = rs.getString("event_id");
                String invitationStatus = rs.getString("invitation_status");
                String invitationRole = rs.getString("invitation_role");
                String invitationDetails = String.format(
                        "%s, %s, %s, %s",
                        invitationId, eventId, invitationStatus, invitationRole
                );
                invitations.add(invitationDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invitations;
    }

    // Accept an invitation for a specific event and user
    public String acceptInvitation(String eventID, String userID) {
        if (eventID == null || userID == null || eventID.isEmpty() || userID.isEmpty()) {
            return "Event ID or User ID cannot be empty.";
        }

        String query = "UPDATE invitation SET invitation_status = 'Accepted' WHERE event_id = ? AND user_id = ?";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setString(1, eventID);  // Assuming event_id is a string
            ps.setInt(2, Integer.parseInt(userID));  // Convert userID (String) to int if necessary
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return "Invitation accepted successfully.";
            } else {
                return "No invitation found for the provided Event ID and User ID.";
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Logging the error
            return "Error: Unable to accept invitation.";
        }
    }

    // View Accepted Invitations
    public Vector<String> viewAcceptedInvitations(int userID) {
        Vector<String> invitations = new Vector<>();

        String query = "SELECT e.event_name, e.event_date, e.event_location, e.event_description " +
                       "FROM invitation i " +
                       "INNER JOIN event e ON i.event_id = e.event_id " +
                       "WHERE i.user_id = ? AND i.invitation_status = 'Accepted'";

        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String eventName = rs.getString("event_name");
                String eventDate = rs.getString("event_date");
                String eventLocation = rs.getString("event_location");
                String eventDescription = rs.getString("event_description");

                String invitationDetails = String.format(
                        "%s, %s, %s, %s",
                        eventName, eventDate, eventLocation, eventDescription
                );

                invitations.add(invitationDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return invitations;
    }

    // Manage Vendor (Add Product)
    public String manageVendor(String description, String productName, String vendorID) {
        String validation = checkManageVendorInput(description, productName);
        if (!validation.equals("Validation successful")) {
            return validation;
        }

        User currentUser = UserSession.getLoggedInUser();

        String productId = UUID.randomUUID().toString();  // Use UUID for unique product_id

        int vendorId = Integer.parseInt(currentUser.getUser_id());  // Convert string user_id to int

        String query = "INSERT INTO product (product_id, product_name, product_description, vendor_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setString(1, productId);
            ps.setString(2, productName);
            ps.setString(3, description);
            ps.setInt(4, vendorId);
            ps.executeUpdate();
            return "Product added successfully.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: Unable to add product.";
        }
    }

    // Validate vendor product details
    public String checkManageVendorInput(String description, String productName) {
        if (productName == null || productName.isEmpty()) {
            return "Product name cannot be empty.";
        }
        if (description == null || description.isEmpty() || description.length() > 200) {
            return "Product description cannot be empty and must not exceed 200 characters.";
        }
        return "Validation successful";
    }

    // View Products for a specific vendor
    public Vector<String> viewProducts(int userID) {
        Vector<String> products = new Vector<>();
        String query = "SELECT product_name, product_description FROM product WHERE vendor_id = ?";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String productName = rs.getString("product_name");
                String productDescription = rs.getString("product_description");
                String productDetails = String.format("Name: %s, Description: %s", productName, productDescription);
                products.add(productDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    // View Product Details by Product ID
    public String viewProductDetails(String productID) {
        String query = "SELECT product_description FROM product WHERE product_id = ?";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setString(1, productID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("product_description");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private BooleanProperty selected = new SimpleBooleanProperty(false); // Add this line

    // Getters and Setters for selected property
    public BooleanProperty selectedProperty() {
        return selected;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }
    
    // Get Vendors by Role (i.e. Vendor)
    public Vector<Vendor> getVendorsByRole(String role) {
        Vector<Vendor> vendors = new Vector<>();
        String query = "SELECT * FROM user WHERE role = 'Vendor'";  // Query to fetch only vendors

        try (PreparedStatement ps = connect.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Assuming Vendor has a name and product associated with it
                String vendorName = rs.getString("name");
                String productName = rs.getString("product_name");  // Assuming product_name is in the user table or join with product table
                Vendor vendor = new Vendor(vendorName, productName);
                vendors.add(vendor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vendors;
    }

    // Check if a vendor has already been invited to an event
    public boolean hasBeenInvited(int userId, String eventId) {
        String query = "SELECT 1 FROM invitation WHERE user_id = ? AND event_id = ? LIMIT 1";

        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setInt(1, userId);  // Set the user ID as an integer
            ps.setString(2, eventId);  // Set the event ID as a string
            ResultSet rs = ps.executeQuery();

            return rs.next();  // Return true if a matching record exists
        } catch (SQLException e) {
            e.printStackTrace();  // Log or print the exception for debugging
        }

        return false;  // Return false if no record is found or an exception occurs
    }

    // Get all Vendors
    public List<Vendor> getAllVendors() {
        List<Vendor> vendors = new ArrayList<>();
        String query = "SELECT id, name, email FROM users WHERE role = 'Vendor'";

        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vendor vendor = new Vendor();
                vendor.setUser_id(String.valueOf(rs.getInt("id")));  // Convert int to String
                vendor.setName(rs.getString("name"));  // Set the vendor's name
                vendor.setEmail(rs.getString("email"));  // Set the vendor's email
                vendors.add(vendor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vendors;
    }
}
