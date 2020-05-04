package edu.ufp.inf.sd.rmi.projeto.server;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author rmoreira
 */
public interface UserFactoryRI extends Remote {
    public boolean register(String uname, String pw) throws RemoteException;
    public UserSessionRI login(String uname, String pw) throws RemoteException;
}

