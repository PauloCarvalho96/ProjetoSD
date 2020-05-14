package edu.ufp.inf.sd.rmi.projeto.server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class DBMockup implements Serializable {

    // array users
    private final ArrayList<User> users;
    // array sessoes (username/session)
    private final HashMap<String,UserSessionRI> sessions;
    // array tasks
    private final ArrayList<TaskSubjectRI> tasks;


    public DBMockup() {
        users = new ArrayList<>();
        sessions = new HashMap<>();
        tasks = new ArrayList<>();
    }

    // registo
    public void register(String u, String p) {
        if (!exists(u, p)) {
            users.add(new User(u, p));
        }
    }

    // verifica se utilizador existe
    public boolean exists(String u, String p) {
        for (User usr : this.users) {
            if (usr.getUname().compareTo(u) == 0 && usr.getPword().compareTo(p) == 0) {
                return true;
            }
        }
        return false;
    }

    public HashMap<String,UserSessionRI> getSessions() {
        return sessions;
    }

    // quando utilizador faz login cria nova sessao
    public void addSession(String uname,UserSessionRI sessionRI){
        sessions.put(uname,sessionRI);
    }

    // remove sessao
    public void removeSession(String uname,UserSessionRI sessionRI){
        sessions.remove(uname,sessionRI);
    }

    // adiciona nova task
    public void addTask(TaskSubjectRI taskSubjectRI){
        tasks.add(taskSubjectRI);
    }

    public void removeTask(TaskSubjectRI taskSubjectRI){
        tasks.remove(taskSubjectRI);
    }

    // todas as tasks disponveis
    public ArrayList<TaskSubjectRI> allTasks(){
        return tasks;
    }

    public TaskSubjectRI getTask(String task) throws RemoteException {
        for (TaskSubjectRI taskSubjectRI:tasks) {
            if(task.compareTo(taskSubjectRI.getName())==0){
                return taskSubjectRI;
            }
        }
        return null;
    }

}
