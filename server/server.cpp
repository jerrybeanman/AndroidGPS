#include "server.h"
#include <fcntl.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <sstream>

std::string hostName = "localhost";
std::string dbName   = "android_gps";
std::string dbUser   = "root";
std::string dbPass   = "c0mmaudi0";


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
	con = mysql_init(NULL);
	conRet = mysql_real_connect( con, hostName.c_str(), dbUser.c_str(), dbPass.c_str(), dbName.c_str(), 0, NULL, 0 );
	if(conRet == NULL)
	{
		std::cerr << "Cannot connect to database." <<std:: endl;
		return 0;
	} 
	if (con == NULL) 
	{
		std::cerr << "sql error " << mysql_error(con) << std::endl;
		return -1;
	}

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


int Server::UserAuth(std::string username, std::string password)
{
	int mStatus  = 0;
	std::string query = "SELECT * FROM users WHERE username='" + username + "' AND password='" + password + "'";
	MYSQL_RES * mRes = NULL;
	my_ulonglong numRows;  

	mStatus = mysql_query(con, query.c_str());
	if(mStatus)
	{
		std::string err = mysql_error(con);
		std::cerr << err << std::endl;
		return 0;
	}  

	mRes = mysql_store_result(con);

	if(mRes)
	{
		numRows = mysql_num_rows(mRes);
	}

	if(numRows < 1)
	{
		std::cerr << "User could not be authenticated";
		return 0;
	}
	return 1;
}

int Server::Query(std::string& queryString)
{
	if (mysql_query(con, queryString.c_str())) 
	{
		std::cerr << "Invalide query" << std::endl;
		return -1;
	}
	std::cout << "valid query" << std::endl;
	return 0;
}


int Server::InsertLocation(std::string username, std::string ip, std::string dev, std::string latitude, std::string longitude)
{
	std::string getUserIDQuery = "SELECT user_id FROM users WHERE username='" + username + "'";
	MYSQL_RES *mRes = NULL;
	std::string insertQuery = "";
	int mStatus = 0;
	int userID  = 0;
	my_ulonglong numRows;
	MYSQL_ROW mRow;

	mStatus = mysql_query(con, getUserIDQuery.c_str());

	if(mStatus)
	{
		std::string err = mysql_error(con);
		std::cerr << err << std::endl;
		return 0;
	} 
	mRes = mysql_store_result(con);

	if(mRes)
	{
		if( (numRows = mysql_num_rows(mRes)) )
		{
			mRow = mysql_fetch_row(mRes);
			userID = atoi(mRow[0]);
		}
	}
	else 
	{
		std::cerr << "InsertLocation error: numRows = mysql_num_rows()" << std::endl;
		return 0;
	}

	if(mRes)
	{
		mysql_free_result(mRes);
		mRes = NULL;
	}


	std::string userID_str;
	std::stringstream out;
	out << userID;
	userID_str = out.str();

	insertQuery += "INSERT INTO location(user_id, ip_address, dev_name, latitude, longitude)";
	insertQuery += " VALUES (" + userID_str + ", '" + ip + "', '" + dev + "', '" + latitude + "', '" + longitude + "')";
	// cerr << insertQuery << endl; 
	mStatus = mysql_query(con, insertQuery.c_str());

	if(mStatus)
	{
		std::string err = mysql_error(con);
		std::cerr << "InsertLocation: after insert query " <<  err << std::endl;
		return 0; 
	}  
	return 1;
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

	// Look for an available slot in CLientList
	for(i = 0; i < FD_SETSIZE; i++)
	{
		if(ClientList[i].socket < 0)
		{
			ClientList[i] = *client;
			break;
		}
	}

	// ClientList is 
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
	Packet packet;
	buf = (char *)malloc(PACKET_LEN); 	        // allocates memory
	memset(buf, 0, PACKET_LEN);
	BytesRead = recv (ClientList[index].socket, buf, PACKET_LEN, 0);
	// recv() failed
	if(BytesRead < 0)
	{
		printf("recv() failed with errno: %d", errno);
		return -1;
	}

	// client d
	if(BytesRead == 0)
	{
		free(buf);
		printf("Client %d has disconnected \n",  index+1);
		close(ClientList[index].socket);
		FD_CLR(ClientList[index].socket, &AllSet);
		return 1;
	}
	std::string ignore;
	std::istringstream iss;
	std::string query;
	sscanf(buf+2, "%*s %s %*s %s %*s %s %*s %s", packet.name, packet.password, packet.latitude, packet.longtitude);
	if(this->UserAuth(packet.name, packet.password))
	{
		this->InsertLocation(packet.name, packet.ip, packet.device, packet.latitude, packet.longtitude);
	}
	printf("Got message: %s\n", buf+2);
	free(buf);
	return 0;
}
