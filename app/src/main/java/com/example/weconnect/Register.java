package com.example.weconnect;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        //editText
        firstName = findViewById(R.id.editFirstName);
        lastName = findViewById(R.id.editLastName);
        email = findViewById(R.id.editTextTextEmailAddress);
        date = findViewById(R.id.editTextDate);
        password = findViewById(R.id.editPassword);
        confirmPass = findViewById(R.id.editConfirmPassword);

        //Button
        signup = findViewById(R.id.btnSignup);

        //RadioButton for Gender
        radioGroupRegisterGender = findViewById(R.id.radioGroup);
        radioGroupRegisterGender.clearCheck();

        //Checkbox

        checkBox = findViewById(R.id.chkAgreement);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedGenderId = radioGroupRegisterGender.getCheckedRadioButtonId();
                radioButtonRegisteredGenderSelected = findViewById(selectedGenderId);

                //obtain the entered data

                String textFirstName = firstName.getText().toString();
                String textLastName = lastName.getText().toString();
                String textEmail = email.getText().toString();
                String textDate = date.getText().toString();
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
                } else if(TextUtils.isEmpty(textDate)){
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

                    registerUser(textFirstName, textLastName, textEmail, textDate , textGender, textPassword);
                }


            }
        });
    }
        //Register User
    private void registerUser(String textFirstName, String textLastName, String textEmail, String textDate, String textGender, String textPassword) {

        auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Register.this, "Account Registered", Toast.LENGTH_SHORT).show();



                    //Open WelcomeUser Activity
                    startActivity(new Intent(Register.this, WelcomeUser.class));

                    //Prevent user from returning back to Register Activity incase they click back button after Registration
                    finish();
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


    }
}
