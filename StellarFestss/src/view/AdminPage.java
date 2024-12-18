package view;

import controller.AdminController;
import controller.PageController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.UserSession;

public class AdminPage implements EventHandler<ActionEvent> {
    private Stage stage;
    private Scene scene;
    private VBox container;
    private GridPane grid;

    private Label titleLbl, userGreetingLbl, errorLbl;

    private Button viewAllEventsBtn, viewEventDetailsBtn, deleteEventBtn, logOutBtn;
    private Button viewAllUsersBtn, deleteUserBtn;

    private AdminController adminController;
    private PageController pageController;

    public AdminPage(Stage stage, PageController pageController, AdminController adminController) {
        this.stage = stage;
        this.pageController = pageController;
        this.adminController = adminController;
        initComponent();
        initPosition();
    }

    public void show() {
        this.stage.setTitle("Admin Dashboard");
        this.stage.setScene(scene);
        this.stage.show();
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

        titleLbl = new Label("Admin Dashboard");
        titleLbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        userGreetingLbl = new Label("Welcome, Admin!");
        userGreetingLbl.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        errorLbl = new Label();
        errorLbl.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        // Buttons for admin actions
        viewAllEventsBtn = new Button("View All Events");
        viewAllEventsBtn.setOnAction(this);

        deleteEventBtn = new Button("Delete Event");
        deleteEventBtn.setOnAction(this);

        viewAllUsersBtn = new Button("View All Users");
        viewAllUsersBtn.setOnAction(this);

        deleteUserBtn = new Button("Delete User");
        deleteUserBtn.setOnAction(this);
        
        logOutBtn = new Button("Log Out");
        logOutBtn.setOnAction(this);

        scene = new Scene(container, 600, 400);
    }

    private void initPosition() {
        container.getChildren().add(userGreetingLbl);
        container.getChildren().add(titleLbl);

        grid.add(viewAllEventsBtn, 0, 0);
        grid.add(deleteEventBtn, 1, 0);
        grid.add(viewAllUsersBtn, 2, 0);
        grid.add(deleteUserBtn, 3, 0);

        container.getChildren().addAll(grid, errorLbl);

        // Create an HBox for the logout button at the bottom
        HBox logoutBox = new HBox(logOutBtn);
        logoutBox.setAlignment(Pos.CENTER);
        logoutBox.setPadding(new Insets(20, 0, 20, 0));  // Add padding to the bottom

        // Add the logout button at the bottom center
        container.getChildren().add(logoutBox);
    }


    @Override
    public void handle(ActionEvent e) {
        if (e.getSource() == viewAllEventsBtn) {
        	pageController.navigateToAllEvents();
        }

        if (e.getSource() == deleteEventBtn) {
            pageController.navigateToDeleteEventPage();
        }

        if (e.getSource() == viewAllUsersBtn) {
        	pageController.navigateToAllUsers();
        }

        if (e.getSource() == deleteUserBtn) {
        	pageController.navigateToDeleteUsers();
        }
        
        if(e.getSource() == logOutBtn) {
        	pageController.navigateToLogin();
        }
    }

    public Scene getScene() {
        return scene;
    }
}
