package com.example.weconnect;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class Register extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText date;
    private EditText password;
    private EditText confirmPass;

    private RadioGroup radioGroupRegisterGender;

    private RadioButton radioButtonRegisteredGenderSelected;
    private Button signup;

    private FirebaseAuth auth;
    private CheckBox checkBox;
    private static final String TAG = "Register";
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    DatabaseReference reference;
    FirebaseDatabase db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initDatePicker();
        dateButton = findViewById(R.id.dateButton);
        dateButton.setText(getTodaysDate());

        //editText
        firstName = findViewById(R.id.editFirstName);
        lastName = findViewById(R.id.editLastName);
        email = findViewById(R.id.editTextTextEmailAddress);
       // date = findViewById(R.id.dateButton);
        password = findViewById(R.id.editPassword);
        confirmPass = findViewById(R.id.editConfirmPassword);

        //Button
        signup = findViewById(R.id.btnSignup);

        //RadioButton for Gender
        radioGroupRegisterGender = findViewById(R.id.radioGroup);
        radioGroupRegisterGender.clearCheck();

        //Checkbox

        checkBox = findViewById(R.id.chkAgreement);

        auth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedGenderId = radioGroupRegisterGender.getCheckedRadioButtonId();
                radioButtonRegisteredGenderSelected = findViewById(selectedGenderId);

                //obtain the entered data

                String textFirstName = firstName.getText().toString();
                String textLastName = lastName.getText().toString();
                String textEmail = email.getText().toString();
                String textBirthDate = dateButton.getText().toString();
                String textPassword = password.getText().toString();
                String textConfirmPassword = confirmPass.getText().toString();
                String textGender;


                // If Field Data is Empty
                if(TextUtils.isEmpty(textFirstName)){
                    Toast.makeText( Register.this, "Please Enter your first name", Toast.LENGTH_LONG).show();
                    firstName.setError("First name is required");
                    firstName.requestFocus();
                } else if (TextUtils.isEmpty(textLastName)){
                    Toast.makeText( Register.this, "Please Enter your last name", Toast.LENGTH_LONG).show();
                    lastName.setError("Last name is required");
                    lastName.requestFocus();

                } else if (TextUtils.isEmpty(textEmail)){
                    Toast.makeText( Register.this, "Please Enter your email address", Toast.LENGTH_LONG).show();
                    email.setError("email is required");
                    email.requestFocus();
                } else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText( Register.this, "Please Enter re-enter email address", Toast.LENGTH_LONG).show();
                    email.setError("email is required");
                    email.requestFocus();
                } else if(TextUtils.isEmpty(textBirthDate)){
                    Toast.makeText( Register.this, "Please Enter your birthdate", Toast.LENGTH_LONG).show();
                    date.setError("birthdate is required");
                    date.requestFocus();
                } else if ( radioGroupRegisterGender.getCheckedRadioButtonId() == -1){
                    Toast.makeText(Register.this, "Please select your gender", Toast.LENGTH_LONG).show();
                    radioButtonRegisteredGenderSelected.setError("Gender is required");
                    radioButtonRegisteredGenderSelected.requestFocus();
                }else if (TextUtils.isEmpty(textPassword)){
                    Toast.makeText(Register.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    password.setError("password is required");
                    password.requestFocus();
                }else if(textPassword.length() < 8 ) {
                    Toast.makeText(Register.this, "Password should be 8 characters long", Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(textConfirmPassword)){
                    Toast.makeText(Register.this, "Please confirm your password", Toast.LENGTH_LONG).show();
                    confirmPass.setError("Password is required");
                    confirmPass.requestFocus();
                }else if (!textPassword.equals(textConfirmPassword)){
                    Toast.makeText(Register.this, "Password doesn't match", Toast.LENGTH_LONG).show();
                    confirmPass.setError("Password confirmation required");
                    //clear entered password
                    password.clearComposingText();
                    confirmPass.clearComposingText();
                }else if (!checkBox.isChecked()){
                    Toast.makeText(Register.this, "Please click the box if you wish to proceed", Toast.LENGTH_LONG).show();
                    checkBox.setError("Checkbox is required to be able to proceed");
                    checkBox.requestFocus();
                }else{
                    textGender = radioButtonRegisteredGenderSelected.getText().toString();
                    Users users = new Users(textFirstName, textLastName, textEmail, textBirthDate, textGender, textPassword); //put all the values needed

                    db = FirebaseDatabase.getInstance();
                    reference = db.getReference("Users");
                    reference.child(textFirstName).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            auth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Register.this, "Account Registered", Toast.LENGTH_SHORT).show();

                                     /* FirebaseUser user = auth.getCurrentUser();
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(textFirstName)
                                                .build();
                                       // user.updateProfile(profileUpdates); */


                                    }else{
                                        Toast.makeText(Register.this, "Registration Failed", Toast.LENGTH_SHORT).show();


                                        try{
                                            throw task.getException();
                                        }catch (FirebaseAuthInvalidCredentialsException e){
                                            email.setError("Your email is invalid. use a valid email");
                                            email.requestFocus();
                                        }catch (FirebaseAuthUserCollisionException e){
                                            email.setError("User is already registered with this email. Please use another email.");
                                            email.requestFocus();
                                        }catch(Exception e){
                                            Log.e(TAG, e.getMessage());
                                            Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }


                                    }
                                }
                            });

                            firstName.setText("");
                            lastName.setText("");
                            email.setText("");
                            date.setText("");
                            password.setText("");
                            Toast.makeText(Register.this,"Successfully Added",Toast.LENGTH_SHORT).show();
                        }
                    });

                    Intent intent = new Intent(Register.this, WelcomeUser.class);
                    intent.putExtra("name", textFirstName);
                    startActivity(intent);
                    //Prevent user from returning back to Register Activity incase they click back button after Registration
                    finish();


                }

            }
        });
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

       return makeDateString(day,month,year);
    }

    //Date Button
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);

            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this,style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private String makeDateString(int day, int month, int year) {

        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        switch (month) {
            case 1:
                return "JAN";
            case 2:
                return "FEB";
            case 3:
                return "MAR";

            case 4:
                return "APR";

            case 5:
                return "MAY";

            case 6:
                return "JUN";

            case 7:
                return "JUL";

            case 8:
                return "AUG";

            case 9:
                return "SEPT";

            case 10:
                return "OCT";

            case 11:
                return "NOV";

            case 12:
                return "DEC";

        }

        return "JAN";
    }
    public void openDatePicker (View view) {
        datePickerDialog.show();
    }

}
