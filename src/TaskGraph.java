import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class TaskGraph{
    private Map<String, Task> tasks;

    public TaskGraph(){
        tasks = new HashMap<>();
    }

    public void addTask(Task task){
        tasks.put(task.getId(), task);
    }

    public Task getTask(String id){
        return tasks.get(id);
    }

    public List<Task> getAllTasks(){
        return new ArrayList<>(tasks.values());
    }

    public List<Task> getReadyTasks(){
        List<Task> readyTasks = new ArrayList<>();

        for(Task task : tasks.values()){
            if(task.getState() == TaskState.PENDING && 
                task.getDependencyCount() == 0){
                    
                task.setState(TaskState.READY);
                readyTasks.add(task);
            }
        }

        return readyTasks;
    }
}