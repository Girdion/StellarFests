package view;

import java.util.ArrayList;
import java.util.Vector;

import controller.PageController;
import controller.VendorController;
import javafx.collections.ObservableList;
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
import model.Event;
import model.Invitation;
import model.User;
import util.UserSession;

public class ViewAccEventsPage implements EventHandler<ActionEvent> {
	private Scene scene;
	private Stage stage;
	private VBox container;
	private Label titleLbl;
	private Button backBtn;
	private TableView<Event> acceptedEventsTable;

	private PageController pageController;
	private VendorController vendorController;

	private UserSession userSession;

	public ViewAccEventsPage(Stage stage, PageController pageController, VendorController vendorController) {
		this.stage = stage;
		this.pageController = pageController;
		this.vendorController = vendorController;
		initializeComponents();
		layoutComponents();
	}

	private void layoutComponents() {
		container = new VBox(10);
		container.setPadding(new Insets(20));


		// Add components to the layout
		container.getChildren().addAll(titleLbl, acceptedEventsTable, backBtn);

		// Set the scene with the VBox
		scene = new Scene(container, 600, 400);

		// Load accepted events into the table
		loadAcceptedEvents();
	}

	private void initializeComponents() {
		titleLbl = new Label("Accepted Events");

		acceptedEventsTable = new TableView<>();

		TableColumn<Event, String> nameCol = new TableColumn<>("Event Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("event_name"));

		TableColumn<Event, String> dateCol = new TableColumn<>("Event Date");
		dateCol.setCellValueFactory(new PropertyValueFactory<>("event_date"));

		TableColumn<Event, String> locationCol = new TableColumn<>("Location");
		locationCol.setCellValueFactory(new PropertyValueFactory<>("event_location"));

		TableColumn<Event, String> descriptionCol = new TableColumn<>("Description");
		descriptionCol.setCellValueFactory(new PropertyValueFactory<>("event_description"));

		acceptedEventsTable.getColumns().addAll(nameCol, dateCol, locationCol, descriptionCol);
		acceptedEventsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		backBtn = new Button("Back");
		backBtn.setOnAction(e -> pageController.navigateBack());
	}

	private void loadAcceptedEvents() {
		User currentUser = UserSession.getLoggedInUser();

		int userID = Integer.parseInt(currentUser.getUser_id());
		String userId = currentUser.getUser_id();
		Vector<String> accEvents = vendorController.getAcceptedInvitations(userID);
		Event event = new Event();
		String eventId = event.getEvent_id();
		String orgId = event.getOrganizer_id();

		try {
			for (String accEventsDetails : accEvents) {
				System.out.println("Processing: " + accEventsDetails); // Debugging
				String[] parts = accEventsDetails.split(", ");

				// Ensure parts have at least 4 expected fields
				if (parts.length >= 4) {
					try {
						// Directly access the expected indices without additional splits
						String name = parts[0];
						String date = parts[1];
						String location = parts[2];
						String description = parts[3];

						// Create the event using the extracted details
						Event events = new Event("", name, date, location, description, "");

						// Add to your table
						acceptedEventsTable.getItems().add(events);
					} catch (Exception ex) {
						System.err.println("Error parsing event details: " + accEventsDetails);
						ex.printStackTrace();
					}
				} else {
					System.err.println("Unexpected data format: " + accEventsDetails);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
