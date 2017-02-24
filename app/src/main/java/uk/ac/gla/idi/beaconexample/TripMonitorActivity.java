package uk.ac.gla.idi.beaconexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import uk.ac.gla.idi.beaconexample.R;


public class TripMonitorActivity extends AppCompatActivity {

    private TextView text, text2, text3, text4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_monitor);
        text = (TextView) findViewById(R.id.textView);
        text2 = (TextView) findViewById(R.id.textView2);
        text3 = (TextView) findViewById(R.id.textView3);
        text4 = (TextView) findViewById(R.id.textView4);
        text.setText("Departure Station: " + getIntent().getStringExtra("DEPARTURE_STATION"));
        text2.setText("Destined Station: " + getIntent().getStringExtra("DESTINATION_STATION"));
        // text3.setText("Direction: "+getIntent().getStringExtra("DIRECTION"));


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

    }
}
