package edu.ufp.inf.sd.rmi.projeto.client;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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
        
    }

    public void handlerLogin(ActionEvent actionEvent) {
    }
}
