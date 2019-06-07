package com.example.tugasakhir;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;

public class ActivityProfile extends AppCompatActivity {

    private TextView registerDeviceTextView, logOutTextView, nameTextViewActivityProfile, positionTextView, genderTextView, phoneTextView, birthDateTextView, emailTextView;
    private CircleImageView profileCircleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "PRODUCT_SANS.ttf", true);

        registerDeviceTextView = (TextView) findViewById(R.id.registerDeviceTextViewActivityProfile);
        logOutTextView = (TextView) findViewById(R.id.logOutTextViewActivityProfile);
        profileCircleImageView = (CircleImageView) findViewById(R.id.profileCircleImageViewActivityProfile);
        nameTextViewActivityProfile = (TextView) findViewById(R.id.nameTextViewActivityProfile);
        positionTextView = (TextView) findViewById(R.id.positionTextViewActivityProfile);
        genderTextView = (TextView) findViewById(R.id.genderTextViewActivityProfile);
        phoneTextView = (TextView) findViewById(R.id.phoneTextViewActivityProfile);
        birthDateTextView = (TextView) findViewById(R.id.birthDateTextViewActivityProfile);
        emailTextView = (TextView) findViewById(R.id.emailTextViewActivityProfile);

        registerDeviceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                moveToActivityRegisterDevice();
            }
        });

        logOutTextView.setOnClickListener(new View.OnClickListener() {
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
