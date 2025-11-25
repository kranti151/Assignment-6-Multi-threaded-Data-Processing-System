import java.util.LinkedList;
import java.util.Queue;

/**
 * Thread-safe task queue implementation using synchronized blocks
 * and wait/notify mechanism for producer-consumer pattern
 */
public class TaskQueue {
    private Queue<String> queue = new LinkedList<>();
    private boolean isClosed = false;
    
    /**
     * Add a task to the queue
     * @param task The task to be added
     */
    public synchronized void addTask(String task) {
        if (isClosed) {
            throw new IllegalStateException("Queue is closed");
        }
        queue.add(task);
        notifyAll(); // notify waiting threads
        System.out.println("[LOG] Task added to queue: " + task);
    }
    
    /**
     * Retrieve and remove a task from the queue
     * Blocks if queue is empty until a task is available
     * @return The next task to process
     * @throws InterruptedException if thread is interrupted while waiting
     */
    public synchronized String getTask() throws InterruptedException {
        while (queue.isEmpty() && !isClosed) {
            wait(); // wait if queue is empty
        }
        
        if (isClosed && queue.isEmpty()) {
            return null; // signal that no more tasks will come
        }
        
        String task = queue.poll();
        System.out.println("[LOG] Task retrieved from queue: " + task);
        return task;
    }
    
    /**
     * Close the queue to signal no more tasks will be added
     */
    public synchronized void close() {
        isClosed = true;
        notifyAll(); // wake up all waiting threads
        System.out.println("[LOG] Task queue closed");
    }
    
    /**
     * Check if queue is closed
     * @return true if queue is closed
     */
    public synchronized boolean isClosed() {
        return isClosed;
    }
    
    /**
     * Get current queue size
     * @return number of tasks in queue
     */
    public synchronized int size() {
        return queue.size();
    }
}

