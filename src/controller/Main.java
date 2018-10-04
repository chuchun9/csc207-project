package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tagram.LoadSave;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Tagram.fxml"));
        primaryStage.setTitle("ImageManager");
        primaryStage.setOnCloseRequest(event -> LoadSave.saveImages());
        primaryStage.setScene(new Scene(root, 1024, 720));
        primaryStage.show();
    }


}
