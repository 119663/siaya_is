package com.evelyne.labs.trialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.evelyne.labs.trialapp.Adapter.AdapterUploadSeller;
import com.evelyne.labs.trialapp.fragments.UploadFragment;
import com.evelyne.labs.trialapp.model.Upload;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class ShowUploads extends AppCompatActivity {

private RelativeLayout productsRl;
private ImageView appBack;
private TextView uploadsText;
private RecyclerView myUploads;

private ArrayList<Upload> uploadList;
private AdapterUploadSeller adapterUploadSeller;

private FirebaseAuth firebaseAuth;
private DatabaseReference mReference;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_uploads);

        productsRl=findViewById(R.id.productsRl);
        appBack = findViewById(R.id.appBack);
        uploadsText=findViewById(R.id.uploadsText);
        myUploads = findViewById(R.id.myUploads);
        firebaseAuth=FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        showUploadsUI();
        loadAllProducts();

        appBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowUploads.this, UploadFragment.class);
                startActivity(intent);
            }
        });

        uploadsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUploadsUI();
            }
        });
    }

    private void loadAllProducts() {
        uploadList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Providers/uploads");
        reference.child(firebaseAuth.getUid()).child("uploads").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        uploadList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Upload upload = dataSnapshot.getValue(Upload.class);
                            uploadList.add(upload);
                        }
                        //setup adapter
                        adapterUploadSeller = new AdapterUploadSeller(uploadList);
                        //set adapter
                        myUploads.setAdapter(adapterUploadSeller);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void showUploadsUI() {
        //show uploads ui
        productsRl.setVisibility(View.VISIBLE);
        uploadsText.setTextColor(getResources().getColor(R.color.black));

    }

}