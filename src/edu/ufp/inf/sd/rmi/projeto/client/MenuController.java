package edu.ufp.inf.sd.rmi.projeto.client;

import edu.ufp.inf.sd.rmi.projeto.server.Result;
import edu.ufp.inf.sd.rmi.projeto.server.TaskSubjectRI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import sun.plugin.ClassLoaderInfo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;

public class MenuController implements Initializable {
    /** Create task **/
    public Tab createTaskTab;
    public TextField nameTaskTF;
    public ComboBox<String> hashTypeCB;
    public TextArea hashPassTA;
    public TextField deltaTaskTF;
    public Label deltaTaskLabel;
    public ComboBox<String> strategyCB;
    public Label lengthPassTaskLabel;
    public TextField lengthPassTaskTF;
    public Label alphabetTaskLabel;
    public TextField alphabetTaskTF;
    public Button createTaskBut;
    public Label messageCreateTask;
    public TextField urlTextBox;
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

    /** Credits */
    public Label creditsText;
    public Label actualCredits;
    public Label costCreditsTask;

    private Client client;

    public void initData(Client client){
        this.client = client;

        /** inicializa créditos do user */
        try {
            actualCredits.setText(String.valueOf(client.userSessionRI.getUserCreditsDB(client.username)));
            /** default cost */
            int taskCredits = 2000000;
            checkUserCreditsToCreateTask(taskCredits);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeComboBoxAndTableView();
    }

    public void initializeComboBoxAndTableView(){
        initializeComboBox();
        initializeTableViewListTasks();
        initializeTableViewListOwnTasks();
        initializeTableViewListOwnWorkers();
        initializeTableViewListInfoTask();
        updateBut.setVisible(true);
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
        deltaTaskTF.clear();
        urlTextBox.clear();
        hashTypeCB.setValue("SHA-512");
        strategyCB.setValue("Strategy 1");
    }

    public void handleExit(ActionEvent actionEvent) throws RemoteException {
        client.userSessionRI.logout(client.username, client.userSessionRI);
        Platform.exit();
        System.exit(0);
    }

    public void handleAbout(ActionEvent actionEvent) {
    }

    public void handlerCreateTaskTab(Event event) {
        try {
            updateBut.setVisible(true);
        } catch (NullPointerException ignored){}
    }

    public void handlerCreateTask(ActionEvent actionEvent) throws RemoteException {
        String name = nameTaskTF.getText();
        String typeHash = hashTypeCB.getValue();
        String[] stCB = strategyCB.getValue().split(" ");
        int strategy = Integer.parseInt(stCB[1]);
        String[] ha = hashPassTA.getText().split(";");
        String url = urlTextBox.getText();
        ArrayList<String> hashPass = new ArrayList<>(Arrays.asList(ha));
        try {
            int taskCredits = 2000000;
            int delta = Integer.parseInt(deltaTaskTF.getText());
            if (!name.isEmpty() && !hashPass.isEmpty() && strategyRequisites()) {
                delta = 500000;
                hashPass.clear();

                // aaa
                //hashPass.add("d6f644b19812e97b5d871658d6d3400ecd4787faeb9b8990c1e7608288664be77257104a58d033bcf1a0e0945ff06468ebe53e2dff36e248424c7273117dac09");
                // 120bu57fu119
                //hashPass.add("7d579f6d59b9a450c43e9a3d211f912528ed46d0ccabde15de3ad1f7a28b5947a0b30173489810b1f16cd13e62d4bf901e9f34ea80533252daa2406fc5cd446e");
                // 13m31
                //hashPass.add("f4c0701bf17c326b3e18680ac15833d07d10715c7653203b6faf68b51d532106a9d8a3f49c939610c3482182ea0e02e90d7ba474c0c8f48e37bdadfedaf024bd");
                //41k
                hashPass.add("47150a22fa31bb0450e9c59c0426d8d63510d89fac8308f097bef572a1038bb266785eb83a8f3f62bda3bb3930d3e742602f24b0e0b0b87ec86148af9c9bc66c");
                // 13m13
//                hashPass.add("a1d4fb6228007348c7c976a910ce6c7182a3c099622c9fe4d4baf7e55176b045d4e2b93c76e28d6462efbdb2f5fbf1a80fa604628e95ddf74afdc6a1a49ec1f4");
                // BANKEY
//                hashPass.add("7859a9046763901b0b3e1da874b3230ccc9a6d34a8527bd208980b39c6c7991bbd274179fae8f01c3408504284c45824c11b5e7e42ac8e142e542e3e18944f45");
                // 59n70m9
//                hashPass.add("ca3789101f01dbb3989c9dcef2f2c175f85ad0a4652de1d4b431b83b2859baf4f802523ae96ee1d15c5f09aefc85a0a61f10ba25b863bee1b0ecc4a2bc7f1075");

                HashMap<String, String> data = new HashMap<>();
                if(strategy2Requisites()){
                    taskCredits = 3000000;
                    String[] le = lengthPassTaskTF.getText().split(";");
                    if(le.length != hashPass.size()){
                        messageCreateTask.setWrapText(true);
                        messageCreateTask.setText("Task was not created! Number of length pass is not the same as hash pass!");
                        return;
                    }
                    data.put("length",lengthPassTaskTF.getText());
                }else if(strategy3Requisites()){
                    taskCredits = 4000000;
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

                /** se não tiver creditos suficientes */
                if(client.userSessionRI.getUserCreditsDB(client.username) < taskCredits){
                    messageCreateTask.setWrapText(true);
                    messageCreateTask.setText("You dont have enough credits to create task!");
                    return;
                }

                TaskSubjectRI taskSubjectRI = this.client.userSessionRI.createTask(name, typeHash, hashPass, delta, client.username, strategy, data,taskCredits,client,url);
                if (taskSubjectRI != null) {
                    initializeCreateTask();
                    client.userSessionRI.setUserCreditsDB(client.username,client.userSessionRI.getUserCreditsDB(client.username)-taskCredits);
                    actualCredits.setText(String.valueOf(client.userSessionRI.getUserCreditsDB(client.username)));
                    taskCredits = 2000000;
                    checkUserCreditsToCreateTask(taskCredits);
                    costCreditsTask.setText(String.valueOf(taskCredits));
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
            WorkerObserverRI workerObserverRI = new WorkerObserverImpl(
                    this.client.userSessionRI.getSizeWorkersDB(this.client.username) + 1, client, n_threads);
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
        actualCredits.setText(String.valueOf(client.userSessionRI.getUserCreditsDB(client.username)));
        infoOwnTaskTable.getItems().clear();
    }

    public void handlerStrategyCB(ActionEvent actionEvent) {
        switch (strategyCB.getValue()){
            case "Strategy 1":
                deltaTaskTF.setVisible(true);
                deltaTaskLabel.setVisible(true);
                lengthPassTaskLabel.setVisible(false);
                lengthPassTaskTF.setVisible(false);
                alphabetTaskLabel.setVisible(false);
                alphabetTaskTF.setVisible(false);
                int taskCredits1 = 2000000;
                checkUserCreditsToCreateTask(taskCredits1);
                break;
            case "Strategy 2":
                deltaTaskTF.setVisible(true);
                deltaTaskLabel.setVisible(true);
                lengthPassTaskLabel.setVisible(true);
                lengthPassTaskTF.setVisible(true);
                alphabetTaskLabel.setVisible(false);
                alphabetTaskTF.setVisible(false);
                int taskCredits2 = 3000000;
                checkUserCreditsToCreateTask(taskCredits2);
                break;
            case "Strategy 3":
                deltaTaskTF.setVisible(false);
                deltaTaskLabel.setVisible(false);
                lengthPassTaskLabel.setVisible(true);
                lengthPassTaskTF.setVisible(true);
                alphabetTaskLabel.setVisible(true);
                alphabetTaskTF.setVisible(true);

                int taskCredits3 = 4000000;
                checkUserCreditsToCreateTask(taskCredits3);
                break;
        }
    }

    public void checkUserCreditsToCreateTask(Integer taskCredits){
        costCreditsTask.setText(String.valueOf(taskCredits));
        try {
            if(client.userSessionRI.getUserCreditsDB(client.username)<taskCredits){
                costCreditsTask.setTextFill(Color.RED);
            } else {
                costCreditsTask.setTextFill(Color.GREEN);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}