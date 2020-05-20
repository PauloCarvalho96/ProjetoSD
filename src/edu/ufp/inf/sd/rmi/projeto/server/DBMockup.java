package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverRI;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class DBMockup implements Serializable {

    // array users
    private final ArrayList<User> users;
    // array sessoes (username/session)
    private final HashMap<String, UserSessionRI> sessions;
    // array tasks
    private final ArrayList<TaskSubjectRI> tasks;
    // array workers
    private final ArrayList<WorkerObserverRI> workers;
    // hashmap user to workers
    private final HashMap<User, ArrayList<WorkerObserverRI>> userWorkers;
    // hashmap user to tasks
    private final HashMap<User, ArrayList<TaskSubjectRI>> userTasks;

    public DBMockup() {
        users = new ArrayList<>();
        sessions = new HashMap<>();
        tasks = new ArrayList<>();
        workers = new ArrayList<>();
        userWorkers = new HashMap<>();
        userTasks = new HashMap<>();
        User user = new User("1","1");
        users.add(user);
        userWorkers.put(user, new ArrayList<>());
        userTasks.put(user, new ArrayList<>());
        User user1 = new User("2","2");
        users.add(user1);
        userWorkers.put(user1, new ArrayList<>());
        userTasks.put(user1, new ArrayList<>());
    }

    // registo
    public void register(String u, String p) {
        if (!exists(u, p)) {
            User user = new User(u, p);
            users.add(user);
            userWorkers.put(user, new ArrayList<>());
            userTasks.put(user, new ArrayList<>());
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
        sessions.remove(uname,sessionRI);
    }

    // adiciona nova task
    public void addTask(TaskSubjectRI taskSubjectRI){
        tasks.add(taskSubjectRI);
    }

    //adiciona novo worker
    public void addWorker(WorkerObserverRI workerObserverRI){
        workers.add(workerObserverRI);
    }

    public void removeTask(TaskSubjectRI taskSubjectRI){
        tasks.remove(taskSubjectRI);
    }

    public void assocWorkerToUser(String uname, WorkerObserverRI workerObserverRI) throws RemoteException {
        if(this.getUser(uname) != null && workerObserverRI != null) {
            this.workers.add(workerObserverRI);
            User user = this.getUser(uname);
            userWorkers.get(user).add(workerObserverRI);
        }
    }

    public void assocTaskToUser(String uname, TaskSubjectRI taskSubjectRI) throws RemoteException {
        if(this.getUser(uname) != null && taskSubjectRI != null) {
            this.tasks.add(taskSubjectRI);
            User user = this.getUser(uname);
            userTasks.get(user).add(taskSubjectRI);
        }
    }

    // todas as tasks disponveis
    public ArrayList<TaskSubjectRI> allTasks(){
        return tasks;
    }

    public ArrayList<WorkerObserverRI> allWorkers(){
        return workers;
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
        return userWorkers.get(this.getUser(uname));
    }

    public ArrayList<TaskSubjectRI> getTasksFromUser(String uname) throws RemoteException {
        return userTasks.get(this.getUser(uname));
    }
}
