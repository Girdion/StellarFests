package view;

import controller.PageController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Event;
import model.EventOrganizer;

public class ViewEventDetailsPage {

    private Scene scene;
    private Stage stage;
    private VBox container;
    private GridPane grid;
    private Label titleLbl, eventNameLbl, eventDateLbl, eventLocationLbl, eventDescriptionLbl;
    private Label eventNameDetail, eventDateDetail, eventLocationDetail, eventDescriptionDetail;
    private Button backBtn;
    private PageController pageController;

    public ViewEventDetailsPage(Stage stage, PageController pageController, EventOrganizer selectedEvent) {
        this.stage = stage;
        this.pageController = pageController;
        initializeComponents(selectedEvent);
        layoutComponents();
    }

    public void show() {
        this.stage.setTitle("Event Details");
        this.stage.setScene(scene);
        this.stage.show();
    }

    private void initializeComponents(EventOrganizer selectedEvent) {
        // Main container
        container = new VBox(10);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.TOP_CENTER);

        // Title Label
        titleLbl = new Label("Event Details");
        titleLbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Labels for event details
        eventNameLbl = new Label("Event Name:");
        eventDateLbl = new Label("Event Date:");
        eventLocationLbl = new Label("Event Location:");
        eventDescriptionLbl = new Label("Event Description:");

        // Details for the selected event
        eventNameDetail = new Label(selectedEvent.getEvent_name());
        eventDateDetail = new Label(selectedEvent.getEvent_date());
        eventLocationDetail = new Label(selectedEvent.getEvent_location());
        eventDescriptionDetail = new Label(selectedEvent.getEvent_description());

        // Style detail labels for emphasis
        eventNameDetail.setStyle("-fx-font-weight: bold;");
        eventDateDetail.setStyle("-fx-font-weight: bold;");
        eventLocationDetail.setStyle("-fx-font-weight: bold;");
        eventDescriptionDetail.setStyle("-fx-font-weight: bold;");

        // Back button to return to the previous page
        backBtn = new Button("Back");
        backBtn.setOnAction(e -> pageController.navigateBack());
        
        // Create the Scene
        scene = new Scene(container, 600, 400);
    }

    private void layoutComponents() {
        // Grid layout for event details
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Add labels and details to the grid
        grid.add(eventNameLbl, 0, 0);
        grid.add(eventNameDetail, 1, 0);

        grid.add(eventDateLbl, 0, 1);
        grid.add(eventDateDetail, 1, 1);

        grid.add(eventLocationLbl, 0, 2);
        grid.add(eventLocationDetail, 1, 2);

        grid.add(eventDescriptionLbl, 0, 3);
        grid.add(eventDescriptionDetail, 1, 3);

        // Add components to the main container
        container.getChildren().addAll(titleLbl, grid, backBtn);

        // Add spacing for the back button
        VBox.setMargin(backBtn, new Insets(20, 0, 0, 0));
    }

    public Scene getScene() {
        return scene;
    }
}
