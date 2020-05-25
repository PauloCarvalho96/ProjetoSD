package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverRI;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class TaskSubjectImpl extends UnicastRemoteObject implements TaskSubjectRI {

    private String name;
    private String hashType;
    private ArrayList<String> hashPass;
    private Integer creditsPerWord;
    private Integer creditsTotal;
    private State subjectState = new State();
    private String status;
    private boolean available = true;
    private Integer start = 0;     //linha atual
    private Integer delta;     //quantidade de linhas
    private static final String url = "https://raw.githubusercontent.com/danielmiessler/SecLists/master/Passwords/darkc0de.txt";
    private ArrayList<WorkerObserverRI> workers = new ArrayList<>();// array de workers
    private ArrayList<Task> tasks = new ArrayList<>();// array tasks
    private ArrayList<Result> result = new ArrayList<>();//array pass found

    private String path_paulo = "C:\\Users\\Paulo\\Documents\\GitHub" +
            "\\ProjetoSD\\src\\edu\\ufp\\inf\\sd\\rmi\\projeto\\server\\passwords_to_verify.txt";
    private String path_other = "C:\\Users\\tmsl9\\GitHub" +
            "\\ProjetoSD\\src\\edu\\ufp\\inf\\sd\\rmi\\projeto\\server\\passwords_to_verify.txt";
    public TaskSubjectImpl(String name, String hashType, ArrayList<String> hashPass,Integer delta) throws RemoteException {
        super();
        this.name = name;
        this.hashType = hashType;
        this.hashPass = hashPass;
        this.delta = delta;
        this.subjectState.setmsg("Available");
        this.status = this.subjectState.AVAILABLE;
        createSubTasks();
    }

    /*public TaskSubjectImpl(String name, String hashType, String hashPass, String creditsPerWord, String creditsTotal) throws RemoteException {
        super();
        this.name = name;
        this.hashType = hashType;
        this.hashPass = hashPass;
        this.creditsPerWord = creditsPerWord;
        this.creditsTotal = creditsTotal;
    }*/

    /** Em testes -> para adicionar tasks na fila */
    public void sendToQueue() throws RemoteException{
        /*try {
            Connection connection = RabbitUtils.newConnection2Server("localhost", "guest", "guest");
            Channel channel=RabbitUtils.createChannel2Server(connection);
            boolean durable=true;
            channel.queueDeclare(name, durable, false, false, null);

            String message="url / start / delta";

            channel.basicPublish("", name, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");

        } catch (IOException|TimeoutException e) {
            e.printStackTrace();
        }*/
    }

    /** divide linhas para criar sub tasks */
    public void createSubTasks(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path_other));
            int lines = 0;
            while (reader.readLine() != null) {
                if(lines == start + delta - 1){
                    Task task = new Task(url,start,delta,this);
                    tasks.add(task);
                    start = lines + 1;
                }
                lines++;
            }
            int lastDelta = delta;
            lastDelta = lines - start;
            if(lastDelta != 0){
                Task task = new Task(url,start,lastDelta,this);
                tasks.add(task);
                reader.close();
            }

            for (Task task:tasks) {
                System.out.println("\nStart: "+task.getStart()+"\nDelta: "+task.getDelta()+"\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeWorkerState(State state, String hash, String pass) throws RemoteException {
        switch (state.getmsg()){
            case "Found":
                for (int i = 0; i < this.hashPass.size() ; i ++){
                    if(this.hashPass.get(i).compareTo(hash)==0){
                        this.result.add(new Result(hash,pass));
                        this.hashPass.remove(i);
                        break;
                    }
                }
                System.out.println("FOUND");
                if(this.hashPass.isEmpty()){
                    System.out.println("COMPLETE");
                    this.subjectState.setmsg("Completed");
                    this.status = this.subjectState.COMPLETED;
                    available = false;
                }else{
                    System.out.println("NOT COMPLETE");
                    this.subjectState.setmsg("Working");
                    this.status = this.subjectState.WORKING;
                }
                break;
                /** Se nao encontrar nenhuma palavra no range */
            case "Not Found":
                if(!this.subjectState.getmsg().equals("Completed") && !this.subjectState.getmsg().equals("Paused")) {
                    this.subjectState.setmsg("Working");
                    this.status = this.subjectState.WORKING;
                }
                break;
            case "Paused":
                System.out.println("PAUSED!");
                break;
        }
        this.notifyAllObservers();
    }

    @Override
    public void notifyAllObservers() throws RemoteException {
        for (WorkerObserverRI obs: workers) {
            try {
                obs.taskUpdated();
            } catch (RemoteException | InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void attach(WorkerObserverRI obsRI) throws RemoteException {
        if(!this.workers.contains(obsRI)){
            this.workers.add(obsRI);
            obsRI.setTask(getTaskFromArray());
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
    public void setState(State state){
        this.subjectState = state;
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
    public ArrayList<String> getHashPass() {
        return hashPass;
    }

    @Override
    public boolean isAvailable() throws RemoteException {
        return available;
    }

    @Override
    public Task getTaskFromArray() throws RemoteException {
        Task task = this.tasks.get(0);
        if(task != null && this.tasks.size() == 1){
            this.tasks.remove(0);
            this.available = false;
            return task;
        }
        if(task != null) {
            this.tasks.remove(0);
            return task;
        }
        return null;
    }

    @Override
    public ArrayList<Result> getResult() throws RemoteException {
        return result;
    }

    @Override
    public void pause() throws RemoteException {
        System.out.println("---->"+
                this.subjectState.getmsg());
        if(!this.subjectState.getmsg().equals("Completed") || !this.subjectState.getmsg().equals("Paused")) {
            this.subjectState.setmsg("Paused");
            this.status = this.subjectState.PAUSED;
            this.notifyAllObservers();
        }
    }

    @Override
    public void resume() throws RemoteException{
        if(this.subjectState.getmsg().equals("Paused")) {
            this.subjectState.setmsg("Working");
            this.status = this.subjectState.WORKING;
            this.notifyAllObservers();
        }
    }


    @Override
    public void stop() throws RemoteException {
        this.hashPass.clear();
        this.subjectState.setmsg("Completed");
        this.status = this.subjectState.COMPLETED;
        this.notifyAllObservers();
        this.available = false;
    }
}
