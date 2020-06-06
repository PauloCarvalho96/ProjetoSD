package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.projeto.client.Client;
import edu.ufp.inf.sd.rmi.projeto.client.TrayIconDemo;
import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverRI;

import java.awt.*;
import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;

public class TaskSubjectImplS1 extends TaskSubjectImplMaster implements TaskSubjectRI , Runnable {

    public TaskSubjectImplS1(String name, String hashType, ArrayList<String> hashPass, Integer creditsWordProcessed, Integer creditsWordFound, Integer delta, Integer taskCredits, Client client) throws RemoteException {
        super(name,hashType,hashPass, creditsWordProcessed, creditsWordFound, delta,1,taskCredits,client);
        createSubTasks();
    }

    /** divide linhas para criar sub tasks */
    @Override
    public void createSubTasks() throws RemoteException{
        Runnable runnable = this;
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public Integer getStrategy() throws RemoteException {
        return this.strategy;
    }

    @Override
    public void finishDividing(ArrayList<Integer> linesWithWordLength, WorkerObserverRI workerObserverRI) throws RemoteException {}

    @Override
    public void changeWorkerState(State state, String hash, String pass) throws RemoteException {
        switch (state.getmsg()){
            case "Found":

                /** retira ao plafond da task */
                this.taskCredits-=10;

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

                    /** entrega resto do plafound ao dono da task */
                    int credits = this.client.userSessionRI.getUserCreditsDB(client.username);
                    credits += this.taskCredits;
                    this.client.userSessionRI.setUserCreditsDB(client.username,credits);

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
                    int creditsToTask = (int) Math.round(delta*0.1);
                    this.taskCredits-=creditsToTask;
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
    public String getHashType() {
        return hashType;
    }

    @Override
    public ArrayList<String> getHashPass() {
        return hashPass;
    }

    @Override
    public String getName() throws RemoteException {
        return name;
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
    public void run() {
        try {
            for(String path: paths){
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(path));
                    int lines = 0;
                    while (reader.readLine() != null) {
                        if(lines == start + delta - 1){
                            Task task = new Task(url,start,delta,this);
                            tasks.add(task);
                            start = lines + 1;
                        }
                        lines++;
                    }
                    int lastDelta = delta;
                    lastDelta = lines - start;
                    if(lastDelta != 0){
                        Task task = new Task(url,start,lastDelta,this);
                        tasks.add(task);
                        reader.close();
                    }

                    for (Task task:tasks) {
                        System.out.println("\nStart: " + task.getStart() + "\nDelta: " + task.getDelta() + "\n");
                    }
                    break;
                }catch (FileNotFoundException ignored){}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
