#include <stdio.h>
#include <mysql/mysql.h>
#include <string>
#include <iostream>
#include <stdlib.h>
#include <sstream>

using namespace std;

int MysqlInit();
int UserAuth(string, string);
int InsertLocation(string, string, string, string, string); 

MYSQL *conRet;
MYSQL *con = NULL;

string hostName = "localhost";
string dbName   = "android_gps";
string dbUser   = "root";
string dbPass   = "c0mmaudi0";

int main(int argc, char **argv)
{
  if( !MysqlInit() )
  {
    return 1;    
  } 

  if( UserAuth("vivek", "location") )
  {
    cout << "User auth successful!" << endl;
  }
 
  InsertLocation("scott", "123", "device", "lat", "long");
  
  return 0; 
}

int MysqlInit() 
{
  con = mysql_init( NULL );
  conRet = mysql_real_connect( con, hostName.c_str(), dbUser.c_str(), dbPass.c_str(), dbName.c_str(), 0, NULL, 0 );
  if(conRet == NULL)
  {
    cerr << "Cannot connect to database." << endl;
    return 0;
  }
  return 1;
}

int UserAuth(string username, string password)
{
  int mStatus  = 0;
  string query = "SELECT * FROM users WHERE username='" + username + "' AND password='" + password + "'";
  MYSQL_RES *mRes = NULL;
  my_ulonglong numRows;  

  mStatus = mysql_query(con, query.c_str());
  if(mStatus)
  {
    string err = mysql_error(con);
    cerr << err << endl;
    return 0;
  }  
  
  mRes = mysql_store_result(con);

  if(mRes)
  {
    numRows = mysql_num_rows(mRes);
  }

  if(numRows < 1)
  {
    cerr << "User could not be authenticated";
    return 0;
  }
  return 1;
}

int InsertLocation(string username, string ip, string dev, string latitude, string longitude)
{
  
  string getUserIDQuery = "SELECT user_id FROM users WHERE username='" + username + "'";
  //string insertQuery = "INSERT INTO location(user_id, ip_address, dev_name, latitude, longitude) VALUES (" + username + ", " + ip + ", " + dev + ", " + latitude + ", " + longitude + ")"; 
  MYSQL_RES *mRes = NULL;
  string insertQuery = "";
  int mStatus = 0;
  int userID  = 0;
  my_ulonglong numRows;
  MYSQL_ROW mRow;
  
  mStatus = mysql_query(con, getUserIDQuery.c_str());

  if(mStatus)
  {
    string err = mysql_error(con);
    cerr << err << endl;
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
    cerr << "InsertLocation error: numRows = mysql_num_rows()" << endl;
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
    string err = mysql_error(con);
    cerr << "InsertLocation: after insert query " <<  err << endl;
    return 0; 
  }  
  return 1;
}

