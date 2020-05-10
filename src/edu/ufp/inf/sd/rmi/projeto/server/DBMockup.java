package edu.ufp.inf.sd.rmi.projeto.server;

import java.io.Serializable;
import java.util.ArrayList;

public class DBMockup implements Serializable {

    // arraylist com utilizadores
    private final ArrayList<User> users;
    // array sessoes
    private final ArrayList<UserSessionRI> sessions;
    // array tasks
    private final ArrayList<TaskSubjectRI> tasks;


    public DBMockup() {
        users = new ArrayList<>();
        sessions = new ArrayList<>();
        tasks = new ArrayList<>();

        //Add one user
        users.add(new User("test", "test"));
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

    // quando utilizador faz login cria nova sessao
    public void addSession(UserSessionRI sessionRI){
        sessions.add(sessionRI);
    }

    // remove sessao
    public void removeSession(UserSessionRI sessionRI){
        sessions.remove(sessionRI);
    }

    // adiciona nova task
    public void addTask(TaskSubjectRI taskSubjectRI){
        tasks.add(taskSubjectRI);
    }

    // todas as tasks disponveis
    public ArrayList<TaskSubjectRI> allTasks(){
        return this.tasks;
    }

}
