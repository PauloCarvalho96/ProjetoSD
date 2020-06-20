package edu.ufp.inf.sd.rmi.projeto.client;

import edu.ufp.inf.sd.rmi.projeto.server.UserSessionRI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.ResourceBundle;

public class AuthenticationController implements Initializable {
    /** Login **/
    public PasswordField passwordLoginTF;
    public PasswordField passwordRegisterTF;
    public TextField usernameLoginTF;
    /** Register **/
    public Button registerBut;
    public TextField usernameRegisterTF;
    public PasswordField confirmPasswordRegisterTF;
    public Label messageLoginRegister;
    public Label registerPassRequisitesLabel;

    private Client client;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clientConnection();
        initializeRegisterPassRequisitesLabel();
    }

    public void clientConnection(){
        client = new Client();
        client.lookupService();
    }

    public void initializeRegisterPassRequisitesLabel(){
        registerPassRequisitesLabel.setWrapText(true);
        registerPassRequisitesLabel.setText("Register password must have 8 or more characters, letters AND numbers.");
    }

    public void handlerRegister(ActionEvent actionEvent) throws RemoteException {
        String username = usernameRegisterTF.getText();
        String password = passwordRegisterTF.getText();
        String confirmPassword = confirmPasswordRegisterTF.getText();

        if(!username.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()){
            if(isInPassRequisites(password, confirmPassword)) {
                if (this.client.userFactoryRI.register(username, password)) {
                    this.client.username = username;
                    usernameRegisterTF.clear();
                    passwordRegisterTF.clear();
                    confirmPasswordRegisterTF.clear();
                    usernameLoginTF.clear();
                    passwordLoginTF.clear();
                    messageLoginRegister.setWrapText(true);
                    messageLoginRegister.setText("Registered with success, do login please.");
                } else {
                    messageLoginRegister.setWrapText(true);
                    messageLoginRegister.setText("Register didn't succeeded! Username is already taken...");
                }
            } else {
                messageLoginRegister.setWrapText(true);
                messageLoginRegister.setText("Register didn't succeeded! Password does not match with requisites...");
            }
        }
    }

    public boolean isInPassRequisites(String pass, String confPass){
        return pass.equals(confPass) && pass.length() >= 8 && containsJustLettersAndNumbers(pass);
    }

    public boolean containsJustLettersAndNumbers(String pass){
        boolean hasLetter = false;
        boolean hasNumber = false;
        for(char c: pass.toCharArray()){
            if(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
                hasLetter = true;
            }else if(c >= '0' && c <= '9'){
                hasNumber = true;
            }else{
                return false;
            }
        }
        return hasLetter && hasNumber;
    }

    public void handlerLogin(ActionEvent actionEvent) throws IOException {
        String username = usernameLoginTF.getText();
        String password = passwordLoginTF.getText();

        if(!username.isEmpty() && !password.isEmpty()) {
            UserSessionRI sessionRI = this.client.userFactoryRI.login(username, password);
            this.client.username = username;
            if (sessionRI != null) {
                this.client.userSessionRI = sessionRI;
                goToMenuScene(actionEvent);
            } else {
                messageLoginRegister.setWrapText(true);
                messageLoginRegister.setText("Login didn't succeeded! Username doesn't exist or data don't match...");
            }
        }
    }

    public void goToMenuScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("layouts/menu.fxml"));
        Parent root = loader.load();
        root.setStyle("-fx-background-color: #c4c4c4;");
        Scene scene = new Scene(root);
        MenuController menuController = loader.getController();
        menuController.initData(this.client);
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Menu");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream( "logo/logo.png" )));
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                try {
                    client.userSessionRI.logout(client.username, client.userSessionRI);
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
                Platform.exit();
                System.exit(0);
            }
        });
    }
}
