package com.evelyne.labs.trialapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

public class CustomerOrderDetails extends AppCompatActivity {

    private ImageButton orderBackBtn;
    private TextView bookIdTv,bookDateTv,bookStatusTv, bookCompanyTv,
            bookBookingsTv,bookAmountTv,bookAddressTv;
    private RecyclerView itemsRv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_details);
    }
}