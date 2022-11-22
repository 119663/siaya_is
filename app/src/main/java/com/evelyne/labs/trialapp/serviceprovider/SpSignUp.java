package com.evelyne.labs.trialapp.serviceprovider;


import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.evelyne.labs.trialapp.R;
import com.evelyne.labs.trialapp.model.Providers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SpSignUp extends AppCompatActivity{

private EditText  fullnamesp,emailsp,companynamesp,phonenumbersp, passwordsp, confirmpasswordsp;
private Button registersp, loginsp;
private ProgressBar progressBar;
public static final String TAG= "SpSignUp";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_sign_up);

        fullnamesp = findViewById(R.id.fullnamesp);
        emailsp = findViewById(R.id.emailaddresssp);
        companynamesp = findViewById(R.id.companysp);
        phonenumbersp = findViewById(R.id.phonenumbersp);
        passwordsp = findViewById(R.id.passwordsp);
        confirmpasswordsp = findViewById(R.id.confirmpasswordsp);

        registersp = findViewById(R.id.registersp);
        loginsp = findViewById(R.id.loginsp);
        loginsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SpSignUp.this,SpLogIn.class);
                startActivity(intent);
            }
        });
        progressBar = findViewById(R.id.progressBar2);
        registersp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textfullnamesp = fullnamesp.getText().toString();
                String textemailsp = emailsp.getText().toString();
                String textcompanynamesp = companynamesp.getText().toString();
                String textphonenumbersp = phonenumbersp.getText().toString();
                String textpasswordsp = passwordsp.getText().toString();
                String textconfirmpasswordsp = confirmpasswordsp.getText().toString();

                if (TextUtils.isEmpty(textfullnamesp)) {
                    Toast.makeText(SpSignUp.this, "Please enter your full name", Toast.LENGTH_LONG).show();
                    fullnamesp.setError("Full name is required");
                    fullnamesp.requestFocus();
                } else if (TextUtils.isEmpty(textemailsp)) {
                    Toast.makeText(SpSignUp.this, "Please enter your email address", Toast.LENGTH_LONG).show();
                    emailsp.setError("Email address is required");
                    emailsp.requestFocus();
                } else if (TextUtils.isEmpty(textcompanynamesp)) {
                    Toast.makeText(SpSignUp.this, "Please enter your company name", Toast.LENGTH_LONG).show();
                    companynamesp.setError("Company name is required");
                    companynamesp.requestFocus();
                } else if (TextUtils.isEmpty(textphonenumbersp)) {
                    Toast.makeText(SpSignUp.this, "Please enter your phone number", Toast.LENGTH_LONG).show();
                    phonenumbersp.setError("Phone number is required");
                    phonenumbersp.requestFocus();
                } else if (textphonenumbersp.length() != 10) {
                    phonenumbersp.setError("Phone number length should be 10 characters");
                    phonenumbersp.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(textpasswordsp)) {
                    Toast.makeText(SpSignUp.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    passwordsp.setError("Password is required");
                    passwordsp.requestFocus();
                } else if (textpasswordsp.length() < 6) {
                    passwordsp.setError("Min password length should be 6 characters");
                    passwordsp.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(textconfirmpasswordsp)) {
                    Toast.makeText(SpSignUp.this, "Please enter your confirm password", Toast.LENGTH_LONG).show();
                    confirmpasswordsp.setError("Confirm password is required");
                    confirmpasswordsp.requestFocus();
                } else if (!textpasswordsp.equals(textconfirmpasswordsp)) {
                    confirmpasswordsp.setError("Passwords not matching");
                    confirmpasswordsp.requestFocus();
                    return;
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    registerProvider(textfullnamesp, textemailsp, textcompanynamesp, textphonenumbersp, textpasswordsp);
                }
            }
            });

    }
    //register user with credentials given
    private void registerProvider(String textfullnamesp, String textemailsp, String textcompanynamesp, String textphonenumbersp, String textpasswordsp) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textemailsp,textpasswordsp).addOnCompleteListener(SpSignUp.
                this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SpSignUp.this,"Successful Registration", Toast.LENGTH_LONG).show();
                    //send email to user
                    FirebaseUser firebaseUser = auth.getCurrentUser();



                    //enter user data into firebase realtime db
                    Providers writeUserDetails = new Providers(textfullnamesp,textemailsp,textcompanynamesp,textphonenumbersp);

                    //extracting providers reference from database for "Registered providers"
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Providers");

                    reference.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                //send verification email
                                firebaseUser.sendEmailVerification();

                                Toast.makeText(SpSignUp.this,"Successful registration",
                                        Toast.LENGTH_LONG).show();
                                //open login
                                Intent intent = new Intent(SpSignUp.this,SpLogIn.class);
                                //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                //  |Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else{

                                Toast.makeText(SpSignUp.this,"Registration Failed",
                                        Toast.LENGTH_LONG).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });


                } else{
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthUserCollisionException e){
                        emailsp.setError("Email already registered");
                        emailsp.requestFocus();
                    } catch( Exception e){
                        Log.e(TAG,e.getMessage());
                         Toast.makeText(SpSignUp.this, e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}