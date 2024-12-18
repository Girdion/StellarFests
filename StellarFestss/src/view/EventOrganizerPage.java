package view;

import controller.EventOrganizerController;
import controller.PageController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import util.UserSession;

public class EventOrganizerPage implements EventHandler<ActionEvent> {

    private Scene scene;
    private Stage stage;
    private VBox container;
    private GridPane grid;
    private Label titleLbl, eventNameLbl, eventDateLbl, eventLocationLbl, eventDescriptionLbl, eventIDLbl;
    private TextField eventNameField, eventLocationField, eventDescriptionField;
    private DatePicker eventDatePicker;
    private Button createEventBtn, viewEventsBtn, editEventBtn;
    private ComboBox<String> eventIDComboBox;
    private Label eventErrorLbl;
    private Label userGreetingLbl;
    private Button editProfileBtn;
    private Label eventDetailsTitleLbl, eventNameDetailLbl, eventDateDetailLbl, eventLocationDetailLbl, eventDescriptionDetailLbl;
    private Label eventNameDetail, eventDateDetail, eventLocationDetail, eventDescriptionDetail;

    private PageController pageController;
    private EventOrganizerController eventOrganizerController;

    public EventOrganizerPage(Stage stage, PageController pageController, EventOrganizerController eventOrganizerController) {
        this.stage = stage;
        this.pageController = pageController;
        this.eventOrganizerController = eventOrganizerController;
        initComponent();
        initPosition();
    }

    public void show() {
        this.stage.setTitle("Event Organizer Page");
        this.stage.setScene(scene);
        this.stage.show();
    }

    private void initPosition() {
        container.getChildren().add(userGreetingLbl); // Add the user greeting label at the top
        container.getChildren().add(titleLbl); // Add title label after user greeting label

        grid.add(eventNameLbl, 0, 0);
        grid.add(eventNameField, 1, 0);
        grid.add(eventErrorLbl, 1, 1); // Position error label below event name field

        grid.add(eventDateLbl, 0, 2);
        grid.add(eventDatePicker, 1, 2);

        grid.add(eventLocationLbl, 0, 3);
        grid.add(eventLocationField, 1, 3);

        grid.add(eventDescriptionLbl, 0, 4);
        grid.add(eventDescriptionField, 1, 4);

        grid.add(viewEventsBtn, 0, 5);
        grid.add(createEventBtn, 3, 5);

        container.getChildren().addAll(grid, editEventBtn, editProfileBtn);
        container.getChildren().add(eventDetailsTitleLbl);
        container.getChildren().add(eventNameDetailLbl);
        container.getChildren().add(eventNameDetail);
        container.getChildren().add(eventDateDetailLbl);
        container.getChildren().add(eventDateDetail);
        container.getChildren().add(eventLocationDetailLbl);
        container.getChildren().add(eventLocationDetail);
        container.getChildren().add(eventDescriptionDetailLbl);
        container.getChildren().add(eventDescriptionDetail);

        VBox.setMargin(editEventBtn, new Insets(0, 0, 10, 0));
        VBox.setMargin(editProfileBtn, new Insets(0, 0, 10, 0));
    }

    private void initComponent() {
        container = new VBox(10);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.TOP_CENTER);

        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        titleLbl = new Label("Event Organizer Dashboard");
        titleLbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        userGreetingLbl = new Label("Hello, " + UserSession.getLoggedInUser().getUsername() + "!");
        userGreetingLbl.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        eventNameLbl = new Label("Event Name:");
        eventDateLbl = new Label("Event Date:");
        eventLocationLbl = new Label("Event Location:");
        eventDescriptionLbl = new Label("Event Description:");
        eventIDLbl = new Label("Select Event:");

        eventErrorLbl = new Label();
        eventErrorLbl.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        // Initialize fields for event creation
        eventNameField = new TextField();
        eventLocationField = new TextField();
        eventDescriptionField = new TextField();
        eventDatePicker = new DatePicker();

        // Buttons for event management
        createEventBtn = new Button("Create Event");
        createEventBtn.setOnAction(this);

        viewEventsBtn = new Button("View Organized Events");
        viewEventsBtn.setOnAction(this);

        editEventBtn = new Button("Edit Event Name");
        editEventBtn.setOnAction(this);

        editProfileBtn = new Button("Edit Profile");
        editProfileBtn.setOnAction(this);

        // ComboBox to select an event for editing
        eventIDComboBox = new ComboBox<>();
        eventIDComboBox.setPromptText("Select Event");

        eventDetailsTitleLbl = new Label("Event Details");
        eventDetailsTitleLbl.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        eventNameDetailLbl = new Label("Event Name:");
        eventDateDetailLbl = new Label("Event Date:");
        eventLocationDetailLbl = new Label("Event Location:");
        eventDescriptionDetailLbl = new Label("Event Description:");

        eventNameDetail = new Label();
        eventDateDetail = new Label();
        eventLocationDetail = new Label();
        eventDescriptionDetail = new Label();

        // Initially set event details labels to be invisible
        eventDetailsTitleLbl.setVisible(false);
        eventNameDetailLbl.setVisible(false);
        eventDateDetailLbl.setVisible(false);
        eventLocationDetailLbl.setVisible(false);
        eventDescriptionDetailLbl.setVisible(false);

        eventNameDetail.setVisible(false);
        eventDateDetail.setVisible(false);
        eventLocationDetail.setVisible(false);
        eventDescriptionDetail.setVisible(false);

        scene = new Scene(container, 600, 550);
    }

    @Override
    public void handle(ActionEvent e) {
        // Get the logged-in user's ID from the session
        String organizerID = UserSession.getLoggedInUser().getUser_id();

        if (e.getSource() == createEventBtn) {
            String eventName = eventNameField.getText();
            String eventLocation = eventLocationField.getText();
            String eventDescription = eventDescriptionField.getText();
            String eventDate = eventDatePicker.getValue() != null ? eventDatePicker.getValue().toString() : "";

            // Clear previous error messages
            eventErrorLbl.setText("");

            // Validate input
            String validationMessage = eventOrganizerController.checkCreateEventInput(eventName, eventDate, eventLocation, eventDescription);

            if (validationMessage.equals("Validation successful")) {
                eventOrganizerController.createEvent(eventName, eventDate, eventLocation, eventDescription, organizerID);
                eventErrorLbl.setText("Event created successfully!");
                eventErrorLbl.setStyle("-fx-text-fill: green;");

                // Clear the form fields
                eventNameField.clear();
                eventLocationField.clear();
                eventDescriptionField.clear();
                eventDatePicker.setValue(null);
            } else {
                eventErrorLbl.setText(validationMessage);
            }
        }

        if (e.getSource() == viewEventsBtn) {
            pageController.navigateToEvents();
        }

        if (e.getSource() == editEventBtn) {
           pageController.navigateToEditEventNamePage();
        }

        if (e.getSource() == editProfileBtn) {
            pageController.navigateToEditProfile();
        }
    }

    public Scene getScene() {
        return scene;
    }
}
