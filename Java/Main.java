import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Main class that orchestrates the multi-threaded task processing system
 */
public class Main {
    private static final int NUM_WORKERS = 3;
    private static final int NUM_TASKS = 10;
    
    public static void main(String[] args) {        
        // Create shared resources
        TaskQueue queue = new TaskQueue();
        List<String> results = new CopyOnWriteArrayList<>(); // Thread-safe list
        
        // Create executor service for managing worker threads
        ExecutorService executor = Executors.newFixedThreadPool(NUM_WORKERS);
        
        try {
            // Start worker threads
            System.out.println("[LOG] Starting " + NUM_WORKERS + " worker threads...");
            for (int i = 1; i <= NUM_WORKERS; i++) {
                executor.submit(new Worker(i, queue, results));
            }
            
            // Give workers time to start
            Thread.sleep(500);
            
            // Add tasks to queue
            System.out.println("\n[LOG] Adding " + NUM_TASKS + " tasks to queue...");
            for (int i = 1; i <= NUM_TASKS; i++) {
                queue.addTask("Task" + i);
                Thread.sleep(200); // Small delay between task additions
            }
            
            // Close queue to signal no more tasks
            System.out.println("\n[LOG] All tasks added. Closing queue...");
            queue.close();
            
            // Shutdown executor and wait for tasks to complete
            System.out.println("[LOG] Shutting down executor service...");
            executor.shutdown();
            
            // Wait for all tasks to complete (with timeout)
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                System.err.println("[WARNING] Not all tasks completed within timeout period");
                executor.shutdownNow();
            }
            
            // Display results
            System.out.println("\n=== Processing Complete ===");
            System.out.println("[LOG] Total results collected: " + results.size());
            System.out.println("\n=== Results ===");
            for (int i = 0; i < results.size(); i++) {
                System.out.println((i + 1) + ". " + results.get(i));
            }
            
            // Save results to file
            saveResultsToFile(results);
            
        } catch (InterruptedException e) {
            System.err.println("[ERROR] Main thread interrupted: " + e.getMessage());
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("[ERROR] Unexpected error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (!executor.isShutdown()) {
                executor.shutdownNow();
            }
        }
    }
    
    /**
     * Save results to output file
     * @param results List of results to save
     */
    private static void saveResultsToFile(List<String> results) {
        try (FileWriter writer = new FileWriter("java_results.txt")) {
            writer.write("=== Task Processing Results ===\n\n");
            writer.write("Total tasks processed: " + results.size() + "\n\n");
            
            for (int i = 0; i < results.size(); i++) {
                writer.write((i + 1) + ". " + results.get(i) + "\n");
            }
            
            System.out.println("\n[LOG] Results saved to java_results.txt");
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to save results to file: " + e.getMessage());
        }
    }
}

