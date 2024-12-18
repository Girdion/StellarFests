package view;

import controller.AdminController;
import controller.PageController;
import controller.UserController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;
import util.UserSession;

public class EditProfilePage implements EventHandler<ActionEvent> {
    private Stage stage;
    private Scene scene;
    private VBox container;
    private GridPane grid;

    private Label titleLbl, errorLbl;
    private TextField nameField, emailField;
    private PasswordField oldPasswordField, newPasswordField;
    private Button saveBtn, cancelBtn;

    private AdminController adminController;
    private PageController pageController;
    private UserController userController;

    public EditProfilePage(Stage stage, PageController pageController, AdminController adminController, UserController userController) {
        this.stage = stage;
        this.pageController = pageController;
        this.adminController = adminController;
        this.userController = userController;  // Ensure UserController is properly initialized
        if (this.userController == null) {
            System.out.println("Error: UserController is not initialized!");
        } else {
            System.out.println("UserController initialized successfully");
        }
        initComponent();
        initPosition();
    }

    public void show() {
        this.stage.setTitle("Edit Profile");
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

        titleLbl = new Label("Edit Profile");
        titleLbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        errorLbl = new Label();
        errorLbl.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        // Input fields for profile edit
        nameField = new TextField();
        nameField.setPromptText("Enter new name");

        emailField = new TextField();
        emailField.setPromptText("Enter new email");

        oldPasswordField = new PasswordField();
        oldPasswordField.setPromptText("Enter current password");

        newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Enter new password");

        // Buttons for saving and canceling the profile edit
        saveBtn = new Button("Save Changes");
        saveBtn.setOnAction(this);

        cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(this);

        scene = new Scene(container, 600, 400);
    }

    private void initPosition() {
        container.getChildren().add(titleLbl);

        grid.add(new Label("New Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Label("New Email:"), 0, 1);
        grid.add(emailField, 1, 1);

        grid.add(new Label("Current Password:"), 0, 2);
        grid.add(oldPasswordField, 1, 2);

        grid.add(new Label("New Password:"), 0, 3);
        grid.add(newPasswordField, 1, 3);

        container.getChildren().add(grid);
        container.getChildren().add(saveBtn);
        container.getChildren().add(cancelBtn);
        container.getChildren().add(errorLbl);
    }

    @Override
    public void handle(ActionEvent e) {
        if (e.getSource() == saveBtn) {
            // Retrieve input fields' values
            String newName = nameField.getText();
            String newEmail = emailField.getText();
            String oldPassword = oldPasswordField.getText();
            String newPassword = newPasswordField.getText();

            // Get the current logged-in user from UserSession
            User currentUser = UserSession.getLoggedInUser();

            // Check if there is a logged-in user
            if (currentUser == null) {
                errorLbl.setText("No user is logged in.");
                return; // Prevent further actions if no user is logged in
            }

            // Check if userController is null
            if (userController == null) {
                errorLbl.setText("UserController is not initialized.");
                return;
            }

            // Validate input and update profile
            String validationMessage = validateInput(newName, newEmail, oldPassword, newPassword, currentUser);
            if (validationMessage.equals("Validation successful")) {
                // Call the UserController to handle the profile change logic
                String updateMessage = userController.changeProfile(newEmail, newName, oldPassword, newPassword);
                errorLbl.setText(updateMessage);
            } else {
                errorLbl.setText(validationMessage);
            }
        }

        if (e.getSource() == cancelBtn) {
            pageController.navigateToLogin(); // Assuming you want to go back to Admin Page
        }
    }

    private String validateInput(String newName, String newEmail, String oldPassword, String newPassword, User currentUser) {
        // Validate email (must be different from the current email)
        if (newEmail.isEmpty()) {
            return "Email cannot be empty.";
        }

        if (newEmail.equals(currentUser.getEmail())) {
            return "Email cannot be the same as the current one.";
        }

        // Validate name (must be different from current name)
        if (newName.isEmpty()) {
            return "Name cannot be empty.";
        }

        if (newName.equals(currentUser.getUsername())) {
            return "Name cannot be the same as the current one.";
        }

        // Validate password
        if (!oldPassword.equals(currentUser.getPassword())) {
            return "Old password does not match.";
        }

        if (newPassword.length() < 5) {
            return "New password must be at least 5 characters long.";
        }

        return "Validation successful";
    }

    public Scene getScene() {
        return scene;
    }
}
