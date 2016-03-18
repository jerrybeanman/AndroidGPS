package a00888621.scott.gps;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class coordinatesActivity extends AppCompatActivity implements
        LocationListener {
    SendData sendData;
    protected LocationManager locationManager;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinates);

        locationManager = (LocationManager) getSystemService
                (Context.LOCATION_SERVICE);
        if(locationManager == null) {
            Toast.makeText(this, "Unable to activate the location manager, " +
                    "please try again later", Toast.LENGTH_LONG).show();
            finish();
        }
        sendData = new SendData(getWindow().getDecorView()
                .getRootView(), "52.37.233.202");

        sendUserData(generateUserData());
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

                    locationManager.requestLocationUpdates(providers, 1, 10,
                            this);
                }
            } catch (SecurityException e) {
                Log.e("Security error: " , e.getStackTrace().toString());
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        DateFormat format = DateFormat.getDateTimeInstance();
         String coordinateData = "Latitude: " + location.getLatitude() +
                "\nLongitude: " + location.getLongitude() + "\nLock time: " +
                format.format(new Date(location.getTime())) + "\nProvider: " +
                location.getProvider();
        sendUserData(coordinateData);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    private void disableUpdates() {
        try {
            locationManager.removeUpdates(this);
        } catch(SecurityException e) {
            Log.e("Security error: ", e.getStackTrace().toString());
        }
    }

    private void sendUserData(final String data) {
        sendData.execute(data);
    }

    private String generateUserData() {
        String userData = MainActivity.USERNAME_EXTRA + ":" + getIntent()
                .getStringExtra(MainActivity.USERNAME_EXTRA) + " " +
                MainActivity.PASSWORD_EXTRA + ":" + getIntent().getStringExtra
                (MainActivity.PASSWORD_EXTRA);
        return userData;
    }
}
