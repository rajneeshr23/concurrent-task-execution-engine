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

    public boolean allTasksCompleted(){
        for(Task task : tasks.values()){
            TaskState state = task.getState();
            if(state == TaskState.PENDING ||
                state == TaskState.READY || 
                state == TaskState.RUNNING) {
                    
                return false;
            }
        }
        return true;
    }

    public void cancelPendingTasks(){
        for(Task task : tasks.values()){
            if(task.getState() == TaskState.PENDING || 
                task.getState() == TaskState.READY){
                task.setState(TaskState.CANCELLED);

                System.out.println("Cancelled "+task.getId());
            }
        }
    }

    public boolean hasCycle(){
        Map<Task, Integer> indegree = new HashMap<>();

        for(Task task : tasks.values()){
            indegree.put(task, 0);
        }

        for(Task task : tasks.values()){
            for(Task child : task.getChildren()){
                // indegree.put(child, indegree.getOrDefault(child, 0) + 1);
                indegree.put(child, indegree.get(child) + 1);
            }
        }

        List<Task> queue = new ArrayList<>();

        int processed = 0, index = 0;

        for(Task task : tasks.values()){
            // if(indegree.getOrDefault(task, 0) == 0) {
            if(indegree.get(task) == 0) {
                queue.add(task);
            }
        }

        while(index < queue.size()){
            Task task = queue.get(index++);
            processed++;

            for(Task child : task.getChildren()){
                int newDegree = indegree.get(child) - 1;
                indegree.put(child, newDegree);

                if(newDegree == 0) {
                    queue.add(child);
                }
            }
        }

        return processed != tasks.size();
    }

    public int countSuccessfulTasks(){
        int count = 0;

        for(Task task : tasks.values()){
            if(task.getState() == TaskState.SUCCESS){
                count++;
            }
        }
        
        return count;
    }

    public void printIncompleteTasks() {
        boolean firstIncompleteTask = true;

        for (Task task : tasks.values()) {
            if (task.getState() != TaskState.SUCCESS) {
                if(firstIncompleteTask) {
                    System.out.println("\n--- Incomplete Tasks ---");
                    firstIncompleteTask = false;
                }
                System.out.println(task.getId()+" | state="+task.getState()+" | remainingDependencies=" + task.getDependencyCount() );

                for (Task dependency : task.getDependencies()) {
                    System.out.println( " depends on " + dependency.getId() + " -> " + dependency.getState() );
                }
            }
        }
    }
}