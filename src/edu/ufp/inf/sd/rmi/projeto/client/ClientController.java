package edu.ufp.inf.sd.rmi.projeto.client;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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
    public TableColumn<String, String> nameCol;
    public TableColumn<String, String> passHashCol;
    public TableColumn<String, String> hashCol;
    public TableColumn<String, String> threadsCol;
    public TableColumn<String, String> availableCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        typeHashCB.getItems().clear();
        typeHashCB.setPromptText("SHA-512");
        typeHashCB.getItems().add("SHA-512");
        typeHashCB.getItems().add("PBKDF2");
        typeHashCB.getItems().add("BCrypt");
        typeHashCB.getItems().add("SCrypt");
    }

    public void handleReadFile(ActionEvent actionEvent) {
        System.out.println("olaaaaaaaaaaa");
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
