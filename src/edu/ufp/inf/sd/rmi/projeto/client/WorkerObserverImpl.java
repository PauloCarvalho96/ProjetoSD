package edu.ufp.inf.sd.rmi.projeto.client;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import edu.ufp.inf.sd.rmi.projeto.server.State;
import edu.ufp.inf.sd.rmi.projeto.server.Task;
import edu.ufp.inf.sd.rmi.projeto.server.TaskSubjectRI;
import edu.ufp.inf.sd.rmi.util.RabbitUtils;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
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
        getTask(taskgroup);
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
                    doWork(message);
                } catch (InterruptedException e) {
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
    private void doWork(String task) throws InterruptedException {

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
