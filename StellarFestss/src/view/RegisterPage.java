package view;

import javafx.event.ActionEvent;
import controller.PageController;
import controller.UserController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterPage {

    private Scene scene;
    private Stage stage;
    private VBox container;
    private GridPane grid;
    private Label titleLbl, emailLbl, usernameLbl, passwordLbl, roleLbl;
    private Label emailErrorLbl, usernameErrorLbl, passwordErrorLbl, roleErrorLbl;
    private TextField emailField, usernameField;
    private PasswordField passwordField;
    private Button regisBtn, loginBtn;
    private ComboBox<String> roleComboBox;
    private PageController pageController;
    private UserController userController;

    public RegisterPage(Stage stage, PageController pageController, UserController userController) {
        this.stage = stage;
        this.pageController = pageController;
        this.userController = userController;  // Initialize UserController
        initializeComponents();
        layoutComponents();
    }

    public void show() {
        this.stage.setTitle("Register Page");
        this.stage.setScene(scene);
        this.stage.show();
    }

    private void initializeComponents() {
        // Create container VBox
        container = new VBox(10);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.TOP_CENTER);

        // Labels
        titleLbl = new Label("Register New Account");
        titleLbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        emailLbl = new Label("Email:");
        usernameLbl = new Label("Username:");
        passwordLbl = new Label("Password:");
        roleLbl = new Label("Role:");

        // Error Labels
        emailErrorLbl = new Label();
        usernameErrorLbl = new Label();
        passwordErrorLbl = new Label();
        roleErrorLbl = new Label();

        // Style error labels
        emailErrorLbl.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        usernameErrorLbl.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        passwordErrorLbl.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        roleErrorLbl.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        // Input Fields
        emailField = new TextField();
        usernameField = new TextField();
        passwordField = new PasswordField();

        // ComboBox for role selection
        roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll(" ", "Event Organizer", "Vendor", "Guest");
        roleComboBox.setValue(" ");

        // Buttons
        regisBtn = new Button("Register");
        loginBtn = new Button("Login");

        // Register button action using lambda for concise code
        regisBtn.setOnAction(e -> handleRegistration(e));

        // Login button action
        loginBtn.setOnAction(e -> pageController.navigateToLogin());

        // Initialize the Scene
        scene = new Scene(container, 600, 550);
    }

    private void layoutComponents() {
        // Create grid layout
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Add components to the grid
        grid.add(emailLbl, 0, 0);
        grid.add(emailField, 1, 0);
        grid.add(emailErrorLbl, 1, 1);

        grid.add(usernameLbl, 0, 2);
        grid.add(usernameField, 1, 2);
        grid.add(usernameErrorLbl, 1, 3);

        grid.add(passwordLbl, 0, 4);
        grid.add(passwordField, 1, 4);
        grid.add(passwordErrorLbl, 1, 5);

        grid.add(roleLbl, 0, 6);
        grid.add(roleComboBox, 1, 6);
        grid.add(roleErrorLbl, 1, 7);

        // Buttons at the bottom
        grid.add(regisBtn, 0, 8, 2, 1);
        grid.add(loginBtn, 0, 9, 2, 1);

        // Add grid to the container
        container.getChildren().addAll(titleLbl, grid);

        // Set margins for buttons
        VBox.setMargin(regisBtn, new Insets(20, 0, 10, 0));
        VBox.setMargin(loginBtn, new Insets(0, 0, 10, 0));
    }

    public void handleRegistration(ActionEvent e) {
        if (e.getSource() == regisBtn) {
            String email = emailField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            String selectedRole = roleComboBox.getValue();

            // Clear previous error messages
            emailErrorLbl.setText("");
            usernameErrorLbl.setText("");
            passwordErrorLbl.setText("");
            roleErrorLbl.setText("");

            // Use UserController to validate registration input
            String validationMessage = userController.checkRegisterInput(username, password, email, selectedRole);

            if (validationMessage.equals("Validation successful")) {
                userController.register(username, password, email, selectedRole);

                // Clear form fields
                emailField.clear();
                usernameField.clear();
                passwordField.clear();
                roleComboBox.setValue(" ");

                // Show success message
                emailErrorLbl.setText("Registration successful!");
                emailErrorLbl.setStyle("-fx-text-fill: green;");

                // Navigate to Login Page
                pageController.navigateToLogin();
            } else {
                // Handle validation errors
            	switch (validationMessage) {
                case "Email cannot be empty.":
                    emailErrorLbl.setText(validationMessage);
                    break;
                case "Username cannot be empty.":
                    usernameErrorLbl.setText(validationMessage);
                    break;
                case "Password cannot be empty.":
                    passwordErrorLbl.setText(validationMessage);
                    break;
                case "Please select a role.":
                    roleErrorLbl.setText(validationMessage);
                    break;
                default:
                    emailErrorLbl.setText(validationMessage); // Catch any unexpected message
                    break;
            }

            }
        }
    }

    public Scene getScene() {
        return scene;
    }
}
