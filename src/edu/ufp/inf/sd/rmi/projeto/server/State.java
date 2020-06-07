package edu.ufp.inf.sd.rmi.projeto.server;

import java.io.Serializable;

public class State implements Serializable {

    private String id;
    
    private String msg;

    private String process;

    private Integer n_credits = 0;

    //TASK & WORKER
    public final String PAUSED = "Paused";
    public final String COMPLETED = "Completed";
    public final String INCOMPLETED = "Incompleted";
    public final String NOT_COMPLETED = "Not Completed";
    public final String WORKING = "Working";

    //TASK
    public final String AVAILABLE = "Available";
    public final String FULL = "Full";

    //WORKER
    public final String FOUND = "Found";
    public final String NOT_FOUND = "Not Found";

    public State(String id, String msg) {
        this.id = id;
        this.msg = msg;
    }

    public State(String msg) {
        this.msg = msg;
    }

    public State() {
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

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public Integer getN_credits() {
        return n_credits;
    }

    public void setN_credits(Integer n_credits) {
        this.n_credits = n_credits;
    }
}
