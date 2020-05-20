package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverRI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface TaskSubjectRI extends Remote {
    public void attach(WorkerObserverRI obsRI) throws RemoteException;
    public void detach(WorkerObserverRI obsRI) throws RemoteException;
    public State getState() throws RemoteException;
    public String getHashType() throws RemoteException;
    public void setState(State state) throws RemoteException;
    public String getName() throws RemoteException;
    public ArrayList<String> getHashPass() throws RemoteException;
    public Task getTaskFromArray() throws RemoteException;
    public boolean isAvailable() throws RemoteException;
    public void changeWorkerState(State state, String hashPass,String pass) throws RemoteException;
    public ArrayList<Result> getResult() throws RemoteException;
}
