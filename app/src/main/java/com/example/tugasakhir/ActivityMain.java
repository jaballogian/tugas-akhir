package com.example.tugasakhir;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

    private ListView devicesListView;
    private DatabaseReference devicesIDReference, allDevicesReference, allRegisteredPlantsReference;
    private FirebaseUser currentUser;
    private String uID, allSerialNumber, serialNumberOwned, allLocation, allPlant, allStatus, allRegisteredPlants;
    private ArrayList<String> serialNumberOwnedArrayList, allSerialNumberArrayList, allLocationArrayList, allPlantArrayList, allStatusArrayList, allRegisteredPlantsArrayList;
    private ArrayList<String> locationOwnedArrayList, plantOwnedArrayList, statusOwnedArrayList;
    private int i, j, totalDevices;
    private CustomDeviceListAdapterActivityMain customDeviceListAdapterActivityMain;
    private ArrayList<Integer> allImagePlantsArrayList, imageOwnedPlantArrayList;
    private ImageButton profileImageButton;
    private TextView yourDevicesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "PRODUCT_SANS.ttf", true);

        devicesListView = (ListView) findViewById(R.id.devicesListViewActivityMain);
        profileImageButton = (ImageButton) findViewById(R.id.profileImageButtonActivityMain);
        yourDevicesTextView = (TextView) findViewById(R.id.yourDevicesTextViewActivityMain);

        checkingUser();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uID = currentUser.getUid();

        devicesIDReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uID).child("Devices");
        allDevicesReference = FirebaseDatabase.getInstance().getReference().child("Devices");
        allRegisteredPlantsReference = FirebaseDatabase.getInstance().getReference().child("Optimal Parameters");

        serialNumberOwnedArrayList = new ArrayList<String>();
        allSerialNumberArrayList = new ArrayList<String>();
        allLocationArrayList = new ArrayList<String>();
        allPlantArrayList = new ArrayList<String>();
        locationOwnedArrayList = new ArrayList<String>();
        plantOwnedArrayList = new ArrayList<String>();
        allStatusArrayList = new ArrayList<String>();
        statusOwnedArrayList = new ArrayList<String>();
        allRegisteredPlantsArrayList = new ArrayList<String>();
        allImagePlantsArrayList = new ArrayList<Integer>();
        imageOwnedPlantArrayList = new ArrayList<Integer>();

        insertAllImagesToArray();

        readDevicesOwnedFromFirebase();

        devicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent toActivityDeviceDetail = new Intent(ActivityMain.this, ActivityDeviceDetail.class);
                toActivityDeviceDetail.putExtra("serialNumber", serialNumberOwnedArrayList.get(position));
                toActivityDeviceDetail.putExtra("image", imageOwnedPlantArrayList.get(position));
                startActivity(toActivityDeviceDetail);
            }
        });

        profileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toActivityProfile = new Intent(ActivityMain.this, ActivityProfile.class);
                startActivity(toActivityProfile);
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

    private void readDevicesOwnedFromFirebase(){

        totalDevices = 0;

        devicesIDReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    serialNumberOwned = ds.getKey();
                    serialNumberOwnedArrayList.add(serialNumberOwned);

                    totalDevices++;
                }

                yourDevicesTextView.setText(getString(R.string.your_devices) + " (" + totalDevices + ")");
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
                    allStatus = ds.child("status").getValue().toString();

                    allSerialNumberArrayList.add(allSerialNumber);
                    allLocationArrayList.add(allLocation);
                    allPlantArrayList.add(allPlant);
                    allStatusArrayList.add(allStatus);
                }

                readAllRegisteredPlantsFromFirebase();
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
                    statusOwnedArrayList.add(allStatusArrayList.get(j));
                }
            }
        }

        compareOwnedAndAllRegisteredPlants();
    }

    private void populateDeviceListView(){

        customDeviceListAdapterActivityMain = new CustomDeviceListAdapterActivityMain(this,
                serialNumberOwnedArrayList.toArray(new String[serialNumberOwnedArrayList.size()]),
                locationOwnedArrayList.toArray(new String[locationOwnedArrayList.size()]),
                plantOwnedArrayList.toArray(new String[plantOwnedArrayList.size()]),
                statusOwnedArrayList.toArray(new String[statusOwnedArrayList.size()]),
                imageOwnedPlantArrayList.toArray(new Integer[imageOwnedPlantArrayList.size()]));
        devicesListView.setAdapter(customDeviceListAdapterActivityMain);
    }

    private void compareOwnedAndAllRegisteredPlants(){

        for(i = 0; i < plantOwnedArrayList.size(); i++){

            for (j = 0; j < allRegisteredPlantsArrayList.size(); j++){

                if(plantOwnedArrayList.get(i).equals(allRegisteredPlantsArrayList.get(j))){

                    imageOwnedPlantArrayList.add(allImagePlantsArrayList.get(j));
                }
            }
        }

        populateDeviceListView();
    }

    private void readAllRegisteredPlantsFromFirebase(){

        allRegisteredPlantsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    allRegisteredPlants = ds.getKey();
                    allRegisteredPlantsArrayList.add(allRegisteredPlants);
                }

                readParametersFromDevices();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void insertAllImagesToArray(){

        allImagePlantsArrayList.add(R.drawable.plant_artichoke);
        allImagePlantsArrayList.add(R.drawable.plant_asparagus);
        allImagePlantsArrayList.add(R.drawable.plant_beans);
        allImagePlantsArrayList.add(R.drawable.plant_beetroot);
        allImagePlantsArrayList.add(R.drawable.plant_broccoli);
        allImagePlantsArrayList.add(R.drawable.plant_cabbage);
        allImagePlantsArrayList.add(R.drawable.plant_capsicum);
        allImagePlantsArrayList.add(R.drawable.plant_carrot);
        allImagePlantsArrayList.add(R.drawable.plant_cauliflower);
        allImagePlantsArrayList.add(R.drawable.plant_celeriac);
        allImagePlantsArrayList.add(R.drawable.plant_celery);
        allImagePlantsArrayList.add(R.drawable.plant_chili);
        allImagePlantsArrayList.add(R.drawable.plant_cucumber);
        allImagePlantsArrayList.add(R.drawable.plant_eggplant);
        allImagePlantsArrayList.add(R.drawable.plant_garlic);
        allImagePlantsArrayList.add(R.drawable.plant_gia_lan);
        allImagePlantsArrayList.add(R.drawable.plant_ginger);
        allImagePlantsArrayList.add(R.drawable.plant_kale);
        allImagePlantsArrayList.add(R.drawable.plant_leek);
        allImagePlantsArrayList.add(R.drawable.plant_lettuce);
        allImagePlantsArrayList.add(R.drawable.plant_onion);
        allImagePlantsArrayList.add(R.drawable.plant_pak_or_bok_choy);
        allImagePlantsArrayList.add(R.drawable.plant_potato);
        allImagePlantsArrayList.add(R.drawable.plant_pumpkin);
        allImagePlantsArrayList.add(R.drawable.plant_radish_red);
        allImagePlantsArrayList.add(R.drawable.plant_radish_white);
        allImagePlantsArrayList.add(R.drawable.plant_shallot);
        allImagePlantsArrayList.add(R.drawable.plant_silverbeet);
        allImagePlantsArrayList.add(R.drawable.plant_snow_peas);
        allImagePlantsArrayList.add(R.drawable.plant_spinach);
        allImagePlantsArrayList.add(R.drawable.plant_sugar_snap_peas);
        allImagePlantsArrayList.add(R.drawable.plant_sweet_corn);
        allImagePlantsArrayList.add(R.drawable.plant_sweet_potato);
        allImagePlantsArrayList.add(R.drawable.plant_tomato);
        allImagePlantsArrayList.add(R.drawable.plant_turnip);
        allImagePlantsArrayList.add(R.drawable.plant_wombok);
        allImagePlantsArrayList.add(R.drawable.plant_zucchini);
    }
}
