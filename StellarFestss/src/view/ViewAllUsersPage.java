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
import model.Admin;
import model.User;
import util.UserSession;

public class ViewAllUsersPage {

    private Scene scene;
    private Stage stage;
    private VBox container;
    private TableView<User> usersTable;
    private TableColumn<User, String> userIDColumn;
    private TableColumn<User, String> userNameColumn;
    private TableColumn<User, String> userEmailColumn;
    private TableColumn<User, String> userRoleColumn;
    private Button backBtn;
    private PageController pageController;
    private ObservableList<User> userList;

    public ViewAllUsersPage(Stage stage, PageController pageController) {
        this.stage = stage;
        this.pageController = pageController;
        initializeComponents();
        layoutComponents();
    }

    public void show() {
        this.stage.setTitle("View All Users");
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
        Label titleLbl = new Label("View All Users");
        titleLbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Create the TableView
        usersTable = new TableView<>();
        usersTable.setEditable(false); // Make sure the table is not editable

        userIDColumn = new TableColumn<>("User ID");
        userIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser_id()));

        userNameColumn = new TableColumn<>("Name");
        userNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));

        userEmailColumn = new TableColumn<>("Email");
        userEmailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

        userRoleColumn = new TableColumn<>("Role");
        userRoleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole()));

        // Add columns to the table
        usersTable.getColumns().addAll(userIDColumn, userNameColumn, userEmailColumn, userRoleColumn);

        // Fetch users and populate the table
        loadUserData();

        // Back button to navigate to the admin page
        backBtn = new Button("Back");
        backBtn.setOnAction(e -> pageController.navigateBack());
        
        // Create the Scene
        scene = new Scene(container, 600, 400);
    }

    private void layoutComponents() {
        // Add components to the container
        container.getChildren().addAll(new Label("View All Users"), usersTable, backBtn);

        // Add spacing for the back button
        VBox.setMargin(backBtn, new Insets(20, 0, 0, 0));
    }

    private void loadUserData() {
        // Create an instance of Admin to fetch all users
        Admin admin = new Admin();

        // Fetch all users from the database
        Vector<User> users = admin.viewAllUsers();

        // Convert the user details into an ObservableList for the TableView
        userList = FXCollections.observableArrayList(users);

        // Populate the TableView with the user data
        usersTable.setItems(userList);
    }


    public Scene getScene() {
        return scene;
    }
}
