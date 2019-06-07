package com.example.tugasakhir;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;

public class ActivityProfile extends AppCompatActivity {

    private TextView nameTextView, positionTextView, genderTextView, phoneTextView, birthDateTextView, emailTextView;
    private CircleImageView profileCircleImageView;
    private DatabaseReference profileReference;
    private FirebaseUser currentUser;
    private String uID, name, position, gender, phone, birthdate, email, photo;
    private ImageButton registerDeviceImageButton, logOutImageButton, settingImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "PRODUCT_SANS.ttf", true);

        registerDeviceImageButton = (ImageButton) findViewById(R.id.registerDeviceImageButtonActivityProfile);
        logOutImageButton = (ImageButton) findViewById(R.id.logOutImageButtonActivityProfile);
        settingImageButton = (ImageButton) findViewById(R.id.settingImageButtonActivityProfile);
        profileCircleImageView = (CircleImageView) findViewById(R.id.profileCircleImageViewActivityProfile);
        nameTextView = (TextView) findViewById(R.id.nameTextViewActivityProfile);
        positionTextView = (TextView) findViewById(R.id.positionTextViewActivityProfile);
        genderTextView = (TextView) findViewById(R.id.genderTextViewActivityProfile);
        phoneTextView = (TextView) findViewById(R.id.phoneTextViewActivityProfile);
        birthDateTextView = (TextView) findViewById(R.id.birthDateTextViewActivityProfile);
        emailTextView = (TextView) findViewById(R.id.emailTextViewActivityProfile);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uID = currentUser.getUid();

        profileReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uID).child("Profile");

        readProfileDataFromFirebase();

        registerDeviceImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                moveToActivityRegisterDevice();
            }
        });

        logOutImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent toActivityLogin = new Intent(ActivityProfile.this, ActivityLogin.class);
                startActivity(toActivityLogin);
            }
        });

        settingImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                moveToActivityEditProfile();
            }
        });
    }

    private void moveToActivityRegisterDevice(){

        Intent toActivityRegisterDevice = new Intent(this, ActivityRegisterDevice.class);
        startActivity(toActivityRegisterDevice);
    }

    private void moveToActivityEditProfile(){

        Intent toActivityEditProfile = new Intent(this, ActivityEditProfile.class);
        startActivity(toActivityEditProfile);
    }

    private void readProfileDataFromFirebase(){

        profileReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                name = dataSnapshot.child("fullname").getValue().toString();
                position = dataSnapshot.child("role").getValue().toString();
                gender = dataSnapshot.child("gender").getValue().toString();
                phone = dataSnapshot.child("phonenumber").getValue().toString();
                birthdate = dataSnapshot.child("birthdate").getValue().toString();
                email = dataSnapshot.child("email").getValue().toString();
                photo = dataSnapshot.child("photo").getValue().toString();

                showProfileData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showProfileData(){

        nameTextView.setText(name);
        positionTextView.setText(position);
        genderTextView.setText(gender);
        phoneTextView.setText(phone);
        birthDateTextView.setText(birthdate);
        emailTextView.setText(email);

        Picasso.get().load(photo).fit().centerCrop().placeholder(R.drawable.ic_account_circle_white_100dp).error(R.drawable.ic_account_circle_white_100dp).into(profileCircleImageView);
    }

}
