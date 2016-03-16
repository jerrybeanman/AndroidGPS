package a00888621.scott.gps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class coordinatesActivity extends AppCompatActivity implements
        LocationListener {
    static final int PORT = 7000;
    static final String IP = "192.168.43.246";
    protected LocationManager locationManager;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinates);
        locationManager = (LocationManager) getSystemService
                (Context.LOCATION_SERVICE);
        
        if(locationManager == null) {
            Log.e("Location manager error","Location Manager null");
            return;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_coordinates, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        List<String> enabledProviders = locationManager.getProviders(criteria,
                true);
        if(!enabledProviders.isEmpty()) {
            try {
                for (String providers: enabledProviders) {

                    locationManager.requestLocationUpdates(providers, 1, 1,
                            this);
                }
            } catch (SecurityException e) {
                Log.e("Security error: " , e.getStackTrace().toString());
            }
        }
    }

    private void sendLocationUpdate(String serverIP, String data) {
        try {
            Socket serverSocket = new Socket(serverIP, PORT);
            serverSocket.setSendBufferSize(256);
            DataOutputStream outputStream = new DataOutputStream(serverSocket
                    .getOutputStream());
            outputStream.writeUTF(data);
            serverSocket.close();
        } catch(IOException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        final TextView testView = (TextView) findViewById(R.id.test);
        DateFormat format = DateFormat.getDateTimeInstance();
       final String coordinateData = "Latitude: " + location.getLatitude() +
                "\nLongitude: " + location.getLongitude() + "\nLock time: " +
                format.format(new Date(location.getTime())) + "\nProvider: " +
                location.getProvider();
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                sendLocationUpdate(IP, coordinateData);
                return null;
            }

            @Override
            protected void onPostExecute( final Void result ) {
                testView.setText("Sent: " + coordinateData);
            }
        }.execute();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }
}
