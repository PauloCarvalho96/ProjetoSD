package edu.ufp.inf.sd.rmi.projeto.server;

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
    public void logout(UserSessionRI userSessionRI) throws RemoteException {
        db.removeSession(userSessionRI);
    }

    @Override
    public ArrayList<TaskSubjectRI> listTask() throws RemoteException {
        return db.allTasks();
    }

    @Override
    public TaskSubjectRI createTask(String name, String hash) throws RemoteException {
        return null;
    }

    @Override
    public Boolean pauseTask(TaskSubjectRI taskSubjectRI) throws RemoteException {
        return null;
    }

    @Override
    public Boolean deleteTask(TaskSubjectRI taskSubjectRI) throws RemoteException {
        return null;
    }

}
