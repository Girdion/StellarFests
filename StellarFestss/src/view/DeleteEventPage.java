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
import model.Admin;

public class DeleteEventPage {

    private Scene scene;
    private Stage stage;
    private VBox container;
    private TableView<EventOrganizer> eventsTable;
    private TableColumn<EventOrganizer, String> eventIDColumn;
    private TableColumn<EventOrganizer, String> nameColumn;
    private TableColumn<EventOrganizer, String> dateColumn;
    private TableColumn<EventOrganizer, String> locationColumn;
    private TableColumn<EventOrganizer, String> deleteColumn; // Column for delete button
    private Button backBtn;
    private PageController pageController;
    private ObservableList<EventOrganizer> eventList;
    private Admin admin;

    public DeleteEventPage(Stage stage, PageController pageController) {
        this.stage = stage;
        this.pageController = pageController;
        this.admin = new Admin();
        initializeComponents();
        layoutComponents();
    }

    public void show() {
        this.stage.setTitle("Delete Event");
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
        Label titleLbl = new Label("Delete Event");
        titleLbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Create the TableView for EventOrganizer
        eventsTable = new TableView<>();
        eventsTable.setEditable(false); // Make sure the table is not editable

        // Event ID Column
        eventIDColumn = new TableColumn<>("Event ID");
        eventIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvent_id()));

        // Name Column
        nameColumn = new TableColumn<>("Event Name");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvent_name()));

        // Date Column
        dateColumn = new TableColumn<>("Event Date");
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvent_date()));

        // Location Column
        locationColumn = new TableColumn<>("Event Location");
        locationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvent_location()));

        // Delete Column with buttons
        deleteColumn = new TableColumn<>("Delete");
        deleteColumn.setCellFactory(param -> new TableCell<EventOrganizer, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button deleteBtn = new Button("Delete");
                    deleteBtn.setOnAction(e -> {
                        EventOrganizer selectedEvent = getTableView().getItems().get(getIndex());
                        confirmDeletion(selectedEvent);
                    });
                    setGraphic(deleteBtn);
                }
            }
        });

        // Add columns to the table
        eventsTable.getColumns().addAll(eventIDColumn, nameColumn, dateColumn, locationColumn, deleteColumn);

        // Fetch events and populate the table
        loadEventData();

        // Back button to navigate to the admin page
        backBtn = new Button("Back");
        backBtn.setOnAction(e -> pageController.navigateToAdmin());

        // Create the Scene
        scene = new Scene(container, 600, 400);
    }

    private void layoutComponents() {
        // Add components to the container
        container.getChildren().addAll(new Label("View and Delete Events"), eventsTable, backBtn);

        // Add spacing for the back button
        VBox.setMargin(backBtn, new Insets(20, 0, 0, 0));
    }

    private void loadEventData() {
        // Fetch all events for deletion
        Vector<EventOrganizer> events = admin.viewAllEvents();

        if (events == null) {
            events = new Vector<>();
        }

        // Convert the event details into an ObservableList for the TableView
        eventList = FXCollections.observableArrayList(events);

        // Populate the TableView
        eventsTable.setItems(eventList);
    }

    private void confirmDeletion(EventOrganizer selectedEvent) {
        // Display a confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete the event?");
        alert.setContentText("Event: " + selectedEvent.getEvent_name() + " will be permanently deleted.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Delete the event if confirmed
                String result = admin.deleteEvent(selectedEvent.getEvent_id());
                Alert resultAlert = new Alert(Alert.AlertType.INFORMATION);
                resultAlert.setTitle("Deletion Result");
                resultAlert.setContentText(result);
                resultAlert.showAndWait();

                // Refresh the table after deletion
                loadEventData();
            }
        });
    }

    public Scene getScene() {
        return scene;
    }
}
