package edu.ufp.inf.sd.rmi.projeto.server;

import java.util.ArrayList;

public class Task {

    private String url;
    private Integer start;
    private Integer delta;
    private TaskSubjectRI taskSubjectRI;
    private String hashType;
    private ArrayList<String> hashPass;

    public Task(String url, Integer start, Integer delta, TaskSubjectRI taskSubjectRI) {
        this.url = url;
        this.start = start;
        this.delta = delta;
        this.taskSubjectRI = taskSubjectRI;
    }

    public String getUrl() {
        return url;
    }

    public Integer getStart() {
        return start;
    }

    public Integer getDelta() {
        return delta;
    }

    public String getHashType() {
        return hashType;
    }

    public ArrayList<String> getHashPass() {
        return hashPass;
    }

    public TaskSubjectRI getTaskSubjectRI() {
        return taskSubjectRI;
    }
}
