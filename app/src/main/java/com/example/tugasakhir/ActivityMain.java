package com.example.tugasakhir;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.anwarshahriar.calligrapher.Calligrapher;

public class ActivityMain extends AppCompatActivity {

    private TextView registerDeviceTextView;
    private ListView devicesListView;
    private DatabaseReference devicesIDReference, allDevicesReference;
    private FirebaseUser currentUser;
    private String uID, allSerialNumber, serialNumberOwned, allLocation, allPlant;
    private ArrayList<String> serialNumberOwnedArrayList, allSerialNumberArrayList, allLocationArrayList, allPlantArrayList, locationOwnedArrayList, plantOwnedArrayList;
    private int i, j;
    private CustomDeviceListAdapterActivityMain customDeviceListAdapterActivityMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "PRODUCT_SANS.ttf", true);

        registerDeviceTextView = (TextView) findViewById(R.id.registerDeviceTextViewActivityMain);
        devicesListView = (ListView) findViewById(R.id.devicesListViewActivityMain);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uID = currentUser.getUid();

        devicesIDReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uID).child("Devices");
        allDevicesReference = FirebaseDatabase.getInstance().getReference().child("devices");

        serialNumberOwnedArrayList = new ArrayList<String>();
        allSerialNumberArrayList = new ArrayList<String>();
        allLocationArrayList = new ArrayList<String>();
        allPlantArrayList = new ArrayList<String>();
        locationOwnedArrayList = new ArrayList<String>();
        plantOwnedArrayList = new ArrayList<String>();

        checkingUser();

        readDevicesOwnedFromFirebase();

        registerDeviceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                moveToActivityRegisterDevice();
            }
        });
    }

    private void checkingUser(){

        if(FirebaseAuth.getInstance().getCurrentUser() == null){

            moveToActivityLogIn();
        }
    }

    private void moveToActivityLogIn(){

        Intent toActivityLogIn = new Intent(this, ActivityLogin.class);
        toActivityLogIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toActivityLogIn);
        finish();
    }

    private void moveToActivityRegisterDevice(){

        Intent toActivityRegisterDevice = new Intent(this, ActivityRegisterDevice.class);
        startActivity(toActivityRegisterDevice);
    }

    private void readDevicesOwnedFromFirebase(){

        devicesIDReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    serialNumberOwned = ds.getKey();
                    serialNumberOwnedArrayList.add(serialNumberOwned);
                }

                readAllDevicesFromFirebase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readAllDevicesFromFirebase(){

        allDevicesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    allSerialNumber = ds.getKey();
                    allLocation = ds.child("location").getValue().toString();
                    allPlant = ds.child("plant").getValue().toString();

                    allSerialNumberArrayList.add(allSerialNumber);
                    allLocationArrayList.add(allLocation);
                    allPlantArrayList.add(allPlant);
                }

                readParametersFromDevices();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readParametersFromDevices(){

        for(i = 0; i < serialNumberOwnedArrayList.size(); i++){

            for(j = 0; j < allSerialNumberArrayList.size(); j++){

                if(serialNumberOwnedArrayList.get(i).equals(allSerialNumberArrayList.get(j))){

                    locationOwnedArrayList.add(allLocationArrayList.get(j));
                    plantOwnedArrayList.add(allPlantArrayList.get(j));
                }
            }
        }

        populateDeviceListView();
    }

    private void populateDeviceListView(){

        customDeviceListAdapterActivityMain = new CustomDeviceListAdapterActivityMain(this,
                serialNumberOwnedArrayList.toArray(new String[serialNumberOwnedArrayList.size()]),
                locationOwnedArrayList.toArray(new String[locationOwnedArrayList.size()]),
                plantOwnedArrayList.toArray(new String[plantOwnedArrayList.size()]));
        devicesListView.setAdapter(customDeviceListAdapterActivityMain);
    }
}
