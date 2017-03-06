package uk.ac.gla.tripnapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomDepartureListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final ArrayList<String> values;

    public CustomDepartureListAdapter(Context context, ArrayList<String> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.destination_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.row);
        textView.setText(values.get(position));
        if (position == 0)
            rowView.setBackgroundColor(Color.GREEN);
        return rowView;
    }
}
