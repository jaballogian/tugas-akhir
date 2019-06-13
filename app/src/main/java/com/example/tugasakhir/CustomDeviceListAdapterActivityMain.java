package com.example.tugasakhir;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;

public class CustomDeviceListAdapterActivityMain extends BaseAdapter {

    Activity activity;
    String[] deviceID, location, plant, status, dates;
    String daysBetween;
    Integer[] image;
    LayoutInflater layoutInflater;

    public CustomDeviceListAdapterActivityMain (Activity activity, String[] deviceID, String[] location, String[] plant, String[] status, Integer[] image, String[] dates){

        this.activity = activity;
        this.deviceID = deviceID;
        this.location = location;
        this.plant = plant;
        this.status = status;
        this.image = image;
        this.dates = dates;
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

        Calligrapher calligrapher = new Calligrapher(activity);
        calligrapher.setFont(activity, "PRODUCT_SANS.ttf", true);

        CircleImageView plantCircleImageView = (CircleImageView) view.findViewById(R.id.plantCircleImageViewCustomDeviceListAdapterActivityMain);
        TextView deviceIDTextView = (TextView) view.findViewById(R.id.deviceIDTextViewCustomDeviceListAdapterActivityMain);
        TextView plantTextView = (TextView) view.findViewById(R.id.plantTextViewCustomDeviceListAdapterActivityMain);
        TextView locationTextView = (TextView) view.findViewById(R.id.locationTextViewCustomDeviceListAdapterActivityMain);
        TextView statusTextView = (TextView) view.findViewById(R.id.statusTextViewCustomDeviceListAdapterActivityMain);

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearLayoutCustomDeviceListAdapterActivityMain);
        TextView textView = (TextView) view.findViewById(R.id.textViewCustomDeviceListAdapterActivityMain);
        TextView timeTextView = (TextView) view.findViewById(R.id.timeTextViewCustomDeviceListAdapterActivityMain);
        ImageView timeImageView = (ImageView) view.findViewById(R.id.timeImageViewCustomDeviceListAdapterActivityMain);


        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
        Calendar calendar = Calendar.getInstance();
        String dateAfterString = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH) + " " + Integer.valueOf(calendar.get(Calendar.MONTH) + 1) + " " + calendar.get(Calendar.YEAR));
        String dateBeforeString = dates[position];

        try {
            Date dateBefore = myFormat.parse(dateBeforeString);
            Date dateAfter = myFormat.parse(dateAfterString);
            long difference = dateAfter.getTime() - dateBefore.getTime();
            daysBetween = String.valueOf((int) (difference / (1000*60*60*24)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        deviceIDTextView.setText(deviceID[position]);
        plantTextView.setText(plant[position]);
        locationTextView.setText(location[position]);
        statusTextView.setText(status[position]);
        plantCircleImageView.setImageResource(image[position]);
        timeTextView.setText(daysBetween);

        if(Integer.valueOf(daysBetween) == 0){

            timeTextView.setTextColor(activity.getResources().getColor(R.color.red_A700));
            timeImageView.setImageResource(R.drawable.ic_timelapse_red_24dp);
        }
        else if(Integer.valueOf(daysBetween) == 1){

            timeTextView.setTextColor(activity.getResources().getColor(R.color.orange_A700));
            timeImageView.setImageResource(R.drawable.ic_timelapse_orange_24dp);
        }

        return view;
    }
}
