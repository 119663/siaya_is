package com.evelyne.labs.trialapp.fragments;

import android.app.ActionBar;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    private TextView textWelcome, textName, textEmail, textPhone, textCompany;
    private Button logout;
    private ProgressBar progressBar;
    private String name,email,phone,company;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_profile2,null);
       // ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Home");

        textWelcome = (TextView) root.findViewById(R.id.spProfileName);
        textName = (TextView) root.findViewById(R.id.pname);
        textEmail = (TextView) root.findViewById(R.id.pemail);
        textPhone = (TextView) root.findViewById(R.id.pphoneNumber);
        textCompany = (TextView) root.findViewById(R.id.pcompany);

        progressBar = (ProgressBar) root.findViewById(R.id.pprogressBar);

       logout= (Button) root.findViewById(R.id.pbutton2);
       logout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(getContext(), MainActivity.class);
               startActivity(intent);
           }
       });
       auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        //check if current user is null
        if(firebaseUser ==null){
            Toast.makeText(getContext(), "Something went wrong.User details not available now",
                    Toast.LENGTH_SHORT).show();
        }else{
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }

        // return inflater.inflate(R.layout.fragment_profile2, container, false);
        return root;
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        //extracting user reference from db for providers
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Providers");
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Providers providers = snapshot.getValue(Providers.class);
                if(providers !=null){
                    name = providers.name;
                    email = firebaseUser.getEmail();
                    phone = providers.phone;
                    company = providers.company;

                    textWelcome.setText("Welcome"+ name+"!");
                    textName.setText(name);
                    textEmail.setText(email);
                    textPhone.setText(phone);
                    textCompany.setText(company);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went wrong.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_profile2, container, false);
       // logout = (Button)view.findViewById(R.id.button2);
       // logout = (Button) getView().findViewById(R.id.button2);
