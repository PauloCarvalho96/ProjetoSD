package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverImpl;
import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class UserSessionImpl extends UnicastRemoteObject implements UserSessionRI{

    private DBMockup db;
    private User user;

    public UserSessionImpl(DBMockup db,User user) throws RemoteException {
        super();
        this.db = db;
        this.user = user;
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
        user.getTasksRI().add(taskSubjectRI);
        return taskSubjectRI;
    }

    @Override
    public WorkerObserverRI createWorker(Task task, int n_threads) throws RemoteException {
        WorkerObserverRI workerObserverRI = new WorkerObserverImpl(this.user.getWorkersRI().size()+1, this.user.getUname(), task, n_threads);
        if(!this.user.getWorkersRI().contains(workerObserverRI)){
            this.user.getWorkersRI().add(workerObserverRI);
        }
        return workerObserverRI;
    }

    @Override
    public User getUser() throws RemoteException {
        return user;
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
