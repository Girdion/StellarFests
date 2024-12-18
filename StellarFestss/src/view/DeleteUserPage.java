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
import model.User;
import model.Admin;

public class DeleteUserPage {

    private Scene scene;
    private Stage stage;
    private VBox container;
    private TableView<User> usersTable;
    private TableColumn<User, String> userIDColumn;
    private TableColumn<User, String> nameColumn;
    private TableColumn<User, String> emailColumn;
    private TableColumn<User, String> roleColumn;
    private TableColumn<User, String> deleteColumn; // Column for delete button
    private Button backBtn;
    private PageController pageController;
    private ObservableList<User> userList;
    private Admin admin;

    public DeleteUserPage(Stage stage, PageController pageController) {
        this.stage = stage;
        this.pageController = pageController;
        this.admin = new Admin();
        initializeComponents();
        layoutComponents();
    }

    public void show() {
        this.stage.setTitle("Delete User");
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
        Label titleLbl = new Label("Delete User");
        titleLbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Create the TableView
        usersTable = new TableView<>();
        usersTable.setEditable(false); // Make sure the table is not editable

        // User ID Column
        userIDColumn = new TableColumn<>("User ID");
        userIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser_id()));

        // Name Column
        nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));

        // Email Column
        emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

        // Role Column
        roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole()));

        // Delete Column with buttons
        deleteColumn = new TableColumn<>("Delete");
        deleteColumn.setCellFactory(param -> new TableCell<User, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button deleteBtn = new Button("Delete");
                    deleteBtn.setOnAction(e -> {
                        User selectedUser = getTableView().getItems().get(getIndex());
                        confirmDeletion(selectedUser);
                    });
                    setGraphic(deleteBtn);
                }
            }
        });

        // Add columns to the table
        usersTable.getColumns().addAll(userIDColumn, nameColumn, emailColumn, roleColumn, deleteColumn);

        // Fetch users and populate the table
        loadUserData();

        // Back button to navigate to the admin page
        backBtn = new Button("Back");
        backBtn.setOnAction(e -> pageController.navigateToAdmin());

        // Create the Scene
        scene = new Scene(container, 600, 400);
    }

    private void layoutComponents() {
        // Add components to the container
        container.getChildren().addAll(new Label("View and Delete Users"), usersTable, backBtn);

        // Add spacing for the back button
        VBox.setMargin(backBtn, new Insets(20, 0, 0, 0));
    }

    private void loadUserData() {
        // Fetch all users for deletion
        Vector<User> users = admin.viewAllUsers();

        if (users == null) {
            users = new Vector<>();
        }

        // Convert the user details into an ObservableList for the TableView
        userList = FXCollections.observableArrayList(users);

        // Populate the TableView
        usersTable.setItems(userList);
    }

    private void confirmDeletion(User selectedUser) {
        // Display a confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete the user?");
        alert.setContentText("User: " + selectedUser.getUsername() + " will be permanently deleted.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Delete the user if confirmed
                String result = admin.deleteUser(selectedUser.getUser_id());
                Alert resultAlert = new Alert(Alert.AlertType.INFORMATION);
                resultAlert.setTitle("Deletion Result");
                resultAlert.setContentText(result);
                resultAlert.showAndWait();

                // Refresh the table after deletion
                loadUserData();
            }
        });
    }

    public Scene getScene() {
        return scene;
    }
}
