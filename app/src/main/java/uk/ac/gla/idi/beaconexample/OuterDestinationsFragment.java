package uk.ac.gla.idi.beaconexample;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class OuterDestinationsFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private ArrayList<String> stations = new ArrayList<>();
    private ArrayList<String> nextStops = new ArrayList<>();
    private String DEPARTURE_STATION = null;
    private String DESTINATION_STATION = null;
    private boolean selected=false;


    @Override
    public void onResume(){
        super.onResume();
        selected=false;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        //selected=false;
        if (bundle != null) {
            DEPARTURE_STATION = bundle.getString("DEPARTURE_STATION", "");
            stations = bundle.getStringArrayList("STATIONS");
        }

        if (!stations.get(stations.size() - 1).equals(DEPARTURE_STATION)) {

        }
        int pos = stations.indexOf(DEPARTURE_STATION);
        for (int i=pos ; i < stations.size() ; i++)
            nextStops.add(stations.get(i));
        for (int j=0 ; j < pos ; j++)
            nextStops.add(stations.get(j));

        setListAdapter(new CustomDepartureListAdapter(getContext(), nextStops));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO implement some logic

        while (selected==false){
            v.setBackgroundColor(Color.RED);
            selected=true;
            DESTINATION_STATION=getListAdapter().getItem(position).toString();
            Toast.makeText(getContext(),getListAdapter().getItem(position).toString()+" Is selected",Toast.LENGTH_LONG).show();
        }
        if (l.getCheckedItemPosition() == position) {
            v.setBackgroundColor(Color.RED);
        }


       if (position != 0) {

            Intent intent = new Intent(getActivity(), TripMonitorActivity.class);
            intent.putExtra("DEPARTURE_STATION",DEPARTURE_STATION);
            intent.putExtra("DESTINATION_STATION",DESTINATION_STATION);
            intent.putExtra("DIRECTION","Outer");
            intent.putExtra("POSITION",position);
            //intent.putStringArrayListExtra("NEXT_STOPS",nextStops);


            startActivity(intent);
       }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
