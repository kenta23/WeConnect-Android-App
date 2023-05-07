package com.example.weconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class Chat extends AppCompatActivity {

    LinearLayout contacts, chat, calls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        contacts = findViewById(R.id.contactContainer);
        chat = findViewById(R.id.chatContainer);
        calls = findViewById(R.id.callsContainer);

        System.out.println("yes!");


        contacts.setOnClickListener(view -> {

        });

    }
}