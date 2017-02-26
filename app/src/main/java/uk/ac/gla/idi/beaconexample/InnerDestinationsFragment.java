package uk.ac.gla.idi.beaconexample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class InnerDestinationsFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private ArrayList<String> stations = new ArrayList<>();
    private ArrayList<String> nextStops = new ArrayList<>();
    private String DEPARTURE_STATION = null;
    private String DESTINATION_STATION = null;
    private boolean selected=false;
    private int pos;



    @Override
    public void onResume(){
        super.onResume();
        selected=false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            DEPARTURE_STATION = bundle.getString("DEPARTURE_STATION", "");
            stations = bundle.getStringArrayList("STATIONS");
        }

        if (!stations.get(stations.size() - 1).equals(DEPARTURE_STATION)) {

        }
        int pos = stations.indexOf(DEPARTURE_STATION);
        for (int i=pos ; i >= 0 ; i--)
            nextStops.add(stations.get(i));
        for (int j=stations.size()-1 ; j > pos ; j--)
            nextStops.add(stations.get(j));

        setListAdapter(new CustomDepartureListAdapter(getContext(), nextStops));
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO implement some logic
        pos = position;
        if (position != 0) {
            while (selected == false) {
                //v.setBackgroundColor(Color.RED);
                selected = true;
                DESTINATION_STATION = getListAdapter().getItem(position).toString();
                Toast.makeText(getContext(), getListAdapter().getItem(position).toString() + " Is selected", Toast.LENGTH_LONG).show();
            }

            if (l.getCheckedItemPosition() == position) {
                v.setBackgroundColor(Color.RED);
            }


            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("From " + DEPARTURE_STATION + " to " + DESTINATION_STATION + "?")
                    .setTitle("Start trip")
                    .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getActivity(), TripMonitorActivity.class);
                            intent.putExtra("DEPARTURE_STATION", DEPARTURE_STATION);
                            intent.putExtra("DESTINATION_STATION", DESTINATION_STATION);
                            intent.putExtra("DIRECTION", "Inner");
                            intent.putExtra("POSITION", pos);
                            //intent.putStringArrayListExtra("NEXT_STOPS",nextStops);

                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            InnerDestinationsFragment.this.onResume();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            Toast.makeText(getContext(), "You are already there!!!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
