package com.example.weconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginOrRegister extends AppCompatActivity {

      private EditText email;
      private EditText password;
      private Button login;
      private FirebaseAuth auth;
    @SuppressLint("MissingInflatedId")
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);

        email = findViewById(R.id.editEmail);
        password = findViewById(R.id.editPassword);
        login = findViewById(R.id.login);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();

                if(emailString.isEmpty() || passwordString.isEmpty()) {
                    Toast.makeText(LoginOrRegister.this, "Empty Email or Password", Toast.LENGTH_SHORT).show();
                }
                else if(passwordString.length() <= 7) {
                    Toast.makeText(LoginOrRegister.this, "Password is too short!", Toast.LENGTH_SHORT).show();
                }
                else {
                    loginUser(emailString, passwordString);
                }
            }

            private void loginUser(String email, String password) {

                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(LoginOrRegister.this, "Logging in", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginOrRegister.this, MainActivity.class));
                        finish();
                    }
                });
            }
        });

    }
}