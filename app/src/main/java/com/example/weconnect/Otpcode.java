package com.example.weconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class Otpcode extends AppCompatActivity {

    EditText getPhonenumber;
    android.widget.Button OtpSender;
    CountryCodePicker countryCodePicker;
    String phonenumber;
    String countryCode;
    ProgressBar progressBar;
    FirebaseAuth auth;


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String codeSent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpcode);

        getPhonenumber = findViewById(R.id.getphone);
        OtpSender = findViewById(R.id.sentOTP);
        countryCodePicker = findViewById(R.id.countryPicker);
        progressBar = findViewById(R.id.progressLoading);

       auth = FirebaseAuth.getInstance();

       countryCode = countryCodePicker.getSelectedCountryCodeWithPlus();
       countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
           @Override
           public void onCountrySelected() {
               countryCode = countryCodePicker.getSelectedCountryCodeWithPlus();
           }
       });

       OtpSender.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String number;

               number = getPhonenumber.getText().toString();
               if(number.isEmpty()) {
                   Toast.makeText(getApplicationContext(), "Please enter your number!", Toast.LENGTH_SHORT).show();
               }
               else if (number.length()>10) {
                   Toast.makeText(getApplicationContext(), "Invalid number", Toast.LENGTH_SHORT).show();
               }
               else {
                   progressBar.setVisibility(view.VISIBLE);
                   phonenumber = countryCode+number;

                   PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber(phonenumber).setTimeout(60L, TimeUnit.SECONDS).setActivity(Otpcode.this).setCallbacks(mCallbacks).build();
                   PhoneAuthProvider.verifyPhoneNumber(options);
               }
           }
       });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.



            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
               // Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(getApplicationContext(), "Too many SMS requests", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
                    // reCAPTCHA verification attempted with null Activity
                }

                // Show a message and update the UI
                Toast.makeText(getApplicationContext(), "Verification failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                super.onCodeSent(verificationId, token);

                Toast.makeText(getApplicationContext(), "OTP is sent", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                codeSent=verificationId;

               Intent intent = new Intent(Otpcode.this, OtpAuth.class);
               intent.putExtra("otp", codeSent);
               startActivity(intent);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(Otpcode.this, Chat.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}