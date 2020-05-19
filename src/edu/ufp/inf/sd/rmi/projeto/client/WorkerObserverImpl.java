package edu.ufp.inf.sd.rmi.projeto.client;

import com.google.common.hash.HashFunction;
import edu.ufp.inf.sd.rmi.projeto.server.State;
import edu.ufp.inf.sd.rmi.projeto.server.Task;
import edu.ufp.inf.sd.rmi.projeto.server.TaskSubjectRI;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.DigestException;
import java.util.ArrayList;
import com.google.common.hash.*;
import java.util.concurrent.TimeoutException;

public class WorkerObserverImpl extends UnicastRemoteObject implements WorkerObserverRI {

    private State lastObserverState;
    private String username;    // username do user
    private Integer n_threads;      // nº de threads para trabalharem na task
    private Task task;      //tarefa

    protected WorkerObserverImpl(String username,Task task,Integer n_threads) throws RemoteException {
        super();
        this.username = username;
        this.task = task;
        this.n_threads = n_threads;
        doWork();
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
    public void doWork() throws RemoteException {
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

        int thread_size=n_threads;

        int start = task.getStart();

        int delta = task.getDelta();

        int res = delta % thread_size;

        delta = delta / thread_size;

        ArrayList<Thread> threads = new ArrayList<>();

        for(int i = 0; i < thread_size ; i++ , start+=delta){
            if(i==thread_size-1 && res!=0){
                delta+=res;
            }
            threads.add(new Thread(new myThread(start-1,delta,i,task.getTaskSubjectRI().getHashType(),task.getTaskSubjectRI().getHashPass(),this)));
        }

        for (Thread t:threads) {
            t.start();
        }
    }

    public static class myThread implements Runnable {
        int start;
        int delta;
        int id;
        String hashType;
        ArrayList<String> hasPass;
        WorkerObserverRI workerObserverRI;

        public myThread(int start, int delta, int id,String hashType, ArrayList<String> hasPass, WorkerObserverRI workerObserverRI) {
            this.start = start;
            this.delta = delta;
            this.id = id;
            this.hashType = hashType;
            this.hasPass = hasPass;
            this.workerObserverRI = workerObserverRI;
        }

        @Override
        public void run() {
            try {
                File file = new File("file.txt");

                int line = 0;

                BufferedReader br = new BufferedReader(new FileReader(file));
                String st;
                String result = null;

                while ((st = br.readLine()) != null) {
                    if (line >= start && line < start + delta) {
                        System.out.println("Thread" + id + ":" + st); //thread work
                        switch (hashType) {
                            case "SHA-512":
                                HashFunction hashFunction = Hashing.sha256();
                                result = hashFunction.hashString(st, Charset.defaultCharset()).toString();
                                //mandar receivedpass
                                //receber no newHash
                                break;
                            case "PBKDF2":
                                //mandar receivedpass
                                //receber no newHash
                                break;
                            case "BCrypt":
                                //mandar receivedpass
                                //receber no newHash
                                break;
                            case "SCrypt":
                                //mandar receivedpass
                                // receber no newHash
                                break;
                            default:
                                System.out.println("Method not recognized");
                        }
                        for (String s:hasPass) {
                            if(workerObserverRI.match(s.toLowerCase(),result)){
                                System.out.println("top chucha");
                            }
                        }

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
        this.lastObserverState = task.getTaskSubjectRI().getState();
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
       return this.task.getTaskSubjectRI().getHashType();
    }

    @Override
    public ArrayList<String> getHashPass() throws RemoteException {
        return this.task.getTaskSubjectRI().getHashPass();
    }
}
