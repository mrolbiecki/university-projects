CC       = gcc
CFLAGS   = -Wall -Wextra -Wno-implicit-fallthrough -std=gnu17 -fPIC -O2
LDFLAGS  = -shared -Wl,--wrap=malloc -Wl,--wrap=calloc -Wl,--wrap=realloc -Wl,--wrap=reallocarray -Wl,--wrap=free -Wl,--wrap=strdup -Wl,--wrap=strndup

.PHONY: all clean

all: libnand.so

nand.o: nand.c nand.h

libnand.so: nand.o
	$(CC) $(LDFLAGS) $^ -o $@

clean:
	rm -rf libnand.so nand_example *.o

