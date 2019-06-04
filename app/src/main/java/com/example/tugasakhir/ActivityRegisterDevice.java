package com.example.tugasakhir;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.anwarshahriar.calligrapher.Calligrapher;

public class ActivityRegisterDevice extends AppCompatActivity {

    private EditText serialNumberEditText, passwordEditText;
    private TextView dontHaveADeviceTextView;
    private Button registerButton;
    private String serialNumberFromUser, passwordFromUser, serialNumberFromFirebase, passwordFromFirebase;
    private DatabaseReference devicesReference;
    private ArrayList<String> serialNumberArrayList, passwordArrayList;
    private int i, j;
    private boolean isDeviceRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_device);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "PRODUCT_SANS.ttf", true);

        devicesReference = FirebaseDatabase.getInstance().getReference().child("devices").child("id");

        serialNumberEditText = (EditText) findViewById(R.id.serialNumberEditTextActivityRegisterDevice);
        passwordEditText = (EditText) findViewById(R.id.passwordEditTextActivityRegisterDevice);
        dontHaveADeviceTextView = (TextView) findViewById(R.id.dontHaveADeviceTextViewActivityRegisterDevice);
        registerButton = (Button) findViewById(R.id.registerButtonActivityRegisterDevice);

        readDevicesFromFirebase();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getStringFromEditText();
                checkAllFields();
            }
        });
    }

    private void getStringFromEditText(){

        serialNumberFromUser = serialNumberEditText.getText().toString();
        passwordFromUser = passwordEditText.getText().toString();
    }

    private void checkAllFields(){

        if(serialNumberFromUser.isEmpty() || passwordFromUser.isEmpty()){

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

                    serialNumberFromFirebase = ds.child("id").getValue().toString();
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
            }
        }

        if(isDeviceRegistered == false){

            Toast.makeText(ActivityRegisterDevice.this, getString(R.string.serial_number_is_invalid), Toast.LENGTH_LONG).show();
        }
        else {

            if(!passwordFromUser.equals(passwordArrayList.get(j))){

                Toast.makeText(ActivityRegisterDevice.this, getString(R.string.password_is_invalid), Toast.LENGTH_LONG).show();
            }
            else {

                moveToActivityMain();
                Toast.makeText(ActivityRegisterDevice.this, getString(R.string.welcome), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void moveToActivityMain(){

        Intent toActivityMain = new Intent(ActivityRegisterDevice.this, ActivityMain.class);
        toActivityMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toActivityMain);
        finish();
    }
}
