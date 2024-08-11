CFLAGS   = -Wall -Wextra -Wno-implicit-fallthrough -std=gnu17 -fPIC -O2

.PHONY: all clean

all: crc

crc.o: crc.asm
	nasm -f elf64 -w+all -g -w+error -o $@ $^
	
crc: crc.o
	ld --fatal-warnings -g -o $@ $^

clean:
	rm -rf *.o
