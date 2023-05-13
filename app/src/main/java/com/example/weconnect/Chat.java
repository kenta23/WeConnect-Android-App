package com.example.weconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

public class Chat extends AppCompatActivity {

    LinearLayout contacts, chat, calls;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        contacts = findViewById(R.id.contactContainer);
        chat = findViewById(R.id.chatContainer);
        calls = findViewById(R.id.callsContainer);
        System.out.println("yes!");

    }
}