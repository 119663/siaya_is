package com.evelyne.labs.trialapp.customer;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.evelyne.labs.trialapp.R;
import com.evelyne.labs.trialapp.fragments.AboutFragment;
import com.evelyne.labs.trialapp.fragments.BookFragment;
import com.evelyne.labs.trialapp.fragments.BookingFragment;
import com.evelyne.labs.trialapp.fragments.ProfilesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class CustomerMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_menu);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);
        //bottomNav.setOnNavigationItemSelectedListener(navListener);

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new BookFragment()).commit();
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
                        case R.id.nav_about:
                            selectedFragment = new AboutFragment();
                            break;
                       case R.id.nav_book:
                            selectedFragment = new BookFragment();
                            break;
                        case R.id.nav_orders:
                            selectedFragment = new BookingFragment();
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new ProfilesFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };
}