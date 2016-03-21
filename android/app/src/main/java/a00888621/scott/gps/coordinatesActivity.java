/*---------------------------------------------------------------------------------------
--	Source File:		coordinatesActivity.java - Android application to send
the users data to the web server to plot on google maps.
--
--	Methods:
--				protected void onCreate(Bundle savedInstanceState)
--              public boolean onCreateOptionsMenu(Menu menu)
--              public boolean onOptionsItemSelected(MenuItem item)
--              protected void onResume()
--              protected void onPause()
--              public void onLocationChanged(Location location)
--              public void onProviderEnabled(String provider)
--              public void onProviderDisabled(String provider)
--              private void disableUpdates()
--              private void sendUserData(final String data)
--              private String generateUserData()
--
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
--	Notes: The activity for getting the user location data.
---------------------------------------------------------------------------------------*/
package a00888621.scott.gps;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.List;

public class coordinatesActivity extends AppCompatActivity implements
        LocationListener {
    protected LocationManager locationManager;

    /*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: onCreate
--
-- DATE: March 18, 2016
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Scott Plummer
--
-- PROGRAMMER: Scott Plummer
--
-- INTERFACE: protected void onCreate(Bundle savedInstanceState)
--                                  savedInstanceState -- the state of the
activity
--
-- RETURNS: void
--
-- NOTES: Creates the locationManager and sends the users username and
password for website login
----------------------------------------------------------------------------------------------------------------------*/
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

        WebView webView = (WebView) findViewById(R.id.website);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://52.10.111.62");
    }
    /*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: onCreateOptionsMenu
--
-- DATE: March 18, 2016
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Scott Plummer
--
-- PROGRAMMER: Scott Plummer
--
-- INTERFACE: public boolean onCreateOptionsMenu(Menu menu)
--                                  menu -- The menu to create
--
-- RETURNS: true
--
-- NOTES:
----------------------------------------------------------------------------------------------------------------------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_coordinates, menu);
        return true;
    }

    /*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: onOptionsItemSelected
--
-- DATE: March 18, 2016
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Scott Plummer
--
-- PROGRAMMER: Scott Plummer
--
-- INTERFACE: public boolean onOptionsItemSelected(MenuItem item)
--                                 item -- the item that was selected
--
-- RETURNS: The option item that was selected
--
-- NOTES:
----------------------------------------------------------------------------------------------------------------------*/
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

    /*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: onResume
--
-- DATE: March 18, 2016
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Scott Plummer
--
-- PROGRAMMER: Scott Plummer
--
-- INTERFACE:  protected void onResume()
--
-- RETURNS: void.
--
-- NOTES: Enables updates from the location manager
----------------------------------------------------------------------------------------------------------------------*/
    @Override
    protected void onResume() {
        super.onResume();
        enableUpdates();
    }


    /*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: onPause
--
-- DATE: March 18, 2016
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Scott Plummer
--
-- PROGRAMMER: Scott Plummer
--
-- INTERFACE:  protected void onPause()
--
-- RETURNS: void.
--
-- NOTES: Disables  updates from the location manager
----------------------------------------------------------------------------------------------------------------------*/
    @Override
    protected void onPause() {
        super.onPause();
        disableUpdates();
    }


    /*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: onLocationChanged
--
-- DATE: March 18, 2016
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Scott Plummer
--
-- PROGRAMMER: Scott Plummer
--
-- INTERFACE:  public void onLocationChanged(Location location)
--                                      location -- Stores the location info
--
-- RETURNS: void.
--
-- NOTES: Retrieves the location data and sends it to the server
----------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void onLocationChanged(Location location) {
         String coordinateData = "latitude: " + location.getLatitude() +
                " longitude: " + location.getLongitude();
        sendUserData(generateUserData() + " " + coordinateData);
    }

    /*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: onStatusChanged
--
-- DATE: March 18, 2016
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Scott Plummer
--
-- PROGRAMMER: Scott Plummer
--
-- INTERFACE:   public void onStatusChanged(String provider, int status, Bundle extras)
--                                          provider -- Name of the provider
--                                          status -- type of status
--                                          extras -- unused
--
-- RETURNS: void.
--
-- NOTES: Resets the location manager updates
----------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        disableUpdates();
        enableUpdates();
    }

    /*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: onProviderEnabled
--
-- DATE: March 18, 2016
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Scott Plummer
--
-- PROGRAMMER: Scott Plummer
--
-- INTERFACE:   public void onProviderEnabled(String provider, int status, Bundle extras)
--                                          provider -- Name of the provider
--
-- RETURNS: void.
--
-- NOTES: Resets the location manager updates
----------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void onProviderEnabled(String provider) {
        disableUpdates();
        enableUpdates();
    }


    /*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: onProviderDisabled
--
-- DATE: March 18, 2016
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Scott Plummer
--
-- PROGRAMMER: Scott Plummer
--
-- INTERFACE:   public void onProviderDisabled(String provider, int status, Bundle extras)
--                                          provider -- Name of the provider
--
-- RETURNS: void.
--
-- NOTES: Resets the location manager updates
----------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void onProviderDisabled(String provider) {
        disableUpdates();
        enableUpdates();
    }


    /*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: disableUpdates
--
-- DATE: March 18, 2016
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Scott Plummer
--
-- PROGRAMMER: Scott Plummer
--
-- INTERFACE:    private void disableUpdates()
--
-- RETURNS: void.
--
-- NOTES: Disables the locations manager updates
----------------------------------------------------------------------------------------------------------------------*/
    private void disableUpdates() {
        try {
            locationManager.removeUpdates(this);
        } catch(SecurityException e) {
            Log.e("Security error: ", e.getStackTrace().toString());
        }
    }

    /*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: enableUpdates
--
-- DATE: March 18, 2016
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Scott Plummer
--
-- PROGRAMMER: Scott Plummer
--
-- INTERFACE:    private void enableUpdates()
--
-- RETURNS: void.
--
-- NOTES: Enables the locations manager updates
----------------------------------------------------------------------------------------------------------------------*/
    private void enableUpdates() {
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


    /*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: sendUserData
--
-- DATE: March 18, 2016
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Scott Plummer
--
-- PROGRAMMER: Scott Plummer
--
-- INTERFACE:    private void sendUserData(final String data)
--                                  data -- location data to send
--
-- RETURNS: void.
--
-- NOTES: Starts the async task
----------------------------------------------------------------------------------------------------------------------*/
    private void sendUserData(final String data) {
        new SendData(getWindow().getDecorView()
                .getRootView()).execute(getIntent().getStringExtra
                (MainActivity.IP_EXTRA), data);
    }

    /*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: generateUserData
--
-- DATE: March 18, 2016
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Scott Plummer
--
-- PROGRAMMER: Scott Plummer
--
-- INTERFACE:   private String generateUserData()
--
-- RETURNS: Users username, password, device name and ip address
--
-- NOTES:
----------------------------------------------------------------------------------------------------------------------*/
    private String generateUserData() {
        String userData = MainActivity.USERNAME_EXTRA + ": " + getIntent()
                .getStringExtra(MainActivity.USERNAME_EXTRA) + " " +
                MainActivity.PASSWORD_EXTRA + ": " + getIntent().getStringExtra
                (MainActivity.PASSWORD_EXTRA) + " deviceName: " + Build
                .MODEL + " deviceIP: " + getIPAddress();
        return userData;
    }

    /*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: getIPAddress
--
-- DATE: March 19, 2016
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Scott Plummer
--
-- PROGRAMMER: Scott Plummer
--
-- INTERFACE:   private String getIPAddress()
--
-- RETURNS: The ip address
--
-- NOTES:
----------------------------------------------------------------------------------------------------------------------*/
    private String getIPAddress() {
        WifiManager wifiManager = (WifiManager) this.getSystemService
                (WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddr;
        try {
            ipAddr = InetAddress.getByAddress(ipArray).getHostAddress();
        } catch (UnknownHostException e) {
            Log.e("Unknown host ", e.getStackTrace().toString());
            ipAddr = null;
        }

        return ipAddr;
    }
}
