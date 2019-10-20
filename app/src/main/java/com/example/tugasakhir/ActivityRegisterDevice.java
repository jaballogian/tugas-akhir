package com.example.tugasakhir;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import me.anwarshahriar.calligrapher.Calligrapher;

public class ActivityRegisterDevice extends AppCompatActivity {

    private EditText serialNumberEditText, passwordEditText, locationEditText, volumeEditText;
    private TextView dontHaveADeviceTextView;
    private Button registerButton;
    private String serialNumberFromUser, passwordFromUser, locationFromUser, serialNumberFromFirebase, passwordFromFirebase, uID, selectedPlant, plantFromFirebase, initialVolume;
    private DatabaseReference devicesReference, databaseReference, plantReference;
    private ArrayList<String> serialNumberArrayList, passwordArrayList, plantArrayList;
    private ArrayAdapter<String> plantAdapter;
    private int i, j;
    private boolean isDeviceRegistered;
    private FirebaseUser currentUser;
    private SpannableString spannableString;
    private SpannableStringBuilder spannableStringBuilder;
    private ForegroundColorSpan foregroundColorSpanGrey, foregroundColorSpanRed;
    private Spinner selectPlantSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_device);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "PRODUCT_SANS.ttf", true);

        devicesReference = FirebaseDatabase.getInstance().getReference().child("Devices");
        plantReference = FirebaseDatabase.getInstance().getReference().child("Optimal Parameters");

        serialNumberEditText = (EditText) findViewById(R.id.serialNumberEditTextActivityRegisterDevice);
        passwordEditText = (EditText) findViewById(R.id.passwordEditTextActivityRegisterDevice);
        dontHaveADeviceTextView = (TextView) findViewById(R.id.dontHaveADeviceTextViewActivityRegisterDevice);
        registerButton = (Button) findViewById(R.id.registerButtonActivityRegisterDevice);
        selectPlantSpinner = (Spinner) findViewById(R.id.selectPlantSpinnerActivityRegister);
        locationEditText = (EditText) findViewById(R.id.locationEditTextActivityRegisterDevice);
        volumeEditText = (EditText) findViewById(R.id.volumeEditTextActivityRegisterDevice);

        spannableString = new SpannableString(getString(R.string.dont_have_a_device_buy_here));
        spannableStringBuilder = new SpannableStringBuilder(getString(R.string.dont_have_a_device_buy_here));

        foregroundColorSpanGrey = new ForegroundColorSpan(getResources().getColor(R.color.grey_700));
        foregroundColorSpanRed = new ForegroundColorSpan(getResources().getColor(R.color.red_A700));

        spannableStringBuilder.setSpan(foregroundColorSpanGrey, 1, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(foregroundColorSpanRed, 21, 29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        dontHaveADeviceTextView.setText(spannableStringBuilder);

        readAllPlantsFromFirebase();
        readDevicesFromFirebase();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getStringFromEditText();
                checkAllFields();
            }
        });
    }

    private void readAllPlantsFromFirebase(){

        plantReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                plantArrayList = new ArrayList<String>();
                plantArrayList.add(getString(R.string.select_plant));

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    plantFromFirebase = ds.getKey();

                    plantArrayList.add(plantFromFirebase);
                }

                populatePlantsToSpinner();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void populatePlantsToSpinner(){

        plantAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, plantArrayList);
        plantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectPlantSpinner.setAdapter(plantAdapter);

        selectPlantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedPlant = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // TODO Auto-generated method stub
            }
        });
    }

    private void getStringFromEditText(){

        serialNumberFromUser = serialNumberEditText.getText().toString();
        passwordFromUser = passwordEditText.getText().toString();
        locationFromUser = locationEditText.getText().toString();
        initialVolume = volumeEditText.getText().toString();

    }

    private void checkAllFields(){

        if(serialNumberFromUser.isEmpty() || passwordFromUser.isEmpty() || selectedPlant.isEmpty() || locationFromUser.isEmpty() || initialVolume.isEmpty()){

            Toast.makeText(ActivityRegisterDevice.this, getString(R.string.please_fill_all_the_fields), Toast.LENGTH_LONG).show();
        }
        else {

            checkInputFromUser();
        }
    }

    private void readDevicesFromFirebase(){

        devicesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                serialNumberArrayList = new ArrayList<String>();
                passwordArrayList = new ArrayList<String>();

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    serialNumberFromFirebase = ds.getKey();
                    passwordFromFirebase = ds.child("password").getValue().toString();

                    serialNumberArrayList.add(serialNumberFromFirebase);
                    passwordArrayList.add(passwordFromFirebase);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkInputFromUser(){

        isDeviceRegistered = false;

        for (i = 0; i < serialNumberArrayList.size(); i++){

            if(serialNumberFromUser.equals(serialNumberArrayList.get(i))){

                isDeviceRegistered = true;
                j = i;
            }
        }

        if(isDeviceRegistered == false){

            Toast.makeText(ActivityRegisterDevice.this, getString(R.string.serial_number_is_invalid), Toast.LENGTH_LONG).show();
        }
        else {

            if(selectedPlant.equals(plantArrayList.get(0))){

                Toast.makeText(ActivityRegisterDevice.this, getString(R.string.please_select_a_valid_plant), Toast.LENGTH_LONG).show();
            }
            else {

                if(!passwordFromUser.equals(passwordArrayList.get(j))){

                    Toast.makeText(ActivityRegisterDevice.this, getString(R.string.password_is_invalid), Toast.LENGTH_LONG).show();
                }
                else {

                    saveDeviceToDatabase();
                }
            }
        }
    }

    private void moveToActivityMain(){

        Intent toActivityMain = new Intent(ActivityRegisterDevice.this, ActivityMain.class);
        toActivityMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toActivityMain);
        finish();
    }

    private void saveDeviceToDatabase(){

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uID = currentUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uID).child("Devices").child(serialNumberFromUser);

        devicesReference.child(serialNumberFromUser).child("plant").setValue(selectedPlant);
        devicesReference.child(serialNumberFromUser).child("location").setValue(locationFromUser);
        devicesReference.child(serialNumberFromUser).child("containerVolume").setValue(initialVolume);

        databaseReference.setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    moveToActivityMain();
                    Toast.makeText(ActivityRegisterDevice.this, getString(R.string.welcome), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
