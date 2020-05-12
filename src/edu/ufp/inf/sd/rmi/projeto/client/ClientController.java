package edu.ufp.inf.sd.rmi.projeto.client;

import edu.ufp.inf.sd.rmi.projeto.server.UserSessionRI;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    /** Create task **/
    public Tab createTaskTab;
    public TextField nameTaskTF;
    public Button createTaskBut;
    public ComboBox<String> hashTypeCB;
    public TextField hashPassTaskTF;
    public Tab listTasksTab;
    /** Join task **/
    public ComboBox<String> nameTasksCB;
    public Button jointTaskBut;
    public Label messageJoinTask;
    /** List tasks **/
    public TableView<String> professionalsDPTable;
    public TableColumn<String, String> nameCol;
    public TableColumn<String, String> passHashCol;
    public TableColumn<String, String> hashCol;
    public TableColumn<String, String> threadsCol;
    public TableColumn<String, String> availableCol;
    /** Login **/
    public PasswordField passwordLoginTF;
    public PasswordField passwordRegisterTF;
    public TextField usernameLoginTF;
    /** Register **/
    public Button registerBut;
    public TextField usernameRegisterTF;
    public PasswordField confirmPasswordRegisterTF;
    public Label messageLoginRegister;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hashTypeCB.getItems().clear();
        hashTypeCB.setPromptText("SHA-512");
        hashTypeCB.getItems().add("SHA-512");
        hashTypeCB.getItems().add("PBKDF2");
        hashTypeCB.getItems().add("BCrypt");
        hashTypeCB.getItems().add("SCrypt");
    }

    public void handleReadFile(ActionEvent actionEvent) {
    }

    public void handleReadBinFile(ActionEvent actionEvent) {
    }

    public void handleSaveFile(ActionEvent actionEvent) {
    }

    public void handleSaveBinFile(ActionEvent actionEvent) {
    }

    public void handleExit(ActionEvent actionEvent) {
    }

    public void handleAbout(ActionEvent actionEvent) {
    }

    public void handlerCreateTask(ActionEvent actionEvent) {
    }

    public void handlerNameTasksCB(ActionEvent actionEvent) {
    }

    public void handlerJoinTask(ActionEvent actionEvent) {
    }

    public void handlerHashType(ActionEvent actionEvent) {
    }

    public void handlerRegister(ActionEvent actionEvent) {
        /*String username = usernameRegisterTF.getText();
        String password = passwordRegisterTF.getText();
        String confirmPassword = confirmPasswordRegisterTF.getText();

        if(!username.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty() && password.equals(confirmPassword)){
            if(this.userFactoryRI.register(username, password)){
                messageLoginRegister.setText("Registered with success, do login please.");
            } else {
                messageLoginRegister.setText("Register didn't succeeded! Username is already taked...");
            }
        }*/
    }

    public void handlerLogin(ActionEvent actionEvent) throws IOException {
        /*String username = usernameLoginTF.getText();
        String password = passwordLoginTF.getText();

        if(!username.isEmpty() && !password.isEmpty()) {
            UserSessionRI sessionRI = this.userFactoryRI.login(username, password);
            if (sessionRI != null) {
                goToMenuScene(actionEvent);
            } else {
                messageLoginRegister.setText("Login didn't succeeded! Username doesn't exist or data don't match...");
            }
        }*/
    }

    public void goToMenuScene(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("layouts/menu.fxml"));
        Scene scene = new Scene(root, 1400, 1000);
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("Sistemas distribuídos");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
