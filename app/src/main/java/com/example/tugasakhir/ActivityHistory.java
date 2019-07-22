package com.example.tugasakhir;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.anwarshahriar.calligrapher.Calligrapher;

public class ActivityHistory extends AppCompatActivity {

    private ListView historyListView;
    private TextView historyTextView;
    private Bundle readDataFromActivityDeviceDetail;
    private String serialNumber, timeFromFirebase, ec, flow, intensity, location, ph, plant;
    private DatabaseReference historyReference;
    private int totalUsed;
    private ArrayList<String> timeFromFirebaseArrayList, ecArraylist, flowArrayList, intensityArrayList, locationArrayList, phArrayList, plantArrayList;
    private CustomHistoryListAdapterActivityHistory customHistoryListAdapterActivityHistory;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "PRODUCT_SANS.ttf", true);

        loading = new ProgressDialog(this);

        historyListView = (ListView) findViewById(R.id.historysListViewActivityHistory);
        historyTextView = (TextView) findViewById(R.id.historyTextViewActivityHistory);

        readDataFromActivityDeviceDetail = getIntent().getExtras();
        serialNumber = readDataFromActivityDeviceDetail.getString("serialNumber");

        historyReference = FirebaseDatabase.getInstance().getReference().child("Devices").child(serialNumber).child("history");

        timeFromFirebaseArrayList = new ArrayList<String>();
        ecArraylist = new ArrayList<String>();
        flowArrayList = new ArrayList<String>();
        intensityArrayList = new ArrayList<String>();
        locationArrayList = new ArrayList<String>();
        phArrayList = new ArrayList<String>();
        plantArrayList = new ArrayList<String>();

        readDevicesHistotyFromFirebase();
    }

    private void readDevicesHistotyFromFirebase(){

        setProgressDialog();

        totalUsed = 0;

        historyReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    timeFromFirebase = ds.getKey();
                    timeFromFirebaseArrayList.add(timeFromFirebase);

                    ec = ds.child("ec").getValue().toString();
                    flow = ds.child("flow").getValue().toString();
                    intensity = ds.child("intensity").getValue().toString();
                    location = ds.child("location").getValue().toString();
                    ph = ds.child("ph").getValue().toString();
                    plant = ds.child("plant").getValue().toString();

                    ecArraylist.add(ec);
                    flowArrayList.add(flow);
                    intensityArrayList.add(intensity);
                    locationArrayList.add(location);
                    phArrayList.add(ph);
                    plantArrayList.add(plant);

                    totalUsed++;
                }

                historyTextView.setText(getString(R.string.history_of) + " " + serialNumber + " ( " + totalUsed+ " )");

                populateHistoryListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void populateHistoryListView(){

        customHistoryListAdapterActivityHistory = new CustomHistoryListAdapterActivityHistory(this,
                timeFromFirebaseArrayList.toArray(new String[timeFromFirebaseArrayList.size()]),
                plantArrayList.toArray(new String[plantArrayList.size()]),
                locationArrayList.toArray(new String[locationArrayList.size()]),
                ecArraylist.toArray(new String[ecArraylist.size()]),
                phArrayList.toArray(new String[phArrayList.size()]),
                intensityArrayList.toArray(new String[intensityArrayList.size()]),
                flowArrayList.toArray(new String[flowArrayList.size()]));
        historyListView.setAdapter(customHistoryListAdapterActivityHistory);

        loading.dismiss();
    }

    private void setProgressDialog(){

        loading.setTitle(R.string.processing);
        loading.setMessage(getString(R.string.please_wait));
        loading.setCanceledOnTouchOutside(false);
        loading.show();
    }
}