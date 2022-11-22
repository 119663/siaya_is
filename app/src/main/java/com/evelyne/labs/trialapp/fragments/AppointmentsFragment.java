package com.evelyne.labs.trialapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.evelyne.labs.trialapp.R;
import com.google.firebase.auth.FirebaseAuth;


public class AppointmentsFragment extends Fragment {

private RelativeLayout productRl;
    private FirebaseAuth firebaseAuth;
    private TextView uploadText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_appointments,null);
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_appointments, container, false);
        productRl = (RelativeLayout) root.findViewById(R.id.productRl);
        uploadText = (TextView) root.findViewById(R.id.uploadText);
        firebaseAuth=FirebaseAuth.getInstance();

        uploadText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAppointmentsUI();
            }
        });

       return root;
    }

    private void showAppointmentsUI() {
        productRl.setVisibility(View.VISIBLE);
        uploadText.setTextColor(getResources().getColor(R.color.black));
    }
}