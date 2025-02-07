**Computational Verification of a Combinatorial Hypothesis**

This project focuses on verifying a combinatorial hypothesis through computational means. Given two constrained multisets of natural numbers, the goal is to find the largest pair of supersets that remain "disjoint in sum" while maximizing their total sum. The problem is solved using two different approaches:

1. **Non-Recursive Implementation**: A single-threaded iterative approach designed to improve memory handling and avoid deep recursion.
2. **Parallel Implementation**: A multi-threaded version leveraging pthreads to achieve better scalability and performance.

### **Key Features**
- Efficient subset sum computations using bitsets.
- Thread-parallel work distribution for improved scalability.
- Constrained memory allocation and execution under strict computational limits.
- Performance evaluation and scalability analysis across multiple test cases.

### **Performance Evaluation**
The parallel implementation is evaluated by measuring its scalability for different values of `d` and varying thread counts. A performance report, including scalability graphs, is provided as `report.pdf`.

### **Disclaimer**
This repository does not include the full project code. The base skeleton of the project was provided with the project description and cannot be shared. Only the code I implemented is included here.


