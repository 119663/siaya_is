package com.evelyne.labs.trialapp.customer;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.evelyne.labs.trialapp.R;
import com.evelyne.labs.trialapp.fragments.ProfileFragment;
import com.evelyne.labs.trialapp.serviceprovider.SpLogIn;
import com.evelyne.labs.trialapp.serviceprovider.SpMenu;
import com.evelyne.labs.trialapp.serviceprovider.SpSignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LogIn extends AppCompatActivity  {

    private EditText LoginCEmail, LoginCPassword;
    private Button logincc,registercc;
    private ProgressBar progressBarCC;
    private FirebaseAuth firebaseAuthC;
    private static final String TAG = "LogIn";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        LoginCEmail = findViewById(R.id.emailaddressC);
        LoginCPassword = findViewById(R.id.passwordC);
        logincc = findViewById(R.id.loginC);
        registercc = findViewById(R.id.registerC);
        registercc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogIn.this, SignUp.class);
                startActivity(intent);
            }
        });
        progressBarCC = findViewById(R.id.progressBarLC);

        firebaseAuthC = FirebaseAuth.getInstance();
        ImageView imageViewC = findViewById(R.id.loginImageC);
        imageViewC.setImageResource(R.drawable.ic_baseline_key_off_24);
        imageViewC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(LoginCPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    //hide password if its visible
                    LoginCPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //change icon
                    imageViewC.setImageResource(R.drawable.ic_baseline_key_off_24);
                }else{
                    LoginCPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewC.setImageResource(R.drawable.ic_baseline_key_off_24);
                }
            }
        });

        logincc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textemailC = LoginCEmail.getText().toString();
                String textpasswordC = LoginCPassword.getText().toString();

                if (TextUtils.isEmpty(textemailC)) {
                    Toast.makeText(LogIn.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    LoginCEmail.setError("Email is required");
                    LoginCEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textemailC).matches()) {
                    Toast.makeText(LogIn.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    LoginCEmail.setError("Valid email is required");
                    LoginCEmail.requestFocus();
                } else if (TextUtils.isEmpty(textpasswordC)) {
                    Toast.makeText(LogIn.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    LoginCPassword.setError("Password is required");
                    LoginCPassword.requestFocus();
                } else {
                    progressBarCC.setVisibility(View.VISIBLE);
                    loginUser(textemailC,textpasswordC);
                }
            }
        });
    }

    private void loginUser(String textemailC, String textpasswordC) {
        firebaseAuthC.signInWithEmailAndPassword(textemailC,textpasswordC).addOnCompleteListener(LogIn.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LogIn.this, "You are logged in", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LogIn.this, CustomerMenu.class));
                    finish();
                }else{
                    try{
                        throw task.getException();
                    } catch(FirebaseAuthInvalidUserException e){
                        LoginCEmail.setError("User doesnt exist.Please register again");
                        LoginCEmail.requestFocus();
                    } catch(FirebaseAuthInvalidCredentialsException e){
                        LoginCEmail.setError("Invalid credentials.Please register again or try logging in again");
                        LoginCEmail.requestFocus();
                    } catch (Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(LogIn.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                progressBarCC.setVisibility(View.GONE);
            }
        });
    }
    //check if user is logged in if yes take them to Profile/Menu
  /*  @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuthC.getCurrentUser() !=null){
            Toast.makeText(LogIn.this, "Already Logged In!", Toast.LENGTH_LONG).show();
            //take them to menu
            startActivity(new Intent(LogIn.this, CustomerMenu.class));
            finish();
        } else{
            Toast.makeText(LogIn.this, "LogIn now", Toast.LENGTH_LONG).show();
        }
    }*/
}

