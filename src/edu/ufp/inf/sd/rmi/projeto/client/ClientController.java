package edu.ufp.inf.sd.rmi.projeto.client;

import edu.ufp.inf.sd.rmi.projeto.server.TaskSubjectRI;
import edu.ufp.inf.sd.rmi.projeto.server.UserFactoryRI;
import edu.ufp.inf.sd.rmi.projeto.server.UserSessionRI;
import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientController implements Initializable {
    /** Create task **/
    public Tab createTaskTab;
    public TextField nameTaskTF;
    public Button createTaskBut;
    public ComboBox<String> hashTypeCB;
    public TextField hashPassTF;
    public Label messageCreateTask;
    /** Join task **/
    public Tab listTasksTab;
    public ComboBox<String> nameTasksCB;
    public Button jointTaskBut;
    public Label messageJoinTask;
    /** List tasks **/
    public TableView<String> tasksTable;
    public TableColumn<TaskSubjectRI, String> nameCol;
    public TableColumn<TaskSubjectRI, String> passHashCol;
    public TableColumn<TaskSubjectRI, String> hashTypeCol;
    public TableColumn<TaskSubjectRI, String> threadsCol;
    public TableColumn<TaskSubjectRI, String> availableCol;
    /** Login **/
    public PasswordField passwordLoginTF;
    public PasswordField passwordRegisterTF;
    public TextField usernameLoginTF;
    /** Register **/
    public Button registerBut;
    public TextField usernameRegisterTF;
    public PasswordField confirmPasswordRegisterTF;
    public Label messageLoginRegister;

    private Client client;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // connection
        client = new Client();
        client.lookupService();
        client.playService();

        if(hashTypeCB != null){
            hashTypeCB.getItems().clear();
            hashTypeCB.setPromptText("SHA-512");
            hashTypeCB.getItems().add("SHA-512");
            hashTypeCB.getItems().add("PBKDF2");
            hashTypeCB.getItems().add("BCrypt");
            hashTypeCB.getItems().add("SCrypt");

            //goes to the class and associates de col with the variable
            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            passHashCol.setCellValueFactory(new PropertyValueFactory<>("hashType"));
            hashTypeCol.setCellValueFactory(new PropertyValueFactory<>("hashPass"));
            //threadsCol.setCellValueFactory(new PropertyValueFactory<>("threads"));
            //availableCol.setCellValueFactory(new PropertyValueFactory<>("available"));
        }
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

    public void handlerListTasks(Event event) {
        /*ArrayList<TaskSubjectRI> taskSubjectRIsList = userSessionRI.listTasks();
        tasksTable.getItems().clear();
        tasksTable.getItems().addAll(taskSubjectRIsList);*/
    }

    public void handlerCreateTask(ActionEvent actionEvent) {
        /*String name = nameTaskTF.getText();
        String typeHash = hashTypeCB.getValue();
        String hashPass = hashPassTF.getText();

        if(!name.isEmpty() && !hashPass.isEmpty()){
            TaskSubjectRI taskSubjectRI = this.userSessionRI.createTask(name, typeHash, hashPass);
            if(taskSubjectRI != null){
                taskSubjectRIs.add(taskSubjectRI);
                nameTaskTF.clear();
                hashTypeCB.setText("SHA-512");
                hashPassTF.clear();
                messageCreateTask.setText("Task was created successfully.");
            }else{
                messageCreateTask.setText("Task was not created! Name already exists, choose other one.");
            }
        }*/
    }

    public void handlerJoinTask(ActionEvent actionEvent) {///comparar threads, etc.....
        //workerObserverImpl.selectTask(nameTasksCB.getValue());
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
        goToMenuScene(actionEvent);
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
