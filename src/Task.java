import java.util.List;
import java.util.ArrayList;

class Task{
    private String id;
    private Runnable work;

    private List<Task> children;
    private int dependencyCount;

    private TaskState state;

    public Task(String id, Runnable work){
        this.id = id;
        this.work = work;

        this.children = new ArrayList<>();
        this.dependencyCount = 0;

        this.state = TaskState.PENDING;
    }

    public void addDependency(Task task){
        task.children.add(this);
        this.dependencyCount++;
    }
    public List<Task> getChildren(){
        return children;
    }
    public synchronized int getDependencyCount(){
        return dependencyCount;
    }

    // public synchronized void dependencyCompleted(){
    //     dependencyCount--;
    // }
    public synchronized boolean dependencyCompleted(){
        dependencyCount--;

        return dependencyCount == 0 && state == TaskState.PENDING;
    }

    public void doWork(){
        work.run();
    }

    public String getId(){
        return id;
    }

    public synchronized TaskState getState(){
        return state;
    }
    public synchronized void setState(TaskState state){
        this.state = state;
    }
}