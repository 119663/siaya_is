package com.evelyne.labs.trialapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.evelyne.labs.trialapp.R;
import com.evelyne.labs.trialapp.ShowUploads;
import com.evelyne.labs.trialapp.serviceprovider.SpReportsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;


public class ManageFragment extends Fragment {

    private Button managesp;
    private Button managecustomers;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_manage,null);
        managesp = (Button) root.findViewById(R.id.msp);
        managesp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SpReportsActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}