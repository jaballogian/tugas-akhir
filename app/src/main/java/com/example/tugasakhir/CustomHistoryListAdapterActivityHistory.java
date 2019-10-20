package com.example.tugasakhir;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;

public class CustomHistoryListAdapterActivityHistory extends BaseAdapter {

    Activity activity;
    String[] time, plant, location, ec, ph, intensity, flow, containerVolume;
    String[] splitterMinus, splitterSlash;
    String year, month, day, clock;
    Integer[] image;
    LayoutInflater layoutInflater;

    public CustomHistoryListAdapterActivityHistory (Activity activity,
        String[] time, String[] plant, String[] location, String[] ec, String[] ph, String[] intensity, String[] flow, Integer[] image, String[] containerVolume){

        this.activity = activity;
        this.time = time;
        this.plant = plant;
        this.location = location;
        this.ec = ec;
        this.ph = ph;
        this.intensity = intensity;
        this.flow = flow;
        this.image = image;
        this.containerVolume = containerVolume;
    }

    @Override
    public int getCount() {
        return time.length;
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
        View view = layoutInflater.inflate(R.layout.custom_history_list_adapter_activity_history, null);

        Calligrapher calligrapher = new Calligrapher(activity);
        calligrapher.setFont(activity, "PRODUCT_SANS.ttf", true);

//        CircleImageView plantImageView = (CircleImageView) view.findViewById(R.id.plantCircleImageViewCustomHistoryListAdapterActivityHistory);
        ImageView plantImageView = (ImageView) view.findViewById(R.id.plantCircleImageViewCustomHistoryListAdapterActivityHistory);
        TextView timeTextView = (TextView) view.findViewById(R.id.timeTextViewCustomHistoryListAdapterActivityHistory);
        TextView locationTextView = (TextView) view.findViewById(R.id.locationTextViewCustomHistoryListAdapterActivityHistory);
        TextView plantTextView = (TextView) view.findViewById(R.id.plantTextViewCustomHistoryListAdapterActivityHistory);
        TextView ecTextView = (TextView) view.findViewById(R.id.ecTextViewCustomHistoryListAdapterActivityHistory);
        TextView phTextView = (TextView) view.findViewById(R.id.phTextViewCustomHistoryListAdapterActivityHistory);
        TextView intensityTextView = (TextView) view.findViewById(R.id.intensityTextViewCustomHistoryListAdapterActivityHistory);
        TextView flowTextView = (TextView) view.findViewById(R.id.flowTextViewCustomHistoryListAdapterActivityHistory);
        TextView containerVolumeTextView = (TextView) view.findViewById(R.id.containerVolumeTextViewCustomHistoryListAdapterActivityHistory);

        plantImageView.setImageResource(image[position]);

        timeTextView.setText(splitter(time[position]));
        locationTextView.setText(location[position]);
        plantTextView.setText(plant[position]);
        ecTextView.setText(ec[position]);
        phTextView.setText(ph[position]);
        intensityTextView.setText(intensity[position]);
        flowTextView.setText(flow[position]);
        containerVolumeTextView.setText(containerVolume[position]);

        return view;
    }

    private String splitter (String input){

        splitterMinus = input.split("-");
        splitterSlash = splitterMinus[0].split(":");

        day = splitterSlash[0];
        year = splitterSlash[2];
        clock = splitterMinus[1];

        switch (splitterSlash[1]){

            case "01":
                month = "Jan";
                break;
            case "02":
                month = "Feb";
                break;
            case "03":
                month = "Mar";
                break;
            case "04":
                month = "Apr";
                break;
            case "05":
                month = "May";
                break;
            case "06":
                month = "Jun";
                break;
            case "07":
                month = "Jul";
                break;
            case "08":
                month = "Aug";
                break;
            case "09":
                month = "Sep";
                break;
            case "10":
                month = "Oct";
                break;
            case "11":
                month = "Nov";
                break;
            case "12":
                month = "Dec";
                break;
        }

        String output = day + " " + month + " " + year + " at " + clock;

        return output;
    }
}
