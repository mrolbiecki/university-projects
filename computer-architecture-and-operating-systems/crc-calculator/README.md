# CRC Checksum Calculator

## Description
This project implements a program `crc` written in assembly, which calculates the CRC (Cyclic Redundancy Code) checksum of data contained in a sparse file. The program processes file data in segments, calculating the checksum byte by byte, using a user-provided CRC polynomial.

## Usage
```bash
./crc file crc_poly
```

- `file`: The name of the sparse file to process.
- `crc_poly`: A binary string representing the CRC polynomial (up to 65 bits, excluding the coefficient for the highest degree term).

## Features
- Handles sparse files with data segments, where each segment has:
  - A 2-byte length field (little-endian).
  - Data.
  - A 4-byte offset to the next segment (little-endian, two's complement).
- Computes the CRC checksum based on the provided polynomial.
- Outputs the calculated checksum as a binary string followed by a newline.
- Returns exit code `0` on success and `1` on error (invalid parameters or system call failures).
