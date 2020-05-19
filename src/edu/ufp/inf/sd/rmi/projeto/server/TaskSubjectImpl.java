package edu.ufp.inf.sd.rmi.projeto.server;

import edu.ufp.inf.sd.rmi.projeto.client.WorkerObserverRI;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class TaskSubjectImpl extends UnicastRemoteObject implements TaskSubjectRI {

    private String name;
    private String hashType;
    private ArrayList<String> hashPass;
    private Integer creditsPerWord;
    private Integer creditsTotal;
    private State subjectState;
    private boolean available;
    private Integer start = 0;     //linha atual
    private Integer delta;     //quantidade de linhas
    private static final String url = "https://raw.githubusercontent.com/danielmiessler/SecLists/master/Passwords/darkc0de.txt";
    // array de workers
    private ArrayList<WorkerObserverRI> workers = new ArrayList<>();
    // array tasks
    private ArrayList<Task> tasks = new ArrayList<>();

    public TaskSubjectImpl(String name, String hashType, ArrayList<String> hashPass,Integer delta) throws RemoteException {
        super();
        this.name = name;
        this.hashType = hashType;
        this.hashPass = hashPass;
        this.delta = delta;
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
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\tmsl9\\OneDrive\\Documentos\\GitHub\\ProjetoSD\\src\\edu\\ufp\\inf\\sd\\rmi\\projeto\\server\\passwords_to_verify.txt"));
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
    public ArrayList<String> getHashPass() {
        return hashPass;
    }

    @Override
    public Task getTaskFromArray() throws RemoteException {
        if(this.tasks.get(0) != null){
            return this.tasks.get(0);
        }
        return null;
    }

}
