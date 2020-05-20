package edu.ufp.inf.sd.rmi.projeto.client;

import edu.ufp.inf.sd.rmi.projeto.server.State;
import edu.ufp.inf.sd.rmi.projeto.server.TaskSubjectRI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface WorkerObserverRI extends Remote {
    public void update(State state, String hashPass,String pass) throws RemoteException;
    public boolean match(String str1, String str2) throws RemoteException;
    public String getHash(String str) throws RemoteException;
    public void selectTask(TaskSubjectRI task) throws RemoteException;
    public void removeWorker() throws RemoteException;
    public void  setStateWorker(State state) throws RemoteException;
    public State  getStateWorker() throws RemoteException;
    public Integer getId() throws RemoteException;
    public String getTaskName() throws RemoteException;
    public String getHashType() throws RemoteException;
    public ArrayList<String> getHashPass() throws RemoteException;
    public void taskUpdated() throws RemoteException;
}
