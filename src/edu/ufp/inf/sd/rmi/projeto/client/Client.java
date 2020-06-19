package edu.ufp.inf.sd.rmi.projeto.client;

import edu.ufp.inf.sd.rmi.projeto.server.TaskSubjectRI;
import edu.ufp.inf.sd.rmi.projeto.server.UserFactoryRI;
import edu.ufp.inf.sd.rmi.projeto.server.UserSessionRI;
import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements Serializable {

    /**
     * Context for connecting a RMI client to a RMI Servant
     */
    private transient SetupContextRMI contextRMI;
    /**
     * Remote interface that will hold the Servant proxy
     */
    public UserFactoryRI userFactoryRI;

    public UserSessionRI userSessionRI;

    public String username;

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

    public ArrayList<WorkerObserverRI> getWorkersRI() {
        try {
            return userSessionRI.getWorkersRI(this.username);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<TaskSubjectRI> getTasksRI() {
        try {
            return userSessionRI.getTasksRI(this.username);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

}
