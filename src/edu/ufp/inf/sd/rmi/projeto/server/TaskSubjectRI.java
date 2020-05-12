package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverRI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TaskSubjectRI extends Remote {
    public void attach(WorkerObserverRI obsRI) throws RemoteException;
    public void detach(WorkerObserverRI obsRI) throws RemoteException;
    public State getState() throws RemoteException;
    public String getName() throws RemoteException;
    public void setState(State state) throws RemoteException;
    public boolean readFile(String pswtodiscover) throws RemoteException;
}
