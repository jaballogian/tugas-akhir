package com.example.tugasakhir;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomDeviceListAdapterActivityMain extends BaseAdapter {

    Activity activity;
    String[] deviceID, location, plant;
    LayoutInflater layoutInflater;

    public CustomDeviceListAdapterActivityMain (Activity activity, String[] deviceID, String[] location, String[] plant){

        this.activity = activity;
        this.deviceID = deviceID;
        this.location = location;
        this.plant = plant;
    }

    @Override
    public int getCount() {
        return deviceID.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        layoutInflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_device_list_adapter_activity_main, null);

        CircleImageView plantCircleImageView = (CircleImageView) view.findViewById(R.id.plantCircleImageViewCustomDeviceListAdapterActivityMain);
        TextView deviceIDTextView = (TextView) view.findViewById(R.id.deviceIDTextViewCustomDeviceListAdapterActivityMain);
        TextView plantTextView = (TextView) view.findViewById(R.id.plantTextViewCustomDeviceListAdapterActivityMain);
        TextView locationTextView = (TextView) view.findViewById(R.id.locationTextViewCustomDeviceListAdapterActivityMain);

        deviceIDTextView.setText(deviceID[position]);
        plantTextView.setText(plant[position]);
        locationTextView.setText(location[position]);

        return view;
    }
}
