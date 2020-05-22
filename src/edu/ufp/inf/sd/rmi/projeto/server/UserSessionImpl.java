package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverImpl;
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
    public TaskSubjectRI createTask(String name, String hashType, ArrayList<String> hashPass,Integer delta, String uname) throws RemoteException {
        /** verifica se existe taskgroup com nome dado */
        if(db.getTask(name) != null){
            return null;
        }
        TaskSubjectRI taskSubjectRI = new TaskSubjectImpl(name, hashType, hashPass,delta);
        db.assocTaskToUser(uname, taskSubjectRI);  // adiciona task a DB
        return taskSubjectRI;
    }

    @Override
    public WorkerObserverRI createWorker(int n_threads, String uname) throws RemoteException {
        WorkerObserverRI workerObserverRI = new WorkerObserverImpl(db.allWorkers().size()+1, uname, n_threads);
        db.assocWorkerToUser(uname, workerObserverRI);
        return workerObserverRI;
    }

    @Override
    public void joinTask(String taskname, WorkerObserverRI workerObserverRI) throws RemoteException {
        TaskSubjectRI task = this.db.getTask(taskname);
        task.attach(workerObserverRI);
    }

    @Override
    public void stopTask(TaskSubjectRI taskSubjectRI,String uname) throws RemoteException {
        TaskSubjectRI taskSubjectRI1 = db.getTask(taskSubjectRI.getName());
        taskSubjectRI.getState().setmsg("Completed");
        taskSubjectRI.notifyAllObservers();
        db.removeTask(taskSubjectRI1,uname);
    }

    @Override
    public void pauseTask(TaskSubjectRI taskSubjectRI,String uname) throws RemoteException {
    }

    @Override
    public ArrayList<WorkerObserverRI> getWorkersRI(String uname) throws RemoteException {
        return db.getWorkersFromUser(uname);
    }

    @Override
    public ArrayList<TaskSubjectRI> getTasksRI(String uname) throws RemoteException {
        return db.getTasksFromUser(uname);
    }
}
