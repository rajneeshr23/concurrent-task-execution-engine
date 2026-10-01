public class Main {

    public static void main(String[] args) throws InterruptedException {

        TaskGraph graph = new TaskGraph();
        Scheduler scheduler = new Scheduler(graph, 2);

        Task A = new Task("A", () -> {
            System.out.println("Executing A on " + Thread.currentThread().getName());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Task B = new Task("B", () -> {
            System.out.println("Executing B on " + Thread.currentThread().getName());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Task C = new Task("C", () -> {
            System.out.println("Executing C on " + Thread.currentThread().getName());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Task D = new Task("D", () -> {
            System.out.println("Executing D on " + Thread.currentThread().getName());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });


        B.addDependency(A);
        C.addDependency(A);

        D.addDependency(B);
        D.addDependency(C);

        graph.addTask(A);
        graph.addTask(B);
        graph.addTask(C);
        graph.addTask(D);

        scheduler.run();
    }
}