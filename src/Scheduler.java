import java.util.List;
import java.util.ArrayList;

class Scheduler implements TaskCompletionListener{
    private TaskGraph graph;
    private final BlockingTaskQueue queue;
    private final List<Thread> workers; 
    
    private final Object lock = new Object();

    public Scheduler(TaskGraph graph, int workerCount){
        this.graph = graph;
        this.queue = new BlockingTaskQueue(100);
        this.workers = new ArrayList<>();

        for(int i = 1; i <= workerCount; i++){
            Thread worker = new Thread(new Worker(queue, "Worker-"+i, this), "Worker-"+i);
            workers.add(worker);
        }
    }

    @Override 
    public void taskCompleted(Task task){

        synchronized (lock){
            for(Task child : task.getChildren()){
                if(child.dependencyCompleted()){
                    child.setState(TaskState.READY);
                    try{
                        queue.put(child);
                    }
                    catch (InterruptedException e){
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }

            // List<Task> readyTasks = graph.getReadyTasks();

            // for(Task readyTask : readyTasks){
            //     try{
            //         queue.put(readyTask);
            //     } catch (InterruptedException e) {
            //         Thread.currentThread().interrupt();
            //         return;
            //     }
            // }
        }
    }

    @Override 
    public void taskFailed(Task task){
        synchronized (lock) {
            System.out.println("Scheduler detected failure of "+task.getId());
        }
    }

    public void run(){

        for(Thread worker : workers){
            worker.start();
        }

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
        shutDownWorkers();
    }

    private void waitForCompletion(){
        while(true){
            synchronized (lock) {
                if(graph.allTasksCompleted()){
                    return;
                }
            }

            try{
                Thread.sleep(50);
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void shutDownWorkers(){
        System.out.println("Shutting down workers...");

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

    /*// public void executeTask(Task task){
    //     task.setState(TaskState.RUNNING);

    //     try{
    //         task.doWork();
    //         task.setState(TaskState.SUCCESS);

    //         for(Task child : task.getChildren()){
    //             child.dependencyCompleted();
    //         }
    //     } catch(RuntimeException e) {
    //         task.setState(TaskState.FAILED);
    //         System.out.println("Task "+task.getId()+" failed.");
    //     }
    // }*/
}
