// import java.util.LinkedList;
// import java.util.Queue;

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
        size--;

        head = (head + 1) % queue.length;

        notifyAll();
        return task;
    }
}
