# NAND Gates Dynamic Library

## Description
This project implements a dynamically loaded C library for handling combinational boolean circuits composed of NAND gates. A NAND gate can have any non-negative number of inputs and a single output. The library provides functions to create and manipulate these gates, connect them to each other, and evaluate the circuit.

## Interface Overview

### Structure
```c
typedef struct nand nand_t;
```
- **`nand_t`**: Represents a NAND gate. This structure is defined in the implementation.

### Functions

#### `nand_t* nand_new(unsigned n);`
Creates a new NAND gate with `n` inputs.

- **Returns**: A pointer to the NAND gate structure, or `NULL` if memory allocation fails (sets `errno` to `ENOMEM`).

#### `void nand_delete(nand_t *g);`
Disconnects signals and frees memory used by the specified NAND gate.

- **Parameter**: `g` - Pointer to the NAND gate structure. No action is taken if `g` is `NULL`.

#### `int nand_connect_nand(nand_t *g_out, nand_t *g_in, unsigned k);`
Connects the output of `g_out` to the `k`-th input of `g_in`.

- **Returns**: `0` on success, `-1` on failure (invalid parameters or memory allocation error, with `errno` set to `EINVAL` or `ENOMEM`).

#### `int nand_connect_signal(bool const *s, nand_t *g, unsigned k);`
Connects a boolean signal to the `k`-th input of `g`.

- **Returns**: `0` on success, `-1` on failure (invalid parameters or memory allocation error, with `errno` set to `EINVAL` or `ENOMEM`).

#### `ssize_t nand_evaluate(nand_t **g, bool *s, size_t m);`
Evaluates the output signals of the specified NAND gates and calculates the critical path length.

- **Returns**: The length of the critical path on success, or `-1` on failure (invalid parameters, cyclic dependencies, or memory allocation error, with `errno` set to `EINVAL`, `ECANCELED`, or `ENOMEM`).

#### `ssize_t nand_fan_out(nand_t const *g);`
Returns the number of inputs connected to the output of the specified NAND gate.

- **Returns**: The number of connected inputs on success, or `-1` if `g` is `NULL` (with `errno` set to `EINVAL`).

#### `void* nand_input(nand_t const *g, unsigned k);`
Returns a pointer to the signal or NAND gate connected to the `k`-th input of the specified NAND gate.

- **Returns**: A pointer to the connected signal or gate, or `NULL` if nothing is connected or if parameters are invalid (`errno` is set accordingly).

#### `nand_t* nand_output(nand_t const *g, ssize_t k);`
Iterates over the gates connected to the output of the specified NAND gate.

- **Returns**: A pointer to the connected gate at index `k`, or `NULL` if the parameters are invalid.

## Building the Library
To build the library, a `Makefile` is provided with the following targets:

- **`libnand.so`**: Compiles the library with the necessary options.
- **`make clean`**: Removes all generated files.
