# Go Implementation

## Installation

If Go is not installed on your system, follow these steps:

### macOS (using Homebrew)
```bash
# Install Homebrew if you don't have it
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Install Go
brew install go

# Verify installation
go version
```

### macOS (Manual Installation)
1. Download Go from https://go.dev/dl/
2. Download the macOS installer (e.g., `go1.21.x.darwin-amd64.pkg`)
3. Run the installer and follow the instructions
4. Verify installation: `go version`

### Alternative: Using the official installer
Visit https://go.dev/doc/install for the latest installation instructions.

## Quick Start

Once Go is installed:

```bash
# Navigate to the go directory
cd go

# Run directly
go run main.go

# Or compile and run
go build -o task-processor
./task-processor
```

## Configuration

You can modify these constants in `main.go`:
- `numWorkers`: Number of worker goroutines (default: 3)
- `numTasks`: Number of tasks to process (default: 10)

## Output

Results are saved to `go_results.txt` in the same directory.

