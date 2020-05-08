package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverRI;

import java.io.Serializable;
import java.util.ArrayList;

public class DBMockup implements Serializable {

    // arraylist com utilizadores
    private final ArrayList<User> users;
    private final ArrayList<TaskSubjectRI> tasks;
    private final ArrayList<WorkerObserverRI> workers;

    public DBMockup() {
        users = new ArrayList<>();
        tasks = new ArrayList<>();
        workers = new ArrayList<>();

        //Add user
        users.add(new User("test", "test"));
    }

    /**
     * Registers a new user.
     * 
     * @param u username
     * @param p passwd
     */
    public void register(String u, String p) {
        if (!exists(u, p)) {
            users.add(new User(u, p));
        }
    }

    /**
     * Checks the credentials of an user.
     * 
     * @param u username
     * @param p passwd
     * @return
     */
    public boolean exists(String u, String p) {
        for (User usr : this.users) {
            if (usr.getUname().compareTo(u) == 0 && usr.getPword().compareTo(p) == 0) {
                return true;
            }
        }
        return false;
    }

}
