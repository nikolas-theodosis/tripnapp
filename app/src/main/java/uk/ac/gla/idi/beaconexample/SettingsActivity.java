package uk.ac.gla.idi.beaconexample;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
                    Spinner spinner = (Spinner) findViewById(R.id.alarm_spinner);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                            R.array.number_of_stations_array, android.R.layout.simple_spinner_item);
                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    spinner.setAdapter(adapter);

                    /*Toast.makeText(getApplicationContext(), "A", Toast.LENGTH_SHORT).show();*/
                    return true;
                }
            });

    }
}
