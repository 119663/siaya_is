package com.evelyne.labs.trialapp.admin;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.evelyne.labs.trialapp.R;
import com.evelyne.labs.trialapp.fragments.AppointmentsFragment;
import com.evelyne.labs.trialapp.fragments.ManageFragment;
import com.evelyne.labs.trialapp.fragments.UploadFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdminMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);
        BottomNavigationView bottomsNav = findViewById(R.id.bottom_admin);
        bottomsNav.setOnItemSelectedListener(navListener);
        //bottomNav.setOnNavigationItemSelectedListener(navListener);

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerad,
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
                        case R.id.nav_manage:
                            selectedFragment = new ManageFragment();
                            break;
                        case R.id.nav_reports:
                            selectedFragment = new AppointmentsFragment();
                            break;



                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerad,
                            selectedFragment).commit();

                    return true;
                }



            };
}

