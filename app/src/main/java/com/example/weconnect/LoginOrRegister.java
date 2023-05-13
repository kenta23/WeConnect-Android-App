package com.example.weconnect;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginOrRegister extends AppCompatActivity {

    private EditText email, password;
    private Button login;
    private TextView forgotPass;

    private ImageView googleButton, facebookButton, githubButton;

    private TextView register;

    private FirebaseAuth authProfile;
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
        forgotPass = findViewById(R.id.txtForgotPassword);

        authProfile = FirebaseAuth.getInstance();

        //Password Reset

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginOrRegister.this, ForgotPassword.class));
            }
        });

        // Login Button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail = email.getText().toString();
                String textPassword = password.getText().toString();

                if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(LoginOrRegister.this, "Please Enter your Email", Toast.LENGTH_SHORT).show();
                    email.setError("Field is empty");
                    email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(LoginOrRegister.this, "Valid email is required", Toast.LENGTH_SHORT).show();
                    email.setError("enter a valid email address");
                    email.requestFocus();
                } else if (TextUtils.isEmpty(textPassword)) {
                    Toast.makeText(LoginOrRegister.this, "Please Enter your Password", Toast.LENGTH_SHORT).show();
                    password.setError("Field is Empty");
                    password.requestFocus();
                } else {

                    loginUser(textEmail, textPassword);

                }
            }

            private void loginUser(String txtEmail, String txtPassword) {

                authProfile.signInWithEmailAndPassword(txtEmail, txtPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginOrRegister.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginOrRegister.this, WelcomeUser.class);
                            startActivity(intent);
                            finish();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                email.setError("User does not exist");
                                email.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                password.setError("Wrong Password");
                                password.requestFocus();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(LoginOrRegister.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
            }
        });


        //GOOGLE AUTHENTICATION
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        //save the last account logged that can't be auto erase even it exited the app
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
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
        if (requestCode == 1000) {
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


    /*@Override
    protected void onStart() {
        super.onStart();
       if(authProfile.getCurrentUser() != null){
          startActivity(new Intent(LoginOrRegister.this, Chat.class));
           finish();
        }
    }
}*/

