package uk.ac.gla.idi.beaconexample;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class SettingsActivity  extends PreferenceActivity {

    //This method is triggered when the user presses the "Settings" button.
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.layout.settings);

            Preference myPref = findPreference("pref_number_of_stations");
            myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Toast.makeText(getApplicationContext(), "A", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

    }
}
