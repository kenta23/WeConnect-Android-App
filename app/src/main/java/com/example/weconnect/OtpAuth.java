package com.example.weconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OtpAuth extends AppCompatActivity {

    TextView changePhone;
    EditText editCode;
    Button verifyButton;
    String enteredOTP;

    FirebaseAuth firebaseauth;
    ProgressBar progressBar;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_auth);

        changePhone = findViewById(R.id.changePhone);
        editCode = findViewById(R.id.editTextCode);
        verifyButton = findViewById(R.id.verifyButton);
        progressBar = findViewById(R.id.progressLoadingAuth);

        firebaseauth = FirebaseAuth.getInstance();

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OtpAuth.this, Otpcode.class);
                startActivity(intent);
            }
        });

      verifyButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
               enteredOTP = editCode.getText().toString();
               if(enteredOTP.isEmpty()) {
                   Toast.makeText(OtpAuth.this, "Empty OTP code", Toast.LENGTH_SHORT).show();
               }else {
                   progressBar.setVisibility(View.VISIBLE);
                   String codeReceiver = getIntent().getStringExtra("otp");
                   PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeReceiver, enteredOTP);
                   signInPhoneAuth(credential);
               }
          }

          private void signInPhoneAuth(PhoneAuthCredential credential) {

              firebaseauth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()) {
                          progressBar.setVisibility(View.INVISIBLE);
                          Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                          Intent intent = new Intent(OtpAuth.this, Profile.class);
                          startActivity(intent);
                          finish();
                      }
                      else {
                          if(task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                              progressBar.setVisibility(View.INVISIBLE);
                              Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                          }
                      }
                  }
              });
          }
      });

    }
}