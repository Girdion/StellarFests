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

public class ViewEventsPage {

    private Scene scene;
    private Stage stage;
    private VBox container;
    private TableView<EventOrganizer> eventsTable;
    private TableColumn<EventOrganizer, String> eventIDColumn;
    private TableColumn<EventOrganizer, String> eventNameColumn;
    private TableColumn<EventOrganizer, String> eventDateColumn;
    private TableColumn<EventOrganizer, String> eventLocationColumn;
    private TableColumn<EventOrganizer, String> detailsColumn;
    private TableColumn<EventOrganizer, String> inviteVendorsColumn;
    private Button backBtn;
    private PageController pageController;
    private ObservableList<EventOrganizer> eventList;

    public ViewEventsPage(Stage stage, PageController pageController) {
        this.stage = stage;
        this.pageController = pageController;
        initializeComponents();
        layoutComponents();
    }

    public void show() {
        this.stage.setTitle("View Organized Events");
        this.stage.setScene(scene);
        this.stage.show();
    }

    @SuppressWarnings("unchecked")
    private void initializeComponents() {
        container = new VBox(10);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.TOP_CENTER);

        Label titleLbl = new Label("View Organized Events");
        titleLbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        eventsTable = new TableView<>();
        eventsTable.setEditable(false);

        eventIDColumn = new TableColumn<>("Event ID");
        eventIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvent_id()));

        eventNameColumn = new TableColumn<>("Event Name");
        eventNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvent_name()));

        eventDateColumn = new TableColumn<>("Event Date");
        eventDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvent_date()));

        eventLocationColumn = new TableColumn<>("Event Location");
        eventLocationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvent_location()));

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

        inviteVendorsColumn = new TableColumn<>("Invite Vendors");
        inviteVendorsColumn.setCellFactory(param -> new TableCell<EventOrganizer, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button inviteBtn = new Button("Invite Vendors");
                    inviteBtn.setOnAction(e -> {
                        EventOrganizer selectedEvent = getTableView().getItems().get(getIndex());
                        if (selectedEvent != null) {
                            pageController.navigateToAddVendorPage(selectedEvent); 
                        }
                    });
                    setGraphic(inviteBtn);
                }
            }
        });

        eventsTable.getColumns().addAll(eventIDColumn, eventNameColumn, eventDateColumn, eventLocationColumn, detailsColumn, inviteVendorsColumn);

        loadEventData();

        eventsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                pageController.navigateToEventDetails(newValue);
            }
        });

        backBtn = new Button("Back");
        backBtn.setOnAction(e -> pageController.navigateBack());

        scene = new Scene(container, 600, 400);
    }

    private void layoutComponents() {
        container.getChildren().addAll(new Label("View Organized Events"), eventsTable, backBtn);
        VBox.setMargin(backBtn, new Insets(20, 0, 0, 0));
    }

    private void loadEventData() {
        String organizerID = UserSession.getLoggedInUser().getUser_id();

        EventOrganizer eventOrganizer = new EventOrganizer();
        Vector<EventOrganizer> events = eventOrganizer.viewOrganizedEvent(organizerID);

        if (events != null && !events.isEmpty()) {
            eventList = FXCollections.observableArrayList(events);
            eventsTable.setItems(eventList);
        } else {
            System.out.println("No events found or error retrieving events.");
            eventsTable.setItems(FXCollections.observableArrayList());
        }
    }

    public Scene getScene() {
        return scene;
    }
}
