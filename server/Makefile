# Makefile
# Jerry Jia
# Scamaz

CC=g++
EXECUTABLE_NAME=Server
FLAGS=-g -ggdb -Wall 
LIBRARIES = -pthread -lmysqlclient 
S_FILES= server.cpp main.cpp
OBJECT_FILES=$(S_FILES:.cpp=.o)
META_FILES=$(S_FILES:.cpp=.cpp.meta)

all:
	$(CC) $(FLAGS) $(LIBRARIES) $(S_FILES) -o $(EXECUTABLE_NAME) `mysql_config --cflags --libs`

debug:
	$(CC) $(FLAGS)-o $(EXECUTABLE_NAME) $(OBJECT_FILES)
clean:
	rm $(OBJECT_FILES) $(EXECUTABLE_NAME) $(META_FILES) Makefile.meta

clean_o:
	rm $(OBJECT_FILES)

remake:
	make clean
	clear
	make
