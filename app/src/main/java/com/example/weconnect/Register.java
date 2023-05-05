package com.example.weconnect;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.Objects;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Register extends AppCompatActivity {

    //TextInput
    EditText firstName, lastName, emailAdd, birthDate, password, confirmPassword;

    //RadioGroup
    RadioGroup radioGroup;

    //Button
    Button btnSignup;

    FirebaseDatabase userDatabase;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        emailAdd = findViewById(R.id.emailAdd);
        birthDate = findViewById(R.id.birthDate);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        btnSignup = findViewById(R.id.btnSignup);
        radioGroup = findViewById(R.id.radioGroup);

        //Saving Data in Firebase

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




            }
        });


    }
}

