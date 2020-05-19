package edu.ufp.inf.sd.rmi.projeto.client;

import edu.ufp.inf.sd.rmi.projeto.server.Task;
import edu.ufp.inf.sd.rmi.projeto.server.TaskSubjectImpl;
import edu.ufp.inf.sd.rmi.projeto.server.TaskSubjectRI;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
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
    public Button joinTaskBut;
    public Label messageJoinTask;
    /** List own tasks **/
    public Tab listOwnTasksTab;
    public TableView<TaskSubjectRI> tasksOwnTable;
    public TableColumn<TaskSubjectRI, String> nameTOwnCol;
    public TableColumn<TaskSubjectRI, String> hashTypeTOwnCol;
    public TableColumn<TaskSubjectRI, String> creditsPerWordTOwnCol;
    public TableColumn<TaskSubjectRI, String> creditsTotalTOwnCol;
    public TableColumn<TaskSubjectRI, String> statusTOwnCol;
    public Pagination listOwnTasksPagination;
    public Label messageOwnTask;
    public Label nameOwnTaskSelectedLabel;
    public Button pauseTaskBut;
    public Button stopTaskBut;
    /** List own workers **/
    public Tab listOwnWorkersTab;
    public TableView<WorkerObserverRI> workersOwnTable;
    public TableColumn<WorkerObserverRI, String> nameTaskWOwnCol;
    public TableColumn<WorkerObserverRI, String> hashTypeWOwnCol;
    public TableColumn<WorkerObserverRI, Integer> threadsWOwnCol;
    public TableColumn<WorkerObserverRI, Integer> wordsWOwnCol;
    public TableColumn<WorkerObserverRI, Integer> creditsWonWOwnCol;
    public TableColumn<WorkerObserverRI, String> statusWOwnCol;
    public Pagination listOwnWorkersPagination;
    public Label messageOwnWorker;
    public Label idOwnWorkerLabel;
    public Button pauseWorkerBut;
    public Button stopWorkerBut;

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
        initializeTableViewListOwnTasks();
        initializeTableViewListOwnWorkers();
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
                    if(tasksTable.getSelectionModel().getSelectedItem().isAvailable()){
                        nameTaskSelectedLabel.setText(tasksTable.getSelectionModel().getSelectedItem().getName());
                    }
                } catch (Exception ignored) { }
            }
        });
    }

    public void initializeTableViewListOwnTasks(){
        //goes to the class and associates de col with the variable
        nameTaskWOwnCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        hashTypeWOwnCol.setCellValueFactory(new PropertyValueFactory<>("hashType"));
        creditsPerWordTOwnCol.setCellValueFactory(new PropertyValueFactory<>("creditsPerWord"));
        creditsTotalTOwnCol.setCellValueFactory(new PropertyValueFactory<>("creditsTotal"));
        statusTOwnCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        tasksOwnTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    nameOwnTaskSelectedLabel.setText(tasksTable.getSelectionModel().getSelectedItem().getName());
                } catch (Exception ignored) { }
            }
        });
    }

    public void initializeTableViewListOwnWorkers(){
        //goes to the class and associates de col with the variable
        nameTaskWOwnCol.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        hashTypeWOwnCol.setCellValueFactory(new PropertyValueFactory<>("hashType"));
        threadsWOwnCol.setCellValueFactory(new PropertyValueFactory<>("n_threads"));
        wordsWOwnCol.setCellValueFactory(new PropertyValueFactory<>("wordsSize"));
        creditsWonWOwnCol.setCellValueFactory(new PropertyValueFactory<>("creditsWon"));
        statusWOwnCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        workersOwnTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    idOwnWorkerLabel.setText(workersOwnTable.getSelectionModel().getSelectedItem().getId().toString());
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
        tasksTable.getItems().clear();
        tasksTable.getItems().addAll(this.client.userSessionRI.listTasks());
    }

    public void handlerCreateTask(ActionEvent actionEvent) throws RemoteException {
        String name = nameTaskTF.getText();
        String typeHash = hashTypeCB.getValue();
        ArrayList<String> hashPass = new ArrayList<>(Arrays.asList(hashPassTA.getText().split(";")));
        Integer delta = Integer.parseInt(deltaTaskTF.getText());

        if(!name.isEmpty() && !hashPass.isEmpty()){
            TaskSubjectRI taskSubjectRI = this.client.userSessionRI.createTask(name, typeHash, hashPass,delta);
            if(taskSubjectRI != null){
                this.client.tasksRI.add(taskSubjectRI);
                nameTaskTF.clear();
                hashPassTA.clear();
                deltaTaskTF.clear();
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

    /** associar worker a um taskgroup */
    public void handlerJoinTask(ActionEvent actionEvent) throws RemoteException {
        TaskSubjectRI taskSubjectRI = tasksTable.getSelectionModel().getSelectedItem();
        if(taskSubjectRI.isAvailable()){
            Task task = taskSubjectRI.getTaskFromArray();
            int n_threads = numberThreadsSpinner.getValue();
            WorkerObserverRI workerObserverRI = new WorkerObserverImpl(client.workersRI.size()+1, client.username,task, n_threads);
            client.workersRI.add(workerObserverRI);
        }
        initializeTableViewListTasks();
    }

    public void handlerListOwnTasks(Event event) {
        tasksOwnTable.getItems().clear();
        tasksOwnTable.getItems().addAll(this.client.tasksRI);
    }

    public void handlerPauseTask(ActionEvent actionEvent) {

    }

    public void handlerStopTask(ActionEvent actionEvent) {
    }

    public void handlerListOwnWorkers(Event event) {
        workersOwnTable.getItems().clear();
        workersOwnTable.getItems().addAll(this.client.workersRI);
    }

    public void handlerPauseWorker(ActionEvent actionEvent) {
    }

    public void handlerStopWorker(ActionEvent actionEvent) {
    }
}
