package com.example.weconnect;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.login.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;




public class ForgotPassword extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        EditText emailAdd = findViewById(R.id.enteredEmail);
        Button btnReset = findViewById(R.id.reset_button);

        firebaseAuth = FirebaseAuth.getInstance();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailAdd.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(ForgotPassword.this, "", Toast.LENGTH_SHORT).show();
                    emailAdd.setError("Field is empty");
                    emailAdd.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(ForgotPassword.this, "Please Enter a valid email", Toast.LENGTH_SHORT).show();
                    emailAdd.setError("Valid email is required");
                    emailAdd.requestFocus();
                }else{

                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(ForgotPassword.this, "Check your Inbox for password Reset", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgotPassword.this, LoginOrRegister.class));
                                finish();
                            }else{
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e) {
                                    emailAdd.setError("Email does not exist");
                                    emailAdd.requestFocus();
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    emailAdd.setError("Invalid Credentials");
                                    emailAdd.requestFocus();
                                } catch (Exception e) {
                                    Log.e(TAG, e.getMessage());
                                    Toast.makeText(ForgotPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });




    }
}