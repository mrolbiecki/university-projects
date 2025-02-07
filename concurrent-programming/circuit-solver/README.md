# Boolean Expression Concurrent Evaluation

## Project Description
This project implements a concurrent solver for evaluating boolean expressions. The solver allows multiple boolean expressions to be computed in parallel, utilizing concurrency to speed up calculations. It follows a tree structure, where each node represents a boolean operation or a leaf value. The implementation ensures that subexpressions are evaluated concurrently wherever possible.

## Features
- Concurrent evaluation of boolean expressions.
- Support for operations: AND, OR, NOT, IF, GT (greater than threshold), and LT (less than threshold).
- Thread-safe handling of multiple `solve()` calls.
- Ability to stop ongoing computations using `stop()`.

## Disclaimer
This repository does not include the full project code. The base skeleton of the project, including the `Circuit` class and its dependencies, was provided with the project description and cannot be shared. Only the code I implemented is included here.

