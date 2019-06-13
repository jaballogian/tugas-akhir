package com.example.tugasakhir;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;

public class ActivityDeviceDetail extends AppCompatActivity {

//    private CircleImageView plantCircleImageView;
    private ImageView plantCircleImageView;
    private TextView serialNumberTextView, plantTextView, locationTextView, statusTextView, yourEcTextView, yourPhTextView, yourFlowTextView, yourIntensityTextView;
    private TextView optimalEcTextView, optimalPhTextView, optimalIntensityTextView, optimalFlowTextView;
    private Bundle readDataFromActivityMain;
    private String serialNumber, location, plant, status, password;
    private double ec, flow, intensity, ph, minec, maxec, minph, maxph, minflow, maxflow, minint, maxint;
    private int image;
    private DatabaseReference selectedDeviceReference, optimalParameterReference;
    private ImageButton settingImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "PRODUCT_SANS.ttf", true);

        plantCircleImageView = (ImageView) findViewById(R.id.plantCircleImageViewActivityDeviceDetail);
        serialNumberTextView = (TextView) findViewById(R.id.serialNumberTextViewActivityDeviceDetail);
        plantTextView = (TextView) findViewById(R.id.plantTextViewActivityDeviceDetail);
        locationTextView = (TextView) findViewById(R.id.locationTextViewActivityDeviceDetail);
        statusTextView = (TextView) findViewById(R.id.statusTextViewActivityDeviceDetail);
        yourEcTextView = (TextView) findViewById(R.id.yourEcTextViewActivityDeviceDetail);
        yourPhTextView = (TextView) findViewById(R.id.yourPhTextViewActivityDeviceDetail);
        yourFlowTextView = (TextView) findViewById(R.id.yourFlowTextViewActivityDeviceDetail);
        yourIntensityTextView = (TextView) findViewById(R.id.yourIntensityTextViewActivityDeviceDetail);
        optimalEcTextView = (TextView) findViewById(R.id.optimalEcTextViewActivityDeviceDetail);
        optimalPhTextView = (TextView) findViewById(R.id.optimalPhTextViewActivityDeviceDetail);
        optimalIntensityTextView = (TextView) findViewById(R.id.optimalIntensityTextViewActivityDeviceDetail);
        optimalFlowTextView = (TextView) findViewById(R.id.optimalFlowTextViewActivityDeviceDetail);
        settingImageButton = (ImageButton) findViewById(R.id.settingImageButtonActivityDeviceDetail);

        readDataFromActivityMain = getIntent().getExtras();
        serialNumber = readDataFromActivityMain.getString("serialNumber");
        image = readDataFromActivityMain.getInt("image");

        plantCircleImageView.setImageResource(image);

        selectedDeviceReference = FirebaseDatabase.getInstance().getReference().child("Devices").child(serialNumber);

        readYourParametersFromDatabase();

        settingImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                moveToActivityDeviceSetting();
            }
        });
    }

    private void moveToActivityDeviceSetting(){

        Intent toActivityDeviceSetting = new Intent(ActivityDeviceDetail.this, ActivityDeviceSetting.class);
        toActivityDeviceSetting.putExtra("serialNumber", serialNumber);
        toActivityDeviceSetting.putExtra("image", image);
        toActivityDeviceSetting.putExtra("plant", plant);
        toActivityDeviceSetting.putExtra("location", location);
        toActivityDeviceSetting.putExtra("password", password);
        startActivity(toActivityDeviceSetting);
    }

    private void readYourParametersFromDatabase(){

        selectedDeviceReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ec = Double.valueOf(dataSnapshot.child("ec").getValue().toString());
                flow = Double.valueOf(dataSnapshot.child("flow").getValue().toString());
                intensity = Double.valueOf(dataSnapshot.child("intensity").getValue().toString());
                location = dataSnapshot.child("location").getValue().toString();
                ph = Double.valueOf(dataSnapshot.child("ph").getValue().toString());
                plant = dataSnapshot.child("plant").getValue().toString();
                status = dataSnapshot.child("status").getValue().toString();
                password = dataSnapshot.child("password").getValue().toString();

                readOptimalParametersFromDatabase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showYourData(){

        serialNumberTextView.setText(serialNumber);
        yourEcTextView.setText(String.valueOf(ec));
        yourFlowTextView.setText(String.valueOf(flow));
        yourIntensityTextView.setText(String.valueOf(intensity));
        locationTextView.setText(location);
        yourPhTextView.setText(String.valueOf(ph));
        plantTextView.setText(plant);
        statusTextView.setText(status);

        optimalEcTextView.setText(String.valueOf(minec) + " - " + String.valueOf(maxec));
        optimalPhTextView.setText(String.valueOf(minph) + " - " + String.valueOf(maxph));
        optimalIntensityTextView.setText(String.valueOf(minint) + " - " + String.valueOf(maxint));
        optimalFlowTextView.setText(String.valueOf(minflow) + " - " + String.valueOf(maxflow));

        setBackgroundColorTextView(yourEcTextView, ec, minec, maxec);
        setBackgroundColorTextView(yourPhTextView, ph, minph, maxph);
        setBackgroundColorTextView(yourFlowTextView, flow, minflow, maxflow);
        setBackgroundColorTextView(yourIntensityTextView, intensity, minint, maxint);
    }

    private void readOptimalParametersFromDatabase(){

        optimalParameterReference = FirebaseDatabase.getInstance().getReference().child("Optimal Parameters").child(plant);

        optimalParameterReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                minec = Double.valueOf(dataSnapshot.child("minec").getValue().toString());
                maxec = Double.valueOf(dataSnapshot.child("maxec").getValue().toString());
                minph = Double.valueOf(dataSnapshot.child("minph").getValue().toString());
                maxph = Double.valueOf(dataSnapshot.child("maxph").getValue().toString());
                minflow = Double.valueOf(dataSnapshot.child("minflow").getValue().toString());
                maxflow = Double.valueOf(dataSnapshot.child("maxflow").getValue().toString());
                minint = Double.valueOf(dataSnapshot.child("minint").getValue().toString());
                maxint = Double.valueOf(dataSnapshot.child("maxint").getValue().toString());

                showYourData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setBackgroundColorTextView(TextView inputTextView, double inputValue, double minValue, double maxValue){

        if(inputValue >= minValue && inputValue <= maxValue){

            inputTextView.setBackgroundColor(getResources().getColor(R.color.green_A700));
        }
        else {

            inputTextView.setBackgroundColor(getResources().getColor(R.color.red_A700));
        }
    }
}
