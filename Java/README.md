# Java Implementation

## Quick Start

```bash
# Compile all Java files
javac *.java

# Run the program
java Main
```

## Files

- **TaskQueue.java**: Thread-safe queue implementation using synchronized blocks
- **Worker.java**: Worker thread that processes tasks
- **Main.java**: Main class that orchestrates the system

## Configuration

You can modify these constants in `Main.java`:
- `NUM_WORKERS`: Number of worker threads (default: 3)
- `NUM_TASKS`: Number of tasks to process (default: 10)

## Output

Results are saved to `java_results.txt` in the same directory.

