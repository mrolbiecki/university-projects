# Arithmetic of Approximate Values

## Description
This project implements a C library for performing arithmetic operations on approximate values. The library provides functions to create and manipulate such values, as well as to perform arithmetic operations while accounting for their uncertainty.

## Interface Overview

### Structure
```c
typedef struct wartosc;
```
- **`wartosc`**: Represents an approximate value, defined in the implementation.

### Constructors

#### `wartosc wartosc_dokladnosc(double x, double p);`
Creates an approximate value representing `x ± p%`.

- **Parameters**:
  - `x`: The central value.
  - `p`: The percentage error.

- **Returns**: A `wartosc` structure representing the range of possible values.

#### `wartosc wartosc_od_do(double x, double y);`
Creates an approximate value from a range `[x, y]`.

- **Parameters**:
  - `x`: Lower bound of the range.
  - `y`: Upper bound of the range.

- **Returns**: A `wartosc` structure representing the range centered at `(x + y) / 2` with an error range of `(y - x) / 2`.

#### `wartosc wartosc_dokladna(double x);`
Creates an exact value `x`.

- **Parameters**:
  - `x`: The exact value.

- **Returns**: A `wartosc` structure representing the exact value `x ± 0`.

### Selectors

#### `bool in_wartosc(wartosc x, double y);`
Checks if a value `y` is within the range of possible values of `x`.

- **Parameters**:
  - `x`: The approximate value.
  - `y`: The value to check.

- **Returns**: `true` if `y` is within the range, otherwise `false`.

#### `double min_wartosc(wartosc x);`
Returns the minimum possible value of `x`.

- **Parameters**:
  - `x`: The approximate value.

- **Returns**: The lower bound of `x`, `-∞` if unbounded, or `nan` if `x` is empty.

#### `double max_wartosc(wartosc x);`
Returns the maximum possible value of `x`.

- **Parameters**:
  - `x`: The approximate value.

- **Returns**: The upper bound of `x`, `∞` if unbounded, or `nan` if `x` is empty.

#### `double sr_wartosc(wartosc x);`
Returns the average of the minimum and maximum values of `x`.

- **Parameters**:
  - `x`: The approximate value.

- **Returns**: The arithmetic mean of `min_wartosc(x)` and `max_wartosc(x)`, or `nan` if either bound is not finite.

### Modifiers

#### `wartosc plus(wartosc a, wartosc b);`
Performs addition on two approximate values.

- **Parameters**:
  - `a`: The first approximate value.
  - `b`: The second approximate value.

- **Returns**: A `wartosc` representing the result of adding `a` and `b`.

#### `wartosc minus(wartosc a, wartosc b);`
Performs subtraction on two approximate values.

- **Parameters**:
  - `a`: The value from which to subtract.
  - `b`: The value to subtract.

- **Returns**: A `wartosc` representing the result of subtracting `b` from `a`.

#### `wartosc razy(wartosc a, wartosc b);`
Performs multiplication on two approximate values.

- **Parameters**:
  - `a`: The first approximate value.
  - `b`: The second approximate value.

- **Returns**: A `wartosc` representing the result of multiplying `a` and `b`.

#### `wartosc podzielic(wartosc a, wartosc b);`
Performs division on two approximate values.

- **Parameters**:
  - `a`: The numerator.
  - `b`: The denominator.

- **Returns**: A `wartosc` representing the result of dividing `a` by `b`. If division by zero is detected, the result will be `HUGE_VAL`, `-HUGE_VAL`, or `nan` as appropriate.
