package uk.ac.gla.idi.beaconexample;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class MainActivity extends AppCompatActivity{

    private static final String TAG = SelectDestinationActivity.class.getSimpleName();
    private BluetoothAdapter bleDev = null;
    // request ID for enabling Bluetooth
    private static final int REQUEST_ENABLE_BT = 1000;
    private boolean isScanning = false;
    private BluetoothLeScanner scanner = null;
    private int scanMode = ScanSettings.SCAN_MODE_BALANCED;
    // currently selected beacon, if any
    private BeaconInfo selectedBeacon = null;
    private HashMap<String, String> beaconStationMap = new HashMap<>();
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadStationsBeaconsFromFile();
        FloatingActionButton toggleScan = (FloatingActionButton) findViewById(R.id.btnStartTrip);
        toggleScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                bleDev = bluetoothManager.getAdapter();
                if (bleDev == null || !bleDev.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
                else {
                    toggleScan();
                }
            }
        });
    }

    /*
    saves the mac address and the corresponding station from
    the txt raw file to the hashmap where key = mac address and
    value = station name
     */
    private void loadStationsBeaconsFromFile() {
        BufferedReader reader;
        String record;
        String[] data;
        try {
            InputStream ins = getApplicationContext().getResources().getAssets().open("beacons");
            reader = new BufferedReader(new InputStreamReader(ins));
            record = reader.readLine();
            data = record.split("=");
            beaconStationMap.put(data[0], data[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, Settings.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        //return false;
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
            toggleScan();

          
            
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

    private void toggleScan() {
        if(!isScanning)
            startScan();
        else
            stopScan();
    }

    private void startScan() {
        if(scanner == null) {
            scanner = bleDev.getBluetoothLeScanner();
            if(scanner == null) {
                // probably tried to start a scan without granting Bluetooth permission
                Toast.makeText(this, "Failed to start scan (BT permission granted?)", Toast.LENGTH_LONG).show();
                Log.w(TAG, "Failed to get BLE scanner instance");
                return;
            }
        }

        progress = new ProgressDialog(this);
        progress.setTitle(getResources().getString(R.string.progress_dialog_title));
        progress.setMessage(getResources().getString(R.string.progress_dialog_message));
        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
        progress.show();

        List<ScanFilter> filters = new ArrayList<>();
        ScanSettings settings = new ScanSettings.Builder().setScanMode(scanMode).build();
        scanner.startScan(filters, settings, bleScanCallback);
        isScanning = true;
    }

    private void stopScan() {
        if(scanner != null && isScanning) {
           // Toast.makeText(this, "Stopping BLE scan...", Toast.LENGTH_SHORT).show();
            isScanning = false;
            Log.i(TAG, "Scan stopped");
            scanner.stopScan(bleScanCallback);
        }

        selectedBeacon = null;
    }

    // class implementing BleScanner callbacks
    private ScanCallback bleScanCallback = new ScanCallback() {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            final BluetoothDevice dev = result.getDevice();
            final int rssi = result.getRssi();

            if(dev != null && isScanning) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // retrieve device info and add to or update existing set of beacon data
                        String address = dev.getAddress();
                        String station = beaconStationMap.get(address);

                        if (station != null) {
                            stopScan();
                            progress.dismiss();
                            Intent intent = new Intent(MainActivity.this, SelectLineActivity.class);
                            intent.putExtra("DEPARTURE_STATION", station);
                            startActivity(intent);
                        }
                    }

                });
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.d(TAG, "BatchScanResult(" + results.size() + " results)");
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.w(TAG, "ScanFailed(" + errorCode + ")");
        }
    };

}
