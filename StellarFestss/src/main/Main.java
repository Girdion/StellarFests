package main;

import javafx.application.Application;
import javafx.stage.Stage;
import util.Database;
import controller.PageController;
import controller.UserController;
import view.RegisterPage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        // Pass the stage to the PageController constructor
        PageController pageController = new PageController(stage);
        UserController userController = new UserController();

        // Create an instance of RegisterPage with the necessary parameters
        RegisterPage registerPage = new RegisterPage(stage, pageController, userController);
        
        // Show the registration page
        registerPage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
