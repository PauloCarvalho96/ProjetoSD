package edu.ufp.inf.sd.rmi.projeto.server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class UserFactoryImpl extends UnicastRemoteObject implements UserFactoryRI {

    private DBMockup db;
    private HashMap<String, UserSessionRI> sessions;

    protected UserFactoryImpl() throws RemoteException {
        super();
        db = new DBMockup();
        sessions = new HashMap<>();
    }

    @Override
    public boolean register(String uname, String pw) throws RemoteException {
        if(!db.exists(uname,pw)){
            db.register(uname,pw);
            return true;
        }
        return false;
    }

    @Override
    public UserSessionRI login(String uname, String pw) throws RemoteException {
        if (db.exists(uname, pw)) {
            if(!this.sessions.containsKey(uname)){
                UserSessionRI userSessionRI = new UserSessionImpl(db);
                return userSessionRI;
            } else {
                return this.sessions.get(uname);
            }
        }
        return null;
    }
}
