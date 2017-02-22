package uk.ac.gla.idi.beaconexample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
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
                    LayoutInflater inflater = SettingsActivity.this.getLayoutInflater();
                    View view= inflater.inflate(R.layout.dialog_stations, null);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(SettingsActivity.this);
                    dialog.setTitle(R.string.dialog_stops_before);
                    dialog.setMessage(R.string.dialog_stops_before_message);
                    dialog.setView(view);
                    NumberPicker np = (NumberPicker) view.findViewById(R.id.numberPicker);
                    np.setMaxValue(13);
                    np.setMinValue(0);
                    np.setWrapSelectorWheel(false);
                    dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //save the value to property file
                            dialog.dismiss();
                        }
                    });
                    dialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    return true;
                }
            });

    }
}
