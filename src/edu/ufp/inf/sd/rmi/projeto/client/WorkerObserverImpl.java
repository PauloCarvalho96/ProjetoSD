package edu.ufp.inf.sd.rmi.projeto.client;

import edu.ufp.inf.sd.rmi.projeto.server.*;

import java.io.*;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


public class WorkerObserverImpl extends UnicastRemoteObject implements WorkerObserverRI {

    private State lastObserverState;
    private int id;
    private String username;    // username do user
    private Integer n_threads;      // nº de threads para trabalharem na task
    private String taskName;
    private Task task;      //tarefa
    private int wordsSize;
    private int creditsWon;
    private ArrayList<Thread> threads = new ArrayList<>();
    private int actualLine;
    ArrayList<Integer> linesWithWordLength = new ArrayList<>();

    public WorkerObserverImpl(int id, String username, Integer n_threads) throws RemoteException {
        super();
        this.id = id;
        this.username = username;
        this.n_threads = n_threads;
        this.actualLine = 0;
        this.lastObserverState = new State("Available");
    }

    private void doWorkDividing() throws RemoteException {
        try (BufferedInputStream in = new BufferedInputStream(new URL("https://raw.githubusercontent.com/danielmiessler/SecLists/master/Passwords/darkc0de.txt").openStream());
             FileOutputStream fileOutputStream = new FileOutputStream("file"+id+".txt")) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            System.out.println("Error");
        }

        int thread_size=n_threads;

        int start = task.getStart();

        int delta = task.getDelta();

        int res = delta % thread_size;

        delta = delta / thread_size;

        for(int i = 0; i < thread_size ; i++ , start+=delta){
            if(i==thread_size-1 && res!=0){
                delta+=res;
            }
            threads.add(new Thread(new Dividing(start-1,delta,i,task.getTaskSubjectRI().getHashType(),this,task)));
            System.out.println("SIZE:"+threads.size());
            threads.get(i).start();
            System.out.println(threads.get(i).getId());
        }
    }

    private void doWork() throws RemoteException {
        int delta = 0;
        int start = 0;
        if (task.getTaskSubjectRI().getStrategy() != 3){
            try (BufferedInputStream in = new BufferedInputStream(new URL("https://raw.githubusercontent.com/danielmiessler/SecLists/master/Passwords/darkc0de.txt").openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream("file"+id+".txt")) {
                byte dataBuffer[] = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
            } catch (IOException e) {
                System.out.println("Error");
            }

            start = task.getStart();

            delta = task.getDelta();
        }else{
            System.out.println("\n\n\nOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n\n\n");
            try {
                BufferedReader reader = new BufferedReader(new FileReader("file"+id+".txt"));
                while (reader.readLine() != null) delta++;
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int thread_size=n_threads;

        int res = delta % thread_size;

        delta = delta / thread_size;

        for(int i = 0; i < thread_size ; i++ , start+=delta){
            if(i==thread_size-1 && res!=0){
                delta+=res;
            }
            threads.add(new Thread(new Hashing(start-1,delta,i,task.getTaskSubjectRI().getHashType(),this,task)));
            System.out.println("SIZE:"+threads.size());
            threads.get(i).start();
            System.out.println(threads.get(i).getId());
        }
    }

    @Override
    public void updateFound(State state, String hashPass, String pass, int line) throws RemoteException {
        this.actualLine = line;
        this.lastObserverState = state;
        this.task.getTaskSubjectRI().changeWorkerState(state, hashPass, pass);
    }

    @Override
    public void updateNotFound(State state, int line) throws RemoteException {
        this.lastObserverState = state;
        this.task.getTaskSubjectRI().changeWorkerState(state, "", "");
    }

    @Override
    public boolean match(String str1, String str2) throws RemoteException {
        return str1.compareTo(str2) == 0;
    }

    @Override
    public String getHash(String str) throws RemoteException {
        return null;
    }

    @Override
    public void selectTask(TaskSubjectRI task) throws RemoteException {
        Thread thread = new Thread();
    }

    @Override
    public void removeWorker() throws RemoteException {

    }

    @Override
    public void setStateWorker(State state) throws RemoteException {
        lastObserverState = state;
    }

    @Override
    public State getStateWorker() throws RemoteException {
        return lastObserverState;
    }

    @Override
    public Integer getId() throws RemoteException {
        return this.id;
    }

    @Override
    public String getTaskName() throws RemoteException {
        return this.taskName;
    }

    @Override
    public String getHashType() throws RemoteException {
       return this.task.getTaskSubjectRI().getHashType();
    }

    @Override
    public ArrayList<String> getHashPass() throws RemoteException {
        return this.task.getTaskSubjectRI().getHashPass();
    }

    @Override
    public int getActualLine() throws RemoteException {
        return this.actualLine;
    }

    @Override
    public void setTask(Task task) throws RemoteException {
        if(task.getTaskSubjectRI().getStrategy() == 3){
            createFileTask(task);
        }else{
            this.task = task;
            this.taskName = task.getTaskSubjectRI().getName();
            this.wordsSize = task.getDelta();
            doWork();
        }
    }

    @Override
    public void createFileTask(Task task) throws RemoteException {
        this.task = task;
        this.taskName = task.getTaskSubjectRI().getName();
        this.wordsSize = task.getWordsSize();
        try {
            File file = new File("file"+id+".txt");
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
            FileWriter myWriter = new FileWriter("file"+id+".txt");
            generateFileTask(myWriter,task.alphabet,"");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        doWork();
    }

    @Override
    public void generateFileTask(FileWriter file, String str, String ans) throws RemoteException {
        // If string is empty
        if (str.length() == 0) {
            try {
                file.write(ans+"\n");
            } catch (IOException e) {
                System.out.println("An error occurred while writing in file.");
                e.printStackTrace();
            }
            System.out.print("\n"+ans);
            return;
        }
        for (int i = 0; i < str.length(); i++) {

            // ith character of str
            char ch = str.charAt(i);

            // Rest of the string after excluding
            // the ith character
            String ros = str.substring(0, i) +
                    str.substring(i + 1);

            // Recurvise call
            generateFileTask(file,ros, ans + ch);
        }
    }

    @Override
    public void taskUpdated() throws RemoteException {
        String state = this.task.getTaskSubjectRI().getState().getmsg();
        switch (state) {
            case "Completed":
                if(!this.lastObserverState.getmsg().equals("Completed")) {
                    this.lastObserverState.setmsg(this.task.getTaskSubjectRI().getState().getmsg());
                    System.out.println("\nWorker all completed!!\n");
                }
                break;
            case "Working":
                if(!this.lastObserverState.getmsg().equals("Working")) {
                    this.lastObserverState.setmsg(this.task.getTaskSubjectRI().getState().getmsg());
                    System.out.println("\nStill working!!\n");
                }
                break;
            case "Paused":
                if(!this.lastObserverState.getmsg().equals("Paused")) {
                    this.lastObserverState.setmsg(this.task.getTaskSubjectRI().getState().getmsg());
                    System.out.println("\nPaused!!\n");
                }
                break;
        }
    }

    @Override
    public void setLinesWithWordLength(int line) throws RemoteException {
        this.linesWithWordLength.add(line);
    }
}
