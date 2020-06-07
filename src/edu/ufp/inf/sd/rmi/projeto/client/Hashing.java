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

public class Hashing implements Runnable {

    int start;
    int delta;
    int id;
    String hashType;
    WorkerObserverImpl workerObserver;
    Task task;
    String file_name;

    public Hashing(int start, int delta, int id, String hashType, WorkerObserverImpl workerObserver, Task task,String file_name) {
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
            int n_check_words = 0;

            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            String result = null;

            MessageDigest hashFunction;

            while ((st = br.readLine()) != null) {
                while (workerObserver.getStateWorker().getmsg().equals("Paused")){
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

                if (line >= start && line < start + delta && !workerObserver.getStateWorker().getmsg().equals("Paused")) {

                    n_check_words++;

                    if (task.getTaskSubjectRI().getStrategy() == 2 && task.lines.contains(line + 1) || task.getTaskSubjectRI().getStrategy() != 2) {
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
                    for (String s : workerObserver.getHashPass()) {
                        if (workerObserver.match(s, result)) {
                            found = true;
                        }
                    }
                    State state = new State("");
                    if (found) {
                        state.setmsg(state.FOUND);
                        Client client = workerObserver.getClient();
                        int userCredits = client.userSessionRI.getUserCreditsDB(client.username);
                        /** atualiza creditos do client */
                        client.userSessionRI.setUserCreditsDB(client.username,userCredits+10);
                        this.workerObserver.updateFound(state, result, st, line);
                    } else if (line % (delta * 0.1) == 0) {
                        Client client = workerObserver.getClient();
                        int userCredits = client.userSessionRI.getUserCreditsDB(client.username);
                        client.userSessionRI.setUserCreditsDB(client.username,userCredits+n_check_words);
                        state.setmsg(state.NOT_FOUND);
                        state.setN_credits(n_check_words);
                        this.workerObserver.updateNotFound(state, line);
                        n_check_words = 0;
                    }
                }
                }
                if (line == start + delta) {
                    State state = new State("");
                    Client client = workerObserver.getClient();
                    int userCredits = client.userSessionRI.getUserCreditsDB(client.username);
                    client.userSessionRI.setUserCreditsDB(client.username,userCredits+n_check_words);
                    state.setN_credits(n_check_words);
                    state.setmsg(state.NOT_FOUND);
                    this.workerObserver.updateNotFound(state, line);
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
