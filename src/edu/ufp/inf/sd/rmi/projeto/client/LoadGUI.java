package edu.ufp.inf.sd.rmi.projeto.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class LoadGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("layouts/login_register.fxml"));
        Scene scene = new Scene(root, 1400, 1000);
        primaryStage.setTitle("Sistemas distribuidos");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
