package edu.ufp.inf.sd.rmi.projeto.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface UserSessionRI extends Remote {
        public void logout(UserSessionRI userSessionRI) throws RemoteException;
        public ArrayList<TaskSubjectRI> listTask() throws RemoteException;
        public TaskSubjectRI createTask(String name,String hash) throws RemoteException;
        public Boolean pauseTask(TaskSubjectRI taskSubjectRI) throws RemoteException;
        public Boolean deleteTask(TaskSubjectRI taskSubjectRI) throws RemoteException;
}
