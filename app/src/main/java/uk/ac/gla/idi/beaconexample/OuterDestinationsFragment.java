package uk.ac.gla.idi.beaconexample;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class OuterDestinationsFragment extends ListFragment implements AdapterView.OnItemClickListener {

    String[] outerArray = {"St. Enoch", "Bridge Street","West Street","Shields Road","Kinning Park","Cessnock","Ibrox","Govan",
                        "Partick", "Kelvinhall","Hillhead","Kelvinbridge","St George's Cross" ,"Cowcaddens"};



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, outerArray);
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
