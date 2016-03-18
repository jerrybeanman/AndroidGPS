package a00888621.scott.gps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    public final static String IP_EXTRA = "IP Address";
    public final static String USERNAME_EXTRA = "username";
    public final static String PASSWORD_EXTRA = "password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void connectToServer(final View v) {
        TextView ipInput = (TextView) findViewById(R.id.ipEnter);
        TextView username = (TextView) findViewById(R.id.usernameEnter);
        TextView password = (TextView) findViewById(R.id.passwordEnter);
        if(!validateIP(ipInput.getText().toString())) {
            Toast.makeText(this, "Invalid IP Address", Toast.LENGTH_LONG)
                    .show();
        } else {
        }

        Intent intent = new Intent(this, coordinatesActivity.class);
        intent.putExtra(IP_EXTRA, ipInput.getText().toString());
        intent.putExtra(USERNAME_EXTRA, username.getText().toString());
        intent.putExtra(PASSWORD_EXTRA, password.getText().toString());
        startActivity(intent);
    }

    private boolean validateIP(String ipAddr) {
        return Patterns.IP_ADDRESS.matcher(ipAddr).matches();
    }
}
