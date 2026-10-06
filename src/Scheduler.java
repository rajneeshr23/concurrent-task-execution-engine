import java.util.List;
import java.util.ArrayList;

class Scheduler implements TaskCompletionListener{
    private TaskGraph graph;
    private final BlockingTaskQueue queue;
    private final List<Thread> workers; 
    private boolean failureDetected = false;
    private volatile boolean shutdown = false;
    
    private final Object lock = new Object();

    public Scheduler(TaskGraph graph, int workerCount){
        this.graph = graph;
        this.queue = new BlockingTaskQueue(graph.getAllTasks().size())  ; 
        this.workers = new ArrayList<>();

        for(int i = 1; i <= workerCount; i++){
            Thread worker = new Thread(new Worker(queue, "Worker-"+i, this, this), "Worker-"+i);
            workers.add(worker);
        }
    }

    @Override 
    public void taskCompleted(Task task){
        List<Task> readyTasks = new ArrayList<>();

        synchronized(lock) {
            if(failureDetected){
                return;
            }
            for(Task child : task.getChildren()){
                if(child.dependencyCompleted()){
                    readyTasks.add(child);
                }
            }
        }

        for(Task child : readyTasks){
            try{
                queue.put(child);
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    @Override 
    public void taskFailed(Task task){
        synchronized (lock) {
            if(failureDetected){
                return;
            }

            failureDetected = true;
            graph.cancelPendingTasks();
            List<Task> queuedTasks = queue.drain();

            for(Task queuedTask : queuedTasks){
                queuedTask.setState(TaskState.CANCELLED);

                System.out.println("Cancelled "+queuedTask.getId());
            }

            System.out.println("Scheduler detected failure of "+task.getId());

            for(Thread worker : workers){
                worker.interrupt();
            }
        }
    }

    public void run(){

        System.out.println("Checking graph for cycles...");
        if(graph.hasCycle()){
            return;
        }

        System.out.println("Cycle check completed.");   
        for(Thread worker : workers){
            worker.start();
        }

        System.out.println("Workers started.");
        List<Task> readyTasks = graph.getReadyTasks();


        for(Task readyTask : readyTasks){
            try{
                queue.put(readyTask);
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
                return;
            }
        }

        waitForCompletion();
        graph.printIncompleteTasks();
        shutdownWorkers();
        System.out.println("Successful tasks: " + graph.countSuccessfulTasks() + "/" + graph.getAllTasks().size());
    }

    private void waitForCompletion(){
        while(true){
            synchronized (lock) {
                if(graph.allTasksCompleted() || failureDetected){
                    return;
                }
            }

            try{
                Thread.sleep(500);
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void shutdownWorkers(){
        System.out.println("Shutting down workers...");
        shutdown = true;

        for(Thread worker : workers) {
            worker.interrupt();
        }

        for(Thread worker : workers){
            try{
                worker.join();
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
                return;
            }
        }
        System.out.println("Scheduler stopped");
    }

    public boolean isShutdown() {
        return shutdown;
    }
}
