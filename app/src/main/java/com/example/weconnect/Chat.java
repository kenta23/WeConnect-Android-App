package com.example.weconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

public class Chat extends AppCompatActivity {

    LinearLayout contacts, chat, calls;
    ImageView logout;

    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        contacts = findViewById(R.id.contactContainer);
        chat = findViewById(R.id.chatContainer);
        calls = findViewById(R.id.callsContainer);
        logout = findViewById(R.id.logout);
        System.out.println("yes!");


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();



       logout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               firebaseAuth.signOut();

               Toast.makeText(Chat.this, "Signed Out", Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(Chat.this, LoginOrRegister.class);
               startActivity(intent);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
           }
       });

    }


}
