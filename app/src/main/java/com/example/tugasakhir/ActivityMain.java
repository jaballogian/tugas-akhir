package com.example.tugasakhir;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import me.anwarshahriar.calligrapher.Calligrapher;

public class ActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "PRODUCT_SANS.ttf", true);

        checkingUser();
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
}
