package uk.ac.gla.idi.beaconexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class OuterDestinationsActivity extends Activity {

    String[] outerArray = {"St. Enoch", "Bridge Street","West Street","Shields Road","Kinning Park","Cessnock","Ibrox","Govan",
            "Partick", "Kelvinhall","Hillhead","Kelvinbridge","St George's Cross" ,"Cowcaddens"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outer_destination);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listdestinations, outerArray);

        ListView listView = (ListView) findViewById(R.id.outer_list);
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
