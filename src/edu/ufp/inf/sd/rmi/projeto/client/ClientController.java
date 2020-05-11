package edu.ufp.inf.sd.rmi.projeto.client;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    public Tab createTaskTab;
    public TextField nameTaskTF;
    public Button createTaskBut;
    public ComboBox<String> typeHashCB;
    public TextField hashPassTaskTF;
    public Tab listTasksTab;
    public ComboBox<String> nameTasksCB;
    public Button jointTaskBut;
    public TableView<String> professionalsDPTable;
    public TableColumn nameCol;
    public TableColumn passHashCol;
    public TableColumn hashCol;
    public TableColumn threadsCol;
    public TableColumn availableCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        typeHashCB.getItems().clear();
        typeHashCB.getItems().add("SHA-512");
        typeHashCB.getItems().add("PBKDF2");
        typeHashCB.getItems().add("BCrypt");
        typeHashCB.getItems().add("SCrypt");
    }

    public void handlerLogin(ActionEvent actionEvent) throws IOException {

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

    public void handlerTypeHash(ActionEvent actionEvent) {
    }
}
