package com.example.weconnect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Profile extends AppCompatActivity {

    private CardView getImage;
    private ImageView imageviewImage;
    private static int pick_image = 123;
    private Uri imagepath;

    private EditText getUsername;
    private Button savedProfile;
    private FirebaseAuth auth;
    private String name;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private String imageUriAccessToken;
    ProgressBar progressbarProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore= FirebaseFirestore.getInstance();

        getImage = findViewById(R.id.cardView);
        imageviewImage = findViewById(R.id.userImage);
        getUsername = findViewById(R.id.editProfileName);
        savedProfile = findViewById(R.id.saveProfile);
        progressbarProfile = findViewById(R.id.progressbarProfile);



        getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI); //to open file manager if it clicks to choose a profile picture
                 startActivityForResult(intent, pick_image); //to go back where the last activity after the process finished
            }
        });

        savedProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = getUsername.getText().toString();

                if(name.isEmpty()) {
                    Toast.makeText(Profile.this, "name is empty", Toast.LENGTH_SHORT).show();
                } else if(imagepath==null) {
                    Toast.makeText(Profile.this, "Profile picture is empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressbarProfile.setVisibility(View.VISIBLE);
                    sendDataForNewUser();
                    progressbarProfile.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(Profile.this, Chat.class);

                    startActivity(intent);
                    finish();
                }
            }


        });


    }

    private void sendDataForNewUser() {
        sendDataToRealTimeDatabase();

    }

    private void sendDataToRealTimeDatabase() {
        name = getUsername.getText().toString();

        //STORING DATA TO REALTIME DATABASE
        FirebaseDatabase firebasedatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebasedatabase.getReference().child("Users").child(auth.getUid());

        //FROM OUR UserProfile Class we retrieve the value of name and uid
        UserProfile userprofile = new UserProfile(name, auth.getUid());
        databaseReference.setValue(userprofile);
        Toast.makeText(this, "New Profile created successfully", Toast.LENGTH_SHORT).show();
        sendImagetoStorage();

    }

    private void sendImagetoStorage() {
          StorageReference imageRef = storageReference.child("Images").child(auth.getUid()).child("Profile picture");

          //Image compression

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagepath);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        //storing images to storage
        UploadTask uploadTask = imageRef.putBytes(data);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUriAccessToken = uri.toString();
                        Toast.makeText(Profile.this, "URI get success", Toast.LENGTH_SHORT).show();
                        sendDataToCloudFirestore();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(Profile.this, "Image uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile.this, "Image not Uploaded", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendDataToCloudFirestore() {
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(auth.getUid());

        Map<String, Object> userdata = new HashMap<>();
        userdata.put("name", name);
        userdata.put("image", imageUriAccessToken);
        userdata.put("uid", auth.getUid());
        userdata.put("status", "Online");
        
        documentReference.set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Profile.this, "Data send Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile.this, "Data send Failed", Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode== pick_image && resultCode== RESULT_OK)
        {
            imagepath=data.getData();
            imageviewImage.setImageURI(imagepath);
        }
        super.onActivityResult(requestCode, resultCode, data);


    }
}