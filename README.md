
---

## **Assignment 4: Smart City/Campus Scheduling - Graph Algorithms**

### **Project Overview**

This project implements a modular solution for analyzing task dependency graphs in smart campus scheduling scenarios.
The system performs a full pipeline of graph analysis including:

1. **Strongly Connected Components (SCC)** using *Kosaraju’s Algorithm*
2. **Topological Sorting** using *Kahn’s Algorithm*
3. **Shortest and Longest Paths** in a **DAG**

Each stage extracts structural and performance insights from the dataset, using consistent metrics tracking across algorithms.

---

### **Architecture**

```
project-root/
├── data/                              // JSON datasets (small / medium / large)
│   ├── small/
│   ├── medium/
│   └── large/
│
└── src/
    ├─── main/
    │    └── java/
    │        ├── App/
    │        │   └── Main.java          // entry point
    │        │
    │        ├── graph/
    │        │   ├── dagsp/             // DAG shortest / longest paths
    │        │   ├── model/             // graph structures
    │        │   ├── scc/               // strongly connected components
    │        │   └── topo/              // topological sorting
    │        │
    │        └── util/
    │            ├── datagen/           // dataset generation
    │            ├── io/                // JSON handling
    │            └── metrics/           // performance measurement
    └── test/
         └── java/                      // unit tests mirror main/java
               └── graph/
                  ├── dagsp/             // tests for DAGShortestPaths & DAGLongestPath
                  ├── scc/               // tests for Kosaraju & SCCResult
                  └── topo/              // tests for TopoSort & Kahn            
```

The main entry point `App/Main.java` integrates all modules, allowing generation, loading, and analysis of datasets interactively.
nstallation & Quick Start
Requirements
Java 17+
Maven 3.6+
Build
cd assignment-4-DAA
mvn clean compile
Run Analysis
## Run main class

        mvn clean compile
        
        java -cp target/classes App.Main

## Run Tests
        mvn test                               # All tests
        mvn test -Dtest=SCCTest                # Run only the SCC algorithm tests
        mvn test -Dtest=TopoSortTest           # Run only the Topological Sorting tests
        mvn test -Dtest=DAGShortestPathsTest   # Run only the DAG Shortest Paths tests
        mvn test -Dtest=DAGLongestPathsTest    # Run only the DAG Longest Paths tests
        mvn test -Dtest=CondensationGraphTest  # Run only the Condensation Graph tests


---

### **Core Algorithms**

#### **1. Strongly Connected Components (SCC)**

* **Algorithm:** Kosaraju (two-pass DFS)
* **Purpose:** Identify cyclic dependencies in task graphs.
* **Output:** Components list and condensation DAG.

**Complexity:**

* Time: O(V + E)
* Space: O(V)

**Metrics Example (medium dataset):**

```
SCCs found: 10
Execution time: 0.296 ms
DFS1 edges: 20
DFS1 visits: 15
DFS2 visits: 15
DFS2 edges: 20
```

**Interpretation:**
Ten SCCs detected; smaller components merged into a 10-node condensation DAG representing acyclic dependencies.

---

#### **2. Topological Sorting**

* **Algorithm:** Kahn’s Algorithm (BFS-based)
* **Purpose:** Generate valid linear ordering for DAG tasks.
* **Output:** Topological order and cycle detection.

**Complexity:**

* Time: O(V + E)
* Space: O(V)

**Metrics Example:**

```
Topo order: [0, 1, 3, 2, 4, 5, 6, 7, 8, 9]
Execution time: 0.142 ms
Pushes: 10
Pops: 10
Edges processed: 12
Relaxations: 12
```

**Interpretation:**
All DAG vertices successfully ordered with no cycles detected. Processing time remained under 0.2 ms.

---

#### **3. Shortest Paths in DAG**

* **Algorithm:** Dynamic Programming over topological order
* **Purpose:** Compute minimum time/distance between tasks.
* **Output:** Distance array and relaxed edges count.

**Complexity:**

* Time: O(V + E)
* Space: O(V)

**Results:**

```
Shortest paths from source 0:
0 -> 0 = 0
0 -> 9 = 9
Execution time: 0.110 ms
Edges processed: 12
Relaxations: 10
```

**Interpretation:**
All reachable nodes computed. Fast execution due to linear pass along DAG edges.

---

#### **4. Longest Paths in DAG (Critical Path)**

* **Algorithm:** Similar to shortest path, but maximizing instead of minimizing.
* **Purpose:** Identify the critical path — the sequence of tasks determining total completion time.
* **Output:** Longest distance array and execution time.

**Results:**

```
Longest paths from source 0:
0 -> 0 = 0
0 -> 9 = 11
Execution time: 0.151 ms
Edges processed: 12
Relaxations: 11
```

**Interpretation:**
Critical path length = **11 units**, representing the bottleneck duration for full schedule completion.

---

### **Performance Summary (medium_multiple_scc.json)**

| Algorithm         | Execution Time (ms) | Edges Processed | Notes                          |
| ----------------- | ------------------: | --------------: | ------------------------------ |
| Kosaraju SCC      |               0.296 |              40 | Detected 10 SCCs               |
| Kahn Topo Sort    |               0.142 |              12 | Produced valid order           |
| DAG Shortest Path |               0.110 |              12 | Found min times                |
| DAG Longest Path  |               0.151 |              12 | Found critical path (11 units) |

**Overall runtime:** < 1 ms for all stages combined.
Performance scales linearly with graph size (O(V + E)), confirming algorithmic efficiency.

---

### **Datasets Summary**

Three dataset categories were used:

| Category | Vertices |  Edges | Type                     | Purpose           |
| -------- | -------: | -----: | ------------------------ | ----------------- |
| small_*  |     6–10 |     ~9 | Cyclic + DAG             | Basic correctness |
| medium_* |    15–20 |    ~20 | Mixed (cyclic + acyclic) | Performance & SCC |
| large_*  |    30–40 | ~50–90 | Complex DAGs             | Stress testing    |

The medium dataset (`medium_multiple_scc.json`) best represents real mixed dependencies and was used for main benchmarking.

---

### **Metrics System**

Each algorithm integrates a shared `Metrics` utility that tracks:

* Operation counters (edges processed, relaxations, pushes/pops)
* Execution time via `System.nanoTime()`
* Formatted summary printing via `printMetrics()`

Metrics were designed for clarity and isolation — not stored in result objects to keep core data structures clean.

---

### **Conclusions**

* **Kosaraju’s algorithm** proved simple and reliable for SCC detection.
* **Kahn’s algorithm** efficiently produced valid topological orders in all DAGs.
* **DAG shortest and longest paths** correctly identified dependency timing and the critical path.
* All computations completed in under **1 millisecond**, even on medium datasets.

**Practical takeaway:**
The pipeline effectively identifies cyclic dependencies, orders independent components, and computes schedule-critical paths — making it suitable for academic scheduling, task planning, or smart campus management systems.

---
