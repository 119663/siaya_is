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

        orderBackBtn = findViewById(R.id.orderBackBtn);
        bookDateTv = findViewById(R.id.bookDateTv);
        bookIdTv = findViewById(R.id.bookIdTv);
        bookStatusTv = findViewById(R.id.bookStatusTv);
        bookCompanyTv = findViewById(R.id.bookCompanyTv);
        bookBookingsTv = findViewById(R.id.bookBookingsTv);
        bookAmountTv = findViewById(R.id.bookAmountTv);
        bookAddressTv = findViewById(R.id.bookAddressTv);
        itemsRv = findViewById(R.id.itemsRv);
    }
}