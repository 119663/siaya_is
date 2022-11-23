package com.evelyne.labs.trialapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.evelyne.labs.trialapp.admin.AdminLogIn;
import com.evelyne.labs.trialapp.admin.AdminMenu;
import com.evelyne.labs.trialapp.customer.SignUp;
import com.evelyne.labs.trialapp.serviceprovider.SpLogIn;
import com.evelyne.labs.trialapp.serviceprovider.SpSignUp;

public class MainActivity extends AppCompatActivity {

    private TextView administrator, serviceprovider, customer;
    private Button customerBtn,adminBtn,servicepBtn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       customerBtn = findViewById(R.id.customer);
        adminBtn = findViewById(R.id.admin);
        servicepBtn = findViewById(R.id.serviceprovider);
        //open register
        customerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });
        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AdminMenu.class);
                startActivity(intent);
            }
        });
        servicepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SpSignUp.class);
                startActivity(intent);
            }
        });
    }
}