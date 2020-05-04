package edu.ufp.inf.sd.rmi.projeto.server;

import java.util.ArrayList;

public class DBMockup {

    // arraylist com utilizadores
    private final ArrayList<User> users;

    public DBMockup() {
        users = new ArrayList();

        //Add one user
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
