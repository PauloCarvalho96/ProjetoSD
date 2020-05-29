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

    public TaskSubjectImplS2(String name, String hashType, ArrayList<String> hashPass, Integer creditsWordProcessed, Integer creditsWordFound, Integer delta) throws RemoteException {
        super(name,hashType,hashPass, creditsWordProcessed, creditsWordFound, delta);
        createSubTasks();
    }

    /** divide linhas para criar sub tasks */
    @Override
    public void createSubTasks() throws RemoteException{
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
    public String getHashType() throws RemoteException {
        return null;
    }

    @Override
    public ArrayList<String> getHashPass() throws RemoteException {
        return null;
    }

    @Override
    public String getName() throws RemoteException {
        return null;
    }

    @Override
    public State getState() throws RemoteException {
        return null;
    }

    @Override
    public ArrayList<Result> getResult() throws RemoteException {
        return result;
    }

    @Override
    public void stop() throws RemoteException {
        this.hashPass.clear();
        this.subjectState.setmsg("Completed");
        this.status = this.subjectState.COMPLETED;
        this.notifyAllObservers();
        this.available = false;
    }
}
