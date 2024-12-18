package view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.Vector;

import controller.PageController;
import controller.VendorController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Invitation;
import model.User;
import util.UserSession;

public class ViewInvitationPage implements EventHandler<ActionEvent> {
	private Scene scene;
	private Stage stage;
	private VBox container;
	private Label titleLbl, userGreetingLbl;
	private TableView<Invitation> invitationTable;
	private TextField eventIdField;
	private Button acceptBtn;
	private Button backBtn;


	private PageController pageController;
	private VendorController vendorController;

	private UserSession userSession;

	public ViewInvitationPage(Stage stage, PageController pageController, VendorController vendorController) {
		this.stage = stage;
		this.pageController = pageController;
		this.vendorController = vendorController;
		initializeComponents();
		layoutComponents();
	}

	private void layoutComponents() {
		container = new VBox(10);
		container.setPadding(new Insets(20));
		container.getChildren().addAll(titleLbl, userGreetingLbl, invitationTable, eventIdField, acceptBtn, backBtn);


		scene = new Scene(container, 600, 400);
	}

	private void initializeComponents() {
		titleLbl = new Label("Invitations");
		titleLbl.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

		String userName = UserSession.getLoggedInUser().getUsername();
		userGreetingLbl = new Label("Hello, " + userName + "! Here are your invitations:");

		invitationTable = new TableView<>();
		TableColumn<Invitation, String> invitationIdCol = new TableColumn<>("Invitation ID");
		invitationIdCol.setCellValueFactory(new PropertyValueFactory<>("invitation_id"));

		TableColumn<Invitation, String> eventIdCol = new TableColumn<>("Event ID");
		eventIdCol.setCellValueFactory(new PropertyValueFactory<>("event_id"));

		TableColumn<Invitation, String> statusCol = new TableColumn<>("Status");
		statusCol.setCellValueFactory(new PropertyValueFactory<>("invitation_status"));

		TableColumn<Invitation, String> roleCol = new TableColumn<>("Role");
		roleCol.setCellValueFactory(new PropertyValueFactory<>("invitation_role"));

		invitationTable.getColumns().addAll(invitationIdCol, eventIdCol, statusCol, roleCol);
		invitationTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		eventIdField = new TextField();
		eventIdField.setPromptText("Enter Event ID to Accept Invitation");

		acceptBtn = new Button("Accept");
		acceptBtn.setOnAction(event -> acceptInvitationById());


		// Load data into the table
		loadInvitations();

		// Back button
		backBtn = new Button("Back");
	    backBtn.setOnAction(e -> pageController.navigateBack());
	}

	private void acceptInvitationById() {
		String eventId = eventIdField.getText().trim();

		if (eventId.isEmpty()) {
			System.out.println("Please enter a valid Event ID.");
			return;
		}
		User currentUser = UserSession.getLoggedInUser();
		String userId = currentUser.getUser_id();

		String success = vendorController.acceptInvitation(eventId, userId);
		
		loadInvitations(); // Refresh table
		eventIdField.clear(); // Clear input field
	}


//	private void loadInvitations() {
//		User currentUser = UserSession.getLoggedInUser();
//
//		int userID = Integer.parseInt(currentUser.getUser_id());
//		String userId = currentUser.getUser_id();
//		Vector<String> invitations = vendorController.getInvitations(userID);
//
//		for (String invitationDetails : invitations) {
//			String[] parts = invitationDetails.split(", ");
//			String invitationId = parts[0].split(": ")[1];
//			String eventId = parts[1].split(": ")[1];
//			String status = parts[2].split(": ")[1];
//			String role = parts[3].split(": ")[1];
//
//			Invitation invitation = new Invitation(invitationId, eventId, userId, status, role);
//			invitationTable.getItems().add(invitation);
//		}
//	}
	
	private void loadInvitations() {
	    User currentUser = UserSession.getLoggedInUser();
	    int userID = Integer.parseInt(currentUser.getUser_id());

	    // Convert userID to String before passing it to Invitation constructor
	    String strUserID = String.valueOf(userID);

	    // Retrieve invitations from the database using the vendorController
	    Vector<String> invitations = vendorController.getInvitations(userID);

	    for (String invitationDetails : invitations) {
	        try {
	            String[] parts = invitationDetails.split(", ");
	            if (parts.length >= 4) {
	                String invitationId = parts[0].trim();
	                String eventId = parts[1].trim();
	                String status = parts[2].trim();
	                String role = parts[3].trim();

	                // Pass strUserID as the userId (which is now a String) to the Invitation constructor
	                Invitation invitation = new Invitation(invitationId, eventId, strUserID, status, role);
	                invitationTable.getItems().add(invitation);
	            } else {
	                System.err.println("Unexpected format for invitation details: " + invitationDetails);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.err.println("Error parsing invitation details: " + invitationDetails);
	        }
	    }
	}




	@Override
	public void handle(ActionEvent event) {
//		if (event.getSource() == backBtn) {
//			pageController.navigateToVendor();
//		}
	}

	public Scene getScene() {
		return scene;
	}
}
