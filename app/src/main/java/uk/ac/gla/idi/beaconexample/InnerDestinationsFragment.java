package uk.ac.gla.idi.beaconexample;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class InnerDestinationsFragment extends ListFragment implements AdapterView.OnItemClickListener {

    String[] innerArray = {"Cowcaddens","St George's Cross","Kelvinbridge","Hillhead", "Kelvinhall",
            "Partick","Govan","Ibrox","Cessnock","Kinning Park","Shields Road","West Street", "Bridge Street", "St. Enoch"};


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, innerArray);
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
