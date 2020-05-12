package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverImpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class UserSessionImpl extends UnicastRemoteObject implements UserSessionRI{

    private DBMockup db;

    public UserSessionImpl(DBMockup db) throws RemoteException {
        super();
        this.db = db;
    }

    @Override
    public void logout(String uname, UserSessionRI userSessionRI) throws RemoteException {
        db.removeSession(uname,userSessionRI);
    }

    @Override
    public ArrayList<TaskSubjectRI> listTask() throws RemoteException {
        return db.allTasks();
    }

    // cria nova task
    @Override
    public TaskSubjectRI createTask(String name, String hashType, String hashPass) throws RemoteException {
        TaskSubjectRI taskSubjectRI = new TaskSubjectImpl(name,hashType,hashPass);
        db.addTask(taskSubjectRI);  // adiciona task a DB
        return taskSubjectRI;
    }

    @Override
    public void joinTask(TaskSubjectRI taskSubjectRI, WorkerObserverImpl workerObserverImpl) throws RemoteException {

    }

    @Override
    public void deleteTask(TaskSubjectRI taskSubjectRI) throws RemoteException {
        db.removeTask(taskSubjectRI);
    }

    @Override
    public void pauseTask(TaskSubjectRI taskSubjectRI) throws RemoteException {

    }

}
