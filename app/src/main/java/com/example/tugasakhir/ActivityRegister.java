package com.example.tugasakhir;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import me.anwarshahriar.calligrapher.Calligrapher;

public class ActivityRegister extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText fullnameEditText, phoneNumberEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button selectBirthDateButton, registerButton;
    private Spinner selectGenderSpinner, selectRoleSpinner;
    private String fullname, phoneNumber, email, password, confirmPassword, selectedGender, selectedBirthDate, selectedRole, uID;
    private String[] genderStrings, roleStrings;
    private ArrayList<String> genderArrayList, roleArrayList;
    private ArrayAdapter<String> genderAdapter, roleAdapter;
    private DialogFragment datePicker;
    private ProgressDialog loading;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    private HashMap<String, Object> userIdentity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "PRODUCT_SANS.ttf", true);

        fullnameEditText = (EditText) findViewById(R.id.fullnameEditTextActivityRegister);
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditTextActivityRegister);
        emailEditText = (EditText) findViewById(R.id.emailEditTextActivityRegister);
        passwordEditText = (EditText) findViewById(R.id.passwordEditTextActivityRegister);
        confirmPasswordEditText = (EditText) findViewById(R.id.confirmPasswordEditTextActivityRegister);
        selectBirthDateButton = (Button) findViewById(R.id.selectBirthDateButtonActivityRegister);
        selectGenderSpinner = (Spinner) findViewById(R.id.selectGenderSpinnerActivityRegister);
        selectRoleSpinner = (Spinner) findViewById(R.id.selectRoleSpinnerActivityRegister);
        registerButton = (Button) findViewById(R.id.registerButtonActivityRegister);

        loading = new ProgressDialog(this);

        selectedBirthDate = "";

        mAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

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

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getStringFromEditText();
                checkAllFields();
            }
        });
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

            Toast.makeText(ActivityRegister.this, getString(R.string.please_select_a_valid_gender), Toast.LENGTH_LONG).show();
        }
        else if(selectedRole.equals(roleArrayList.get(0))){

            Toast.makeText(ActivityRegister.this, getString(R.string.please_select_a_valid_role), Toast.LENGTH_LONG).show();
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

            Toast.makeText(ActivityRegister.this, getString(R.string.please_fill_all_the_fields), Toast.LENGTH_LONG).show();
        }
        else {

            checkStringFromSpinner();
        }
    }

    private void comparePasswordAndConfirmPassword(){

        if(password.equals(confirmPassword)){

            setProgressDialog();
            registerUser();
        }
        else {

            Toast.makeText(ActivityRegister.this, getString(R.string.password_and_confirm_password_doesnt_match), Toast.LENGTH_LONG).show();
        }
    }

    private void registerUser() {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    saveUserToDatabase();

                    loading.dismiss();
                }
                else {

                    loading.hide();
                    Toast.makeText(ActivityRegister.this, R.string.choose_different_email_and_password, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveUserToDatabase(){

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uID = currentUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uID).child("Profile");

        userIdentity = new HashMap<>();
        userIdentity.put("fullname", fullname);
        userIdentity.put("email", email);
        userIdentity.put("password", password);
        userIdentity.put("phonenumber", phoneNumber);
        userIdentity.put("birthdate", selectedBirthDate);
        userIdentity.put("gender", selectedGender);
        userIdentity.put("role", selectedRole);
        userIdentity.put("photo", "photo");

        databaseReference.setValue(userIdentity).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    moveToActivityRegisterDevice();
                    Toast.makeText(ActivityRegister.this, getString(R.string.welcome), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setProgressDialog(){

        loading.setTitle(R.string.processing);
        loading.setMessage(getString(R.string.please_wait));
        loading.setCanceledOnTouchOutside(false);
        loading.show();
    }

    private void moveToActivityRegisterDevice(){

        Intent toActivityRegisterDevice = new Intent(ActivityRegister.this, ActivityRegisterDevice.class);
        toActivityRegisterDevice.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toActivityRegisterDevice);
        finish();
    }
}
