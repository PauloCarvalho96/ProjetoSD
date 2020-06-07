package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.projeto.client.Client;
import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class TaskSubjectImplMaster extends UnicastRemoteObject {

    public String name;
    public String hashType;
    public Client client;   // dono do taskgroup
    public ArrayList<String> hashPass;
    public Integer creditsWordProcessed;
    public Integer creditsWordFound;
    public Integer taskCredits;
    public State subjectState = new State();
    public String status;
    public boolean available = true;
    public Integer start = 0;     //linha atual
    public final Integer delta;     //quantidade de linhas
    public ArrayList<WorkerObserverRI> workers = new ArrayList<>();// array de workers
    public ArrayList<Task> tasks = new ArrayList<>();// array tasks
    public ArrayList<Task> dividingTasks = new ArrayList<>();
    public ArrayList<Result> result = new ArrayList<>();//array pass found
    public static final String url = "https://raw.githubusercontent.com/danielmiessler/SecLists/master/Passwords/darkc0de.txt";
    public ArrayList<String> paths = new ArrayList<>();
    public Integer strategy = 0;

    protected TaskSubjectImplMaster(String name, String hashType, ArrayList<String> hashPass, Integer delta,Integer strategy,Integer taskCredits,Client client) throws RemoteException {
        this.name = name;
        this.client = client;
        this.hashType = hashType;
        this.hashPass = hashPass;
        this.taskCredits = taskCredits;
        this.delta = delta;
        this.subjectState.setmsg("Available");
        this.status = this.subjectState.AVAILABLE;
        this.strategy = strategy;
    }

    public void attach(WorkerObserverRI obsRI) throws RemoteException {
        if(!this.workers.contains(obsRI)){
            this.workers.add(obsRI);
            if(this.subjectState.getProcess()!=null && this.subjectState.getProcess().compareTo("Dividing")==0){
                obsRI.setTask(getTaskFromArrayDividing());
            } else {
                obsRI.setTask(getTaskFromArray());
            }
        }
    }

    public Task getTaskFromArray() throws RemoteException {
        Task task = this.tasks.get(0);
        if(task != null && this.tasks.size() == 1){
            this.tasks.remove(0);
            this.available = false;
            return task;
        }
        if(task != null) {
            this.tasks.remove(0);
            return task;
        }
        return null;
    }

    public Task getTaskFromArrayDividing() throws RemoteException {
        Task task = this.dividingTasks.get(0);
        if(task != null && this.dividingTasks.size() == 1){
            this.dividingTasks.remove(0);
            this.available = false;
            return task;
        }
        if(task != null) {
            this.dividingTasks.remove(0);
            return task;
        }
        return null;
    }

    public void detach(WorkerObserverRI obsRI) throws RemoteException {
        this.workers.remove(obsRI);
    }

    public void setState(State state){
        this.subjectState = state;
    }

    public boolean isAvailable() throws RemoteException {
        return available;
    }

    public Integer getTaskCredits() throws RemoteException {
        return taskCredits;
    }

    public void setTaskCredits(Integer taskCredits) throws RemoteException {
        this.taskCredits = taskCredits;
    }
}
