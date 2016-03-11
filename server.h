#idndef SERVER
#define SERVER
#include <iostream>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <vector>
#include <unistd.h>
#include <errno.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
struct Client
{
	int 		Socket;
	sockaddr_in Connection;
};
class Server
{
	public:
		Server(){}
		~Server(){}
		bool InitializeSocket(const char * name, short port);
		bool MultiplexSettings();
		int  Accept(Client * client);
		int Receive(int index);
		void Broadcast(const char * message, int ExcludeIndex);
	private:
		struct sockaddr_in _ServerAddress;
};

#endif