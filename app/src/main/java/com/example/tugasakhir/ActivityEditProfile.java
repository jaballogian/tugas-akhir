package com.example.tugasakhir;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityEditProfile extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText fullnameEditText, phoneNumberEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button selectBirthDateButton, saveButton;
    private Spinner selectGenderSpinner, selectRoleSpinner;
    private DatabaseReference profileReference;
    private FirebaseUser currentUser;
    private DialogFragment datePicker;
    private ProgressDialog loading;
    private String uID, fullname, selectedRole, phoneNumber, email, photo, selectedGender, selectedBirthDate, password, confirmPassword;
    private CircleImageView profileCircleImageView;
    private String[] genderStrings, roleStrings;
    private ArrayList<String> genderArrayList, roleArrayList;
    private ArrayAdapter<String> genderAdapter, roleAdapter;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fullnameEditText = (EditText) findViewById(R.id.fullnameEditTextActivityEditProfile);
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditTextActivityEditProfile);
        emailEditText = (EditText) findViewById(R.id.emailEditTextActivityEditProfile);
        passwordEditText = (EditText) findViewById(R.id.passwordEditTextActivityEditProfile);
        confirmPasswordEditText = (EditText) findViewById(R.id.confirmPasswordEditTextActivityEditProfile);
        selectBirthDateButton = (Button) findViewById(R.id.selectBirthDateButtonActivityEditProfile);
        selectGenderSpinner = (Spinner) findViewById(R.id.selectGenderSpinnerActivityEditProfile);
        selectRoleSpinner = (Spinner) findViewById(R.id.selectRoleSpinnerActivityEditProfile);
        saveButton = (Button) findViewById(R.id.saveButtonActivityEditProfile);
        profileCircleImageView = (CircleImageView) findViewById(R.id.profileCircleImageViewActivityEditProfile);

        loading = new ProgressDialog(this);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uID = currentUser.getUid();

        profileReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uID).child("Profile");

        readProfileDataFromFirebase();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getStringFromEditText();
                checkAllFields();
            }
        });

        genderStrings = getResources().getStringArray(R.array.select_gender_array);
        roleStrings = getResources().getStringArray(R.array.select_role_array);

        genderArrayList = new ArrayList<String>(Arrays.asList(genderStrings));
        roleArrayList = new ArrayList<String>(Arrays.asList(roleStrings));

        genderAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genderArrayList);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectGenderSpinner.setAdapter(genderAdapter);

        selectGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedGender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // TODO Auto-generated method stub
            }
        });

        roleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roleArrayList);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectRoleSpinner.setAdapter(roleAdapter);

        selectRoleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedRole = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // TODO Auto-generated method stub
            }
        });

        selectBirthDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePicker = new CustomDatePickerFragment();
                datePicker.show(getSupportFragmentManager(), getString(R.string.select_birth_date));
            }
        });
    }

    private void readProfileDataFromFirebase(){

        profileReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                fullname = dataSnapshot.child("fullname").getValue().toString();
                selectedRole = dataSnapshot.child("role").getValue().toString();
                selectedGender = dataSnapshot.child("gender").getValue().toString();
                phoneNumber = dataSnapshot.child("phonenumber").getValue().toString();
                selectedBirthDate = dataSnapshot.child("birthdate").getValue().toString();
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

        fullnameEditText.setText(fullname);
        phoneNumberEditText.setText(phoneNumber);
        selectBirthDateButton.setText(getString(R.string.birth_date) + " " + selectedBirthDate);
        emailEditText.setText(email);
        setSelectedSpinner(selectGenderSpinner, selectedGender, genderArrayList);
        setSelectedSpinner(selectRoleSpinner, selectedRole, roleArrayList);

        Picasso.get().load(photo).fit().centerCrop().placeholder(R.drawable.ic_account_circle_grey_50dp).error(R.drawable.ic_account_circle_grey_50dp).into(profileCircleImageView);
    }

    private void setSelectedSpinner (Spinner spinner, String input, ArrayList<String> arrayList){

        for(i = 0; i < arrayList.size(); i++){

            if(input.equals(arrayList.get(i))){

                spinner.setSelection(i);
            }
        }
    }

    private void getStringFromEditText(){

        fullname = fullnameEditText.getText().toString();
        phoneNumber = phoneNumberEditText.getText().toString();
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();
        confirmPassword = confirmPasswordEditText.getText().toString();
    }

    private void checkStringFromSpinner(){

        if(selectedGender.equals(genderArrayList.get(0))){

            Toast.makeText(ActivityEditProfile.this, getString(R.string.please_select_a_valid_gender), Toast.LENGTH_LONG).show();
        }
        else if(selectedRole.equals(roleArrayList.get(0))){

            Toast.makeText(ActivityEditProfile.this, getString(R.string.please_select_a_valid_role), Toast.LENGTH_LONG).show();
        }
        else {

            comparePasswordAndConfirmPassword();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.YEAR, year);
        calendar.set(calendar.MONTH, month);
        calendar.set(calendar.DAY_OF_MONTH, dayOfMonth);

        selectedBirthDate = DateFormat.getDateInstance().format(calendar.getTime());
        selectBirthDateButton.setText(getString(R.string.birth_date) + " " + selectedBirthDate);
    }

    private void checkAllFields(){

        if(fullname.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                selectedGender.isEmpty() || selectedBirthDate.isEmpty() || selectedRole.isEmpty()){

            Toast.makeText(ActivityEditProfile.this, getString(R.string.please_fill_all_the_fields), Toast.LENGTH_LONG).show();
        }
        else {

            checkStringFromSpinner();
        }
    }

    private void comparePasswordAndConfirmPassword(){

        if(password.equals(confirmPassword)){

            setProgressDialog();
            saveDataToFirebase();
        }
        else {

            Toast.makeText(ActivityEditProfile.this, getString(R.string.password_and_confirm_password_doesnt_match), Toast.LENGTH_LONG).show();
        }
    }

    private void setProgressDialog(){

        loading.setTitle(R.string.processing);
        loading.setMessage(getString(R.string.please_wait));
        loading.setCanceledOnTouchOutside(false);
        loading.show();
    }

    private void saveDataToFirebase(){

        profileReference.child("fullname").setValue(fullname);
        profileReference.child("role").setValue(selectedRole);
        profileReference.child("gender").setValue(selectedGender);
        profileReference.child("phonenumber").setValue(phoneNumber);
        profileReference.child("birthdate").setValue(selectedBirthDate);
        profileReference.child("email").setValue(email);
        profileReference.child("photo").setValue(photo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    moveToActivityProfile();
                    loading.dismiss();
                    Toast.makeText(ActivityEditProfile.this, getString(R.string.saved), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void moveToActivityProfile(){

        Intent toActivityProfile = new Intent(ActivityEditProfile.this, ActivityProfile.class);
        toActivityProfile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toActivityProfile);
        finish();
    }
}
