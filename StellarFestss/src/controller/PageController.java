package controller;

import java.util.Stack;

import javafx.scene.Scene;
import javafx.stage.Stage;
import view.AddVendorPage;
import view.AdminPage;
import view.DeleteEventPage;
import view.DeleteUserPage;
import view.EditEventNamePage;
import view.EditProfilePage;
import view.EventOrganizerPage;
import view.GuestPage;
import view.LoginPage;
import view.VendorPage;
import view.ViewAccEventsPage;
import view.ViewAllEventsPage;
import view.ViewAllUsersPage;
import view.ViewEventDetailsPage;
import view.ViewEventsPage;
import view.ViewInvitationPage;
import view.ViewProductPage;
import model.Event;
import model.EventOrganizer;

public class PageController {

	private Stage primaryStage;  // Reference to the main stage
	private Scene currentScene;  // Reference to the current scene
	private Stack<Scene> navigationStack = new Stack<>();


	public PageController(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	// Navigate to Login Page
	public void navigateToLogin() {
		UserController userController = new UserController();  // Instantiate UserController
		LoginPage loginPage = new LoginPage(primaryStage, this, userController);  // Pass it to LoginPage constructor
		currentScene = loginPage.getScene();
		primaryStage.setScene(currentScene);
		primaryStage.setTitle("Login Page");
		primaryStage.show();
        pushAndNavigate(loginPage.getScene());

	}


	// Navigate to Admin Page
	public void navigateToAdmin() {
		AdminPage adminPage = new AdminPage(primaryStage, this, null);
		currentScene = adminPage.getScene();  // Assuming AdminPage has a getScene() method
		primaryStage.setScene(currentScene);
		primaryStage.setTitle("Admin Dashboard");
		primaryStage.show();
        pushAndNavigate(adminPage.getScene());

	}

	// Navigate to Guest Page
	public void navigateToGuest() {
		GuestController GuestController = new GuestController();
		GuestPage guestPage = new GuestPage(primaryStage, this, GuestController);
		currentScene = guestPage.getScene();
		primaryStage.setScene(currentScene);
		primaryStage.setTitle("Guest Dashboard");
		primaryStage.show();
        pushAndNavigate(guestPage.getScene());

	}

	// Navigate to Event Organizer Page
	public void navigateToEventOrganizer() {
		EventOrganizerController eventController = new EventOrganizerController();
		EventOrganizerPage organizerPage = new EventOrganizerPage(primaryStage, this, eventController);
		currentScene = organizerPage.getScene();
		primaryStage.setScene(currentScene);
		primaryStage.setTitle("Event Organizer Dashboard");
		primaryStage.show();
        pushAndNavigate(organizerPage.getScene());

	}

	// Navigate to Vendor Page
	public void navigateToVendor() {
		VendorController vendorController = new VendorController();
		VendorPage vendorPage = new VendorPage(primaryStage, this, vendorController);
		currentScene = vendorPage.getScene();
		primaryStage.setScene(currentScene);
		primaryStage.setTitle("Vendor Dashboard");
		primaryStage.show();
        pushAndNavigate(vendorPage.getScene());

	}

	public void navigateToEventDetails(EventOrganizer selectedEvent) {
		// Ensure the selected event is not null
		if (selectedEvent == null) {
			System.out.println("Error: Selected event is null. Cannot navigate to event details page.");
			return;
		}

		// Create or show the event details page with the given event details
		ViewEventDetailsPage eventDetailsPage = new ViewEventDetailsPage(primaryStage, this, selectedEvent);
		currentScene = eventDetailsPage.getScene();
		primaryStage.setScene(currentScene);
		primaryStage.setTitle("Event Details Page");
		primaryStage.show();
        pushAndNavigate(eventDetailsPage.getScene());

	}

	public void navigateToEvents() {
		// Create or show the ViewEventsPage
		ViewEventsPage viewEventsPage = new ViewEventsPage(primaryStage, this);
		currentScene = viewEventsPage.getScene(); // Get the scene from ViewEventsPage
		primaryStage.setScene(currentScene);
		primaryStage.setTitle("Events Page");
		primaryStage.show();
        pushAndNavigate(viewEventsPage.getScene());

	}

	public void navigateToAllEvents() {
		ViewAllEventsPage allEventsPage = new ViewAllEventsPage(primaryStage, this);
		currentScene = allEventsPage.getScene(); // Get the scene from ViewEventsPage
		primaryStage.setScene(currentScene);
		primaryStage.setTitle("All Events Page");
		primaryStage.show();
        pushAndNavigate(allEventsPage.getScene());

	}

	public void navigateToAllUsers() {
		ViewAllUsersPage allUsersPage = new ViewAllUsersPage(primaryStage, this);
		currentScene = allUsersPage.getScene();
		primaryStage.setScene(currentScene);
		primaryStage.setTitle("All Users Page");
		primaryStage.show();
        pushAndNavigate(allUsersPage.getScene());

	}

	public void navigateToDeleteUsers() {
		DeleteUserPage deleteUserPage = new DeleteUserPage(primaryStage, this);
		currentScene = deleteUserPage.getScene();
		primaryStage.setScene(currentScene);
		primaryStage.setTitle("Delete Users Page");
		primaryStage.show();
        pushAndNavigate(deleteUserPage.getScene());

	}

	public void navigateToEditProfile() {
		// Initialize the UserController and AdminController
		UserController userController = new UserController();
		AdminController adminController = new AdminController(); // If you need this, initialize it here

		// Pass the initialized controllers to EditProfilePage
		EditProfilePage editProfilePage = new EditProfilePage(primaryStage, this, adminController, userController);

		// Set the scene for EditProfilePage
		currentScene = editProfilePage.getScene();
		primaryStage.setScene(currentScene);
		primaryStage.setTitle("Edit Profile Page");
		primaryStage.show();
        pushAndNavigate(editProfilePage.getScene());

	}

	public void navigateToViewProduct() {
		VendorController vendorController = new VendorController();
		ViewProductPage viewProductPage = new ViewProductPage(primaryStage, this, vendorController);
		currentScene = viewProductPage.getScene();
		primaryStage.setScene(currentScene);
		primaryStage.setTitle("All Products View");
		primaryStage.show();
        pushAndNavigate(viewProductPage.getScene());

	}

	public void navigateToViewInvitation() {
		VendorController vendorController = new VendorController();
		ViewInvitationPage viewInvitationPage = new ViewInvitationPage(primaryStage, this, vendorController);
		currentScene = viewInvitationPage.getScene();
		primaryStage.setScene(currentScene);
		primaryStage.setTitle("All Invitations View");
		primaryStage.show();
        pushAndNavigate(viewInvitationPage.getScene());
	}

	public void navigateToViewAcceptedEvents() {
		VendorController vendorController = new VendorController();
		ViewAccEventsPage viewAccEventsPage = new ViewAccEventsPage(primaryStage, this, vendorController);
		currentScene = viewAccEventsPage.getScene();
		primaryStage.setScene(currentScene);
		primaryStage.setTitle("All Invitations View");
		primaryStage.show();
        pushAndNavigate(viewAccEventsPage.getScene());
	}

	public void pushAndNavigate(Scene scene) {
		if (navigationStack.isEmpty()) {
			navigationStack.push(scene);
		} else if (!navigationStack.peek().equals(scene)) {
			navigationStack.push(scene);
		}
		primaryStage.setScene(scene);
	}

	public void navigateBack() {
		if (navigationStack.size() > 1) { 
			navigationStack.pop(); // Remove the current scene
			Scene previousScene = navigationStack.peek();
			primaryStage.setScene(previousScene);
		} else {
			System.err.println("No previous page in the navigation history.");
		}
	}
	
	public void navigateToAddVendorPage(EventOrganizer selectedEvent) {
	    if (selectedEvent == null) {
	        System.out.println("Error: Selected event is null. Cannot navigate to add vendor page.");
	        return;
	    }

	    // Create the AddVendorPage and pass the necessary parameters
	    AddVendorPage addVendorPage = new AddVendorPage(primaryStage, this, selectedEvent);

	    // Set the current scene to the AddVendorPage scene
	    currentScene = addVendorPage.getScene();
	    primaryStage.setScene(currentScene);
	    primaryStage.setTitle("Add Vendors to Event");
	    primaryStage.show();

	    // Push the scene to the navigation stack for back functionality
	    pushAndNavigate(addVendorPage.getScene());
	}

	public void navigateToDeleteEventPage() {
	    // Create the DeleteEventPage instance
	    DeleteEventPage deleteEventPage = new DeleteEventPage(primaryStage, this);

	    // Get the scene from the DeleteEventPage
	    currentScene = deleteEventPage.getScene();

	    // Set the scene to the DeleteEventPage
	    primaryStage.setScene(currentScene);
	    primaryStage.setTitle("Delete Event Page");
	    primaryStage.show();

	    // Push the scene to the navigation stack for back functionality
	    pushAndNavigate(deleteEventPage.getScene());
	}
	
	public void navigateToEditEventNamePage() {
	    // Ensure the selected event is not null
	 
	    
	    AdminController adminController = new AdminController();

	    // Create the EditEventNamePage with the given event details
	    EditEventNamePage editEventNamePage = new EditEventNamePage(primaryStage, this, adminController);

	    // Get the scene from the EditEventNamePage
	    currentScene = editEventNamePage.getScene();

	    // Set the scene to the EditEventNamePage
	    primaryStage.setScene(currentScene);
	    primaryStage.setTitle("Edit Event Name");
	    primaryStage.show();

	    // Push the scene to the navigation stack for back functionality
	    pushAndNavigate(editEventNamePage.getScene());
	}

}
