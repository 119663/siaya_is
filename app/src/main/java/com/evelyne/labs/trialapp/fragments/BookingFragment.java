package com.evelyne.labs.trialapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.evelyne.labs.trialapp.MainActivity;
import com.evelyne.labs.trialapp.MapsActivity;
import com.evelyne.labs.trialapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookingFragment extends Fragment {

  // private Button btn;
    @BindView(R.id.recycler_orders)
    RecyclerView recycler_orders;

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_booking, container, false);
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_booking,null);
        unbinder = ButterKnife.bind(this,root);

      /*  btn = (Button) root.findViewById(R.id.bookingloc);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), MapsActivity.class);
                startActivity(intent);
            }
        }); */
        return root;
    }
}