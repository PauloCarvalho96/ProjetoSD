package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverRI;

import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskSubjectImpl extends UnicastRemoteObject implements TaskSubjectRI {

    private String name;
    private String hashType;
    private String hashPass;
    private State subjectState;
    // array de workers
    private final ArrayList<WorkerObserverRI> workers = new ArrayList<>();

    public TaskSubjectImpl(String name,String hashType, String hashPass) throws RemoteException {
        super();
        this.name = name;
        this.hashType = hashType;
        this.hashPass = hashPass;
    }

    @Override
    public boolean readFile(String pswtodiscover) throws RemoteException{
        try {
            File myObj = new File("C:\\Users\\Paulo\\Documents\\GitHub\\ProjetoSD\\src\\edu\\ufp\\inf\\sd\\rmi\\projeto\\server\\passwords_to_verify.txt");
            Scanner myReader = new Scanner(myObj);
//            for(int i=start;i<end;i++){
            while (myReader.hasNextLine()){
                String data = myReader.nextLine();
                if(data.compareTo(pswtodiscover) == 0){
                    System.out.println("\nDescobri a password\n");
                    myReader.close();
                    return true;
                }
            }
            System.out.println("\nPassword não descoberta\n");
            myReader.close();
            return false;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return false;
    }

    public void notifyAllObservers(){
        for (WorkerObserverRI obs: workers) {
            try {
                obs.update();
            } catch (RemoteException ex) {
                Logger.getLogger(TaskSubjectImpl.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
    }

    @Override
    public void attach(WorkerObserverRI obsRI) throws RemoteException {
        if(!this.workers.contains(obsRI)){
            this.workers.add(obsRI);
        }
    }

    @Override
    public void detach(WorkerObserverRI obsRI) throws RemoteException {
        this.workers.remove(obsRI);
    }

    @Override
    public State getState() throws RemoteException {
        return this.subjectState;
    }

    @Override
    public void setState(State state) throws RemoteException {
        this.subjectState = state;
        this.notifyAllObservers();
    }

    @Override
    public String getName() {
        return name;
    }

}
