package com.example.weconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaSession2;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.ktx.Firebase;

public class WelcomeUser extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private TextView welcomeText;
    private Button continuebtn;
    private Button logout;

    private FirebaseUser currentUser;

    FirebaseUser user;
    FirebaseAuth authProfile;
    GoogleSignInAccount googleAccount;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_user);

         googleAccount = GoogleSignIn.getLastSignedInAccount(this);
         user = FirebaseAuth.getInstance().getCurrentUser();


        FirebaseApp.initializeApp(this);
        //firebase auth instance


        welcomeText = findViewById(R.id.txtUserWelcome);
        continuebtn = findViewById(R.id.btnContinue);
        logout = findViewById(R.id.buttonLogout);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        authProfile = FirebaseAuth.getInstance();
        currentUser = authProfile.getCurrentUser();


        Intent intent = getIntent();
        String userPhone = intent.getStringExtra("username");
        welcomeText.setText("Welcome ");

          GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
          FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String name = user.getDisplayName();
            welcomeText.setText("Welcome");
         } else {
            if (googleAccount != null) {
                String username = googleAccount.getDisplayName();
                welcomeText.setText("Welcome, " + username + "!");
            }
            //follows here
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               signOut();
            }
        });


        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(googleAccount!=null || user != null) {

                    Intent intent = new Intent(WelcomeUser.this, Chat.class);
                    startActivity(intent);
                    finish();

                }
                else {
                    Intent intent = new Intent(WelcomeUser.this, Profile.class);
                    startActivity(intent);
                    finish();
                }


            }
        });

        // Check if user is already logged in

    }

    // Method to display the account name
    private void displayAccountName(FirebaseUser user) {
        String accountName = "";
        String providerId = user.getProviderId();

        if (providerId.equals("google.com")) {
            accountName = user.getDisplayName();
        } else if (providerId.equals("facebook.com")) {
            accountName = user.getDisplayName();
        } else if (providerId.equals("phone")) {
            // If using Phone authentication, you can retrieve the account name from the PhoneAuthCredential
            PhoneAuthCredential credential = (PhoneAuthCredential) user.getProviderData().get(0);
            accountName = credential.getProvider();
        } else if (providerId.equals("password")) {
            accountName = user.getEmail();
        }

        // Display the account name in your UI
        welcomeText.setText("Welcome ");
    }


    public void signOut() {

        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeUser.this);
        builder.setMessage("Are you sure you want to Log out?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked Yes button
                        // Add your logic here
                        signOutGoogle();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked No button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void signOutGoogle() {

        //Sign out
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(WelcomeUser.this, "Successfully signed out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(WelcomeUser.this, LoginOrRegister.class));
                finish();
            }
        });
    }


}