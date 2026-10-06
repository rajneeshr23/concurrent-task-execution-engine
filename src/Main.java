import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        TaskGraph graph = new TaskGraph();
        List<Task> tasks = new ArrayList<>();

        Random random = new Random(42);

        // Create 100 tasks
        for (int i = 1; i <= 1000; i++) {

            final int taskId = i;

            Task task = new Task("Task-" + taskId, () -> {

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            tasks.add(task);
            graph.addTask(task);
        }

        // Add random dependencies
        for (int i = 1; i < tasks.size(); i++) {

            int dependencyCount = random.nextInt(Math.min(3, i) + 1);

            for (int j = 0; j < dependencyCount; j++) {
                int dependencyIndex = random.nextInt(i);

                tasks.get(i).addDependency(tasks.get(dependencyIndex));
            }
        }
        
        int tasksWithDependencies = 0;

        for (Task task : tasks) {
            if (task.getDependencyCount() > 0) {
                tasksWithDependencies++;
            }
        }

        System.out.println("Tasks with dependencies: " + tasksWithDependencies);

        Scheduler scheduler = new Scheduler(graph, 8);
        scheduler.run();
    }
}