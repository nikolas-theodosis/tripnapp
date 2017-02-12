package uk.ac.gla.idi.beaconexample;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class OuterDestinationsFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private ArrayList<String> stations = new ArrayList<>();
    private ArrayList<String> nextStops = new ArrayList<>();
    private String DEPARTURE_STATION = null;

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
        for (int i=pos ; i < stations.size() ; i++)
            nextStops.add(stations.get(i));
        for (int j=0 ; j < pos ; j++)
            nextStops.add(stations.get(j));


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, nextStops);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO implement some logic
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
