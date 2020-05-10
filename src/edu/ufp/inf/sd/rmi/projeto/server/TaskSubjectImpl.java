package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskSubjectImpl extends UnicastRemoteObject implements TaskSubjectRI {

    private State subjectState;
    private final ArrayList<WorkerObserverRI> observers = new ArrayList<>();

    public TaskSubjectImpl() throws RemoteException {
        super();
    }

    public void notifyAllObservers(){
        for (WorkerObserverRI obs: observers) {
            try {
                obs.update();
            } catch (RemoteException ex) {
                Logger.getLogger(TaskSubjectImpl.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
    }

    @Override
    public void attach(WorkerObserverRI obsRI) throws RemoteException {

    }

    @Override
    public void detach(WorkerObserverRI obsRI) throws RemoteException {

    }

    @Override
    public State getState() throws RemoteException {
        return null;
    }

    @Override
    public void setState(State state) throws RemoteException {

    }
}
