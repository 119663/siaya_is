package com.evelyne.labs.trialapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.evelyne.labs.trialapp.R;
import com.evelyne.labs.trialapp.model.ModelOrderedItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookingFragmentAdapter extends RecyclerView.Adapter<BookingFragmentAdapter.MyViewHolder> {

    private Context context;
    private List<ModelOrderedItem> orderList;

    public BookingFragmentAdapter(Context context, List<ModelOrderedItem> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_booking_item,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //load default image from cart
      //  Glide.with(context).load(orderList.get(position).getCartItemList().get(0).getBookImage())
               // .into(holder.img_booking);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_booking_status)
        TextView txt_booking_status;
        @BindView(R.id.txt_booking_capacity)
        TextView txt_booking_capacity;
        @BindView(R.id.txt_booking_company)
        TextView txt_booking_company;
        @BindView(R.id.txt_booking_id)
        TextView txt_booking_id;
        @BindView(R.id.txt_booking_date)
        TextView txt_booking_date;
        @BindView(R.id.txt_booking_time)
        TextView txt_booking_time;
        @BindView(R.id.txt_booking_location)
        TextView txt_booking_location;
        @BindView(R.id.img_booking)
        ImageView img_booking;

        Unbinder unbinder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
        }
    }
}
