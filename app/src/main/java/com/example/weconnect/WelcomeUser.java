package com.example.weconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaSession2;
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

public class WelcomeUser extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private TextView welcomeText;
    private Button continuebtn;
    private Button logout;


    @SuppressLint("MissingInflatedId")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_user);

        FirebaseApp.initializeApp(this);

        welcomeText = findViewById(R.id.txtUserWelcome);
        continuebtn = findViewById(R.id.btnContinue);
        logout = findViewById(R.id.buttonLogout);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);


        if(acct != null) {
            String username = acct.getDisplayName();
            welcomeText.setText("Welcome "+ username);
        }
        else {
            // Retrieve the display name from the Intent extra
            String displayName = getIntent().getStringExtra("displayName");
            // Set the welcome message with the user's display name
            welcomeText.setText(getString(R.string.welcome_message, displayName));
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               signOut();
            }
        });





    }
    public void signOut() {
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