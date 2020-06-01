package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.projeto.client.TrayIconDemo;
import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverRI;

import java.awt.*;
import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;

public class TaskSubjectImplS2 extends TaskSubjectImplMaster implements TaskSubjectRI {

    public ArrayList<Integer> wordsSize = new ArrayList<>();

    ArrayList<Integer> lines = new ArrayList<>();

    public TaskSubjectImplS2(String name, String hashType, ArrayList<String> hashPass, Integer creditsWordProcessed, Integer creditsWordFound, Integer delta, ArrayList<Integer> wordsSize) throws RemoteException {
        super(name,hashType,hashPass, creditsWordProcessed, creditsWordFound, delta,2);
        this.wordsSize.addAll(wordsSize);
        this.subjectState.setProcess("Dividing");
        createSubTasksDividing();
    }

    /** Tasks para divisao do ficheiro por linhas */
    public void createSubTasksDividing(){
        try {
            for(String path: paths){
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(path));
                    int lines = 0;
                    while (reader.readLine() != null) {
                        if(lines == start + delta - 1){
                            Task task = new Task(url,start,delta,this);
                            task.setWordsSize(wordsSize);
                            task.isHashing = false;
                            dividingTasks.add(task);
                            start = lines + 1;
                        }
                        lines++;
                    }
                    int lastDelta = delta;
                    lastDelta = lines - start;
                    if(lastDelta != 0){
                        Task task = new Task(url,start,lastDelta,this);
                        task.setWordsSize(wordsSize);
                        task.isHashing = false;
                        dividingTasks.add(task);
                        reader.close();
                    }
                    break;
                }catch (FileNotFoundException ignored){}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createSubTasks() throws RemoteException{
        start = 0;
        try {
            for(String path: paths){
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(path));
                    int lines = 0;
                    while (reader.readLine() != null) {
                        if(lines == start + delta - 1){
                            Task task = new Task(url,start,delta,this);
                            task.isHashing = true;
                            tasks.add(task);
                            for (Integer l:this.lines) {
                                if(l > start && l < start + delta +1){
                                    task.lines.add(l);
                                }
                            }
                            if(task.lines.isEmpty()){
                                tasks.remove(task);
                            }
                            start = lines + 1;
                        }
                        lines++;
                    }
                    int lastDelta = delta;
                    lastDelta = lines - start;
                    if(lastDelta != 0){
                        Task task = new Task(url,start,lastDelta,this);
                        task.isHashing = true;
                        tasks.add(task);
                        for (Integer l:this.lines) {
                            if(l > start && l < start + lastDelta +1){
                                task.lines.add(l);
                            }
                        }
                        if(task.lines.isEmpty()){
                            tasks.remove(task);
                        }
                        reader.close();
                    }
                    break;
                }catch (FileNotFoundException ignored){}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeWorkerState(State state, String hash, String pass) throws RemoteException {
        switch (state.getmsg()){
            case "Found":
                for (int i = 0; i < this.hashPass.size() ; i ++){
                    if(this.hashPass.get(i).compareTo(hash)==0){
                        this.result.add(new Result(hash,pass));
                        this.hashPass.remove(i);
                        break;
                    }
                }
                System.out.println("FOUND");
                if(this.hashPass.isEmpty()){
                    System.out.println("COMPLETE");
                    this.subjectState.setmsg("Completed");
                    this.status = this.subjectState.COMPLETED;
                    try { /** Notification in server, we have **/
                        TrayIconDemo notif = new TrayIconDemo(this.name);
                    } catch (AWTException e) {
                        e.printStackTrace();
                    }
                    available = false;
                }else{
                    System.out.println("NOT COMPLETE");
                    this.subjectState.setmsg("Working");
                    this.status = this.subjectState.WORKING;
                }
                break;
            /** Se nao encontrar nenhuma palavra no range */
            case "Not Found":
                if(!this.subjectState.getmsg().equals("Completed") && !this.subjectState.getmsg().equals("Paused")) {
                    this.subjectState.setmsg("Working");
                    this.status = this.subjectState.WORKING;
                }
                break;
            case "Paused":
                System.out.println("PAUSED!");
                break;
        }
        this.notifyAllObservers();
    }

    @Override
    public void notifyAllObservers() throws RemoteException {
        for (WorkerObserverRI obj : workers) {
            try {
                obj.taskUpdated();
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }

        }
    }

    @Override
    public void pause() throws RemoteException {
        if(!this.subjectState.getmsg().equals("Completed") || !this.subjectState.getmsg().equals("Paused")) {
            System.out.println("PAUSED");
            this.subjectState.setmsg("Paused");
            this.status = this.subjectState.PAUSED;
            this.notifyAllObservers();
        }
    }

    @Override
    public void resume() throws RemoteException{
        if(this.subjectState.getmsg().equals("Paused")) {
            this.subjectState.setmsg("Working");
            this.status = this.subjectState.WORKING;
            this.notifyAllObservers();
        }
    }

    @Override
    public void stop() throws RemoteException {
        this.hashPass.clear();
        this.subjectState.setmsg("Completed");
        this.status = this.subjectState.COMPLETED;
        this.notifyAllObservers();
        this.available = false;
    }

    @Override
    public String getHashType() throws RemoteException {
        return this.hashType;
    }

    @Override
    public ArrayList<String> getHashPass() throws RemoteException {
        return this.hashPass;
    }

    @Override
    public String getName() throws RemoteException {
        return this.name;
    }

    @Override
    public State getState() throws RemoteException {
        return this.subjectState;
    }

    @Override
    public ArrayList<Result> getResult() throws RemoteException {
        return result;
    }

    @Override
    public Integer getStrategy() throws RemoteException {
        return this.strategy;
    }

    @Override
    public void finishDividing(ArrayList<Integer> linesWithWordLength,WorkerObserverRI workerObserverRI) throws RemoteException {
        lines.addAll(linesWithWordLength);

        workerObserverRI.setN_threads_dividing(workerObserverRI.getN_threads_dividing()-1);
        if(workerObserverRI.getN_threads_dividing()==0){
            workers.remove(workerObserverRI);
            System.out.println("finishing_Dividing");
        }
        if(dividingTasks.isEmpty() && workers.isEmpty()){
            System.out.println("finishing_Dividing COMPLETE");
            createSubTasks();
            this.available = true;
            this.subjectState.setProcess("Hashing");
        }
    }

    public ArrayList<Integer> getWordsSize() {
        return wordsSize;
    }

}
