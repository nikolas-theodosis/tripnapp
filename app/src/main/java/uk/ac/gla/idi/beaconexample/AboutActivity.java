package uk.ac.gla.idi.beaconexample;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;



public class AboutActivity extends AppCompatActivity {

    /**
     * This method is triggered when the user presses the "About" button.
     */
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
