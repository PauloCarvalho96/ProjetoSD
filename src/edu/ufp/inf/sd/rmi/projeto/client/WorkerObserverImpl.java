package edu.ufp.inf.sd.rmi.projeto.client;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import edu.ufp.inf.sd.rmi.projeto.server.*;
import edu.ufp.inf.sd.rmi.util.RabbitUtils;

import java.io.*;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;


public class WorkerObserverImpl extends UnicastRemoteObject implements WorkerObserverRI {

    private State lastObserverState;
    private int id;
    private Client client;    // dono do worker
    private Integer n_threads;      // nº de threads para trabalharem na task
    private String taskName;
    private Task task;      //tarefa
    private int wordsSize;
    private ArrayList<Thread> threads = new ArrayList<>();
    private int actualLine;
    private Integer n_threads_dividing;
    private String file_name;

    public WorkerObserverImpl(int id, Client client, Integer n_threads) throws RemoteException {
        super();
        this.id = id;
        this.client = client;
        this.n_threads = n_threads;
        this.actualLine = 0;
        this.lastObserverState = new State("Available");
        this.file_name="file_"+client.username+"_"+id+".txt";
        n_threads_dividing = this.n_threads;
    }

    private void doWorkDividing() throws RemoteException {
        try (BufferedInputStream in = new BufferedInputStream(new URL("https://raw.githubusercontent.com/danielmiessler/SecLists/master/Passwords/darkc0de.txt").openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(file_name)) {
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
            threads.add(new Thread(new Dividing(start-1,delta,i,task.getTaskSubjectRI().getHashType(),this,task,file_name)));
            threads.get(i).start();
        }
    }

    public void doWork() throws RemoteException {
        int delta = 0;
        int start = 0;
        if (task.getTaskSubjectRI().getStrategy() != 3){
            try (BufferedInputStream in = new BufferedInputStream(new URL(task.getUrl()).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(file_name)) {
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
            createFileTask();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file_name));
                while (reader.readLine() != null){
                    delta++;
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            task.setStart(0);
        }

        int thread_size=n_threads;

        int res = delta % thread_size;

        delta = delta / thread_size;

        for(int i = 0; i < thread_size ; i++ , start+=delta){
            if(i==thread_size-1 && res!=0){
                delta+=res;
            }
            threads.add(new Thread(new Hashing(start-1,delta,i,task.getTaskSubjectRI().getHashType(),this,task,file_name)));
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
        if(task.getTaskSubjectRI().getStrategy() == 2 && task.getTaskSubjectRI().getState().getProcess().compareTo("Dividing")==0){
            this.task = task;
            this.taskName = task.getTaskSubjectRI().getName();
            doWorkDividing();
        } else {
            this.task = task;
            this.taskName = task.getTaskSubjectRI().getName();
            this.wordsSize = task.getDelta();

            /** RabbitMQ */
            try {
                connectToQueue(taskName);
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }

            doWork();
        }
    }
    
    @Override
    public void createFileTask() throws RemoteException {
        try {
            File file = new File(file_name);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
            FileWriter myWriter = new FileWriter(file_name);
            generateFileTask(myWriter);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    @Override
    public void generateFileTask(FileWriter myWriter) throws RemoteException {
        HashMap<Integer, Character> ha = new HashMap<>();

        char[] alphabet_aux = this.task.getAlphabet().toCharArray();
        for(int i = 0; i < task.getAlphabet().length() ; i++){
            ha.put(i, alphabet_aux[i]);
        }

        for (int i = task.getStart(); i <= task.getStart()+task.getDelta(); i++){
            StringBuilder ch = new StringBuilder(Integer.toString(i, task.getAlphabet().length()));
            while(ch.length() != task.wordsSize.get(0)){
                ch.insert(0, "0");
            }
            StringBuilder pass = new StringBuilder();
            for(char s: ch.toString().toCharArray()){
                if(Character.isLetter(s)){
                    pass.append(ha.get(Integer.parseInt(Integer.toString(Integer.parseInt(String.valueOf(s), task.getAlphabet().length()),10))));
                }else{
                    pass.append(ha.get(Integer.parseInt(String.valueOf(s))));
                }
            }
            try {
                myWriter.write(pass+"\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            case "Incompleted":
                if(!this.lastObserverState.getmsg().equals("Incompleted")) {
                    this.lastObserverState.setmsg(this.task.getTaskSubjectRI().getState().getmsg());
                    System.out.println("\nWorker all incompleted!!\n");
                }
                break;
        }
    }

    @Override
    public Integer getN_threads_dividing() throws RemoteException {
        return n_threads_dividing;
    }

    @Override
    public void setN_threads_dividing(Integer n_threads_dividing) throws RemoteException {
        this.n_threads_dividing = n_threads_dividing;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public Integer getN_threads() throws RemoteException {
        return n_threads;
    }

    /** RabbitMQ */

    public Task connectToQueue(String taskName) throws IOException, TimeoutException {
        Connection connection = RabbitUtils.newConnection2Server("localhost","guest", "guest");
        Channel channel=RabbitUtils.createChannel2Server(connection);

        boolean durable = true;
        channel.queueDeclare(taskName, durable, false, false, null);
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);

        DeliverCallback deliverCallback=(consumerTag, delivery) -> {
            try (ByteArrayInputStream bis = new ByteArrayInputStream(delivery.getBody());
                 ObjectInput in = new ObjectInputStream(bis)) {
                try {
                    Task task = (Task) in.readObject();
                    System.out.println("\nSTART "+task.getStart()+"\nDELTA "+task.getDelta());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };

        boolean autoAck = false;
        channel.basicConsume(taskName, autoAck, deliverCallback, consumerTag -> { });

        return task;
    }

}
