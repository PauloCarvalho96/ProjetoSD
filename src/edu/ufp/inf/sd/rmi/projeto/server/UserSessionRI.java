package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverRI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface UserSessionRI extends Remote {
        public void logout(String uname,UserSessionRI userSessionRI) throws RemoteException;
        public ArrayList<TaskSubjectRI> listTasks() throws RemoteException;
        public TaskSubjectRI createTask(String name,String hashType,ArrayList<String> hashPass,Integer delta, String uname) throws RemoteException;
        public WorkerObserverRI createWorker(int n_threads, String uname) throws RemoteException ;
        public void joinTask(String task, WorkerObserverRI workerObserverRI) throws RemoteException;
        public void pauseTask(TaskSubjectRI taskSubjectRI,String uname) throws RemoteException;
        public void stopTask(TaskSubjectRI taskSubjectRI,String uname) throws RemoteException;
        public ArrayList<WorkerObserverRI> getWorkersRI(String uname) throws RemoteException;
        public ArrayList<TaskSubjectRI> getTasksRI(String uname) throws RemoteException;
}
