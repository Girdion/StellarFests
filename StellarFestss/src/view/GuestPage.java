package view;

import controller.EventController;
import controller.GuestController;
import controller.InvitationController;
import controller.PageController;
import controller.VendorController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.UserSession;

public class GuestPage implements EventHandler<ActionEvent> {

	private Scene scene;
	private Stage stage;
	private VBox container;
	private GridPane grid;
	private Label titleLbl, userGreetingLbl;
	private TextField invitationIdField;
	private Button viewInvitationsBtn, viewAcceptedEventsBtn;
	private Label errorLbl;

	private PageController pageController;
	private InvitationController invitationController;
	private GuestController guestController;
	private EventController eventController;

	public GuestPage(Stage stage, PageController pageController, GuestController guestController) {
		this.stage = stage;
		this.pageController = pageController;
		this.guestController = new GuestController();
		this.invitationController = new InvitationController();
		this.eventController = new EventController();
		initializeComponents();
		layoutComponents();
	}

	public void show() {
		this.stage.setTitle("Guest Page");
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

		titleLbl = new Label("Guest Dashboard");
		titleLbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

		userGreetingLbl = new Label("Hello, " + UserSession.getLoggedInUser().getUsername() + "!");
		userGreetingLbl.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

		errorLbl = new Label();
		errorLbl.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

		invitationIdField = new TextField();
		invitationIdField.setPromptText("Enter Invitation ID");

		viewInvitationsBtn = new Button("View Invitations");
		viewInvitationsBtn.setOnAction(this);

		viewAcceptedEventsBtn = new Button("View Accepted Events");
		viewAcceptedEventsBtn.setOnAction(this);

		scene = new Scene(container, 600, 550);
	}

	private void layoutComponents() {
		container.getChildren().addAll(userGreetingLbl, titleLbl);

		VBox.setMargin(viewInvitationsBtn, new Insets(10, 0, 0, 0));
		VBox.setMargin(viewAcceptedEventsBtn, new Insets(10, 0, 0, 0));

		container.getChildren().addAll(
				viewInvitationsBtn,
				viewAcceptedEventsBtn,
				errorLbl
				);
	}

	@Override
	public void handle(ActionEvent e) {
		if (e.getSource() == viewInvitationsBtn) {
			pageController.navigateToViewInvitation();
		}

		if (e.getSource() == viewAcceptedEventsBtn) {
			pageController.navigateToViewAcceptedEvents();
		}
	}


	public Scene getScene() {
		return scene;
	}
}
