import java.util.ArrayList;
import java.util.List;

public class BlockingTaskQueue {
    private Task[] queue;

    private int head;
    private int tail;
    private int size;

    public BlockingTaskQueue(int capacity){
        queue = new Task[capacity];
        head = 0;
        tail = 0;
        size = 0;
    }

    public synchronized void put(Task task) throws InterruptedException {
        while(size == queue.length){
            System.out.println("QUEUE FULL - waiting...");
            wait();
        }
        queue[tail] = task;
        tail = (tail + 1) % queue.length;
        size++;
        notifyAll();
    }

    public synchronized Task take() throws InterruptedException {
        while(size == 0){
            wait();
        }

        Task task = queue[head];
        queue[head] = null;
        size--;

        head = (head + 1) % queue.length;

        notifyAll();
        return task;
    }

    public synchronized List<Task> drain(){
        List<Task> remainingTask = new ArrayList<>();

        while(size > 0){
            Task task = queue[head];
            queue[head] = null;
            head = (head + 1) % queue.length;

            remainingTask.add(task);
            size--;
        }
        notifyAll();
        return remainingTask;
    }
}
