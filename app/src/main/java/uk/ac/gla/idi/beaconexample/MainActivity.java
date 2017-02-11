package uk.ac.gla.idi.beaconexample;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity{

    private BluetoothAdapter bleDev = null;
    // request ID for enabling Bluetooth
    private static final int REQUEST_ENABLE_BT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton toggleScan = (FloatingActionButton) findViewById(R.id.btnStartTrip);
        toggleScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                bleDev = bluetoothManager.getAdapter();
                if (bleDev == null || !bleDev.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQUEST_ENABLE_BT) && (resultCode == RESULT_OK))
        {
            Intent intent = new Intent(MainActivity.this, SelectLineActivity.class);
            startActivity(intent);
            boolean isEnabling = bleDev.enable();
            if (!isEnabling)
            {
                // an immediate error occurred - perhaps the bluetooth is already on?
            }
            else if (bleDev.getState() == BluetoothAdapter.STATE_TURNING_ON)
            {
                // the system, in the background, is trying to turn the Bluetooth on
                // while your activity carries on going without waiting for it to finish;
                // of course, you could listen for it to finish yourself - eg, using a
                // ProgressDialog that checked mBluetoothAdapter.getState() every x
                // milliseconds and reported when it became STATE_ON (or STATE_OFF, if the
                // system failed to start the Bluetooth.)
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

}
