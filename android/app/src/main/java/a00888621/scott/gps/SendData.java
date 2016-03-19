/*---------------------------------------------------------------------------------------
--	Source File:		SendData.java - Android application to send the users
 data to the web server to plot on google maps.
--
--	Methods:
--				doInBackground
--              onPostExecute
--              initializeSocket
--
--
--	Date:			March 18, 2016
--
--	Revisions:		(Date and Description)
--
--	Designer:		Scott Plummer
--
--	Programmer:		Scott Plummer
--
--	Notes: The thread that handles creating the connection to the server and
--  sending the user data.
---------------------------------------------------------------------------------------*/
package a00888621.scott.gps;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by scott on 17/03/16.
 */
public class SendData extends AsyncTask<String, Void, String> {
    private static final int PORT = 7000;
    private View rootView;

    /*------------------------------------------------------------------------------------------------------------------
-- CONSTRUCTOR: SendData
--
-- DATE: March 18, 2016
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Scott Plummer
--
-- PROGRAMMER: Scott Plummer
--
-- INTERFACE: public SendData(View rootView, final String ip)
--                      rootView -- View of the calling activity
--                      ip -- IP Address of the server
--
-- RETURNS: void
--
-- NOTES: sends user data to the server
----------------------------------------------------------------------------------------------------------------------*/
    public SendData(View rootView) {
        this.rootView = rootView;
    }

    /*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: doInBackground
--
-- DATE: March 18, 2016
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Scott Plummer
--
-- PROGRAMMER: Scott Plummer
--
-- INTERFACE: protected String doInBackground(String... params)
--                                  params -- data to send
--
-- RETURNS: The data that was sent.
--
-- NOTES: sends user data to the server
----------------------------------------------------------------------------------------------------------------------*/
    @Override
    protected String doInBackground(String... params) {
        Socket serverSocket = initializeSocket(params[0]);
        if(serverSocket == null) {
            return "Error opening Socket";
        }

        try {
            DataOutputStream outputStream = new DataOutputStream(serverSocket
                    .getOutputStream());
            outputStream.writeUTF(params[1]);
        } catch(SocketException e) {
            e.printStackTrace();
            return e.getStackTrace().toString();
        } catch(Exception e) {
            e.printStackTrace();
            return e.getStackTrace().toString();
        }
        return params[1];
    }

    /*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: onPostExecute
--
-- DATE: March 18, 2016
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Scott Plummer
--
-- PROGRAMMER: Scott Plummer
--
-- INTERFACE: protected void onPostExecute( final String result )
--                              result -- the data sent to the server
--
-- RETURNS: void
--
-- NOTES: Updates UI with the data that was sent
----------------------------------------------------------------------------------------------------------------------*/
    @Override
    protected void onPostExecute( final String result ) {
        TextView sentData  = (TextView) rootView.findViewById(R.id.sentData);
        sentData.setText("Sent: " + result);
    }

    /*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: initializeSocket
--
-- DATE: March 18, 2016
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Scott Plummer
--
-- PROGRAMMER: Scott Plummer
--
-- INTERFACE: private Socket initializeSocket(String ipAddress)
--                              ipAddress -- The ip address of the server
--
-- RETURNS: The socket that was initialized.
--
-- NOTES: Sets the send buffer size to match that over the server(256)
----------------------------------------------------------------------------------------------------------------------*/
    private Socket initializeSocket(String ipAddress) {
        Socket serverSocket;
        try {
            serverSocket = new Socket(ipAddress, PORT);
            serverSocket.setSendBufferSize(256);
        } catch(IOException e) {
            Log.e("IOException", e.getStackTrace()
                    .toString());
            return null;
        } catch(Exception e) {
            Log.e("None IO exception", e.getStackTrace().toString());
            return null;
        }

        return serverSocket;
    }
}
