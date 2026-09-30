public class Main {

    public static void main(String[] args) throws InterruptedException {

        TaskGraph graph = new TaskGraph();
        // Scheduler scheduler = new Scheduler(graph);

        Task A = new Task("A", () -> {
            System.out.println("Executing A");
        });

        Task B = new Task("B", () -> {
            System.out.println("Executing B");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Task C = new Task("C", () -> {
            System.out.println("Executing C");
        });

        Task D = new Task("D", () ->
                System.out.println("Executing D"));

        /*// A → B → C
        // A.addChild(B);
        // B.addParent();

        // B.addChild(C);
        // C.addParent();

        // A.addDependency(B);
        // B.addDependency(C);

        // System.out.println("Task A");
        // System.out.println("Dependents: " + A.getChildren().size());
        // System.out.println("Dependencies: " + A.getDependencyCount());

        // System.out.println();

        // System.out.println("Task B");
        // System.out.println("Dependents: " + B.getChildren().size());
        // System.out.println("Dependencies: " + B.getDependencyCount());

        // System.out.println();

        // System.out.println("Task C");
        // System.out.println("Dependents: " + C.getChildren().size());
        // System.out.println("Dependencies " + C.getDependencyCount());

        B.addDependency(A);
        C.addDependency(A);

        D.addDependency(B);
        D.addDependency(C);

        graph.addTask(A);
        graph.addTask(B);
        graph.addTask(C);
        graph.addTask(D);

        // System.out.println("A dependency count: " + A.getDependencyCount());
        // System.out.println("B dependency count: " + B.getDependencyCount());
        // System.out.println("C dependency count: " + C.getDependencyCount());
        // System.out.println("D dependency count: " + D.getDependencyCount());

        // System.out.println();

        // System.out.println("A children: " + A.getChildren().size());
        // System.out.println("B children: " + B.getChildren().size());
        // System.out.println("C children: " + C.getChildren().size());
        // System.out.println("D children: " + D.getChildren().size());

        // System.out.println();
        // System.out.println("Initially ready:");*/

        B.addDependency(A);
        C.addDependency(A);

        D.addDependency(B);
        D.addDependency(C);

        graph.addTask(A);
        graph.addTask(B);
        graph.addTask(C);
        graph.addTask(D);

        // System.out.println("Initially ready:");

        // for (Task task : graph.getReadyTasks()) {
        //     System.out.println(task.getId());
        // }

        // Task task = graph.getTask("A");
        // task.setState(TaskState.RUNNING);

        // try{
        //     task.doWork();
        //     task.setState(TaskState.SUCCESS);

        //     for(Task child : task.getChildren()){
        //         child.dependencyCompleted();
        //     }
        // } catch (Exception e){
        //     task.setState(TaskState.FAILED);
        // }

        // System.out.println("\n Ready after A completes: ");

        // for(Task readyTask : graph.getReadyTasks()){
        //     System.out.println(readyTask.getId());
        // }

        // Scheduler scheduler = new Scheduler(graph);
        // scheduler.run();

        // BlockingTaskQueue queue = new BlockingTaskQueue(2);

        // Thread producer = new Thread(() -> {
        //     try{
        //         System.out.println("Putting A");
        //         queue.put(A);

        //         System.out.println("Putting B");
        //         queue.put(B);

        //         System.out.println("Putting C");
        //         queue.put(C);

        //         System.out.println("Putting D");
        //         queue.put(D);

        //     } catch (InterruptedException e){
        //         Thread.currentThread().interrupt();
        //     }
        // });

        // Thread consumer = new Thread(() -> {
        //     try{
        //         Thread.sleep(2000);

        //         System.out.println("Taking task");
        //         Task task = queue.take();

        //         System.out.println("Took "+ task.getId());

        //         System.out.println("Taking task");
        //         task = queue.take();

        //         System.out.println("Took "+ task.getId());
        //     } catch(InterruptedException e){
        //         Thread.currentThread().interrupt();
        //     }
        // });

        // producer.start();
        // consumer.start();

        BlockingTaskQueue queue = new BlockingTaskQueue(10);

        Thread worker1 = new Thread(
            new Worker(queue, "Worker-1")
        );

        Thread worker2 = new Thread(
            new Worker(queue, "Worker-2")
        );

        worker1.start();
        worker2.start();
        queue.put(B);
        queue.put(C);  
        Thread.sleep(1000);

        worker1.interrupt();
        worker2.interrupt();
    }
}