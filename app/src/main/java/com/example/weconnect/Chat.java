package com.example.weconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

public class Chat extends AppCompatActivity {

    //FRAGMENTS
    private TabLayout tabLayout;
    private ViewPager viewpager;
    private TabItem chat,calls,status;
    private PagerAdapter pagerAdapter;
    private Toolbar toolbar;

    private ImageView logout;

    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        tabLayout = findViewById(R.id.include);
        chat = findViewById(R.id.chats);
        status = findViewById(R.id.status);
        calls = findViewById(R.id.calls);

        toolbar = findViewById(R.id.toolbarMenu);
        setSupportActionBar(toolbar);
        viewpager = findViewById(R.id.viewpageMain);
       // logout = findViewById(R.id.logout);


        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.baseline_more_vert_24);
        toolbar.setOverflowIcon(drawable); //clickable icon on toolbar



        pagerAdapter pageradapter = new pagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewpager.setAdapter(pageradapter);



        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());

                if(tab.getPosition()== 0 || tab.getPosition()== 1 || tab.getPosition() == 2) {
                    pageradapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));




    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.profile:
                Intent intentProfile = new Intent(Chat.this, ProfileActivity.class);
                startActivity(intentProfile);
                break;
            case R.id.settings:
                Intent intentSettings = new Intent(Chat.this, settings.class);
                startActivity(intentSettings);
                break;
            case R.id.logoutProfile:
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();

                Toast.makeText(Chat.this, "Signed Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Chat.this, LoginOrRegister.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;


        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuinflater = getMenuInflater();
        menuinflater.inflate(R.menu.menu, menu);



        return true;
    }
}
