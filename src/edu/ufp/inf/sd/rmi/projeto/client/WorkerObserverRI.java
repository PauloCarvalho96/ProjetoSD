package edu.ufp.inf.sd.rmi.projeto.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WorkerObserverRI extends Remote {
    public void update() throws RemoteException;
}
