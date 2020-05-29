package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class TaskSubjectImplMaster extends UnicastRemoteObject {

    public String name;
    public String hashType;
    public ArrayList<String> hashPass;
    public Integer creditsWordProcessed;
    public Integer creditsWordFound;
    public State subjectState = new State();
    public String status;
    public boolean available = true;
    public Integer start = 0;     //linha atual
    public Integer delta;     //quantidade de linhas
    public ArrayList<WorkerObserverRI> workers = new ArrayList<>();// array de workers
    public ArrayList<Task> tasks = new ArrayList<>();// array tasks
    public ArrayList<Result> result = new ArrayList<>();//array pass found
    public static final String url = "https://raw.githubusercontent.com/danielmiessler/SecLists/master/Passwords/darkc0de.txt";
    public ArrayList<String> paths = new ArrayList<>();
    public Integer strategy = 0;

    protected TaskSubjectImplMaster(String name, String hashType, ArrayList<String> hashPass, Integer creditsWordProcessed, Integer creditsWordFound, Integer delta,Integer strategy) throws RemoteException {
        this.name = name;
        this.hashType = hashType;
        this.hashPass = hashPass;
        this.creditsWordProcessed = creditsWordProcessed;
        this.creditsWordFound = creditsWordFound;
        this.delta = delta;
        this.subjectState.setmsg("Available");
        this.status = this.subjectState.AVAILABLE;
        this.strategy = strategy;
        paths.add("C:\\Users\\Paulo\\Documents\\GitHub\\ProjetoSD\\src\\edu\\ufp\\inf\\sd\\rmi\\projeto\\server\\passwords_to_verify.txt");
        paths.add("C:\\Users\\Rui\\Documents\\ProjetoSD\\src\\edu\\ufp\\inf\\sd\\rmi\\projeto\\server\\passwords_to_verify.txt");
        paths.add("C:\\Users\\tmsl9\\GitHub\\ProjetoSD\\src\\edu\\ufp\\inf\\sd\\rmi\\projeto\\server\\passwords_to_verify.txt");
    }


    public void attach(WorkerObserverRI obsRI) throws RemoteException {
        if(!this.workers.contains(obsRI)){
            this.workers.add(obsRI);
            obsRI.setTask(getTaskFromArray());
        }
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
}
