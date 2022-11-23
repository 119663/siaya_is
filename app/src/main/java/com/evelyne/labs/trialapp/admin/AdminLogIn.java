package com.evelyne.labs.trialapp.admin;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.evelyne.labs.trialapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLogIn extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl
            ("https://trial-app-6d0cb-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_log_in);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText Pass = findViewById(R.id.passwordad);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText Phone= findViewById(R.id.emailaddressad);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final Button loginBtnad = findViewById(R.id.loginad);
       // @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final TextView registerBtnad = findViewById(R.id.registerad);



        loginBtnad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Phonetxt = Phone.getText().toString();
                final String Passtxt= Pass.getText().toString();

                if(Phonetxt.isEmpty() || Passtxt.isEmpty()){
                    Toast.makeText(AdminLogIn.this, "Please enter both Emailaddress and Password", Toast.LENGTH_SHORT).show();

                } else {
                    databaseReference.child("Admin").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // check if phone number exist in database
                            if(snapshot.hasChild(Phonetxt)){
                                // no exist in db
                                //now get password of user from db and match
                                final String getpasswordsp = snapshot.child(Phonetxt).child("Pass")
                                        .getValue(String.class);
                                if(getpasswordsp.equals(Passtxt)){
                                    Toast.makeText(AdminLogIn.this,"LogIn Successful", Toast.LENGTH_SHORT).show();

                                    // open mainActivity on successful login
                                    startActivity(new Intent(AdminLogIn.this, AdminMenu.class));
                                    finish();
                                }
                                else{
                                    Toast.makeText(AdminLogIn.this,"Wrong Password .Try Again"
                                            , Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(AdminLogIn.this,"Wrong Phone Number.Try Again",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        //open register
       // registerBtnsp.setOnClickListener(new View.OnClickListener() {
          //  @Override
           // public void onClick(View view) {
              //  startActivity(new Intent(SpLogIn.this, SpSignUp.class));
           // }
       // });


    }
}


