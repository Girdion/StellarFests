package view;

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
import model.EventOrganizer;
import model.Invitation;
import model.Vendor;
import util.UserSession;

import java.util.List;
import java.util.UUID;

public class AddVendorPage {

    private Scene scene;
    private Stage stage;
    private VBox container;
    private TableView<Vendor> vendorTable;
    private TableColumn<Vendor, String> vendorIDColumn;
    private TableColumn<Vendor, String> vendorNameColumn;
    private TableColumn<Vendor, String> vendorLocationColumn;
    private Button inviteButton;
    private Button backBtn;
    private PageController pageController;
    private ObservableList<Vendor> vendorList;
    private EventOrganizer selectedEvent;

    public AddVendorPage(Stage stage, PageController pageController, EventOrganizer selectedEvent) {
        this.stage = stage;
        this.pageController = pageController;
        this.selectedEvent = selectedEvent;
        initializeComponents();
        layoutComponents();
    }

    public void show() {
        this.stage.setTitle("Add Vendors to Event");
        this.stage.setScene(scene);
        this.stage.show();
    }

    private void initializeComponents() {
        container = new VBox(10);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.TOP_CENTER);

        Label titleLbl = new Label("Add Vendors to Event");
        titleLbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        vendorTable = new TableView<>();
        vendorTable.setEditable(true);

        vendorIDColumn = new TableColumn<>("Vendor ID");
        vendorIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser_id()));

        vendorNameColumn = new TableColumn<>("Vendor Name");
        vendorNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));

        TableColumn<Vendor, Boolean> selectColumn = new TableColumn<>("Select");
        selectColumn.setCellFactory(param -> new TableCell<Vendor, Boolean>() {
            private final CheckBox checkBox = new CheckBox();

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Vendor vendor = getTableView().getItems().get(getIndex());
                    checkBox.setSelected(vendor.isSelected());
                    checkBox.setOnAction(e -> vendor.setSelected(checkBox.isSelected()));
                    setGraphic(checkBox);
                }
            }
        });

        vendorTable.getColumns().addAll(vendorIDColumn, vendorNameColumn, selectColumn);

        loadVendorData();

        inviteButton = new Button("Invite Selected Vendors");
        inviteButton.setOnAction(e -> inviteVendors());

        backBtn = new Button("Back");
        backBtn.setOnAction(e -> pageController.navigateBack());

        scene = new Scene(container, 600, 400);
    }



    private void layoutComponents() {
        container.getChildren().addAll(new Label("Add Vendors to Event"), vendorTable, inviteButton, backBtn);
        VBox.setMargin(backBtn, new Insets(20, 0, 0, 0));
    }

    private void loadVendorData() {
        Vendor vendorInstance = new Vendor();
        List<Vendor> vendors = vendorInstance.getAllVendors();

        vendorList = FXCollections.observableArrayList(vendors);
        vendorTable.setItems(vendorList);
    }

    private void inviteVendors() {
        ObservableList<Vendor> selectedVendors = FXCollections.observableArrayList();
        for (Vendor vendor : vendorTable.getItems()) {
            if (vendor.isSelected()) {
                selectedVendors.add(vendor);
            }
        }

        if (selectedVendors.isEmpty()) {
            showErrorMessage("Please select at least one vendor to invite.");
            return;
        }

        for (Vendor vendor : selectedVendors) {
            try {
                int vendorId = Integer.parseInt(vendor.getUser_id());
                if (vendor.hasBeenInvited(vendorId, selectedEvent.getEvent_id())) {
                    showErrorMessage("Vendor " + vendor.getUsername() + " has already been invited to this event.");
                    return;
                }
            } catch (NumberFormatException e) {
                showErrorMessage("Invalid vendor ID for " + vendor.getUsername());
                return;
            }
        }

        for (Vendor vendor : selectedVendors) {
            sendInvitation(vendor);
        }

        showSuccessMessage("Invitations sent successfully.");
        pageController.navigateBack();
    }

    private void sendInvitation(Vendor vendor) {
        // Create a new Invitation object
        Invitation invitation = new Invitation(UUID.randomUUID().toString(), selectedEvent.getEvent_id(), vendor.getUser_id(), "Pending", "Vendor");

        // Try to save the invitation to the database
        boolean isInvited = invitation.save();  // Assuming Invitation class has a save method

        if (isInvited) {
            System.out.println("Invitation sent to vendor: " + vendor.getUsername());
        } else {
            System.err.println("Failed to send invitation to vendor: " + vendor.getUsername());
        }
    }


    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Scene getScene() {
        return scene;
    }
}
