package edu.ufp.inf.sd.rmi.projeto.client;

import edu.ufp.inf.sd.rmi.projeto.server.TaskSubjectRI;
import edu.ufp.inf.sd.rmi.projeto.server.UserFactoryRI;
import edu.ufp.inf.sd.rmi.projeto.server.UserSessionRI;
import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;
import javafx.application.Application;
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

public class Client extends Application {

    /**
     * Context for connecting a RMI client to a RMI Servant
     */
    private SetupContextRMI contextRMI;
    /**
     * Remote interface that will hold the Servant proxy
     */
    private UserFactoryRI userFactoryRI;

    public static void main(String[] args) {
        //1. ============ Setup client RMI context ============
        Client hwc = new Client(args);
        //2. ============ Lookup service ============
        hwc.lookupService();
        //3. ============ Play with service ============
        hwc.playService();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("login_register.fxml"));

        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("Application");
        stage.setScene(scene);
        stage.show();
    }

    public Client(String[] args) {
        try {
            //List ans set args
            SetupContextRMI.printArgs(this.getClass().getName(), args);
            String registryIP = args[0];
            String registryPort = args[1];
            String serviceName = args[2];
            //Create a context for RMI setup
            contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});
        } catch (RemoteException e) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private Remote lookupService() {
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
    private void playService() {
        try {
            String usr = "test";
            String psw = "test";

            // registo
            if(this.userFactoryRI.register(usr, psw)){
                System.out.println("User criado com sucesso");
            } else {
                System.out.println("Erro ao criar user!");
            }

            // login
            UserSessionRI sessionRI = this.userFactoryRI.login(usr,psw);
            if(sessionRI != null){
                // abre menu
                System.out.println("Sessao iniciada!");
                // cria task
                TaskSubjectRI taskSubjectRI = sessionRI.createTask("task","MD5","abcdefgh");
                // password existente no ficheiro
                System.out.println("\n"+taskSubjectRI.readFile("herdhaak")+"\n");
            } else {
                System.out.println("Erro no login!");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
