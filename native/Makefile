# Makefile for compiling the JNI C++ code into a DLL on Windows

# Compiler and compiler flags
CC := g++
CFLAGS := -shared -I"/c/Users/Adrienne/.jdks/openjdk-19.0.1/include" -I"/c/Users/Adrienne/.jdks/openjdk-19.0.1/include/win32"

# Source files and output DLL name
SRC := com_networkscan_cis18_HelloJNI.cpp
DLL := native.dll

all: $(DLL)

$(DLL): $(SRC)
	$(CC) $(CFLAGS) -o $@ $<

clean:
	rm -f $(DLL)
