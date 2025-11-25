# Assignment-6-Multi-threaded-Data-Processing-System

This project implements a concurrent task processing system in both Java and Go, demonstrating different concurrency models and synchronization mechanisms.

## Project Structure

```
.
├── java/
│   ├── TaskQueue.java    # Thread-safe task queue with synchronized blocks
│   ├── Worker.java       # Worker thread implementation
│   ├── Main.java         # Main orchestrator class
├── go/
│   ├── main.go           # Complete Go implementation with goroutines
└── README.md             # This file
```

## Features

- **Shared Task Queue**: Thread-safe queue for distributing tasks to workers
- **Multiple Workers**: Concurrent processing using threads (Java) or goroutines (Go)
- **Synchronization**: Proper synchronization mechanisms to prevent race conditions
- **Error Handling**: Comprehensive error handling and logging
- **Result Collection**: Thread-safe result storage and file output
- **Graceful Shutdown**: Proper cleanup and resource management

## Java Implementation

### Requirements
- JDK 8 or higher
- Java compiler (`javac`)
- Java runtime (`java`)

### How to Run

1. Navigate to the Java directory:
```bash
cd java
```

2. Compile the Java files:
```bash
javac *.java
```

3. Run the program:
```bash
java Main
```

### Java Concurrency Model

- **Thread-based**: Uses `ExecutorService` with fixed thread pool
- **Synchronization**: `synchronized` blocks with `wait()`/`notifyAll()` for producer-consumer pattern
- **Shared Memory**: Thread-safe collections (`CopyOnWriteArrayList`) for results
- **Exception Handling**: Try-catch blocks for `InterruptedException` and general exceptions

### Key Components

- **TaskQueue**: Synchronized queue using `LinkedList` with wait/notify mechanism
- **Worker**: Runnable implementation that processes tasks from the queue
- **Main**: Orchestrates workers, adds tasks, and collects results

## Go Implementation

### Requirements
- Go 1.21 or higher
- Go compiler (`go`)

### How to Run

1. Navigate to the Go directory:
```bash
cd go
```

2. Run the program:
```bash
go run main.go
```

Or compile and run:
```bash
go build -o task-processor
./task-processor
```

### Go Concurrency Model

- **Goroutine-based**: Lightweight goroutines managed by Go runtime
- **Channels**: Buffered channels for task queue and results (thread-safe by design)
- **Message Passing**: Communication through channels instead of shared memory
- **Error Handling**: Explicit error returns with `if err != nil` checks

### Key Components

- **Channels**: `tasks` channel for task distribution, `results` channel for result collection
- **Worker Function**: Goroutine function that processes tasks from channel
- **WaitGroup**: Synchronization primitive to wait for all workers to complete

## Concurrency Comparison

| Aspect | Java | Go |
|--------|------|-----|
| **Model** | Thread-based, shared memory | Goroutine-based, message passing |
| **Synchronization** | `synchronized`, `ReentrantLock`, `BlockingQueue` | Channels (inherently thread-safe) |
| **Error Handling** | Exceptions (try-catch) | Explicit error returns |
| **Resource Management** | `ExecutorService.shutdown()` | Channel closing, `WaitGroup` |
| **Overhead** | Higher (OS threads) | Lower (lightweight goroutines) |

## Output

Both implementations will:
1. Start multiple workers
2. Add tasks to the queue
3. Process tasks concurrently with simulated delays
4. Collect and display results
5. Save results to a text file

Expected output includes:
- Worker start/stop messages
- Task processing logs
- Completion status
- Results summary
- File output confirmation

## Error Handling

### Java
- Handles `InterruptedException` for thread interruption
- Try-catch blocks for general exceptions
- Graceful shutdown with timeout

### Go
- Explicit error checking with `if err != nil`
- `defer` statements for resource cleanup
- Channel closing for graceful termination

## Logging

Both implementations include comprehensive logging:
- Worker thread/goroutine lifecycle
- Task addition and retrieval
- Task processing status
- Error messages
- Result collection

