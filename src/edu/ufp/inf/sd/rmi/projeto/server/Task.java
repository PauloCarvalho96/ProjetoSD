package edu.ufp.inf.sd.rmi.projeto.server;

import java.io.Serializable;

public class Task implements Serializable {

    private String url;
    private Integer start;
    private Integer delta;
    private TaskSubjectRI taskSubjectRI;
    private State state;
    public String alphabet;
    public Integer wordsSize;

    public Task(String url, Integer start, Integer delta, TaskSubjectRI taskSubjectRI) {
        this.url = url;
        this.start = start;
        this.delta = delta;
        this.taskSubjectRI = taskSubjectRI;
        this.state = new State("Available"); // passwords por encontrar
    }

    public Task(TaskSubjectRI taskSubjectRI,String alphabet,Integer wordsSize) {
        this.alphabet = alphabet;
        this.taskSubjectRI = taskSubjectRI;
        this.wordsSize = wordsSize;
        this.state = new State("Available"); // passwords por encontrar
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

    public String getAlphabet() {
        return alphabet;
    }

    public Integer getWordsSize() {
        return wordsSize;
    }
}
