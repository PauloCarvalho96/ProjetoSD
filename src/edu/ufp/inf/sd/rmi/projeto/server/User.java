package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverRI;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private String uname;
    private String pword;
    private Integer credits = 0;

    public User(String uname, String pword) {
        this.uname = uname;
        this.pword = pword;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPword() {
        return pword;
    }

    public void setPword(String pword) {
        this.pword = pword;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    @Override
    public String toString() {
        return "User{" + "uname=" + uname + ", pword=" + pword + '}';
    }

}
