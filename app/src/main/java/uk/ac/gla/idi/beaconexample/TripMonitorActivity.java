package uk.ac.gla.idi.beaconexample;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TripMonitorActivity extends AppCompatActivity {

    private TextView text, text2, text3, text4;
    private static final String TAG = "MyActivity";
    private HashMap<String, String> beaconStationMap = new HashMap<>();
    private BluetoothAdapter bleAdapter;
    private int REQUEST_ENABLE_BT = 1;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 10000;
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private String DESTINATION_STATION;
    private String DEPARTURE_STATION;
    private String DIRECTION;
    private ArrayList<String> next_stops = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_monitor);
        text = (TextView) findViewById(R.id.textView);
        text2 = (TextView) findViewById(R.id.textView2);
        text3 = (TextView) findViewById(R.id.textView3);
        text4 = (TextView) findViewById(R.id.textView4);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            DEPARTURE_STATION = bundle.getString("DEPARTURE_STATION", "");
            DESTINATION_STATION = bundle.getString("DESTINATION_STATION", "");
            DIRECTION = bundle.getString("DIRECTION", "");
            next_stops = bundle.getStringArrayList("STATIONS");
        }

        text.setText("Departure Station: " + DEPARTURE_STATION);
        text2.setText("Destined Station: " + DESTINATION_STATION);
        text3.setText("Direction: " + DIRECTION);


        mHandler = new Handler();
        //Check whether the phone supports BLE.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE Not Supported",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bleAdapter = bluetoothManager.getAdapter();
        loadStationsBeaconsFromFile();



    }
    @Override
    protected void onResume() {
        super.onResume();
        if (bleAdapter == null || !bleAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                mLEScanner = bleAdapter.getBluetoothLeScanner();
                settings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                       // .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
                        .build();

            }
            scanLeDevice(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bleAdapter != null && bleAdapter.isEnabled()) {
            scanLeDevice(false);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                //Bluetooth not enabled.
                finish();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable)
            mLEScanner.startScan(mScanCallback);
        else
            mLEScanner.stopScan(mScanCallback);
    }

//    private void scanLeDevice(final boolean enable) {
//        if (enable) {
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                        mLEScanner.stopScan(mScanCallback);
//                }
//            }, SCAN_PERIOD);
//
//               // mLEScanner.startScan(filters, settings, mScanCallback);
//           // mLEScanner.startScan(filters, settings, mScanCallback);
//            mLEScanner.startScan(mScanCallback);
//
//        }
//        else {
//
//                mLEScanner.stopScan(mScanCallback);
//
//        }
//    }


    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.i("callbackType", String.valueOf(callbackType));
            Log.i("result", result.toString());
            //text4.setText("result "+ result.toString());

            String address = result.getDevice().toString();
            String station = beaconStationMap.get(address);
            text4.setText("Current Station "+ station);

        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult sr : results) {
                Log.i("ScanResult - Results", sr.toString());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("Scan Failed", "Error Code: " + errorCode);
        }
    };

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("onLeScan", device.toString());

                        }
                    });
                }
            };


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

}