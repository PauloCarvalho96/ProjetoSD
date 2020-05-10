package edu.ufp.inf.sd.rmi.projeto.client;

import edu.ufp.inf.sd.rmi.projeto.server.State;
import edu.ufp.inf.sd.rmi.projeto.server.TaskSubjectRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class WorkerObserverImpl extends UnicastRemoteObject implements WorkerObserverRI {

    private State lastObserverState;
    protected TaskSubjectRI taskSubjectRI;

    protected WorkerObserverImpl() throws RemoteException {
        super();
    }

    @Override
    public void update() throws RemoteException {
        this.lastObserverState = taskSubjectRI.getState();
    }
}
