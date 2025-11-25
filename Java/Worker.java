import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Worker thread that processes tasks from the shared queue
 */
public class Worker implements Runnable {
    private TaskQueue queue;
    private List<String> results;
    private int workerId;
    private static AtomicInteger taskCounter = new AtomicInteger(0);
    
    public Worker(int workerId, TaskQueue queue, List<String> results) {
        this.workerId = workerId;
        this.queue = queue;
        this.results = results;
    }
    
    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println("[LOG] Worker " + workerId + " (" + threadName + ") started");
        
        try {
            while (true) {
                String task = queue.getTask();
                
                // Check if queue is closed and empty
                if (task == null && queue.isClosed()) {
                    System.out.println("[LOG] Worker " + workerId + " (" + threadName + ") shutting down - no more tasks");
                    break;
                }
                
                if (task != null) {
                    processTask(task, threadName);
                }
            }
        } catch (InterruptedException e) {
            System.err.println("[ERROR] Worker " + workerId + " (" + threadName + ") interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("[ERROR] Worker " + workerId + " (" + threadName + ") encountered error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("[LOG] Worker " + workerId + " (" + threadName + ") finished");
    }
    
    /**
     * Process a single task with simulated computation delay
     * @param task The task to process
     * @param threadName Name of the processing thread
     */
    private void processTask(String task, String threadName) {
        try {
            System.out.println("[LOG] Worker " + workerId + " (" + threadName + ") processing: " + task);
            
            // Simulate CPU-intensive processing
            Thread.sleep(1000 + (int)(Math.random() * 500)); // 1-1.5 seconds
            
            // Generate result
            int taskNum = taskCounter.incrementAndGet();
            String result = String.format("Worker %d completed %s (Task #%d)", workerId, task, taskNum);
            
            // Store result in shared list (thread-safe)
            synchronized (results) {
                results.add(result);
                System.out.println("[LOG] Worker " + workerId + " (" + threadName + ") completed: " + task);
                System.out.println("[LOG] Total results collected: " + results.size());
            }
            
        } catch (InterruptedException e) {
            System.err.println("[ERROR] Worker " + workerId + " interrupted during task processing: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("[ERROR] Worker " + workerId + " error processing task " + task + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}

