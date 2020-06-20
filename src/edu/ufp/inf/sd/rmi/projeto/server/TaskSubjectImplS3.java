package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.projeto.client.Client;
import edu.ufp.inf.sd.rmi.projeto.client.TrayIconDemo;
import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverRI;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class TaskSubjectImplS3 extends TaskSubjectImplMaster implements TaskSubjectRI, Runnable{
    public Integer wordsSize;
    public String alphabet;

    public TaskSubjectImplS3(String name, String hashType, ArrayList<String> hashPass, Integer delta, Integer wordsSize, String alphabet, Integer taskCredits, Client client) throws RemoteException {
        super(name, hashType, hashPass, delta,3,taskCredits,client);
        this.wordsSize = wordsSize;
        this.alphabet = alphabet;
        createSubTasks();
    }

    @Override
    public void checkWorkers() throws RemoteException {
        for (WorkerObserverRI wo:this.workers) {
            if(wo==null){
                System.out.println("PILINHAS");
            }
        }
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
    public void finishDividing(ArrayList<Integer> linesWithWordLength, WorkerObserverRI workerObserverRI) throws RemoteException {
    }

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
                if(!this.subjectState.getmsg().equals("Completed") || !this.subjectState.getmsg().equals("Incompleted") || !this.subjectState.getmsg().equals("Paused")) {
                    this.subjectState.setmsg("Working");
                    this.status = this.subjectState.WORKING;
                    int creditsToTask = state.getN_credits();
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
        if(!this.subjectState.getmsg().equals("Completed") || !this.subjectState.getmsg().equals("Incompleted") || !this.subjectState.getmsg().equals("Paused")) {
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
    public void stop() throws RemoteException {
        this.hashPass.clear();
        this.subjectState.setmsg("Incompleted");
        this.status = this.subjectState.INCOMPLETED;
        System.out.println("Incompleted");
        this.notifyAllObservers();
        this.available = false;
    }

    public Integer getWordsSize() {
        return wordsSize;
    }

    @Override
    public void run() {
        int percentagens = 10;
        double range_size = Math.pow(alphabet.length(),wordsSize);
        int range_ammount = Math.toIntExact(Math.round(range_size / percentagens));
        ArrayList<Integer> wordsize = new ArrayList<>();
        wordsize.add(wordsSize);
        for (int i = 0; i < percentagens ; i++){
            Task task = new Task(this,alphabet,wordsize,i*range_ammount+i,range_ammount);
            tasks.add(task);
        }
    }
}
