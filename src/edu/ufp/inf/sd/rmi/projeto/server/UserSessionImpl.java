package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverRI;

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
    public ArrayList<TaskSubjectRI> listTasks() throws RemoteException {
        return db.allTasks();
    }

    // cria nova task
    @Override
    public TaskSubjectRI createTask(String name, String hashType, ArrayList<String> hashPass,Integer delta) throws RemoteException {
        /** verifica se existe taskgroup com nome dado */
        for (TaskSubjectRI taskSubjectRI:db.allTasks()) {
            if(taskSubjectRI.getName().equals(name)){
                return null;
            }
        }

        TaskSubjectRI taskSubjectRI = new TaskSubjectImpl(name, hashType, hashPass,delta);
        db.addTask(taskSubjectRI);  // adiciona task a DB
        return taskSubjectRI;
    }

    @Override
    public void joinTask(String taskname, WorkerObserverRI workerObserverRI) throws RemoteException {
        TaskSubjectRI task = this.db.getTask(taskname);
        task.attach(workerObserverRI);
    }

    @Override
    public void deleteTask(TaskSubjectRI taskSubjectRI) throws RemoteException {
        db.removeTask(taskSubjectRI);
    }

    @Override
    public void pauseTask(TaskSubjectRI taskSubjectRI) throws RemoteException {

    }

}
