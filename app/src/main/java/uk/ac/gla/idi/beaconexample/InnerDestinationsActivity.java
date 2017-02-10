package uk.ac.gla.idi.beaconexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class InnerDestinationsActivity extends Activity {

    String[] innerArray = {"Cowcaddens","St George's Cross","Kelvinbridge","Hillhead", "Kelvinhall",
            "Partick","Govan","Ibrox","Cessnock","Kinning Park","Shields Road","West Street", "Bridge Street", "St. Enoch"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_destination);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listdestinations, innerArray);

        ListView listView = (ListView) findViewById(R.id.inner_list);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
