package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    /**
     * Context for running a RMI Servant on a host
     */
    private SetupContextRMI contextRMI;
    /**
     * Remote interface that will hold reference to the Servant impl
     */
    private UserFactoryRI userFactoryRI;

    public static void main(String[] args) {
        if (args != null && args.length < 3) {
            System.exit(-1);
        } else {
            //1. ============ Create Servant ============
            Server hws = new Server(args);
            //2. ============ Rebind servant on rmiregistry ============
            hws.rebindService();
        }
    }

    /**
     *
     * @param args
     */
    public Server(String args[]) {
        try {
            //============ List and Set args ============
            SetupContextRMI.printArgs(this.getClass().getName(), args);
            String registryIP = args[0];
            String registryPort = args[1];
            String serviceName = args[2];
            //============ Create a context for RMI setup ============
            contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});
        } catch (RemoteException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    private void rebindService() {
        try {
            //Get proxy to rmiregistry
            Registry registry = contextRMI.getRegistry();
            //Bind service on rmiregistry and wait for calls
            if (registry != null) {
                //============ Create Servant ============
                userFactoryRI = new UserFactoryImpl();

                //Get service url (including servicename)
                String serviceUrl = contextRMI.getServicesUrl(0);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "going to rebind service @ {0}", serviceUrl);

                //============ Rebind servant ============
                registry.rebind(serviceUrl, userFactoryRI);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "service bound and running. :)");
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "registry not bound (check IPs). :(");
            }
        } catch (RemoteException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void loadProperties() throws IOException {

        Logger.getLogger(Thread.currentThread().getName()).log(Level.INFO, "goig to load props...");
        // create and load default properties
        Properties defaultProps = new Properties();
        FileInputStream in = new FileInputStream("defaultproperties.txt");
        defaultProps.load(in);
        in.close();

        BiConsumer<Object, Object> bc = (key, value) ->{
            System.out.println(key.toString()+"="+value.toString());
        };
        defaultProps.forEach(bc);

        // create application properties with default
        Properties props = new Properties(defaultProps);

        FileOutputStream out = new FileOutputStream("defaultproperties2.txt");
        props.store(out, "---No Comment---");
        out.close();
    }
}
