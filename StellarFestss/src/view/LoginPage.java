package view;

import javafx.event.ActionEvent;
import controller.PageController;
import controller.UserController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginPage {

    private Scene scene;
    private Stage stage;
    private VBox container;
    private GridPane grid;
    private Label titleLbl, emailLbl, passwordLbl, roleLbl;
    private Label emailErrorLbl, passwordErrorLbl;
    private TextField emailField;
    private PasswordField passwordField;
    private Button loginBtn, registerBtn;
    private PageController pageController;
    private UserController userController;

    public LoginPage(Stage stage, PageController pageController, UserController userController) {
        if (userController == null) {
            throw new IllegalArgumentException("UserController cannot be null");
        }
        this.stage = stage;
        this.pageController = pageController;
        this.userController = userController; // Initialize UserController
        initializeComponents();
        layoutComponents();
    }


    public void show() {
        this.stage.setTitle("Login Page");
        this.stage.setScene(scene);
        this.stage.show();
    }

    private void initializeComponents() {
        // Create container VBox
        container = new VBox(10);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.TOP_CENTER);

        // Labels
        titleLbl = new Label("Login to Your Account");
        titleLbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        emailLbl = new Label("Email:");
        passwordLbl = new Label("Password:");

        // Error Labels
        emailErrorLbl = new Label();
        passwordErrorLbl = new Label();

        // Style error labels
        emailErrorLbl.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        passwordErrorLbl.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        // Input Fields
        emailField = new TextField();
        passwordField = new PasswordField();

        // Buttons
        loginBtn = new Button("Login");
        registerBtn = new Button("Register");

        // Login button action using lambda
        loginBtn.setOnAction(e -> handleLogin(e));

        // Register button action
        registerBtn.setOnAction(e -> pageController.navigateToLogin());
        
        // Initialize the Scene
        scene = new Scene(container, 600, 400);
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

        grid.add(passwordLbl, 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(passwordErrorLbl, 1, 3);

        // Buttons at the bottom
        grid.add(loginBtn, 0, 4, 2, 1);
        grid.add(registerBtn, 0, 5, 2, 1);

        // Add grid to the container
        container.getChildren().addAll(titleLbl, grid);

        // Set margins for buttons
        VBox.setMargin(loginBtn, new Insets(20, 0, 10, 0));
        VBox.setMargin(registerBtn, new Insets(0, 0, 10, 0));
    }

    private void handleLogin(ActionEvent e) {
        if (e.getSource() == loginBtn) {
            String email = emailField.getText();
            String password = passwordField.getText();

            // Clear previous error messages
            emailErrorLbl.setText("");
            passwordErrorLbl.setText("");

            // Use UserController to validate login
            String roleOrError = userController.login(email, password);

            if (roleOrError != null) {
                if (roleOrError.equals("Admin")) {
                    pageController.navigateToAdmin();
                } else if (roleOrError.equals("Guest")) {
                    pageController.navigateToGuest();
                } else if (roleOrError.equals("Event Organizer")) {
                    pageController.navigateToEventOrganizer();
                } else if (roleOrError.equals("Vendor")) {
                    pageController.navigateToVendor();
                } else {
                    emailErrorLbl.setText(roleOrError);  // Show the error message (e.g., "Invalid username or password.")
                }
            }
        }
    }


    public Scene getScene() {
        return scene;
    }
}
