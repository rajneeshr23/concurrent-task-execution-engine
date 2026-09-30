import java.util.List;

public class Scheduler {
    private TaskGraph graph;

    public Scheduler(TaskGraph graph){
        this.graph = graph;
    }

    public void run(){
        while(true){
            List<Task> ready = graph.getReadyTasks();

            if(ready.isEmpty()) {
                break;
            }

            for(Task task : ready){
                executeTask(task);
            }
        }
    }

    public void executeTask(Task task){
        task.setState(TaskState.RUNNING);

        try{
            task.doWork();
            task.setState(TaskState.SUCCESS);

            for(Task child : task.getChildren()){
                child.dependencyCompleted();
            }
        } catch(RuntimeException e) {
            task.setState(TaskState.FAILED);
            System.out.println("Task "+task.getId()+" failed.");
        }
    }
}
