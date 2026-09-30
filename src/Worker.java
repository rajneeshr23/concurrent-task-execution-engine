class Worker implements Runnable {

    private final BlockingTaskQueue queue;
    private final String name;  

    public Worker(BlockingTaskQueue queue, String name){
        this.queue = queue;
        this.name = name;
    }

    @Override
    public void run() {
        while(true){
            try{
                Task task = queue.take();
                task.setState(TaskState.RUNNING);

                System.out.println(name+" picked up "+task.getId());

                try{
                    task.doWork();
                    task.setState(TaskState.SUCCESS);

                    System.out.println(name+" completed "+task.getId());
                }catch(RuntimeException e){
                    task.setState(TaskState.FAILED);

                    System.out.println(name+" failed "+task.getId());
                }
            } catch(InterruptedException e){
                Thread.currentThread().interrupt();

                System.out.println(name+" shutting down");
                break;
            }
        }
    }
    
}
