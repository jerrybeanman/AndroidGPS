#include "server.h"
#include <fcntl.h>
#include <stdlib.h>
#include <sys/stat.h>
/*-----------------------------------------------------------------------------------------------
--    Name:     [InitializeSocket]         Date:         [March 6th, 2016]
--
--    Designer: [Jerry Jia]                Programmer:   [Jerry Jia]
--
--    Interface:  int InitializeSocket(short port)
--                [port] Port number
--
--    @return: -1 on failure, 0 on success
--
--    Notes: Initialize ListeningSocket, set server b, Br server address to listeningSocket,
--             call listen on ListeningSocket, and set the corresponding parameters to use select()
------------------------------------------------------------------------------------------------*/

int Server::InitializeSocket(short port)
{
    int optval = -1;
    // Create a TCP streaming socket
    if ((ListeningSocket = socket(AF_INET, SOCK_STREAM, 0)) == -1 )
    {
        std::cout << "socket() failed with errno " << errno << std::endl;
        return -1;
    }

    // Allows other sockets to bind() to this port, unless there is an active listening socket bound to the port already.
    setsockopt(ListeningSocket, SOL_SOCKET, SO_REUSEADDR, &optval, sizeof(optval));

    // Fill in server address information
    memset(&_ServerAddress, 0, sizeof(struct sockaddr_in));
    _ServerAddress.sin_family = AF_INET;
    _ServerAddress.sin_port = htons(port);
    _ServerAddress.sin_addr.s_addr = htonl(INADDR_ANY); // Accept connections from any client

    // bind server address to accepting socket
    if (bind(ListeningSocket, (struct sockaddr *)&_ServerAddress, sizeof(_ServerAddress)))
    {
        std::cout << "InitializeSocket: bind() failed with errno " << errno << std::endl;
        return -1;
    }

    // Listen for connections
    listen(ListeningSocket, MAX_CONNECTIONS);

    MaxSocket = ListeningSocket;

    for(int i =0; i < FD_SETSIZE; i++)
        ClientList[i].socket = -1;

    FD_ZERO(&AllSet);
    FD_SET(ListeningSocket, &AllSet);

    return 0;
}

/*-----------------------------------------------------------------------------------------------
--    Name:     [Accept]                   Date:         [March 6th, 2016]
--
--    Designer: [Jerry Jia]                Programmer:   [Jerry Jia]
--
--    Interface:  int Accept(Client * client)
--                [client] Pointer to a client structure
--
--    @return: Index in ClientList where the client is stored at
--
--    Notes: Calls accept on a client's socket. Sets the returning socket and client address structure
--    to the client. Add connected client to ClientList
------------------------------------------------------------------------------------------------*/
int Server::Accept(Client * client)
{
    int             i;
    unsigned int    ClientLen = sizeof(client->connection);

    // Accepts a connection from the client
    if ((client->socket = accept(ListeningSocket, (struct sockaddr *)&client->connection, &ClientLen)) == -1)
    {
        std::cerr << "Accept() failed with errno:" << errno << std::endl;
        return -1;
    }

    std::cout << "Client Connect! Remote Address: " << inet_ntoa(client->connection.sin_addr) << std::endl;
    if((client->file_desc = open(inet_ntoa(client->connection.sin_addr), O_WRONLY | O_APPEND | O_CREAT | O_EXCL, 0777)) < 0)
    {
        if(errno == EEXIST)
        {
            client->file_desc = open(inet_ntoa(client->connection.sin_addr), O_WRONLY | O_APPEND, 0777);       
        }else
        {
           std::cerr << "Failed to open file " << inet_ntoa(client->connection.sin_addr) << std::endl;
           std::cerr << "errno: " << errno << std::endl;
          return -1;
        }
    }
    char mode[] = "0777";
    int in = strtol(mode, 0, 8); 
    chmod(inet_ntoa(client->connection.sin_addr) ,in);
    // Look for an available slot in CLientList
    for(i = 0; i < FD_SETSIZE; i++)
    {
        if(ClientList[i].socket < 0)
        {
            ClientList[i] = *client;
            break;
        }
    }

    // ClientList is full
    if(i == FD_SETSIZE)
    {
        std::cerr << "Too many clients to accept" << std::endl;
        return -1;
    }

    FD_SET(client->socket, &AllSet);
    if(client->socket > MaxSocket)
        MaxSocket = client->socket;

    return i;
}


/*-----------------------------------------------------------------------------------------------
--    Name:     [Receive]                  Date:         [March 6th, 2016]
--
--    Designer: [Jerry Jia]                Programmer:   [Jerry Jia]
--
--    Interface:  int Receive(int index)
--                [index] Index in CLientList to call recv() on
--
--    @return: -1 on error, 0 for success, 1 for client disconnect
--
--    Notes: Recieves data from the specific client's socket indicated by index, which will be
--        accessed on ClientList using index
------------------------------------------------------------------------------------------------*/
int Server::Receive(int index)
{
    int BytesRead;
    char * buf;

    buf = (char *)malloc(PACKET_LEN); 	        // allocates memory

    BytesRead = recv (ClientList[index].socket, buf, PACKET_LEN, 0);
        // recv() failed
        if(BytesRead < 0)
        {
            printf("recv() failed with errno: %d", errno);
            return -1;
        }

        // client disconnected
        if(BytesRead == 0)
        {
            free(buf);
            printf("Client %d has disconnected \n",  index+1);
            close(ClientList[index].socket);
            FD_CLR(ClientList[index].socket, &AllSet);
            close(ClientList[index].file_desc);
            return 1;
        }
    printf("Read %d bytes\n", BytesRead);
    printf("Got message: %s\n", buf+2);
    write(ClientList[index].file_desc, buf+2, strlen(buf));
    free(buf);
    return 0;
}
