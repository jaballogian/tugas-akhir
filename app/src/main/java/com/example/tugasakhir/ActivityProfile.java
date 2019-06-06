package com.example.tugasakhir;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ActivityProfile extends AppCompatActivity {

    private TextView registerDeviceTextView, logOutTextViewActivityMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        registerDeviceTextView = (TextView) findViewById(R.id.registerDeviceTextViewActivityProfile);
        logOutTextViewActivityMain = (TextView) findViewById(R.id.logOutTextViewActivityProfile);

        registerDeviceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                moveToActivityRegisterDevice();
            }
        });

        logOutTextViewActivityMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent toActivityLogin = new Intent(ActivityProfile.this, ActivityLogin.class);
                startActivity(toActivityLogin);
            }
        });
    }

    private void moveToActivityRegisterDevice(){

        Intent toActivityRegisterDevice = new Intent(this, ActivityRegisterDevice.class);
        startActivity(toActivityRegisterDevice);
    }

}
