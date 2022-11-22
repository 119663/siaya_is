package com.evelyne.labs.trialapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.evelyne.labs.trialapp.MainActivity;
import com.evelyne.labs.trialapp.R;
import com.evelyne.labs.trialapp.model.Providers;
import com.evelyne.labs.trialapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfilesFragment extends Fragment {
    private TextView textWelcomeC, textNameC, textEmailC, textPhoneC;
    private Button logoutC;
    private ProgressBar progressBarC;
    private String nameC,emailC,phoneC;
    private FirebaseAuth authC;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_profiles,null);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("My Profile");

        textWelcomeC = (TextView) root.findViewById(R.id.CProfileName);
        textNameC = (TextView) root.findViewById(R.id.Cname);
        textEmailC = (TextView) root.findViewById(R.id.Cemail);
        textPhoneC = (TextView) root.findViewById(R.id.CphoneNumber);

        progressBarC = (ProgressBar) root.findViewById(R.id.CprogressBar);

        logoutC= (Button) root.findViewById(R.id.Cbutton2);
        logoutC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        authC = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authC.getCurrentUser();

        //check if current user is null
        if(firebaseUser ==null){
            Toast.makeText(getContext(), "Something went wrong.User details not available now",
                    Toast.LENGTH_SHORT).show();
        }else{
            progressBarC.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }

        // return inflater.inflate(R.layout.fragment_profile2, container, false);
        return root;
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        //extracting user reference from db for providers
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User users = snapshot.getValue(User.class);
                if(users !=null){
                    nameC = users.nameC;
                    emailC = firebaseUser.getEmail();
                    phoneC = users.phoneC;

                    textWelcomeC.setText("Welcome"+""+ nameC+"!");
                    textNameC.setText(nameC);
                    textEmailC.setText(emailC);
                    textPhoneC.setText(phoneC);
                }
                progressBarC.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went wrong.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}

