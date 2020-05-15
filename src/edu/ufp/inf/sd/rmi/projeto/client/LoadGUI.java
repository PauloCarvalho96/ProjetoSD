package edu.ufp.inf.sd.rmi.projeto.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class LoadGUI extends Application {

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("layouts/authentication.fxml"));
        root.setStyle("-fx-background-color: #F0591E;");
        Scene scene = new Scene(root);
        primaryStage.setTitle("Authentication");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream( "logo/logo.png" )));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
