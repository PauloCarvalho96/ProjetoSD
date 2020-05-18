package edu.ufp.inf.sd.rmi.projeto.server;

public class Task {

    private String url;
    private Integer start;
    private Integer delta;

    public Task(String url, Integer start, Integer delta) {
        this.url = url;
        this.start = start;
        this.delta = delta;
    }

}
