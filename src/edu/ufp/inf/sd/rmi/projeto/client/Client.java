package edu.ufp.inf.sd.rmi.projeto.client;

import edu.ufp.inf.sd.rmi.projeto.server.TaskSubjectRI;
import edu.ufp.inf.sd.rmi.projeto.server.UserFactoryRI;
import edu.ufp.inf.sd.rmi.projeto.server.UserSessionRI;
import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;

import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.common.hash.Hashing;

public class Client {

    /**
     * Context for connecting a RMI client to a RMI Servant
     */
    private SetupContextRMI contextRMI;
    /**
     * Remote interface that will hold the Servant proxy
     */
    public UserFactoryRI userFactoryRI;

    public UserSessionRI userSessionRI;

    public String username;

    public ArrayList<WorkerObserverRI> workersRI = new ArrayList<>();

    public ArrayList<TaskSubjectRI> tasksRI = new ArrayList<>();

    public Float cash;

    public Client() {
        try {
            //List ans set args
            String registryIP = "localhost";
            String registryPort = "1099";
            String serviceName = "ProjetoService";
            //Create a context for RMI setup
            contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});
        } catch (RemoteException e) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public Remote lookupService() {
        try {
            //Get proxy to rmiregistry
            Registry registry = contextRMI.getRegistry();
            //Lookup service on rmiregistry and wait for calls
            if (registry != null) {
                //Get service url (including servicename)
                String serviceUrl = contextRMI.getServicesUrl(0);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "going to lookup service @ {0}", serviceUrl);

                //============ Get proxy to service ============
                userFactoryRI = (UserFactoryRI) registry.lookup(serviceUrl);
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "registry not bound (check IPs). :(");
                //registry = LocateRegistry.createRegistry(1099);
            }
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return userFactoryRI;
    }

    public static class myThread implements Runnable {
        WorkerObserverRI worker;
        myThread(WorkerObserverRI w){worker=w;}
        public void run() {
            File file= new File("");
            Scanner textToHash = null;
            try {
                textToHash = new Scanner(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            while(textToHash.hasNextLine()){
                String receivedpass = textToHash.nextLine();
                String newHash= "";
                try {
                    String type= worker.getHashType();
                    String hashTocompare=worker.getHashPass();
                    switch (type) {
                        case "SHA-512":
                            //mandar receivedpass
                            //receber no newHash
                            break;
                        case "PBKDF2":
                            //mandar receivedpass
                            //receber no newHash
                            break;
                        case "BCrypt":
                            //mandar receivedpass
                            //receber no newHash
                            break;
                        case "SCrypt":
                            //mandar receivedpass
                            // receber no newHash
                            break;

                        default:
                            System.out.println("Method not recognized");
                    }
                    if(!worker.match(hashTocompare,newHash)){
                       break;
                    }
                    else{
                        //update state
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void ReceiveWorker(WorkerObserverRI worker){
        Thread t1= new Thread(new myThread(worker));
        t1.start();
    }

    // para fazer testes as funçoes
    public void playService() { }

}
