package main

import (
	"fmt"
	"log"
	"os"
	"sync"
	"time"
)

const (
	numWorkers = 3
	numTasks   = 10
)

// Result represents a completed task result
type Result struct {
	WorkerID int
	Task     string
	TaskNum  int
	Message  string
}

func main() {
	// Ensure output is flushed
	os.Stdout.Sync()
	
	fmt.Println("=== Multi-Threaded Task Processing System (Go) ===")
	fmt.Println()

	// Create channels for tasks and results
	tasks := make(chan string, numTasks)
	results := make(chan Result, numTasks)

	// Use WaitGroup to wait for all workers to complete
	var wg sync.WaitGroup

	// Start worker goroutines
	fmt.Printf("[LOG] Starting %d worker goroutines...\n", numWorkers)
	os.Stdout.Sync()
	for i := 1; i <= numWorkers; i++ {
		wg.Add(1)
		go worker(i, tasks, results, &wg)
	}
	
	// Give workers a moment to start
	time.Sleep(100 * time.Millisecond)

	// Add tasks to the queue
	fmt.Printf("\n[LOG] Adding %d tasks to queue...\n", numTasks)
	for i := 1; i <= numTasks; i++ {
		task := fmt.Sprintf("Task%d", i)
		tasks <- task
		fmt.Printf("[LOG] Task added to queue: %s\n", task)
		time.Sleep(200 * time.Millisecond) // Small delay between task additions
	}

	// Close tasks channel to signal no more tasks will be added
	fmt.Println("\n[LOG] All tasks added. Closing tasks channel...")
	close(tasks)

	// Start a goroutine to close results channel when all workers are done
	go func() {
		wg.Wait()
		close(results)
		fmt.Println("[LOG] All workers completed. Results channel closed.")
	}()

	// Collect results
	var allResults []Result
	fmt.Println("\n[LOG] Collecting results...")
	for result := range results {
		allResults = append(allResults, result)
		fmt.Printf("[LOG] Result received: %s\n", result.Message)
		fmt.Printf("[LOG] Total results collected: %d\n", len(allResults))
	}

	// Display results
	fmt.Println("\n=== Processing Complete ===")
	fmt.Printf("[LOG] Total results collected: %d\n", len(allResults))
	fmt.Println("\n=== Results ===")
	for i, result := range allResults {
		fmt.Printf("%d. %s\n", i+1, result.Message)
	}

	// Save results to file
	if err := saveResultsToFile(allResults); err != nil {
		log.Printf("[ERROR] Failed to save results to file: %v\n", err)
	} else {
		fmt.Println("\n[LOG] Results saved to go_results.txt")
	}
}

// worker processes tasks from the tasks channel and sends results to the results channel
func worker(id int, tasks <-chan string, results chan<- Result, wg *sync.WaitGroup) {
	defer wg.Done()

	fmt.Printf("[LOG] Worker %d started\n", id)
	taskCounter := 0

	for task := range tasks {
		taskCounter++
		fmt.Printf("[LOG] Worker %d processing: %s\n", id, task)

		// Simulate CPU-intensive processing
		// Random delay between 1-1.5 seconds
		delay := time.Duration(1000+int(time.Now().UnixNano()%500)) * time.Millisecond
		time.Sleep(delay)

		// Generate result
		result := Result{
			WorkerID: id,
			Task:     task,
			TaskNum:  taskCounter,
			Message:  fmt.Sprintf("Worker %d completed %s (Task #%d)", id, task, taskCounter),
		}

		// Send result to results channel
		results <- result
		fmt.Printf("[LOG] Worker %d completed: %s\n", id, task)
	}

	fmt.Printf("[LOG] Worker %d finished (processed %d tasks)\n", id, taskCounter)
}

// saveResultsToFile writes results to a file
func saveResultsToFile(results []Result) error {
	file, err := os.Create("go_results.txt")
	if err != nil {
		return fmt.Errorf("error creating file: %w", err)
	}
	defer file.Close() // Ensure file is closed

	// Write header
	_, err = fmt.Fprintf(file, "=== Task Processing Results ===\n\n")
	if err != nil {
		return fmt.Errorf("error writing header: %w", err)
	}

	_, err = fmt.Fprintf(file, "Total tasks processed: %d\n\n", len(results))
	if err != nil {
		return fmt.Errorf("error writing count: %w", err)
	}

	// Write results
	for i, result := range results {
		_, err = fmt.Fprintf(file, "%d. %s\n", i+1, result.Message)
		if err != nil {
			return fmt.Errorf("error writing result %d: %w", i, err)
		}
	}

	return nil
}

