package com.example.weconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class OtpAuth extends AppCompatActivity {

    TextView changePhone;
    EditText editCode;
    Button verifyButton;
    String enteredOTP;

    FirebaseAuth firebasesuth;
    ProgressBar progressBar;


    @SuppressLint("MissingInflatedId")
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_auth);

        changePhone = findViewById(R.id.changePhone);
        editCode = findViewById(R.id.editTextCode);
        verifyButton = findViewById(R.id.verifyButton);

    }
}