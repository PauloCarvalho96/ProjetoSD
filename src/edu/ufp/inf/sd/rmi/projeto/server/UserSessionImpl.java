package edu.ufp.inf.sd.rmi.projeto.server;

import java.rmi.RemoteException;

public class UserSessionImpl implements UserSessionRI {

    private DBMockup db;

    public UserSessionImpl(DBMockup db) {
        this.db = db;
    }

    @Override
    public void logout() throws RemoteException {
        // FALTA FAZER
    }
}
