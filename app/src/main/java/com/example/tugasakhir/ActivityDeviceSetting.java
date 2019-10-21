package com.example.tugasakhir;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.anwarshahriar.calligrapher.Calligrapher;

public class ActivityDeviceSetting extends AppCompatActivity {

    private TextView serialNumberTextView, showPasswordTextView;
    private ImageView plantImageView;
    private Spinner selectPlantSpinner;
    private EditText locationEditText, containerVolumeEditText;
    private Button saveButton;
    private Bundle readDataFromActivityDeviceDetail;
    private String serialNumber, plantFromFirebase, selectedPlant, plant, location, password, showPasswordString, allRegisteredPlants, containerVolume;
    private int image, j;
    private DatabaseReference plantReference, selectedDeviceReference, allRegisteredPlantsReference;
    private ArrayList<String> plantArrayList, allRegisteredPlantsArrayList;
    private ArrayList<Integer> allImagePlantsArrayList;
    private ArrayAdapter<String> plantAdapter;
    private SpannableString spannableString;
    private SpannableStringBuilder spannableStringBuilder;
    private ForegroundColorSpan foregroundColorSpanGrey, foregroundColorSpanRed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setting);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "PRODUCT_SANS.ttf", true);

        serialNumberTextView = (TextView) findViewById(R.id.serialNumberTextViewActivityDeviceDetail);
        plantImageView = (ImageView) findViewById(R.id.plantImageViewActivityDeviceSetting);
        selectPlantSpinner = (Spinner) findViewById(R.id.selectPlantSpinnerActivityDeviceDetail);
        locationEditText = (EditText) findViewById(R.id.locationEditTextActivityDeviceSetting);
        saveButton = (Button) findViewById(R.id.saveButtonActivityDeviceSetting);
        showPasswordTextView = (TextView) findViewById(R.id.showPasswordTextViewActivityDeviceSetting);
        containerVolumeEditText = (EditText) findViewById(R.id.containerVolumeEditTextActivityDeviceSetting);

        readDataFromActivityDeviceDetail = getIntent().getExtras();
        serialNumber = readDataFromActivityDeviceDetail.getString("serialNumber");
        image = readDataFromActivityDeviceDetail.getInt("image");
        plant = readDataFromActivityDeviceDetail.getString("plant");
        location = readDataFromActivityDeviceDetail.getString("location");
        password = readDataFromActivityDeviceDetail.getString("password");
        containerVolume = readDataFromActivityDeviceDetail.getString("containerVolume");

        allImagePlantsArrayList = new ArrayList<Integer>();
        allRegisteredPlantsArrayList = new ArrayList<String>();

        plantReference = FirebaseDatabase.getInstance().getReference().child("Optimal Parameters");
        selectedDeviceReference = FirebaseDatabase.getInstance().getReference().child("Devices").child(serialNumber);
        allRegisteredPlantsReference = FirebaseDatabase.getInstance().getReference().child("Optimal Parameters");

        serialNumberTextView.setText(serialNumber);
        plantImageView.setImageResource(image);
        locationEditText.setText(location);
        containerVolumeEditText.setText(containerVolume);

        readAllPlantsFromFirebase();

        showPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPasswordString = getString(R.string.password_for) + " " + serialNumber + " " + getString(R.string.is) + " " + password;

                showPasswordTextView.setText(showPasswordString);

                spannableString = new SpannableString(showPasswordString);
                spannableStringBuilder = new SpannableStringBuilder(showPasswordString);

                foregroundColorSpanGrey = new ForegroundColorSpan(getResources().getColor(R.color.grey_700));
                foregroundColorSpanRed = new ForegroundColorSpan(getResources().getColor(R.color.red_A700));

                spannableStringBuilder.setSpan(foregroundColorSpanGrey, 1, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.setSpan(foregroundColorSpanRed, 26, 36, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                showPasswordTextView.setText(spannableStringBuilder);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        selectPlantSpinner.setSelection(plantArrayList.indexOf(plant));

        selectPlantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedPlant = parent.getItemAtPosition(position).toString();

                readAllRegisteredPlantsFromFirebase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // TODO Auto-generated method stub
            }
        });
    }

    private void readAllRegisteredPlantsFromFirebase(){

        allRegisteredPlantsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    allRegisteredPlants = ds.getKey();
                    allRegisteredPlantsArrayList.add(allRegisteredPlants);
                }

                changePlantImageView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void changePlantImageView(){

        insertAllImagesToArray();

        for (int i = 0; i < allRegisteredPlantsArrayList.size(); i++){

            if(selectedPlant.equals(allRegisteredPlantsArrayList.get(i))){

                plantImageView.setImageResource(allImagePlantsArrayList.get(i));
                j = i;
            }
        }
    }

    private void checkAllFields(){

        location = locationEditText.getText().toString();
        containerVolume = containerVolumeEditText.getText().toString();

        if(selectedPlant.isEmpty() || location.isEmpty() || containerVolume.isEmpty()){

            Toast.makeText(ActivityDeviceSetting.this, getString(R.string.please_fill_all_the_fields), Toast.LENGTH_LONG).show();
        }
        else {

            if(selectedPlant.equals(plantArrayList.get(0))){

                Toast.makeText(ActivityDeviceSetting.this, getString(R.string.please_select_a_valid_plant), Toast.LENGTH_LONG).show();
            }
            else {

                saveDataToFirebase();
            }
        }
    }

    private void saveDataToFirebase(){

        selectedDeviceReference.child("plant").setValue(selectedPlant);
        selectedDeviceReference.child("containerVolume").setValue(containerVolume);
        selectedDeviceReference.child("location").setValue(location).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    moveToActivityDeviceDetail();
                    Toast.makeText(ActivityDeviceSetting.this, getString(R.string.saved), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void moveToActivityDeviceDetail(){

        Intent toActivityDeviceDetail = new Intent(ActivityDeviceSetting.this, ActivityDeviceDetail.class);
        toActivityDeviceDetail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        toActivityDeviceDetail.putExtra("serialNumber", serialNumber);
        toActivityDeviceDetail.putExtra("image", allImagePlantsArrayList.get(j));
        startActivity(toActivityDeviceDetail);
        finish();
    }

    private void insertAllImagesToArray(){

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
        allImagePlantsArrayList.add(R.drawable.plant_jerusalem_artichoke);
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
