package edu.ufp.inf.sd.rmi.projeto.client;

import edu.ufp.inf.sd.rmi.projeto.server.Result;
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
import java.util.*;

public class MenuController implements Initializable {
    /** Create task **/
    public Tab createTaskTab;
    public TextField nameTaskTF;
    public TextField creditsProcTaskTF;
    public TextField creditsFoundTaskTF;
    public ComboBox<String> hashTypeCB;
    public TextArea hashPassTA;
    public TextField deltaTaskTF;
    public ComboBox<String> strategyCB;
    public Label lengthPassTaskLabel;
    public TextField lengthPassTaskTF;
    public Label alphabetTaskLabel;
    public TextField alphabetTaskTF;
    public Button createTaskBut;
    public Label messageCreateTask;
    /** List tasks **/
    public Tab listTasksTab;
    public TableView<TaskSubjectRI> tasksTable;
    public TableColumn<TaskSubjectRI, String> nameCol;
    public TableColumn<TaskSubjectRI, String> hashTypeCol;
    public TableColumn<TaskSubjectRI, String> creditsPerWordCol;
    public TableColumn<TaskSubjectRI, String> creditsTotalCol;
    public TableColumn<TaskSubjectRI, String> availableCol;
    public Pagination listTasksPagination;
    /** Join task **/
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
    public Button resumeTaskBut;
    public Button stopTaskBut;
    public TableView<Result> infoOwnTaskTable;
    public TableColumn<Result, String> hashPassTOwnCol;
    public TableColumn<Result, String> resultTOwnCol;
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
    /** Update all **/
    public Button updateBut;

    private Client client;

    public void initData(Client client){ this.client = client; }

    @Override
    public void initialize(URL location, ResourceBundle resources) { initializeComboBoxAndTableView(); }

    public void initializeComboBoxAndTableView(){
        initializeComboBox();
        initializeTableViewListTasks();
        initializeTableViewListOwnTasks();
        initializeTableViewListOwnWorkers();
        initializeTableViewListInfoTask();
        updateBut.setVisible(false);
        lengthPassTaskLabel.setVisible(false);
        lengthPassTaskTF.setVisible(false);
        alphabetTaskLabel.setVisible(false);
        alphabetTaskTF.setVisible(false);
    }

    public void initializeComboBox(){
        hashTypeCB.getItems().clear();
        hashTypeCB.setValue("SHA-512");
        hashTypeCB.getItems().add("SHA-512");
        hashTypeCB.getItems().add("PBKDF2");
        hashTypeCB.getItems().add("BCrypt");
        hashTypeCB.getItems().add("SCrypt");
        strategyCB.getItems().clear();
        strategyCB.setValue("Strategy 1");
        strategyCB.getItems().add("Strategy 1");
        strategyCB.getItems().add("Strategy 2");
        strategyCB.getItems().add("Strategy 3");
    }

    public void initializeTableViewListTasks(){
        //goes to the class and associates de col with the variable
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        hashTypeCol.setCellValueFactory(new PropertyValueFactory<>("hashType"));
        creditsPerWordCol.setCellValueFactory(new PropertyValueFactory<>("creditsWordProcessed"));
        creditsTotalCol.setCellValueFactory(new PropertyValueFactory<>("creditsWordFound"));
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
        nameTOwnCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        hashTypeTOwnCol.setCellValueFactory(new PropertyValueFactory<>("hashType"));
        creditsPerWordTOwnCol.setCellValueFactory(new PropertyValueFactory<>("creditsWordProcessed"));
        creditsTotalTOwnCol.setCellValueFactory(new PropertyValueFactory<>("creditsWordFound"));
        statusTOwnCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        tasksOwnTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    nameOwnTaskSelectedLabel.setText(tasksOwnTable.getSelectionModel().getSelectedItem().getName());
                    infoOwnTaskTable.getItems().clear();
                    infoOwnTaskTable.getItems().addAll(tasksOwnTable.getSelectionModel().getSelectedItem().getResult());
                } catch (Exception ignored) { }
            }
        });
    }

    public void initializeTableViewListInfoTask(){
        hashPassTOwnCol.setCellValueFactory(new PropertyValueFactory<>("hash"));
        resultTOwnCol.setCellValueFactory(new PropertyValueFactory<>("result"));
    }

    public void initializeTableViewListOwnWorkers(){
        //goes to the class and associates de col with the variable
        nameTaskWOwnCol.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        hashTypeWOwnCol.setCellValueFactory(new PropertyValueFactory<>("hashType"));
        threadsWOwnCol.setCellValueFactory(new PropertyValueFactory<>("n_threads"));
        wordsWOwnCol.setCellValueFactory(new PropertyValueFactory<>("wordsSize"));
        creditsWonWOwnCol.setCellValueFactory(new PropertyValueFactory<>("creditsWon"));
        statusWOwnCol.setCellValueFactory(new PropertyValueFactory<>(""));
        workersOwnTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    idOwnWorkerLabel.setText(workersOwnTable.getSelectionModel().getSelectedItem().getId().toString());
                } catch (Exception ignored) { }
            }
        });
    }

    public void initializeCreateTask(){
        nameTaskTF.clear();
        hashPassTA.clear();
        creditsFoundTaskTF.clear();
        creditsProcTaskTF.clear();
        deltaTaskTF.clear();
        hashTypeCB.setValue("SHA-512");
        strategyCB.setValue("Strategy 1");
    }

    public void handleExit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void handleAbout(ActionEvent actionEvent) {
    }

    public void handlerCreateTaskTab(Event event) {
        try {
            updateBut.setVisible(false);
        } catch (NullPointerException ignored){}
    }

    public void handlerCreateTask(ActionEvent actionEvent) throws RemoteException {
        String name = nameTaskTF.getText();
        String typeHash = hashTypeCB.getValue();
        String[] stCB = strategyCB.getValue().split(" ");
        int strategy = Integer.parseInt(stCB[1]);
        String[] ha = hashPassTA.getText().split(";");
        ArrayList<String> hashPass = new ArrayList<>(Arrays.asList(ha));
        try {
            int creditsProc = Integer.parseInt(creditsProcTaskTF.getText());
            int creditsFound = Integer.parseInt(creditsFoundTaskTF.getText());
            int delta = Integer.parseInt(deltaTaskTF.getText());
            if (!name.isEmpty() && !hashPass.isEmpty() && strategyRequisites()) {
                delta = 500000;
                hashPass.clear();

                // aaa
                hashPass.add("d6f644b19812e97b5d871658d6d3400ecd4787faeb9b8990c1e7608288664be77257104a58d033bcf1a0e0945ff06468ebe53e2dff36e248424c7273117dac09");

                HashMap<String, String> data = new HashMap<>();
                if(strategy2Requisites()){
                    String[] le = lengthPassTaskTF.getText().split(";");
                    if(le.length != hashPass.size()){
                        messageCreateTask.setWrapText(true);
                        messageCreateTask.setText("Task was not created! Number of length pass is not the same as hash pass!");
                        return;
                    }
                    data.put("length",lengthPassTaskTF.getText());
                }else if(strategy3Requisites()){
                    String le = lengthPassTaskTF.getText();
                    try {
                        Integer.parseInt(le);
                    } catch (IllegalArgumentException e){
                        messageCreateTask.setText("Task was not created! Length pass has to be a number!");
                        messageCreateTask.setWrapText(true);
                        return;
                    }
                    data.put("length",lengthPassTaskTF.getText());
                    data.put("alphabet", alphabetTaskTF.getText());
                }
                TaskSubjectRI taskSubjectRI = this.client.userSessionRI.createTask(name, typeHash, hashPass, creditsProc, creditsFound, delta, client.username, strategy, data);
                if (taskSubjectRI != null) {
                    initializeCreateTask();
                    messageCreateTask.setWrapText(true);
                    messageCreateTask.setText("Task was created successfully.");
                } else {
                    messageCreateTask.setWrapText(true);
                    messageCreateTask.setText("Task was not created! Name already exists, choose other one.");
                }
            }
        }catch (IllegalArgumentException e){
            messageCreateTask.setWrapText(true);
            messageCreateTask.setText("Delta and credits have to be numbers!");
        }
    }

    public boolean strategyRequisites(){
        return strategy1Requisites() || strategy2Requisites() || strategy3Requisites();
    }

    public boolean strategy1Requisites(){
        return strategyCB.getValue().equals("Strategy 1");
    }

    public boolean strategy2Requisites(){
        return strategyCB.getValue().equals("Strategy 2") && !lengthPassTaskTF.getText().isEmpty();
    }

    public boolean strategy3Requisites(){
        return strategyCB.getValue().equals("Strategy 3") && !lengthPassTaskTF.getText().isEmpty()
                && !alphabetTaskTF.getText().isEmpty();
    }

    public void handlerListTasksTab(Event event) throws RemoteException {
        listTasks();
    }

    public void listTasks() throws RemoteException{
        tasksTable.getItems().clear();
        tasksTable.getItems().addAll(this.client.userSessionRI.listTasks());
        nameTaskSelectedLabel.setText("");
        messageJoinTask.setText("");
        updateBut.setVisible(true);
    }

    /** associar worker a um taskgroup */
    public void handlerJoinTask(ActionEvent actionEvent) throws RemoteException {
        TaskSubjectRI taskSubjectRI = tasksTable.getSelectionModel().getSelectedItem();
        if (taskSubjectRI != null && taskSubjectRI.isAvailable()) {
            int n_threads = numberThreadsSpinner.getValue();
            WorkerObserverRI workerObserverRI = new WorkerObserverImpl(this.client.userSessionRI.getSizeWorkersDB(), client.username, n_threads);
            this.client.userSessionRI.createWorker(workerObserverRI, client.username);
            taskSubjectRI.attach(workerObserverRI);     // adiciona worker na task
            initializeTableViewListTasks();
            listTasks();
            messageJoinTask.setWrapText(true);
            messageJoinTask.setText("Worker was created with success!");
        }
    }

    public void handlerListOwnTasksTab(Event event) throws RemoteException {
        listOwnTasks();
    }

    public void listOwnTasks() throws RemoteException {
        tasksOwnTable.getItems().clear();
        tasksOwnTable.getItems().addAll(this.client.getTasksRI());
        updateBut.setVisible(true);
    }


    public void handlerPauseTask(ActionEvent actionEvent) throws RemoteException {
        TaskSubjectRI taskSubjectRI = tasksOwnTable.getSelectionModel().getSelectedItem();
        if(taskSubjectRI != null){
            client.userSessionRI.pauseTask(taskSubjectRI);
        }
    }

    public void handlerStopTask(ActionEvent actionEvent) throws RemoteException {
        TaskSubjectRI taskSubjectRI = tasksOwnTable.getSelectionModel().getSelectedItem();
        if(taskSubjectRI != null){
            client.userSessionRI.stopTask(taskSubjectRI,client.username);
        }
    }

    public void handlerResumeTask(ActionEvent actionEvent) throws RemoteException {
        TaskSubjectRI taskSubjectRI = tasksOwnTable.getSelectionModel().getSelectedItem();
        if(taskSubjectRI != null){
            client.userSessionRI.resumeTask(taskSubjectRI);
        }
    }

    public void handlerListOwnWorkersTab(Event event) throws RemoteException{
        listOwnWorkers();
        nameOwnTaskSelectedLabel.setText("");
    }

    public void listOwnWorkers(){
        workersOwnTable.getItems().clear();
        workersOwnTable.getItems().addAll(this.client.getWorkersRI());
        updateBut.setVisible(true);
    }

    public void handlerPauseWorker(ActionEvent actionEvent) {
    }

    public void handlerStopWorker(ActionEvent actionEvent) throws RemoteException {///////////////test////////delete from tsk, get passwords that weren´t found
        WorkerObserverRI workerObserverRI = workersOwnTable.getSelectionModel().getSelectedItem();
        if(workerObserverRI != null){
            workerObserverRI.getStateWorker().setmsg("Completed");
        }
    }

    public void handlerUpdate(ActionEvent actionEvent) throws RemoteException {
        listTasks();
        listOwnTasks();
        listOwnWorkers();
        infoOwnTaskTable.getItems().clear();
    }

    public void handlerStrategyCB(ActionEvent actionEvent) {
        switch (strategyCB.getValue()){
            case "Strategy 1":
                lengthPassTaskLabel.setVisible(false);
                lengthPassTaskTF.setVisible(false);
                alphabetTaskLabel.setVisible(false);
                alphabetTaskTF.setVisible(false);
                break;
            case "Strategy 2":
                lengthPassTaskLabel.setVisible(true);
                lengthPassTaskTF.setVisible(true);
                alphabetTaskLabel.setVisible(false);
                alphabetTaskTF.setVisible(false);
                break;
            case "Strategy 3":
                lengthPassTaskLabel.setVisible(true);
                lengthPassTaskTF.setVisible(true);
                alphabetTaskLabel.setVisible(true);
                alphabetTaskTF.setVisible(true);
                break;
        }
    }
}