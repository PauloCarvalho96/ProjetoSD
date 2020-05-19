package edu.ufp.inf.sd.rmi.projeto.client;

import edu.ufp.inf.sd.rmi.projeto.server.State;
import edu.ufp.inf.sd.rmi.projeto.server.Task;
import edu.ufp.inf.sd.rmi.projeto.server.TaskSubjectRI;

import javax.print.DocFlavor;
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

    protected WorkerObserverImpl(int id, String username,Task task,Integer n_threads) throws RemoteException {
        super();
        this.id = id;
        this.username = username;
        this.task = task;
        this.taskName = task.getTaskSubjectRI().getName();
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

        for(int i = 0; i < thread_size ; i++ , start+=delta){
            if(i==thread_size-1 && res!=0){
                delta+=res;
            }
            threads.add(new Thread(new myThread(start-1,delta,i,task.getTaskSubjectRI().getHashType(),this,task)));
            threads.get(i).start();
        }
    }

    public static class myThread implements Runnable {
        int start;
        int delta;
        int id;
        String hashType;
        WorkerObserverRI workerObserverRI;
        Task task;

        public myThread(int start, int delta, int id,String hashType, WorkerObserverRI workerObserverRI,Task task) {
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
                File file = new File("file.txt");

                int line = 0;

                BufferedReader br = new BufferedReader(new FileReader(file));
                String st;
                String result = null;

                MessageDigest hashFunction;

                while ((st = br.readLine()) != null) {
                    if (line >= start && line < start + delta) {
                        switch (hashType) {
                            case "SHA-512":
                                hashFunction = MessageDigest.getInstance("SHA-512");
                                hashFunction.reset();
                                hashFunction.update(st.getBytes("utf8"));
                                result = String.format("%0128x", new BigInteger(1, hashFunction.digest()));
                                System.out.println("Thread "+id+" Password: "+st);
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
                        for (String s:workerObserverRI.getHashPass()) {
                            if(workerObserverRI.match(s,result)){
                                System.out.println("\n\nThread:"+id+"Password Encontrada!");
                                System.out.println("hashPass:"+s);
                                System.out.println("Password:"+st);
                                State state = new State();
                                state.setmsg(state.FOUND);
                                this.workerObserverRI.update(state,s,st);
                            }
                            System.out.println(s);
                        }

                    }
                    if (line == start + delta) {
                        break;
                    }
                    line++;
                }
                br.close();
            } catch (IOException|NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(State state, String hashPass,String pass) throws RemoteException {
        this.lastObserverState = state;
        this.task.getTaskSubjectRI().changeWorkerState(state,hashPass,pass);
        taskUpdated();
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
    public Integer getId() throws RemoteException {
        return this.id;
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
    public void taskUpdated() throws RemoteException {
        //switch (this.task.getTaskSubjectRI().getState().getmsg()) {
          //  case "Completed":
                System.out.println("\nThread GoodBye\n");
                for (Thread t:this.threads) {
                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        //}
    }

}
