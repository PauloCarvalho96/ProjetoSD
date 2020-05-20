package edu.ufp.inf.sd.rmi.projeto.server;

public class Result {

    private String hash;
    private String result;

    public Result(String hash, String result) {
        this.hash = hash;
        this.result = result;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
