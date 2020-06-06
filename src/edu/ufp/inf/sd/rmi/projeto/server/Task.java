package edu.ufp.inf.sd.rmi.projeto.server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Task implements Serializable {

    private String url;
    private Integer start;
    private Integer delta;
    private TaskSubjectRI taskSubjectRI;
    private State state;
    public String alphabet;
    public ArrayList<Integer> wordsSize = new ArrayList<>();
    public Boolean isHashing;
    public ArrayList<Integer> lines = new ArrayList<>();

    public Task(String url, Integer start, Integer delta, TaskSubjectRI taskSubjectRI) {
        this.url = url;
        this.start = start;
        this.delta = delta;
        this.taskSubjectRI = taskSubjectRI;
        this.state = new State("Available"); // passwords por encontrar
    }

    public Task(TaskSubjectRI taskSubjectRI,String alphabet,ArrayList<Integer> wordsSize) {
        this.alphabet = alphabet;
        this.taskSubjectRI = taskSubjectRI;
        this.wordsSize = wordsSize;
        this.state = new State("Available"); // passwords por encontrar
    }

    public Task(TaskSubjectRI taskSubjectRI,String alphabet,ArrayList<Integer> wordsSize,Integer start, Integer delta){
        this.alphabet = alphabet;
        this.taskSubjectRI = taskSubjectRI;
        this.wordsSize = wordsSize;
        this.state = new State("Available"); // passwords por encontrar
        this.start = start;
        this.delta = delta;
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

    public ArrayList<Integer> getWordsSize() {
        return wordsSize;
    }

    public void setStart(Integer start) {
        this.start = start;
    }
    
    public void setWordsSize(ArrayList<Integer> wordsSize) {
        this.wordsSize.addAll(wordsSize);
    }
}
