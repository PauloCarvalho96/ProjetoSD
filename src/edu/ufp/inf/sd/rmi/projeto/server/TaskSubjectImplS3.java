package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.projeto.client.TrayIconDemo;
import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverRI;

import java.awt.*;
import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;

public class TaskSubjectImplS3 extends TaskSubjectImplMaster implements TaskSubjectRI {
    public Integer wordsSize;
    public String alphabet;

    public TaskSubjectImplS3(String name, String hashType, ArrayList<String> hashPass, Integer creditsWordProcessed, Integer creditsWordFound, Integer delta, Integer wordsSize, String alphabet) throws RemoteException {
        super(name,hashType,hashPass, creditsWordProcessed, creditsWordFound, delta);
        this.wordsSize = wordsSize;
        this.alphabet = alphabet;
        createSubTasks();
    }

    /** divide linhas para criar sub tasks */
    @Override
    public void createSubTasks() throws RemoteException{
        //TODO: Comentar o que cada variavel é
        recursiveAlphabet(alphabet.toCharArray(), new char[wordsSize], 0, alphabet.length() - 1, 0, wordsSize);
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

    public void recursiveAlphabet(char[] alphabet, char[] data, int start, int end, int index, int wordSize)
    {
        if (index == wordSize)
        {
            String str = new String(data);
            Task task = new Task(this,str);
            tasks.add(task);
            return;
        }

        for (int i=start; i<=end && end-i+1 >= wordSize-index; i++)
        {
            data[index] = alphabet[i];
            recursiveAlphabet(alphabet, data, i+1, end, index+1, wordSize);
        }
    }
}
