package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverRI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

public class TaskSubjectImpl extends UnicastRemoteObject implements TaskSubjectRI {

    private String name;
    private String hashType;
    private String hashPass;
    private String creditsPerWord;
    private String creditsTotal;
    private State subjectState;
    private boolean available;
    private Integer start = 0;     //linha atual
    private Integer delta = 1000;     //linha atual
    // array de workers
    private final ArrayList<WorkerObserverRI> workers = new ArrayList<>();

    public TaskSubjectImpl(String name, String hashType, String hashPass) throws RemoteException {
        super();
        this.name = name;
        this.hashType = hashType;
        this.hashPass = hashPass;
    }

    /*public TaskSubjectImpl(String name, String hashType, String hashPass, String creditsPerWord, String creditsTotal) throws RemoteException {
        super();
        this.name = name;
        this.hashType = hashType;
        this.hashPass = hashPass;
        this.creditsPerWord = creditsPerWord;
        this.creditsTotal = creditsTotal;
    }*/

    @Override
    public void divideFile() throws RemoteException{
        try {
            // ficheiro do servidor
            File passwords = new File("C:\\Users\\Paulo\\Documents\\GitHub\\ProjetoSD\\src\\edu\\ufp\\inf\\sd\\rmi\\projeto\\server\\passwords_to_verify.txt");
            Scanner passwordsReader = new Scanner(passwords);

            try {
                // ficheiro para entregar ao cliente
                File txtToDelivery = createFile(start,delta);

                if(txtToDelivery != null){
                    FileWriter txtToDeliveryReader = new FileWriter(txtToDelivery);

                    int j = 0;
                    while(passwordsReader.hasNextLine()){
                        if(j>=start){
                            String data = passwordsReader.nextLine();
                            txtToDeliveryReader.write(data);
                            if(j < start + delta - 1){
                                txtToDeliveryReader.write("\n");
                            }
                        } else {
                            passwordsReader.nextLine();
                        }
                        j++;
                        if(j==start+delta){
                            break;
                        }
                    }
                    txtToDeliveryReader.close();
                    passwordsReader.close();

                    start = start + delta;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private File createFile(Integer start,Integer delta) {
        try {
            String filename = name+start+delta+"txt";
            File newFile = new File("C:\\Users\\Paulo\\Documents\\GitHub\\ProjetoSD\\src\\edu\\ufp\\inf\\sd\\rmi\\projeto\\server\\"+filename);
            if (newFile.createNewFile()) {
                System.out.println("File created");
                return newFile;
            } else {
                System.out.println("Error!");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void notifyAllObservers(){
        /*for (WorkerObserverRI obs: workers) {
            try {
//                obs.update();
            } catch (RemoteException ex) {
                Logger.getLogger(TaskSubjectImpl.class.getName()).log(Level.SEVERE,null,ex);
            }
        }*/
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
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public String getHashType() {
        return hashType;
    }

    @Override
    public String getHashPass() {
        return hashPass;
    }



}
