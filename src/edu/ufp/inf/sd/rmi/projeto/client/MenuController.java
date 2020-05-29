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
    public TextField maxLengthWordTF;
    public TextField maxWordsTF;
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

    public void initializeListTasks(){
        maxLengthWordTF.clear();
        maxWordsTF.clear();
        numberThreadsSpinner.getValueFactory().setValue(1);
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
        ArrayList<String> hashPass = new ArrayList<>(Arrays.asList(hashPassTA.getText().split(";")));
        try {
            int creditsProc = Integer.parseInt(creditsProcTaskTF.getText());
            int creditsFound = Integer.parseInt(creditsFoundTaskTF.getText());
            int delta = Integer.parseInt(deltaTaskTF.getText());
            if (!name.isEmpty() && !hashPass.isEmpty() && strategyRequisites()) {
                delta = 500000;
                hashPass.clear();
                hashPass.add("4e2083e0fc093f7f0fcf43b145fb586e476cdce4e38533462160a3656ef63f4ad75c027d45ee5ccbf652c8745210a2b7a1e652c79f0e8be3c926f591c4a667db");
                hashPass.add("91fae6a834ad600709174d63bb98d7ff8bc5b4dab65b83a53b640be44e1a78fbc9d5caac0c4ab53a9af0d77b79696fe460e98d87211cd66c16c436eeb9fb0b27");
                hashPass.add("f9cd0599ad0623251da70f2a9c97a9a89c2f034e9ab7a93cef3702d3c1d9b377738c6410079ad6a74cef9b84b4396621b4d0954a4419c302d389ce4ddbb03573");
                hashPass.add("26016268623f834338088a1492e3caf284ac00093fefef95ddfdb4f7ed34b5e7d80e7ceceef7902d20762f93323eefd2900d38eb065213612c94a3fecb13e4ac");
                hashPass.add("681e29b8f594a0560a8568cd1ddef081feccfd564e164207b2151e14620092f9fbbb20c9f79daaf2a01e7dda846a326a02a1cb3ddb27f2c685e43d2c86f2c5ad");
                hashPass.add("9ca5e00e64ca5f5e03b2cd02a38dee70d2d559608c8ffe1814029d3f2fa86bcc245a5eace3da57efa9f2dac58ac21750bf61ba0dc812b01b45b02010ea271a68");
                hashPass.add("bdc247a1a0e28a586ed40744d281993d519abe981aaef33277d4877d167e1150816e9723d068a59509991ed0cdd8c5cea0f9ecd0ef23664db7cb85db5a0dbe12");
                TaskSubjectRI taskSubjectRI = this.client.userSessionRI.createTask(name, typeHash, hashPass, creditsProc, creditsFound, delta, client.username, strategy, strategyData());
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
                && !alphabetTaskLabel.getText().isEmpty();
    }

    public HashMap<String, String> strategyData(){
        HashMap<String, String> data = new HashMap<>();
        if(strategy2Requisites()){
            data.put("length", lengthPassTaskTF.getText());
        }else if(strategy3Requisites()){
            data.put("length", lengthPassTaskTF.getText());
            data.put("alphabet", alphabetTaskLabel.getText());
        }
        return data;
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
        if(!maxLengthWordTF.getText().isEmpty() && !maxWordsTF.getText().isEmpty()) {
            try {
                //int maxLength = Integer.getInteger(maxLengthWordTF.getText());
                //int maxWords = Integer.getInteger(maxWordsTF.getText());
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
            }catch (IllegalArgumentException e){
                messageJoinTask.setWrapText(true);
                messageJoinTask.setText("Max length word and max words have to be numbers!");
            }
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