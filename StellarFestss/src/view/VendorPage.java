package view;

import java.util.Vector;

import controller.PageController;
import controller.VendorController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.UserSession;

public class VendorPage implements EventHandler<ActionEvent> {
	private Scene scene;
	private Stage stage;
	private VBox container;
	private GridPane grid;
	private Label titleLbl, productNameLbl, productDescLbl;
	private TextField productNameField, productDescField;
	private Button viewProductBtn, viewAccEventBtn, viewInvitationBtn, addProductBtn;
	private Label productErrorLbl;
	private Label userGreetingLbl;

	private PageController pageController;
	private VendorController vendorController;

	private UserSession userSession;

	public VendorPage(Stage stage, PageController pageController, VendorController vendorController) {
		this.stage = stage;
		this.pageController = pageController;
		this.vendorController = vendorController;
		initializeComponents();
		layoutComponents();
	}

	public void show() {
		this.stage.setTitle("Vendor Page");
		this.stage.setScene(scene);
		this.stage.show();
	}

	private void initializeComponents() {
		container = new VBox(10);
		container.setPadding(new Insets(20));
		container.setAlignment(Pos.TOP_CENTER);

		grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setPadding(new Insets(10));
		grid.setHgap(10);
		grid.setVgap(10);

		titleLbl = new Label("Vendor Dashboard");
		titleLbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

		userGreetingLbl = new Label("Hello, " + UserSession.getLoggedInUser().getUsername() + "!");
		userGreetingLbl.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

		productNameLbl = new Label("Product Name:");
		productDescLbl = new Label("Product Description:");

		productErrorLbl = new Label();
		productErrorLbl.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

		productNameField = new TextField();
		productDescField = new TextField();

		viewInvitationBtn = new Button("View Invitations");
		viewInvitationBtn.setOnAction(this);

		viewAccEventBtn = new Button("View Accepted Events");
		viewAccEventBtn.setOnAction(this);

		viewProductBtn = new Button("View Products");
		viewProductBtn.setOnAction(this);

		addProductBtn = new Button("Add Product");
		addProductBtn.setOnAction(this);

		scene = new Scene(container, 600, 550);
	}

	private void layoutComponents() {
		container.getChildren().add(userGreetingLbl);
		grid.add(productNameLbl, 0, 0);
		grid.add(productNameField, 1, 0);

		grid.add(productDescLbl, 0, 2);
		grid.add(productDescField, 1, 2);

		grid.add(productErrorLbl, 1, 3); 

		grid.add(addProductBtn, 0, 5);

		container.getChildren().addAll(titleLbl, grid, viewInvitationBtn, viewAccEventBtn, viewProductBtn);
		VBox.setMargin(viewInvitationBtn, new Insets(20, 0, 10, 0));
		VBox.setMargin(viewAccEventBtn, new Insets(0, 0, 10, 0));
		VBox.setMargin(viewProductBtn, new Insets(0, 0, 10, 0));
	}

	@Override
	public void handle(ActionEvent e) {
		// Get the logged-in user's ID from the session
		String vendorID = UserSession.getLoggedInUser().getUser_id();

		if (e.getSource() == addProductBtn) {
			String productName = productNameField.getText();
			String productDesc = productDescField.getText();

			// Clear previous error messages
			productErrorLbl.setText("");

			// Validate input
			String validationMessage = vendorController.checkManageVendorInput(productDesc, productName);

			if (validationMessage.equals("Validation successful")) {
				vendorController.manageVendor(productName, productDesc, vendorID);
				productErrorLbl.setText("Product added successfully!");
				productErrorLbl.setStyle("-fx-text-fill: green;");

				// Clear the form fields
				productNameField.clear();
				productDescField.clear();
			} else {
				productErrorLbl.setText(validationMessage);
			}
		}

		if (e.getSource() == viewProductBtn) {
	        pageController.navigateToViewProduct();
		}

		if (e.getSource() == viewInvitationBtn) {
	        pageController.navigateToViewInvitation();
		}

		if (e.getSource() == viewAccEventBtn) {
	        pageController.navigateToViewAcceptedEvents();
		}


	}

	public Scene getScene() {
		return scene;
	}
}
