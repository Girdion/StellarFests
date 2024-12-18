package view;

import java.util.Vector;

import controller.PageController;
import controller.VendorController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Product;
import model.User;
import util.UserSession;

public class ViewProductPage implements EventHandler<ActionEvent> {
	private Scene scene;
	private Stage stage;
	private VBox container;
	private Label titleLbl, userGreetingLbl;
	private TableView<Product> productTable;
	private Button backBtn;

	private PageController pageController;
	private VendorController vendorController;

	private UserSession userSession;

	public ViewProductPage(Stage stage, PageController pageController, VendorController vendorController) {
		this.stage = stage;
		this.pageController = pageController;
		this.vendorController = vendorController;
		initializeComponents();
		layoutComponents();
	}

	private void layoutComponents() {
		container = new VBox(10);
		container.setPadding(new Insets(20));
		container.getChildren().addAll(titleLbl, userGreetingLbl, productTable, backBtn);

		scene = new Scene(container, 600, 400);
	}

	private void initializeComponents() {
		titleLbl = new Label("Products");
		titleLbl.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

		String userName = UserSession.getLoggedInUser().getUsername();
		userGreetingLbl = new Label("Hello, " + userName + "! Here are your products:");

		// Table for products
		productTable = new TableView<>();
		TableColumn<Product, String> nameCol = new TableColumn<>("Product Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("product_name"));

		TableColumn<Product, String> descCol = new TableColumn<>("Description");
		descCol.setCellValueFactory(new PropertyValueFactory<>("product_description"));

		productTable.getColumns().addAll(nameCol, descCol);
		productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		// Load data into the table
		loadProducts();

		// Back button
		backBtn = new Button("Back");
	    backBtn.setOnAction(e -> pageController.navigateBack());
	}
	
	private void loadProducts() {
		User currentUser = UserSession.getLoggedInUser();
		int userID = Integer.parseInt(currentUser.getUser_id());
		String userId = currentUser.getUser_id();
        Vector<String> products = vendorController.viewProducts(userID);

        for (String productDetails : products) {
            String[] parts = productDetails.split(", ");
            String productName = parts[0];
            String productDescription = parts[1];
            
            Product product = new Product();
            product = new Product(product.getProduct_id(), productName, productDescription, userId);
            productTable.getItems().add(product);
        }
    }

	@Override
	public void handle(ActionEvent event) {
//		if (event.getSource() == backBtn) {
//            pageController.navigateToVendor();;
//        }
	}

	public Scene getScene() {
		return scene;
	}

}
