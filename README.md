# Concurrent Task Execution Engine

A Java-based task execution engine for running tasks with dependencies while exploring concurrent execution, thread synchronization, and custom concurrency primitives.

> **Status:**  In Development

## Overview

The project models tasks as a Directed Acyclic Graph (DAG), where each task can depend on one or more other tasks.

A task becomes eligible for execution only after all of its dependencies have completed successfully.

The eventual execution flow is:

```text
Task Graph
    │
    ▼
Dependency Resolution
    │
    ▼
Ready Tasks
    │
    ▼
Blocking Queue
    │
    ▼
Worker Threads
    │
    ▼
Concurrent Task Execution