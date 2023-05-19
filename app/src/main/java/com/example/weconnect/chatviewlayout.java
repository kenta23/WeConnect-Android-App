package com.example.weconnect;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class chatviewlayout extends AppCompatActivity {

    TextView messageView;
    String stringMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatviewlayout);


        messageView = findViewById(R.id.newMessages);


    }
    public void setTextView(TextView textView) {
        this.messageView = textView;
    }

    public void someMethod() {
        stringMessage = String.valueOf(messageView);
        ChatMain chatMain = new ChatMain();
       // chatMain.calltextview(stringMessage);
    }
}
