package edu.ufp.inf.sd.rmi.projeto.client;

import edu.ufp.inf.sd.rmi.projeto.server.State;
import edu.ufp.inf.sd.rmi.projeto.server.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Dividing implements Runnable {

    int start;
    int delta;
    int id;
    String hashType;
    WorkerObserverRI workerObserverRI;
    Task task;
    ArrayList<Integer> linesWithWordLength = new ArrayList<>();


    public Dividing(int start, int delta, int id, String hashType, WorkerObserverRI workerObserverRI, Task task) {
        this.start = start;
        this.delta = delta;
        this.id = id;
        this.hashType = hashType;
        this.workerObserverRI = workerObserverRI;
        this.task = task;
    }

    @Override
    public void run() {
        try {
            File file = new File("file"+workerObserverRI.getId()+".txt");

            int line = 0;

            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            String result = null;

            MessageDigest hashFunction;

            while ((st = br.readLine()) != null) {

                if(workerObserverRI.getStateWorker().getmsg().equals("Paused")){////pause thread
                    try {
                        System.out.println("\nTOU A DORMIR!");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(workerObserverRI.getStateWorker().getmsg().equals("Completed")){////stop thread
                    return;
                }

                if(task.getWordsSize().contains(st.length()) && line >= start && line < start + delta && !workerObserverRI.getStateWorker().getmsg().equals("Paused")){
                    boolean found = false;
                    linesWithWordLength.add(line+1);
                }
                if (line == start + delta) {
                    break;
                }
                line++;
            }
            task.getTaskSubjectRI().finishDividing(linesWithWordLength,workerObserverRI);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
