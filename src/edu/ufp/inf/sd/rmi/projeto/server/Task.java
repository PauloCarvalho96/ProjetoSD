package edu.ufp.inf.sd.rmi.projeto.server;

import java.io.Serializable;
import java.util.ArrayList;

public class Task implements Serializable {

    private String url;
    private Integer start;
    private Integer delta;
    private TaskSubjectRI taskSubjectRI;
    private State state;

    public Task(String url, Integer start, Integer delta, TaskSubjectRI taskSubjectRI) {
        this.url = url;
        this.start = start;
        this.delta = delta;
        this.taskSubjectRI = taskSubjectRI;
        this.state = new State("Avalible"); // passwords por encontrar
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

    public TaskSubjectRI getTaskSubjectRI() {
        return taskSubjectRI;
    }


}
