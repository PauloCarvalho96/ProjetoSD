package edu.ufp.inf.sd.rmi.projeto.client;

import edu.ufp.inf.sd.rmi.projeto.server.State;
import edu.ufp.inf.sd.rmi.projeto.server.TaskSubjectRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class WorkerObserverImpl extends UnicastRemoteObject implements WorkerObserverRI {

    private State lastObserverState;
    protected TaskSubjectRI task;
    private String uname;

    protected WorkerObserverImpl(String uname) throws RemoteException {
        super();
    }

    @Override
    public void update(State stateTask) throws RemoteException {
        this.lastObserverState = task.getState();
    }

    @Override
    public boolean match(String str1, String str2) throws RemoteException {
        return str1.compareTo(str2) == 0;
    }

    @Override
    public String getHash(String str) throws RemoteException {
        return null;
    }

    @Override
    public void selectTask(TaskSubjectRI task) throws RemoteException {
        Thread thread = new Thread();
    }

    @Override
    public void removeWorker() throws RemoteException {

    }

    @Override
    public void setStateWorker(State state) throws RemoteException {

    }

    @Override
    public State getStateWorker() throws RemoteException {
        return null;
    }
}
