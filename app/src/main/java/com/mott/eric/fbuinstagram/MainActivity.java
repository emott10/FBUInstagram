package com.mott.eric.fbuinstagram;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mott.eric.fbuinstagram.fragments.ComposeFragment;
import com.mott.eric.fbuinstagram.fragments.ProfileFragment;
import com.mott.eric.fbuinstagram.fragments.TimelineFragment;


public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        final FragmentManager fragmentManager = getSupportFragmentManager();

        //reference to bottom nav
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()){
                    case R.id.action_profile:
                        fragment = new ProfileFragment();
                        break;
                    case R.id.action_capture:
                        fragment = new ComposeFragment();
                        break;
                    case R.id.action_home:
                    default:
                        fragment = new TimelineFragment();
                        break;

                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    public void changeProfilePic(View v){

    }
}
