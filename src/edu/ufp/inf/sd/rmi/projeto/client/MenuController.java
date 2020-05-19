package edu.ufp.inf.sd.rmi.projeto.client;

import edu.ufp.inf.sd.rmi.projeto.server.TaskSubjectRI;
import edu.ufp.inf.sd.rmi.projeto.server.UserSessionRI;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
    public ComboBox<String> hashTypeCB;
    public TextArea hashPassTA;
    public TextField deltaTaskTF;
    public Button createTaskBut;
    public Label messageCreateTask;

    /** List tasks **/
    public TableView<TaskSubjectRI> tasksTable;
    public TableColumn<TaskSubjectRI, String> nameCol;
    public TableColumn<TaskSubjectRI, String> hashTypeCol;
    public TableColumn<TaskSubjectRI, String> creditsPerWordCol;
    public TableColumn<TaskSubjectRI, String> creditsTotalCol;
    public TableColumn<TaskSubjectRI, String> availableCol;
    public Pagination listTasksPagination;

    /** Join task **/
    public Tab listTasksTab;
    public Label nameTaskSelectedLabel;
    public Spinner<Integer> numberThreadsSpinner;
    public Button jointTaskBut;
    public Label messageJoinTask;

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
        hashTypeCB.setValue("SHA-512");
        hashTypeCB.getItems().add("SHA-512");
        hashTypeCB.getItems().add("PBKDF2");
        hashTypeCB.getItems().add("BCrypt");
        hashTypeCB.getItems().add("SCrypt");
    }

    public void initializeTableViewListTasks(){
        //goes to the class and associates de col with the variable
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        hashTypeCol.setCellValueFactory(new PropertyValueFactory<>("hashType"));
        creditsPerWordCol.setCellValueFactory(new PropertyValueFactory<>("creditsPerWord"));
        creditsTotalCol.setCellValueFactory(new PropertyValueFactory<>("creditsTotal"));
        availableCol.setCellValueFactory(new PropertyValueFactory<>("available"));
        tasksTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    nameTaskSelectedLabel.setText(tasksTable.getSelectionModel().getSelectedItem().getName());
                } catch (Exception ignored) { }
            }
        });
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
        tasksTable.getItems().addAll(taskSubjectRIsList);
    }

    public void handlerCreateTask(ActionEvent actionEvent) throws RemoteException {
        String name = nameTaskTF.getText();
        String typeHash = hashTypeCB.getValue();
        String hashPass = hashPassTA.getText();
        Integer delta = Integer.parseInt(deltaTaskTF.getText());

        if(!name.isEmpty() && !hashPass.isEmpty()){
            TaskSubjectRI taskSubjectRI = this.client.userSessionRI.createTask(name, typeHash, hashPass,delta);
            if(taskSubjectRI != null){
                this.client.tasksRI.add(taskSubjectRI);
                nameTaskTF.clear();
                hashPassTA.clear();
                initializeHashTypeCombBox();
                messageCreateTask.setWrapText(true);
                messageCreateTask.setText("Task was created successfully.");
            }else{
                initializeHashTypeCombBox();
                messageCreateTask.setWrapText(true);
                messageCreateTask.setText("Task was not created! Name already exists, choose other one.");
            }
        }
    }

    public void handlerJoinTask(ActionEvent actionEvent) {///comparar threads, etc.....
//        workerObserverImpl.selectTask(nameTaskSelectedLabel.getValue());
    }
}
