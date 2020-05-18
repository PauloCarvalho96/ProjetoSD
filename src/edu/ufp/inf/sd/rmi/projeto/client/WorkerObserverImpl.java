package edu.ufp.inf.sd.rmi.projeto.client;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import edu.ufp.inf.sd.rmi.projeto.server.State;
import edu.ufp.inf.sd.rmi.projeto.server.Task;
import edu.ufp.inf.sd.rmi.projeto.server.TaskSubjectRI;
import edu.ufp.inf.sd.rmi.util.RabbitUtils;

import java.io.*;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class WorkerObserverImpl extends UnicastRemoteObject implements WorkerObserverRI {

    private State lastObserverState;
    private TaskSubjectRI taskgroup;     // taskgroup em que está
    private String username;    // username do user
    private ArrayList<Thread> threads;      // array de threads para trabalharem na task
    private Task task;      //tarefa

    protected WorkerObserverImpl(String username,TaskSubjectRI taskSubjectRI,ArrayList<Thread> threads) throws RemoteException {
        super();
        this.username = username;
        this.taskgroup = taskSubjectRI;
        this.threads = threads;
    }

    /** Em testes */
    public void getTask(TaskSubjectRI taskSubjectRI) throws RemoteException{
        try {
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
    }

    /** thread vai fazer o trabalho */
    public void doWork(){
        try (BufferedInputStream in = new BufferedInputStream(new URL("https://raw.githubusercontent.com/danielmiessler/SecLists/master/Passwords/darkc0de.txt").openStream());
             FileOutputStream fileOutputStream = new FileOutputStream("file.txt")) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            System.out.println("Error");
        }

        int thread_size=3; //valor que vem do XML, a mudar

        int start = task.getStart();

        int delta = task.getDelta();

        int res = delta % thread_size;

        delta = delta / thread_size;

        System.out.println(delta);


        ArrayList<Thread> threads = new ArrayList<>();


        System.out.println(res);
        for(int i = 0; i < thread_size ; i++ , start+=delta){
            if(i==thread_size-1 && res!=0){
                delta+=res;
            }
            threads.add(new Thread(new myThread(start-1,delta,i)));
        }

        for (Thread t:threads) {
            t.start();
        }
    }


    public static class myThread implements Runnable {
        int start;
        int delta;
        int id;

        public myThread(int start, int delta, int id) {
            this.start = start;
            this.delta = delta;
            this.id = id;
        }

        @Override
        public void run() {
            try {
                File file = new File("file.txt");

                int line = 0;

                BufferedReader br = new BufferedReader(new FileReader(file));
                String st;

                while ((st = br.readLine()) != null) {
                    if (line >= start && line < start + delta) {
                        System.out.println("Thread" + id + ":" + st); //thread work
                    }
                    if (line == start + delta) {
                        break;
                    }
                    line++;
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(State stateTask) throws RemoteException {
        this.lastObserverState = taskgroup.getState();
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

    }

    @Override
    public State getStateWorker() throws RemoteException {
        return null;
    }

    @Override
    public String getHashType() throws RemoteException {
       return this.taskgroup.getHashType();
    }

    @Override
    public String getHashPass() throws RemoteException {
        return this.taskgroup.getHashPass();
    }
}
