package view;

import java.util.Vector;

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
import model.EventOrganizer;
import util.UserSession;

public class ViewAllEventsPage {

    private Scene scene;
    private Stage stage;
    private VBox container;
    private TableView<EventOrganizer> eventsTable;
    private TableColumn<EventOrganizer, String> eventIDColumn;
    private TableColumn<EventOrganizer, String> eventNameColumn;
    private TableColumn<EventOrganizer, String> eventDateColumn;
    private TableColumn<EventOrganizer, String> eventLocationColumn;
    private TableColumn<EventOrganizer, String> detailsColumn;  // New column for details button
    private Button backBtn;
    private PageController pageController;
    private ObservableList<EventOrganizer> eventList;

    public ViewAllEventsPage(Stage stage, PageController pageController) {
        this.stage = stage;
        this.pageController = pageController;
        initializeComponents();
        layoutComponents();
    }

    public void show() {
        this.stage.setTitle("View All Events");
        this.stage.setScene(scene);
        this.stage.show();
    }

    @SuppressWarnings("unchecked")
    private void initializeComponents() {
        // Main container
        container = new VBox(10);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.TOP_CENTER);

        // Title Label
        Label titleLbl = new Label("View All Events");
        titleLbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

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

        // New Details Column with buttons
        detailsColumn = new TableColumn<>("Details");
        detailsColumn.setCellFactory(param -> new TableCell<EventOrganizer, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button detailsBtn = new Button("View Details");
                    detailsBtn.setOnAction(e -> {
                        EventOrganizer selectedEvent = getTableView().getItems().get(getIndex());
                        pageController.navigateToEventDetails(selectedEvent);
                    });
                    setGraphic(detailsBtn);
                }
            }
        });

        // Add columns to the table
        eventsTable.getColumns().addAll(eventIDColumn, eventNameColumn, eventDateColumn, eventLocationColumn, detailsColumn);

        // Fetch events and populate the table
        loadEventData();

        // Add event selection handler for table row selection
        eventsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                pageController.navigateToEventDetails(newValue);
            }
        });

        // Back button to navigate to the event organizer page
        backBtn = new Button("Back");
        backBtn.setOnAction(e -> pageController.navigateBack());
        
        // Create the Scene
        scene = new Scene(container, 600, 400);
    }

    private void layoutComponents() {
        // Add components to the container
        container.getChildren().addAll(new Label("View All Events"), eventsTable, backBtn);

        // Add spacing for the back button
        VBox.setMargin(backBtn, new Insets(20, 0, 0, 0));
    }

    private void loadEventData() {
        // Create an instance of EventOrganizer and fetch all events
        EventOrganizer eventOrganizer = new EventOrganizer();
        Vector<EventOrganizer> events = eventOrganizer.viewAllEvents(); // Fetch all events for admin

        if (events == null) {
            events = new Vector<>();
        }

        // Convert the event details into an ObservableList for the TableView
        eventList = FXCollections.observableArrayList(events);

        // Populate the TableView
        eventsTable.setItems(eventList);
    }

    public Scene getScene() {
        return scene;
    }
}
