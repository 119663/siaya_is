package com.evelyne.labs.trialapp.serviceprovider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.evelyne.labs.trialapp.R;
import com.evelyne.labs.trialapp.fragments.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class SpLogIn extends AppCompatActivity {

    private EditText LoginEmail, LoginPassword;
    private Button loginspp,registerspp;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "SpLogIn";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_log_in);

        LoginEmail = findViewById(R.id.emailaddressspp);
        LoginPassword = findViewById(R.id.passwordspp);
        loginspp = findViewById(R.id.loginspp);
        registerspp = findViewById(R.id.registerspp);
        registerspp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SpLogIn.this,SpSignUp.class);
                startActivity(intent);
            }
        });
        progressBar = findViewById(R.id.progressBar3);

        firebaseAuth = FirebaseAuth.getInstance();
        ImageView imageView = findViewById(R.id.loginImage);
        imageView.setImageResource(R.drawable.ic_baseline_key_off_24);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(LoginPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    //hide password if its visible
                    LoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //change icon
                    imageView.setImageResource(R.drawable.ic_baseline_key_off_24);
                }else{
                    LoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageView.setImageResource(R.drawable.ic_baseline_key_off_24);
                }
            }
        });

        loginspp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textemail = LoginEmail.getText().toString();
                String textpassword = LoginPassword.getText().toString();

                if (TextUtils.isEmpty(textemail)) {
                    Toast.makeText(SpLogIn.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    LoginEmail.setError("Email is required");
                    LoginEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textemail).matches()) {
                    Toast.makeText(SpLogIn.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    LoginEmail.setError("Valid email is required");
                    LoginEmail.requestFocus();
                } else if (TextUtils.isEmpty(textpassword)) {
                    Toast.makeText(SpLogIn.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    LoginPassword.setError("Password is required");
                    LoginPassword.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(textemail,textpassword);
                }
            }
        });
    }

    private void loginUser(String textemail, String textpassword) {
        firebaseAuth.signInWithEmailAndPassword(textemail,textpassword).addOnCompleteListener(SpLogIn.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SpLogIn.this, "You are logged in", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SpLogIn.this,SpMenu.class));
                    finish();
                }else{
                    try{
                        throw task.getException();
                    } catch(FirebaseAuthInvalidUserException e){
                        LoginEmail.setError("User doesnt exist.Please register again");
                        LoginEmail.requestFocus();
                    } catch(FirebaseAuthInvalidCredentialsException e){
                        LoginEmail.setError("Invalid credentials.Please register again or try logging in again");
                        LoginEmail.requestFocus();
                    } catch (Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(SpLogIn.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
//check if user is logged in if yes take them to Profile/Menu
   /* @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser() !=null){
            Toast.makeText(SpLogIn.this, "Already Logged In!", Toast.LENGTH_LONG).show();
            //take them to menu
            startActivity(new Intent(SpLogIn.this,SpMenu.class));
            finish();
        } else{
            Toast.makeText(SpLogIn.this, "LogIn now", Toast.LENGTH_LONG).show();
        }
    } */
}


