package com.example.weconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginOrRegister extends AppCompatActivity {

      private EditText email;
      private EditText password;
      private Button login;
      private FirebaseAuth auth;
      private ImageView googleButton;
      private ImageView facebookButton;
      private ImageView githubButton;
      private TextView register;

      //For google Authentication
      GoogleSignInOptions gso;
      GoogleSignInClient gsc;

    @SuppressLint("MissingInflatedId")
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);

        email = findViewById(R.id.editEmail);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        googleButton = findViewById(R.id.imgGoogle);
        facebookButton = findViewById(R.id.imgFacebook);
        register = findViewById(R.id.txtRegister);
        githubButton = findViewById(R.id.imgGithub);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Login Button
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
                        startActivity(new Intent(LoginOrRegister.this, WelcomeUser.class));
                        finish();
                    }
                });
            }
        });

        //GOOGLE AUTHENTICATION
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        //save the last account logged that can't be auto erase even it exited the app
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct != null) {
            nextActivity();
        }

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signGoogleAccount();
            }
        });

        //FACEBOOK AUTHENTICATION
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginOrRegister.this, FacebookAuth.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        //GITHUB AUTHENTICATION
        githubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginOrRegister.this, githubAuth.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

            }


        });

        //REGISTER ACCOUNT
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginOrRegister.this, Register.class));
                finish();
            }
        });

    }
    public void signGoogleAccount() {
       Intent signInIntent = gsc.getSignInIntent();
       startActivityForResult(signInIntent, 1000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                 nextActivity();
            } catch (ApiException e) {
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }




    private void nextActivity() {
        finish();
        startActivity(new Intent(LoginOrRegister.this, WelcomeUser.class));
    }
}