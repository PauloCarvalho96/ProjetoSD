package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverRI;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class DBMockup implements Serializable {

    // array users
    private ArrayList<User> users;
    // array sessoes (username/session)
    private HashMap<String, UserSessionRI> sessions;
    // array tasks
    private ArrayList<TaskSubjectRI> tasks;
    // array workers
    private ArrayList<WorkerObserverRI> workers;
    // hashmap user to workers
    private HashMap<String, ArrayList<WorkerObserverRI>> userWorkers;
    // hashmap user to tasks
    private HashMap<String, ArrayList<TaskSubjectRI>> userTasks;


    public DBMockup() {
        users = new ArrayList<>();
        sessions = new HashMap<>();
        tasks = new ArrayList<>();
        workers = new ArrayList<>();
        userWorkers = new HashMap<>();
        userTasks = new HashMap<>();
        User user = new User("1","1");
        user.setCredits(10000000);
        users.add(user);
        userWorkers.put(user.getUname(), new ArrayList<>());
        userTasks.put(user.getUname(), new ArrayList<>());
        User user1 = new User("2","2");
        users.add(user1);
        user1.setCredits(0);
        userWorkers.put(user1.getUname(), new ArrayList<>());
        userTasks.put(user1.getUname(), new ArrayList<>());
        User user2 = new User("3","3");
        user2.setCredits(3000000);
        users.add(user2);
        userWorkers.put(user2.getUname(), new ArrayList<>());
        userTasks.put(user2.getUname(), new ArrayList<>());
    }

    // registo
    public void register(String u, String p) {
        if (!exists(u, p)) {
            User user = new User(u, p);
            users.add(user);
            userWorkers.put(user.getUname(), new ArrayList<>());
            userTasks.put(user.getUname(), new ArrayList<>());
        }
    }

    // verifica se utilizador existe
    public boolean exists(String u, String p) {
        for (User usr : this.users) {
            if (usr.getUname().compareTo(u) == 0 && usr.getPword().compareTo(p) == 0) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public HashMap<String,UserSessionRI> getSessions() {
        return sessions;
    }

    // quando utilizador faz login cria nova sessao
    public void addSession(String uname,UserSessionRI sessionRI){
        sessions.put(uname,sessionRI);
    }

    // remove sessao
    public void removeSession(String uname,UserSessionRI sessionRI){
        sessions.remove(uname);
    }

    /** nao esta a eliminar da DB */
    public void removeTask(TaskSubjectRI taskSubjectRI, String uname){
        tasks.remove(taskSubjectRI);
    }

    public void assocWorkerToUser(String uname, WorkerObserverRI workerObserverRI) throws RemoteException {
        this.workers.add(workerObserverRI);
        userWorkers.get(uname).add(workerObserverRI);
    }

    public void assocTaskToUser(String uname, TaskSubjectRI taskSubjectRI) throws RemoteException {
        this.tasks.add(taskSubjectRI);
        userTasks.get(uname).add(taskSubjectRI);
    }

    // todas as tasks disponveis
    public ArrayList<TaskSubjectRI> allTasks(){
        return tasks;
    }

    public ArrayList<WorkerObserverRI> allWorkers(){
        return workers;
    }

    public ArrayList<WorkerObserverRI> allWorkersUsers(String name){
        return userWorkers.get(name);
    }

    public User getUser(String uname) throws RemoteException {
        for (User user:users) {
            if(user.getUname().equals(uname)){
                return user;
            }
        }
        return null;
    }

    public TaskSubjectRI getTask(String task) throws RemoteException {
        for (TaskSubjectRI taskSubjectRI:tasks) {
            if(task.compareTo(taskSubjectRI.getName())==0){
                return taskSubjectRI;
            }
        }
        return null;
    }

    public WorkerObserverRI getWorker(int id) throws RemoteException {
        for (WorkerObserverRI worker:workers) {
            if(worker.getId().equals(id)){
                return worker;
            }
        }
        return null;
    }

    public ArrayList<WorkerObserverRI> getWorkersFromUser(String uname) throws RemoteException {
        return userWorkers.get(uname);
    }

    public ArrayList<TaskSubjectRI> getTasksFromUser(String uname) throws RemoteException {
        return userTasks.get(uname);
    }
}
