package uk.ac.gla.idi.beaconexample;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Michalis on 24-Feb-17.
 */

public class AboutActivity extends Activity {

    /**
     * This method is triggered when the user presses the "About" button.
     */
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
