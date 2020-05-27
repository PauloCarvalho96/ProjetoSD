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

public class MyThread implements Runnable {

    int start;
    int delta;
    int id;
    String hashType;
    WorkerObserverRI workerObserverRI;
    Task task;

    public MyThread(int start, int delta, int id,String hashType, WorkerObserverRI workerObserverRI,Task task) {
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

                if (line >= start && line < start + delta && !workerObserverRI.getStateWorker().getmsg().equals("Paused")) {
                    switch (hashType) {
                        case "SHA-512":
                            hashFunction = MessageDigest.getInstance("SHA-512");
                            hashFunction.reset();
                            hashFunction.update(st.getBytes("utf8"));
                            result = String.format("%0128x", new BigInteger(1, hashFunction.digest()));
                            break;
                        case "PBKDF2":
                            break;
                        case "BCrypt":
                            break;
                        case "SCrypt":
                            break;
                        default:
                            System.out.println("Method not recognized");
                    }
                    boolean found = false;
                    for (String s:workerObserverRI.getHashPass()) {
                        if(workerObserverRI.match(s,result)){
                            found = true;
                        }
                    }
                    State state = new State("");
                    //System.out.println(st);
                    if(found){
                        state.setmsg(state.FOUND);
                        this.workerObserverRI.updateFound(state,result,st, line);
                    }else{
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
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
