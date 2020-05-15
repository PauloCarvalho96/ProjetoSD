package edu.ufp.inf.sd.rmi.projeto.server;

import java.io.Serializable;

public class State implements Serializable {

    private String id;
    
    private String msg;

    public State(String id, String msg) {
        this.id = id;
        this.msg = msg;
    }

    public String getmsg() {
        return msg;
    }

    public void setmsg(String msg) {
        this.msg = msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
