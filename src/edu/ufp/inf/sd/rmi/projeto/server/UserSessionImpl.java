package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class UserSessionImpl extends UnicastRemoteObject implements UserSessionRI{

    private final DBMockup db;

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
    public TaskSubjectRI createTask(String name, String hashType, ArrayList<String> hashPass, Integer creditsProc, Integer creditsFound, Integer delta, String uname, int strategy, HashMap<String, String> dataStrategy, Integer taskCredits) throws RemoteException {
        /** verifica se existe taskgroup com nome dado */
        if(db.getTask(name) != null){
            return null;
        }
        TaskSubjectRI taskSubjectRI = null;
        switch (strategy){
            case 1:
                taskSubjectRI = new TaskSubjectImplS1(name, hashType, hashPass, creditsProc, creditsFound, delta,taskCredits);
                break;
            case 2:
                String[] s2 = dataStrategy.get("length").split(";");
                ArrayList<Integer> len2 = new ArrayList<>();
                for(String st:s2){
                    len2.add(Integer.parseInt(st));
                }
                taskSubjectRI = new TaskSubjectImplS2(name, hashType, hashPass, creditsProc, creditsFound, delta, len2,taskCredits);
                break;
            case 3:
                int wordsize = Integer.parseInt(dataStrategy.get("length"));
                taskSubjectRI = new TaskSubjectImplS3(name, hashType, hashPass, creditsProc, creditsFound, delta,wordsize , dataStrategy.get("alphabet"),taskCredits);
                break;
        }
        db.assocTaskToUser(uname, taskSubjectRI);  // adiciona task a DB
        return taskSubjectRI;
    }

    @Override
    public void createWorker(WorkerObserverRI workerObserverRI, String uname) throws RemoteException {
        db.assocWorkerToUser(uname, workerObserverRI);
    }

    @Override
    public void joinTask(String taskname, WorkerObserverRI workerObserverRI) throws RemoteException {
        TaskSubjectRI task = this.db.getTask(taskname);
        task.attach(workerObserverRI);
    }

    @Override
    public void stopTask(TaskSubjectRI taskSubjectRI,String uname) throws RemoteException {
        TaskSubjectRI taskDB = db.getTask(taskSubjectRI.getName());
        if(taskDB != null) {
            taskDB.stop();
            db.removeTask(taskDB, uname);
        }
    }

    @Override
    public void pauseTask(TaskSubjectRI taskSubjectRI) throws RemoteException {
        TaskSubjectRI taskDB = db.getTask(taskSubjectRI.getName());
        if(taskDB != null) {
            taskDB.pause();
        }
    }

    @Override
    public void resumeTask(TaskSubjectRI taskSubjectRI) throws RemoteException {
        TaskSubjectRI taskDB = db.getTask(taskSubjectRI.getName());
        if(taskDB != null) {
            taskDB.resume();
        }
    }

    @Override
    public ArrayList<WorkerObserverRI> getWorkersRI(String uname) throws RemoteException {
        return db.getWorkersFromUser(uname);
    }

    @Override
    public ArrayList<TaskSubjectRI> getTasksRI(String uname) throws RemoteException {
        return db.getTasksFromUser(uname);
    }

    @Override
    public int getSizeWorkersDB(String name) throws RemoteException {
        return db.allWorkersUsers(name).size();
    }

    /** créditos do user */
    @Override
    public int getUserCreditsDB(String usr) throws RemoteException {
        return db.getUser(usr).getCredits();
    }

    @Override
    public void setUserCreditsDB(String usr,Integer credits) throws RemoteException {
        db.getUser(usr).setCredits(credits);
    }

}
