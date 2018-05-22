package com.trafficticketbuddy.client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.trafficticketbuddy.client.R;
import com.trafficticketbuddy.client.model.StateNameResult;
import com.trafficticketbuddy.client.model.country.Response;

import java.util.List;

public class CountryBaseAdapter extends BaseAdapter {
    private Context context; //context
    private List<Response> items; //data source of the list adapter

    //public constructor
    public CountryBaseAdapter(Context context, List<Response> response) {
        this.context = context;
        this.items = response;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.row_state_items, parent, false);
        }

        TextView textViewItemName = (TextView)
                convertView.findViewById(R.id.textViewName);

        textViewItemName.setText(items.get(position).getCountryName());
        // returns the view for the current row
        return convertView;
    }
}
