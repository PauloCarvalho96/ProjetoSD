package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverImpl;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface UserSessionRI extends Remote {
        public void logout(String uname,UserSessionRI userSessionRI) throws RemoteException;
        public ArrayList<TaskSubjectRI> listTask() throws RemoteException;
        public TaskSubjectRI createTask(String name,String hashType,String hashPass) throws RemoteException;
        public void joinTask(TaskSubjectRI taskSubjectRI, WorkerObserverImpl workerObserverImpl) throws RemoteException;
        public void pauseTask(TaskSubjectRI taskSubjectRI) throws RemoteException;
        public void deleteTask(TaskSubjectRI taskSubjectRI) throws RemoteException;
}
