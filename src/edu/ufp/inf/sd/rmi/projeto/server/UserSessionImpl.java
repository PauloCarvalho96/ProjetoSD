package edu.ufp.inf.sd.rmi.projeto.server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class UserSessionImpl extends UnicastRemoteObject implements UserSessionRI{

    private DBMockup db;

    public UserSessionImpl(DBMockup db) throws RemoteException {
        super();
        this.db = db;
    }

    @Override
    public void logout() throws RemoteException {
        // FALTA FAZER
    }
}
