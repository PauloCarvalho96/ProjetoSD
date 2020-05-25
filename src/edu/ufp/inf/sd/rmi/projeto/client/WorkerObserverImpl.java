package edu.ufp.inf.sd.rmi.projeto.client;

import edu.ufp.inf.sd.rmi.projeto.server.State;
import edu.ufp.inf.sd.rmi.projeto.server.Task;
import edu.ufp.inf.sd.rmi.projeto.server.TaskSubjectRI;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    public WorkerObserverImpl(int id, String username, Integer n_threads) throws RemoteException {
        super();
        this.id = id;
        this.username = username;
        this.n_threads = n_threads;
        this.actualLine = 0;
        this.lastObserverState = new State("Available");
    }

    /** Em testes
    public void getTask(TaskSubjectRI taskSubjectRI) throws RemoteException{
        /*try {
            Connection connection = RabbitUtils.newConnection2Server("localhost","guest", "guest");
            Channel channel=RabbitUtils.createChannel2Server(connection);
            boolean durable = true;
            channel.queueDeclare(taskSubjectRI.getName(), durable, false, false, null);
            int prefetchCount = 1;
            channel.basicQos(prefetchCount);

            DeliverCallback deliverCallback=(consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
                try {
                    doWork();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    System.out.println(" [x] Done processing task");
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            };
            boolean autoAck = false;
            channel.basicConsume(taskSubjectRI.getName(), autoAck, deliverCallback, consumerTag -> { });

        } catch (IOException|TimeoutException e) {
            e.printStackTrace();
        }
     }*/

    /** threads vao fazer o trabalho */
    private void doWork() throws RemoteException {
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
            threads.add(new Thread(new MyThread(start-1,delta,i,task.getTaskSubjectRI().getHashType(),this,task)));
            threads.get(i).start();
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
        this.task = task;
        this.taskName = task.getTaskSubjectRI().getName();
        this.wordsSize = task.getDelta();
        doWork();
    }

    @Override
    public void taskUpdated() throws RemoteException, InterruptedException {
        switch (this.task.getTaskSubjectRI().getState().getmsg()) {
            case "Completed":
                this.lastObserverState.setmsg(this.task.getTaskSubjectRI().getState().getmsg());
                System.out.println("\nWorker all completed!!\n");
                break;
            case "Working":
                this.lastObserverState.setmsg(this.task.getTaskSubjectRI().getState().getmsg());
                System.out.println("\nStill working!!\n");
                break;
            case "Paused":
                this.lastObserverState.setmsg(this.task.getTaskSubjectRI().getState().getmsg());
                System.out.println("\nPaused!!\n");
                break;
        }
    }

}
