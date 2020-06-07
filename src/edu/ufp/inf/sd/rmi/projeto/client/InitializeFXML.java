package edu.ufp.inf.sd.rmi.projeto.client;

import edu.ufp.inf.sd.rmi.projeto.server.Result;
import edu.ufp.inf.sd.rmi.projeto.server.TaskSubjectRI;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.rmi.RemoteException;

public class InitializeFXML {
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
    public Label urlTaskLabel;
    public TextField urlTextBox;
    /** List tasks **/
    public Tab listTasksTab;
    public TableView<TaskSubjectRI> tasksTable;
    public TableColumn<TaskSubjectRI, String> nameCol;
    public TableColumn<TaskSubjectRI, String> hashTypeCol;
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
    public TableColumn<WorkerObserverRI, String> threadsWOwnCol;
    public TableColumn<WorkerObserverRI, String> statusWOwnCol;
    public Pagination listOwnWorkersPagination;
    public Label idOwnWorkerLabel;
    /** Update all **/
    public Button updateBut;

    /** Credits */
    public Label creditsText;
    public Label actualCredits;
    public Label costCreditsTask;


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
        statusTOwnCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TaskSubjectRI, String> , ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TaskSubjectRI,String> p) {
                if(p.getValue() != null){
                    try {
                        return new SimpleStringProperty(String.valueOf(p.getValue().getState().getmsg()));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                return new SimpleStringProperty("-");
            }
        });
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
        threadsWOwnCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WorkerObserverRI, String> , ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<WorkerObserverRI,String> p) {
                if(p.getValue() != null){
                    try {
                        return new SimpleStringProperty(String.valueOf(p.getValue().getN_threads()));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                return new SimpleStringProperty("-");
            }
        });
        statusWOwnCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WorkerObserverRI, String> , ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<WorkerObserverRI,String> p) {
                if(p.getValue() != null){
                    try {
                        return new SimpleStringProperty(String.valueOf(p.getValue().getStateWorker().getmsg()));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                return new SimpleStringProperty("-");
            }
        });
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
        lengthPassTaskTF.clear();
        alphabetTaskTF.clear();
        hashTypeCB.setValue("SHA-512");
        strategyCB.setValue("Strategy 1");
    }

}
