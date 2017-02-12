package uk.ac.gla.idi.beaconexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SelectLineActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String DEPARTURE_STATION = null;
    private ArrayList<String> stations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_destination);
        loadStationsFromFile();
        DEPARTURE_STATION = getIntent().getStringExtra("DEPARTURE_STATION");
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        InnerDestinationsFragment inner = new InnerDestinationsFragment();
        OuterDestinationsFragment outer = new OuterDestinationsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("DEPARTURE_STATION", DEPARTURE_STATION);
        bundle.putStringArrayList("STATIONS", stations);
        inner.setArguments(bundle);
        outer.setArguments(bundle);
        adapter.addFragment(inner, getResources().getString(R.string.tab_inner));
        adapter.addFragment(outer, getResources().getString(R.string.tab_outer));
        viewPager.setAdapter(adapter);
    }

    private void loadStationsFromFile() {
        BufferedReader reader;
        String record;
        String[] data;
        InputStream ins = null;
        try {
            ins = getApplicationContext().getResources().getAssets().open("stations");
            reader = new BufferedReader(new InputStreamReader(ins));
            record = reader.readLine();
            while (record != null){
                stations.add(record);
                record = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
}
