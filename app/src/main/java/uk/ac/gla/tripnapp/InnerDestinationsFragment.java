package uk.ac.gla.tripnapp;

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
        pos = position;
        if (position != 0) {
            while (selected == false) {
                selected = true;
                DESTINATION_STATION = getListAdapter().getItem(position).toString();
            }

            if (l.getCheckedItemPosition() == position) {
                v.setBackgroundColor(Color.RED);
            }


            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
            builder.setMessage("From " + DEPARTURE_STATION + " to " + DESTINATION_STATION + "?")
                    .setTitle("Start trip")
                    .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getActivity(), TripMonitorActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("DEPARTURE_STATION", DEPARTURE_STATION);
                            bundle.putString("DESTINATION_STATION", DESTINATION_STATION);
                            bundle.putString("DIRECTION", "Inner");
                            bundle.putString("POSITION", "pos");
                            bundle.putStringArrayList("NEXT_STOPS", nextStops);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    })
                    .setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            InnerDestinationsFragment.this.onResume();
                        }
                    })
                    .setIcon(android.R.drawable.ic_media_play);
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
