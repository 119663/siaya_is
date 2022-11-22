package com.evelyne.labs.trialapp.customer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.evelyne.labs.trialapp.MainActivity;
import com.evelyne.labs.trialapp.R;
import com.evelyne.labs.trialapp.model.Providers;
import com.evelyne.labs.trialapp.model.User;
import com.evelyne.labs.trialapp.serviceprovider.SpLogIn;
import com.evelyne.labs.trialapp.serviceprovider.SpSignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    private EditText  fullname,email,companyname,phonenumber, password, confirmpassword;
    private Button register, login;
    private ProgressBar progressBar;
    public static final String TAG= "SignUp";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fullname = findViewById(R.id.fullnamec);
        email = findViewById(R.id.emailaddressc);
        phonenumber = findViewById(R.id.phonenumberc);
        password = findViewById(R.id.passwordc);
        confirmpassword = findViewById(R.id.confirmpasswordc);

        register = findViewById(R.id.registerc);
        login = findViewById(R.id.loginc);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, LogIn.class);
                startActivity(intent);
            }
        });
        progressBar = findViewById(R.id.progressBarc);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textfullnamec = fullname.getText().toString();
                String textemailc = email.getText().toString();
                String textphonenumberc = phonenumber.getText().toString();
                String textpasswordc = password.getText().toString();
                String textconfirmpasswordc = confirmpassword.getText().toString();

                if (TextUtils.isEmpty(textfullnamec)) {
                    Toast.makeText(SignUp.this, "Please enter your full name", Toast.LENGTH_LONG).show();
                    fullname.setError("Full name is required");
                    fullname.requestFocus();
                } else if (TextUtils.isEmpty(textemailc)) {
                    Toast.makeText(SignUp.this, "Please enter your email address", Toast.LENGTH_LONG).show();
                    email.setError("Email address is required");
                    email.requestFocus();
                } else if (TextUtils.isEmpty(textphonenumberc)) {
                    Toast.makeText(SignUp.this, "Please enter your phone number", Toast.LENGTH_LONG).show();
                    phonenumber.setError("Phone number is required");
                    phonenumber.requestFocus();
                } else if (textphonenumberc.length() != 10) {
                    phonenumber.setError("Phone number length should be 10 characters");
                    phonenumber.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(textpasswordc)) {
                    Toast.makeText(SignUp.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    password.setError("Password is required");
                    password.requestFocus();
                } else if (textpasswordc.length() < 6) {
                    password.setError("Min password length should be 6 characters");
                    password.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(textconfirmpasswordc)) {
                    Toast.makeText(SignUp.this, "Please enter your confirm password", Toast.LENGTH_LONG).show();
                    confirmpassword.setError("Confirm password is required");
                    confirmpassword.requestFocus();
                } else if (!textpasswordc.equals(textconfirmpasswordc)) {
                    confirmpassword.setError("Passwords not matching");
                    confirmpassword.requestFocus();
                    return;
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    registerCustomer(textfullnamec, textemailc, textphonenumberc, textpasswordc);
                }
            }
        });
    }
    //register user with credentials given
    private void registerCustomer(String textfullnamec, String textemailc, String textphonenumberc, String textpasswordc) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textemailc,textpasswordc).addOnCompleteListener(SignUp.
                this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUp.this,"Successful Registration", Toast.LENGTH_LONG).show();
                    //send email to user
                    FirebaseUser firebaseUser = auth.getCurrentUser();



                    //enter user data into firebase realtime db
                    User writeUserDetails = new User(textfullnamec,textemailc,textphonenumberc);

                    //extracting providers reference from database for "Registered providers"
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

                    reference.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                //send verification email
                                firebaseUser.sendEmailVerification();

                                Toast.makeText(SignUp.this,"Successful registration",
                                        Toast.LENGTH_LONG).show();
                                //open login
                                Intent intent = new Intent(SignUp.this,LogIn.class);
                                //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                //  |Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else{

                                Toast.makeText(SignUp.this,"Registration Failed",
                                        Toast.LENGTH_LONG).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                }else{
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthUserCollisionException e){
                        email.setError("Email already registered");
                        email.requestFocus();
                    } catch( Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(SignUp.this, e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}