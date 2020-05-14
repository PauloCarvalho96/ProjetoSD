package edu.ufp.inf.sd.rmi.projeto.client;

import edu.ufp.inf.sd.rmi.projeto.server.TaskSubjectRI;
import edu.ufp.inf.sd.rmi.projeto.server.UserFactoryRI;
import edu.ufp.inf.sd.rmi.projeto.server.UserSessionRI;
import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

    /**
     * Context for connecting a RMI client to a RMI Servant
     */
    private SetupContextRMI contextRMI;
    /**
     * Remote interface that will hold the Servant proxy
     */
    private UserFactoryRI userFactoryRI;

    private UserSessionRI session;

    private String uname;

    private ArrayList<WorkerObserverRI> workers = new ArrayList<>();


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

    // para fazer testes as funçoes
    public void playService() {
        try {
            String usr = "test";
            String psw = "test";

            // registo
            if(this.userFactoryRI.register(usr, psw)){
                System.out.println("User criado com sucesso");
            } else {
                System.out.println("Erro ao criar user!");
            }

            session = this.userFactoryRI.login(usr,psw);

            if(session != null){
                // abre menu
                System.out.println("Sessao iniciada!");
                // cria task
                //TaskSubjectRI taskSubjectRI = session.createTask("task","MD5","abcdefgh");
                // password existente no ficheiro
                //System.out.println("\n"+taskSubjectRI.readFile("herdhaak")+"\n");
                uname=usr;
            } else {
                System.out.println("Erro no login!");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void handlerJoinTask(ActionEvent actionEvent) throws RemoteException {///comparar threads, etc.....
        WorkerObserverRI worker = new WorkerObserverImpl(uname);
        workers.add(worker);
        //worker.selectTask(nameTasksCB.getValue());
        session.joinTask("task",worker);
    }

}
