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
    int wordLength;
    ArrayList<Integer> linesWithWordLength = new ArrayList<>();

    public Dividing(int start, int delta, int id, String hashType, WorkerObserverRI workerObserverRI, Task task,int wordLength) {
        this.start = start;
        this.delta = delta;
        this.id = id;
        this.hashType = hashType;
        this.workerObserverRI = workerObserverRI;
        this.task = task;
        this.wordLength=wordLength;

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

                if(st.length()==wordLength && !workerObserverRI.getStateWorker().getmsg().equals("Paused")){
                    boolean found = false;
                    linesWithWordLength.add(line);

                    found = true;

                    State state = new State("");
                    //System.out.println(st);
                    if(found){
                        state.setmsg(state.FOUND);
                        this.workerObserverRI.updateFound(state,result,st, line);
                    }else if(line % (delta * 0.1) == 0){
                        state.setmsg(state.NOT_FOUND);
                        this.workerObserverRI.updateNotFound(state, line);
                    }
                }
                if (line == start + delta) {
                    break;
                }
                line++;
            }
            br.close();
            workerObserverRI.setLinesWithWordLength(linesWithWordLength);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
