package uk.ac.gla.idi.beaconexample;


import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ng.max.slideview.SlideView;


public class TripMonitorActivity extends AppCompatActivity {

    private static final String TAG = TripMonitorActivity.class.getSimpleName();
    private HashMap<String, String> beaconStationMap = new HashMap<>();
    private BluetoothAdapter bleDev;
    private int REQUEST_ENABLE_BT = 1;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 10000;
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private String DESTINATION_STATION;
    private String DEPARTURE_STATION;
    private String DIRECTION;
    private ArrayList<String> next_stops = new ArrayList<>();
    private String PREVIOUS_STOP;
    private int pos;
    private Ringtone r;
    private BeaconInfo selectedBeacon = null;
    private boolean isScanning = false;
    private BluetoothLeScanner scanner = null;
    private int scanMode = ScanSettings.SCAN_MODE_BALANCED;
    private Vibrator vibrator;
    private boolean WAKE_UP_FLAG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_monitor);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            DEPARTURE_STATION = bundle.getString("DEPARTURE_STATION", "");
            DESTINATION_STATION = bundle.getString("DESTINATION_STATION", "");
            DIRECTION = bundle.getString("DIRECTION", "");
            next_stops = bundle.getStringArrayList("NEXT_STOPS");
        }
        for (int i=0 ; i < next_stops.size() ; i++) {
            if (next_stops.get(i).toString().equals(DESTINATION_STATION)) {
                pos = i - 1;
                break;
            }
        }
        PREVIOUS_STOP = next_stops.get(pos);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE Not Supported",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bleDev = bluetoothManager.getAdapter();
        if (bleDev == null || !bleDev.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            toggleScan();
        }

        loadStationsBeaconsFromFile();

        TextView departure = (TextView) findViewById(R.id.textView2);
        TextView destination = (TextView) findViewById(R.id.textView6);
        departure.setText(DEPARTURE_STATION);
        destination.setText(DESTINATION_STATION);
        ((SlideView) findViewById(R.id.slideView)).setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {
                if (r != null) {
                    if (r.isPlaying()) {
                        r.stop();
                        vibrator.cancel();
                    }
                }
                Intent intent = new Intent(TripMonitorActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    blink();
    }

    private void blink(){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 500;
                try{Thread.sleep(timeToBlink);}catch (Exception e) {}
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView txt = (TextView) findViewById(R.id.textView2);
                        if(txt.getVisibility() == View.VISIBLE){
                            txt.setVisibility(View.INVISIBLE);
                        }else{
                            txt.setVisibility(View.VISIBLE);
                        }
                        blink();
                    }
                });
            }
        }).start();
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
        if ((requestCode == REQUEST_ENABLE_BT) && (resultCode == RESULT_OK)) {
            toggleScan();


            boolean isEnabling = bleDev.enable();
            if (!isEnabling) {
                // an immediate error occurred - perhaps the bluetooth is already on?
            } else if (bleDev.getState() == BluetoothAdapter.STATE_TURNING_ON) {
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
        if (!isScanning)
            startScan();
        else
            stopScan();
    }

    private void startScan() {
        if (scanner == null) {
            scanner = bleDev.getBluetoothLeScanner();
            if (scanner == null) {
                // probably tried to start a scan without granting Bluetooth permission
                Toast.makeText(this, "Failed to start scan (BT permission granted?)", Toast.LENGTH_LONG).show();
                Log.w(TAG, "Failed to get BLE scanner instance");
                return;
            }
        }

        List<ScanFilter> filters = new ArrayList<>();
        ScanSettings settings = new ScanSettings.Builder().setScanMode(scanMode).build();
        scanner.startScan(filters, settings, bleScanCallback);
        isScanning = true;
    }

    private void stopScan() {
        if (scanner != null && isScanning) {
            // Toast.makeText(this, "Stopping BLE scan...", Toast.LENGTH_SHORT).show();
            isScanning = false;
            Log.i(TAG, "Scan stopped");
            scanner.stopScan(bleScanCallback);
        }

        selectedBeacon = null;
    }



    private ScanCallback bleScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            String address = result.getDevice().toString();
            String station = beaconStationMap.get(address);
            if (station != null) {
                if (station.equals(DEPARTURE_STATION)) {
                    if (!WAKE_UP_FLAG) {
                        WAKE_UP_FLAG = true;
                        wakeup();
                    }
                }
            }
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

    public void wakeup() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(10000);
        stopScan();
        AlertDialog.Builder builder = new AlertDialog.Builder(TripMonitorActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        builder.setMessage("You must get off in the next station. The alarm will go off. Are you awake?")
                .setTitle("Wake up!")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        stopScan();
                        if (r != null) {
                            if (r.isPlaying()) {
                                r.stop();
                            }
                        }
                        if (vibrator != null) {
                            if (vibrator.hasVibrator()) {
                                vibrator.cancel();
                            }
                        }
                        WAKE_UP_FLAG = false;
//                        Intent intent = new Intent(TripMonitorActivity.this, MainActivity.class);
//                        startActivity(intent);
                    }
                })
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TripMonitorActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        builder.setMessage("Going back will terminate the current trip. Are you sure you want to proceed?")
                .setTitle("Cancel Trip?")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        stopScan();
                        if (r != null) {
                            if (r.isPlaying()) {
                                r.stop();
                            }
                        }
                        if (vibrator != null) {
                            if (vibrator.hasVibrator()) {
                                vibrator.cancel();
                            }
                        }
                        TripMonitorActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TripMonitorActivity.this.onResume();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();

    }


    private void loadStationsBeaconsFromFile() {
        BufferedReader reader;
        String record;
        String[] data;
        try {
            InputStream ins = getApplicationContext().getResources().getAssets().open("beacons");
            reader = new BufferedReader(new InputStreamReader(ins));
            record = reader.readLine();
            while(record != null) {
                data = record.split("=");
                beaconStationMap.put(data[0], data[1]);
                record = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}