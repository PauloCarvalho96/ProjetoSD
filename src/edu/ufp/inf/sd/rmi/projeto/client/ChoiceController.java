package edu.ufp.inf.sd.rmi.projeto.client;

import edu.ufp.inf.sd.rmi.projeto.server.UserSessionRI;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class ChoiceController implements Initializable {

    public RadioButton strategy1RB;
    public RadioButton strategy2RB;
    public RadioButton strategy3RB;
    public Button startBut;

    private int choice = 0;

    private Client client;

    public void initData(Client client){ this.client = client; }

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    public void goToStrategyScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("layouts/strategy" + choice + ".fxml"));
        Parent root = loader.load();
        root.setStyle("-fx-background-color: #c4c4c4;");
        Scene scene = new Scene(root);
        switch (choice){
            case 1:
                Strategy1Controller strategy1Controller = loader.getController();
                strategy1Controller.initData(this.client);
                break;
            case 2:
                Strategy2Controller strategy2Controller = loader.getController();
                strategy2Controller.initData(this.client);
                break;
            case 3:
                Strategy3Controller strategy3Controller = loader.getController();
                strategy3Controller.initData(this.client);
                break;
        }
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Strategy " + choice);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream( "logo/logo.png" )));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void handlerStartStrategy(ActionEvent actionEvent) throws IOException {
        if(choice > 0 && choice < 4){
            goToStrategyScene(actionEvent);
        }
    }

    public void handlerStrategy1(ActionEvent actionEvent) { choice = 1; }

    public void handlerStrategy2(ActionEvent actionEvent) { choice = 2; }

    public void handlerStrategy3(ActionEvent actionEvent) { choice = 3; }
}
