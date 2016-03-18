package a00888621.scott.gps;

import android.content.Context;
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
    Socket serverSocket;
    public SendData(View rootView, final String ip) {
        this.rootView = rootView;
        serverSocket = initializeSocket(ip);
    }
    @Override
    protected String doInBackground(String... params) {
        if(serverSocket == null) {
            return "Error opening Socket";
        }

        try {
            DataOutputStream outputStream = new DataOutputStream(serverSocket
                    .getOutputStream());
            outputStream.writeUTF(params[0]);
        } catch(SocketException e) {
            e.printStackTrace();
            return e.getStackTrace().toString();
        } catch(Exception e) {
            e.printStackTrace();
            return e.getStackTrace().toString();
        }
        return params[0];
    }

    @Override
    protected void onPostExecute( final String result ) {
        TextView sentData  = (TextView) rootView.findViewById(R.id.sentData);
        sentData.setText("Sent: " + result);
    }

    private Socket initializeSocket(String ipAddress) {
        Socket serverSocket;
        try {
            serverSocket = new Socket(ipAddress, PORT);
            serverSocket.setSendBufferSize(256);
        } catch(IOException e) {
            return null;
        } catch(Exception e) {
            Log.e("None IO exception", e.getStackTrace().toString());
            return null;
        }

        return serverSocket;
    }
}
