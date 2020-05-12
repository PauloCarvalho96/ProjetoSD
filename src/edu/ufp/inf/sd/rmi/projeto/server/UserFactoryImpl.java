package edu.ufp.inf.sd.rmi.projeto.server;

import sun.rmi.runtime.Log;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class UserFactoryImpl extends UnicastRemoteObject implements UserFactoryRI {

    private DBMockup db;

    protected UserFactoryImpl() throws RemoteException {
        super();
        db = new DBMockup();
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
            if(!this.db.getSessions().containsKey(uname)){
                UserSessionRI userSessionRI = new UserSessionImpl(db);
                this.db.addSession(uname,userSessionRI);     // insere no hashmap de sessoes
                return userSessionRI;
            } else {
                return this.db.getSessions().get(uname);
            }
        }
        return null;
    }
}
