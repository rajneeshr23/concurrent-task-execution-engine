import java.util.List;
import java.util.ArrayList;

class Task{
    private String id;
    private Runnable work;
    // private int parentCount;

    private List<Task> children;
    private int dependencyCount;

    private TaskState state;

    // private List<Task> dependencyList; 

    public Task(String id, Runnable work){
        this.id = id;
        this.work = work;

        this.children = new ArrayList<>();
        this.dependencyCount = 0;
        
        // this.dependencyList = new ArrayList<>();
        // this.parentCount = 0;

        this.state = TaskState.PENDING;
    }

    public void addDependency(Task task){
        // children.add(task);
        task.children.add(this);
        this.dependencyCount++;
    }
    public List<Task> getChildren(){
        return children;
    }
    public int getDependencyCount(){
        return dependencyCount;
    }

    public void dependencyCompleted(){
        dependencyCount--;
    }

    // public void addParent(){
    //     parentCount++;
    // }
    // public int getParentCount(){
    //     return parentCount;
    // }

    // public void addChild(Task task){
    //     children.add(task);
    // }
    // public List<Task> getChildren(){
    //     return children;
    // }

    public void doWork(){
        work.run();
    }

    public String getId(){
        return id;
    }

    public TaskState getState(){
        return state;
    }
    public void setState(TaskState state){
        this.state = state;
    }
}