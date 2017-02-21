package uk.ac.gla.idi.beaconexample;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Settings  extends PreferenceActivity {

    //This method is triggered when the user presses the "Settings" button.
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.layout.settings);
    }
}
