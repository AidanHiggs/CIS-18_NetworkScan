# Makefile for compiling JNI C++ code into a shared library (.so) on Linux

# Compiler
CC = gcc

# Source files
C_SRC = PingJNI.c

# Object files
C_OBJ = $(C_SRC:.c=.o)

# Target shared library
LIBRARY = libnative.so

all: $(LIBRARY)

$(LIBRARY): $(C_OBJ)
	$(CC) $(CFLAGS) -o $@ $^ $(LDFLAGS)

%.o: %.c
	$(CC) $(CFLAGS) -c -o $@ $<

clean:
	rm -f $(C_OBJ) $(LIBRARY)