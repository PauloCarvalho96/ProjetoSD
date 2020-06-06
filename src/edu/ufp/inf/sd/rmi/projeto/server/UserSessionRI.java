package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.projeto.client.Client;
import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverRI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface UserSessionRI extends Remote {
        public void logout(String uname,UserSessionRI userSessionRI) throws RemoteException;
        public ArrayList<TaskSubjectRI> listTasks() throws RemoteException;
        public TaskSubjectRI createTask(String name, String hashType, ArrayList<String> hashPass, Integer creditsProc, Integer creditsFound, Integer delta, String uname, int strategy, HashMap<String, String> dataStrategy, Integer taskCredits, Client client) throws RemoteException;
        public void createWorker(WorkerObserverRI workerObserverRI, String uname) throws RemoteException;
        public void joinTask(String task, WorkerObserverRI workerObserverRI) throws RemoteException;
        public void pauseTask(TaskSubjectRI taskSubjectRI) throws RemoteException;
        public void resumeTask(TaskSubjectRI taskSubjectRI) throws RemoteException;
        public void stopTask(TaskSubjectRI taskSubjectRI,String uname) throws RemoteException;
        public ArrayList<WorkerObserverRI> getWorkersRI(String uname) throws RemoteException;
        public ArrayList<TaskSubjectRI> getTasksRI(String uname) throws RemoteException;
        public int getSizeWorkersDB(String name) throws RemoteException;
        public int getUserCreditsDB(String usr) throws RemoteException;
        public void setUserCreditsDB(String usr,Integer credits) throws RemoteException;
}
