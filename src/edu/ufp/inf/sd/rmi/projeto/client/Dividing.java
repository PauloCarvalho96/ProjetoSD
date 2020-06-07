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
    WorkerObserverImpl workerObserver;
    Task task;
    ArrayList<Integer> linesWithWordLength = new ArrayList<>();
    String file_name;


    public Dividing(int start, int delta, int id, String hashType, WorkerObserverImpl workerObserver, Task task,String file_name) {
        this.start = start;
        this.delta = delta;
        this.id = id;
        this.hashType = hashType;
        this.workerObserver = workerObserver;
        this.task = task;
        this.file_name = file_name;
    }

    @Override
    public void run() {
        try {
            File file = new File(file_name);

            int line = 0;

            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            String result = null;

            MessageDigest hashFunction;

            while ((st = br.readLine()) != null) {

                while(workerObserver.getStateWorker().getmsg().equals("Paused")){////pause thread
                    try {
                        System.out.println("\nTOU A DORMIR!");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(workerObserver.getStateWorker().getmsg().equals("Completed")){////stop thread
                    break;
                }

                if(task.getWordsSize().contains(st.length()) && line >= start && line < start + delta && !workerObserver.getStateWorker().getmsg().equals("Paused")){
                    boolean found = false;
                    linesWithWordLength.add(line+1); // não entendi o line+1
                    State state = new State("");
                    Client client = workerObserver.getClient();
                    int userCredits = client.userSessionRI.getUserCreditsDB(client.username);
                    client.userSessionRI.setUserCreditsDB(client.username,userCredits+1);
                    linesWithWordLength.add(line+1);
                    state.setmsg("Line Found");
                    this.workerObserver.updateNotFound(state, line);
                }
                if (line == start + delta) {
//                    State state = new State("");
//                    Client client = workerObserver.getClient();
//                    int userCredits = client.userSessionRI.getUserCreditsDB(client.username);
//                    client.userSessionRI.setUserCreditsDB(client.username,userCredits+1);
//                    linesWithWordLength.add(line+1);
//                    state.setmsg("Line Found");
//                    this.workerObserver.updateNotFound(state, line);
                    break;
                }
                line++;
            }
            task.getTaskSubjectRI().finishDividing(linesWithWordLength,workerObserver);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
