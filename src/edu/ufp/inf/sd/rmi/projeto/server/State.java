package edu.ufp.inf.sd.rmi.projeto.server;

import java.io.Serializable;

public class State implements Serializable {

    private String id;

    public State(String id, String m) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
