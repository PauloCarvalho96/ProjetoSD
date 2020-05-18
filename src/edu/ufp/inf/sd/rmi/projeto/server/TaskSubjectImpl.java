package edu.ufp.inf.sd.rmi.projeto.server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverRI;
import edu.ufp.inf.sd.rmi.util.RabbitUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class TaskSubjectImpl extends UnicastRemoteObject implements TaskSubjectRI {

    private String name;
    private String hashType;
    private String hashPass;
    private String creditsPerWord;
    private String creditsTotal;
    private State subjectState;
    private boolean available;
    private Integer start = 0;     //linha atual
    private Integer delta = 500000;     //quantidade de linhas
    // array de workers
    private ArrayList<WorkerObserverRI> workers = new ArrayList<>();
    // array tasks
    private ArrayList<Task> tasks = new ArrayList<>();

    public TaskSubjectImpl(String name, String hashType, String hashPass) throws RemoteException {
        super();
        this.name = name;
        this.hashType = hashType;
        this.hashPass = hashPass;
        createTasks();
    }

    /*public TaskSubjectImpl(String name, String hashType, String hashPass, String creditsPerWord, String creditsTotal) throws RemoteException {
        super();
        this.name = name;
        this.hashType = hashType;
        this.hashPass = hashPass;
        this.creditsPerWord = creditsPerWord;
        this.creditsTotal = creditsTotal;
    }*/

    /** Em testes */
    public void createTasks() throws RemoteException{

        try {
            Connection connection = RabbitUtils.newConnection2Server("localhost", "guest", "guest");
            Channel channel=RabbitUtils.createChannel2Server(connection);
            boolean durable=true;
            channel.queueDeclare(name, durable, false, false, null);

            String message="Test";

            channel.basicPublish("", name, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");

        } catch (IOException|TimeoutException e) {
            e.printStackTrace();
        }

    }


    public void notifyAllObservers(){
        /*for (WorkerObserverRI obs: workers) {
            try {
//                obs.update();
            } catch (RemoteException ex) {
                Logger.getLogger(TaskSubjectImpl.class.getName()).log(Level.SEVERE,null,ex);
            }
        }*/
    }

    @Override
    public void attach(WorkerObserverRI obsRI) throws RemoteException {
        if(!this.workers.contains(obsRI)){
            this.workers.add(obsRI);
        }
    }

    @Override
    public void detach(WorkerObserverRI obsRI) throws RemoteException {
        this.workers.remove(obsRI);
    }

    @Override
    public State getState() throws RemoteException {
        return this.subjectState;
    }

    @Override
    public void setState(State state) throws RemoteException {
        this.subjectState = state;
        this.notifyAllObservers();
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public String getHashType() {
        return hashType;
    }

    @Override
    public String getHashPass() {
        return hashPass;
    }

}
