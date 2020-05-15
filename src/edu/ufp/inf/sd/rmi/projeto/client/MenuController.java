package edu.ufp.inf.sd.rmi.projeto.client;

import edu.ufp.inf.sd.rmi.projeto.server.TaskSubjectRI;
import edu.ufp.inf.sd.rmi.projeto.server.UserSessionRI;
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
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
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
    public TableView<TaskSubjectRI> tasksTable;
    public TableColumn<TaskSubjectRI, String> nameCol;
    public TableColumn<TaskSubjectRI, String> passHashCol;
    public TableColumn<TaskSubjectRI, String> hashTypeCol;
    public TableColumn<TaskSubjectRI, String> threadsCol;
    public TableColumn<TaskSubjectRI, String> availableCol;
    public Pagination listTasksPagination;

    private Client client;

    public void initData(Client client){
        this.client = client;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeComboBoxAndTableView();
    }

    public void initializeComboBoxAndTableView(){
        initializeHashTypeCombBox();
        initializeTableViewListTasks();
    }

    public void initializeHashTypeCombBox(){
        hashTypeCB.getItems().clear();
        hashTypeCB.setPromptText("SHA-512");
        hashTypeCB.getItems().add("SHA-512");
        hashTypeCB.getItems().add("PBKDF2");
        hashTypeCB.getItems().add("BCrypt");
        hashTypeCB.getItems().add("SCrypt");
    }

    public void initializeTableViewListTasks(){
        //goes to the class and associates de col with the variable
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
//        passHashCol.setCellValueFactory(new PropertyValueFactory<>("hashType"));
        hashTypeCol.setCellValueFactory(new PropertyValueFactory<>("hashPass"));
        //threadsCol.setCellValueFactory(new PropertyValueFactory<>("threads"));
        //availableCol.setCellValueFactory(new PropertyValueFactory<>("available"));
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

    public void handlerListTasks(Event event) throws RemoteException {
        ArrayList<TaskSubjectRI> taskSubjectRIsList = this.client.userSessionRI.listTasks();
        tasksTable.getItems().clear();
        tasksTable.getItems().addAll(taskSubjectRIsList);///////probably has to be task impl
    }

    public void handlerCreateTask(ActionEvent actionEvent) throws RemoteException {
        String name = nameTaskTF.getText();
        String typeHash = hashTypeCB.getValue();
        String hashPass = hashPassTF.getText();

        if(!name.isEmpty() && !hashPass.isEmpty()){
            TaskSubjectRI taskSubjectRI = this.client.userSessionRI.createTask(name, typeHash, hashPass);
            if(taskSubjectRI != null){
                this.client.tasksRI.add(taskSubjectRI);
                nameTaskTF.clear();
                hashPassTF.clear();
                messageCreateTask.setWrapText(true);
                messageCreateTask.setText("Task was created successfully.");
            }else{
                messageCreateTask.setWrapText(true);
                messageCreateTask.setText("Task was not created! Name already exists, choose other one.");
            }
        }
    }

    public void handlerJoinTask(ActionEvent actionEvent) {///comparar threads, etc.....
        //workerObserverImpl.selectTask(nameTasksCB.getValue());
    }
}
