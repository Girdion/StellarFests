package view;

import java.util.Vector;

import controller.AdminController;
import controller.PageController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Event;
import model.EventOrganizer;
import util.UserSession;

public class EditEventNamePage {

    private Stage stage;
    private Scene scene;
    private VBox container;
    private TableView<EventOrganizer> eventsTable;
    private TableColumn<EventOrganizer, String> eventIDColumn;
    private TableColumn<EventOrganizer, String> eventNameColumn;
    private TableColumn<EventOrganizer, String> eventDateColumn;
    private TableColumn<EventOrganizer, String> eventLocationColumn;
    private Button saveBtn, cancelBtn;
    private TextField eventNameField;
    private Label titleLbl, errorLbl;
    private ObservableList<EventOrganizer> eventList;
    private PageController pageController;
    private AdminController adminController;

    public EditEventNamePage(Stage stage, PageController pageController, AdminController adminController) {
        this.stage = stage;
        this.pageController = pageController;
        this.adminController = adminController;
        initializeComponents();
        layoutComponents();
    }

    public void show() {
        this.stage.setTitle("Edit Event Name");
        this.stage.setScene(scene);
        this.stage.show();
    }

    private void initializeComponents() {
        // Main container
        container = new VBox(10);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.TOP_CENTER);

        // Title Label
        titleLbl = new Label("Edit Event Name");
        titleLbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Error label
        errorLbl = new Label();
        errorLbl.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        // Create the TableView
        eventsTable = new TableView<>();
        eventsTable.setEditable(false); // Make sure the table is not editable

        eventIDColumn = new TableColumn<>("Event ID");
        eventIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvent_id()));

        eventNameColumn = new TableColumn<>("Event Name");
        eventNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvent_name()));

        eventDateColumn = new TableColumn<>("Event Date");
        eventDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvent_date()));

        eventLocationColumn = new TableColumn<>("Event Location");
        eventLocationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvent_location()));

        // Add columns to the table
        eventsTable.getColumns().addAll(eventIDColumn, eventNameColumn, eventDateColumn, eventLocationColumn);

        // Fetch events and populate the table using the provided loadEventData method
        loadEventData();

        // Add event selection handler for table row selection
        eventsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                eventNameField.setText(newValue.getEvent_name()); // Populate the event name field with the selected event
            }
        });

        // Event name input field
        eventNameField = new TextField();
        eventNameField.setPromptText("Enter new event name");

        // Save button for updating event name
        saveBtn = new Button("Save Changes");
        saveBtn.setOnAction(e -> saveChanges());

        // Cancel button to navigate back
        cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> pageController.navigateBack());

        // Create the Scene
        scene = new Scene(container, 600, 400);
    }

    private void layoutComponents() {
        // Add components to the container
        container.getChildren().add(titleLbl);
        container.getChildren().add(eventsTable);
        container.getChildren().add(eventNameField);
        container.getChildren().add(saveBtn);
        container.getChildren().add(cancelBtn);
        container.getChildren().add(errorLbl);

        // Add spacing for buttons
        VBox.setMargin(saveBtn, new Insets(10, 0, 0, 0));
        VBox.setMargin(cancelBtn, new Insets(10, 0, 0, 0));
    }

    private void loadEventData() {
        // Get the logged-in organizer ID from the user session
        String organizerID = UserSession.getLoggedInUser().getUser_id();

        EventOrganizer eventOrganizer = new EventOrganizer();
        Vector<EventOrganizer> events = eventOrganizer.viewOrganizedEvent(organizerID); // Fetch events for the logged-in organizer

        if (events != null && !events.isEmpty()) {
            eventList = FXCollections.observableArrayList(events);
            eventsTable.setItems(eventList);
        } else {
            System.out.println("No events found or error retrieving events.");
            eventsTable.setItems(FXCollections.observableArrayList()); // Clear the table if no events are found
        }
    }

    private void saveChanges() {
        EventOrganizer selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            String newEventName = eventNameField.getText();
            if (newEventName != null && !newEventName.trim().isEmpty()) {
                // Assuming EventOrganizer has a method to update event name
                selectedEvent.setEvent_name(newEventName);
                boolean isUpdated = selectedEvent.updateEvent(); // Update event name in the database

                if (isUpdated) {
                    errorLbl.setText("Event name updated successfully.");
                    loadEventData(); // Reload event data to show updated info
                } else {
                    errorLbl.setText("Failed to update event name.");
                }
            } else {
                errorLbl.setText("Event name cannot be empty.");
            }
        } else {
            errorLbl.setText("No event selected.");
        }
    }
    
    public Scene getScene() {
    	return scene;
    }
}
