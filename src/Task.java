import java.util.List;
import java.util.ArrayList;

class Task{
    private String id;
    private Runnable work;

    private List<Task> children;
    private List<Task> dependencies;
    private int dependencyCount;

    private TaskState state;

    public Task(String id, Runnable work){
        this.id = id;
        this.work = work;

        this.children = new ArrayList<>();
        this.dependencies = new ArrayList<>();
        this.dependencyCount = 0;

        this.state = TaskState.PENDING;
    }

    public void addDependency(Task task){
        if(dependencies.contains(task)){
            return;
        }
        
        task.children.add(this);
        this.dependencies.add(task);
        this.dependencyCount++;
    }
    public List<Task> getChildren(){
        return children;
    }
    public synchronized int getDependencyCount(){
        return dependencyCount;
    }
    public List<Task> getDependencies(){
        return dependencies;
    }

    public boolean dependenciesCompleted(){
        for(Task task : dependencies){
            if(task.getState() != TaskState.SUCCESS){
                return false;
            }
        }
        return true;
    }

    public synchronized boolean dependencyCompleted(){
        if(dependencyCount > 0){
            dependencyCount--;
        }

        if(dependencyCount == 0 && state == TaskState.PENDING){
            state = TaskState.READY;
            return true;
        }
        return false;
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