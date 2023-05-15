package com.example.weconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private  EditText viewUsername;
    private TextView updateProfile;
    private ImageView userImageView;
    private Toolbar toolbar;
    private ImageButton Backbutton;



    private FirebaseFirestore firebasefirestore;
    private  FirebaseAuth auth;
    private  FirebaseDatabase firebaseDb;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;


    private String ImageURIaccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        viewUsername = findViewById(R.id.viewProfilename);
        updateProfile = findViewById(R.id.updateProfile);
        userImageView = findViewById(R.id.userImageProfile);
        updateProfile = findViewById(R.id.updateProfile);
        Backbutton = findViewById(R.id.backButton);
        toolbar = findViewById(R.id.toolbarMenuProfile);

        //FIREBASE INSTANCE
        firebasefirestore = FirebaseFirestore.getInstance();
        firebaseDb = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);


        Backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        storageReference = firebaseStorage.getReference();

        storageReference.child("Images").child(auth.getUid()).child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageURIaccessToken=uri.toString();
                Picasso.get().load(uri).into(userImageView);

            }
        });


        DatabaseReference databaseReference = firebaseDb.getReference(auth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 UserProfile userProfile = snapshot.getValue(UserProfile.class); //call the UserProfile class
                 viewUsername.setText(userProfile.getUsername());  //get the saved username from the account logged and display to the textview
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileActivity.this,UpdateProfile.class);
                intent.putExtra("user",viewUsername.getText().toString());
                startActivity(intent);
            }
        });


    }
}