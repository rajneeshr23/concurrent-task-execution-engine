class Worker implements Runnable {

    private final BlockingTaskQueue queue;
    private final String name;  
    private final TaskCompletionListener listener;
    private final Scheduler scheduler;

    public Worker(BlockingTaskQueue queue, String name, TaskCompletionListener listener, Scheduler scheduler){
        this.queue = queue;
        this.name = name;
        this.listener = listener;
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        while(!scheduler.isShutdown()){
            Task task;
            try{
                task = queue.take();
            } catch(InterruptedException e){
                Thread.currentThread().interrupt();
                break;
            }

            if(task == null) {
                continue;
            }            

            task.setState(TaskState.RUNNING);
            
            try{
                task.doWork();
                if(Thread.currentThread().isInterrupted()){
                    task.setState(TaskState.CANCELLED);
                    continue;
                }
                task.setState(TaskState.SUCCESS);
                
                listener.taskCompleted(task);
            }catch(RuntimeException e){
                task.setState(TaskState.FAILED);
                
                listener.taskFailed(task);
            }
        }
    }
    
}
