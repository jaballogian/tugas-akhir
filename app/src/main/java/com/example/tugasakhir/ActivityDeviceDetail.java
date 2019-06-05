package com.example.tugasakhir;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityDeviceDetail extends AppCompatActivity {

    private CircleImageView plantCircleImageView;
    private TextView serialNumberTextView, plantTextView, locationTextView, statusTextView, ecTextView, phTextView, flowTextView, intensityTextView;
    private Bundle readDataFromActivityMain;
    private String serialNumber, ec, flow, intensity, location, ph, plant, status;
    private DatabaseReference selectedDeviceReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);

        plantCircleImageView = (CircleImageView) findViewById(R.id.plantCircleImageViewActivityDeviceDetail);
        serialNumberTextView = (TextView) findViewById(R.id.serialNumberTextViewActivityDeviceDetail);
        plantTextView = (TextView) findViewById(R.id.plantTextViewActivityDeviceDetail);
        locationTextView = (TextView) findViewById(R.id.locationTextViewActivityDeviceDetail);
        statusTextView = (TextView) findViewById(R.id.statusTextViewActivityDeviceDetail);
        ecTextView = (TextView) findViewById(R.id.ecTextViewActivityDeviceDetail);
        phTextView = (TextView) findViewById(R.id.phTextViewActivityDeviceDetail);
        flowTextView = (TextView) findViewById(R.id.flowTextViewActivityDeviceDetail);
        intensityTextView = (TextView) findViewById(R.id.intensityTextViewActivityDeviceDetail);

        readDataFromActivityMain = getIntent().getExtras();
        serialNumber = readDataFromActivityMain.getString("serialNumber");

        selectedDeviceReference = FirebaseDatabase.getInstance().getReference().child("Devices").child(serialNumber);

        readParametersFromDatabase();
    }

    private void readParametersFromDatabase(){

        selectedDeviceReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ec = dataSnapshot.child("ec").getValue().toString();
                flow = dataSnapshot.child("flow").getValue().toString();
                intensity = dataSnapshot.child("intensity").getValue().toString();
                location = dataSnapshot.child("location").getValue().toString();
                ph = dataSnapshot.child("ph").getValue().toString();
                plant = dataSnapshot.child("plant").getValue().toString();
                status = dataSnapshot.child("status").getValue().toString();

                showData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showData(){

        serialNumberTextView.setText(serialNumber);
        ecTextView.setText(ec);
        flowTextView.setText(flow);
        intensityTextView.setText(intensity);
        locationTextView.setText(location);
        phTextView.setText(ph);
        plantTextView.setText(plant);
        statusTextView.setText(status);
    }
}
