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

                System.out.println(name+" shutting down");
                break;
            }

            if(task == null) {
                continue;
            }            

            task.setState(TaskState.RUNNING);

            System.out.println(name+" picked up "+task.getId());
            
            try{
                task.doWork();
                if(Thread.currentThread().isInterrupted()){
                    task.setState(TaskState.CANCELLED);
                    System.out.println(name+" cancelled "+task.getId());
                    continue;
                }
                task.setState(TaskState.SUCCESS);

                System.out.println(name+" completed "+task.getId());
                
                listener.taskCompleted(task);
            }catch(RuntimeException e){
                task.setState(TaskState.FAILED);

                System.out.println(name+" failed "+task.getId());
                
                listener.taskFailed(task);
            }
        }

        System.out.println(name+" shutting down");
    }
    
}
