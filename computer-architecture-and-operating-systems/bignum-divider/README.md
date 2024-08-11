# mdiv: Integer Division Function in Assembly

## Description
This project implements an integer division function `mdiv` in assembly, callable from C. The function divides a large integer stored in an array by a 64-bit integer, returning the remainder and storing the quotient back in the array.

## Function Declaration
```c
int64_t mdiv(int64_t *x, size_t n, int64_t y);
```

- `x`: Pointer to a non-empty array of `n` 64-bit integers representing the dividend (stored in little-endian order).
- `n`: The number of 64-bit integers in the dividend array.
- `y`: The divisor (a 64-bit integer).

**Return Value**: The function returns the remainder of the division. The quotient is stored back in the `x` array.

## Function Behavior
- The function performs integer division with remainder, treating the dividend, divisor, quotient, and remainder as two's complement integers.
- If the quotient cannot be represented within the bounds of the provided array `x`, an overflow occurs.
- Division by zero is treated as a special case of overflow.

## Error Handling
- The function handles overflow and division by zero by raising an interrupt number `0`, consistent with the behavior of `div` and `idiv` instructions in assembly.
