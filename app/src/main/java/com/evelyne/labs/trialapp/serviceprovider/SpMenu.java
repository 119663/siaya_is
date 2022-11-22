package com.evelyne.labs.trialapp.serviceprovider;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.evelyne.labs.trialapp.R;
import com.evelyne.labs.trialapp.fragments.AppointmentsFragment;
import com.evelyne.labs.trialapp.fragments.ProfileFragment;
import com.evelyne.labs.trialapp.fragments.UploadFragment;
import com.evelyne.labs.trialapp.fragments.mybookingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class SpMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_menu);
        BottomNavigationView bottomsNav = findViewById(R.id.bottom_sp);
        bottomsNav.setOnItemSelectedListener(navListener);
        //bottomNav.setOnNavigationItemSelectedListener(navListener);

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containersp,
                    new UploadFragment()).commit();
        }
    }
    //private BottomNavigationView.OnNavigationItemSelectedListener navListener =
    // new BottomNavigationView.OnNavigationItemSelectedListener() {
    NavigationBarView.OnItemSelectedListener navListener =
            new NavigationBarView.OnItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_upload:
                            selectedFragment = new UploadFragment();
                            break;
                        case R.id.nav_appointments:
                            selectedFragment = new AppointmentsFragment();
                            break;
                        case R.id.nav_myuploads:
                            selectedFragment = new mybookingsFragment();
                            break;
                        case R.id.nav_myprofile:
                            selectedFragment = new ProfileFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containersp,
                            selectedFragment).commit();

                    return true;
                }



    };
}