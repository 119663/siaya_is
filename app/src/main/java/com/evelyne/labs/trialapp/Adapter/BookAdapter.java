package com.evelyne.labs.trialapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.evelyne.labs.trialapp.R;
import com.evelyne.labs.trialapp.model.Bookings;
import com.evelyne.labs.trialapp.model.Upload;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder>{
    private Context context;
    private List<Upload> uploadsList;
    public BookAdapter(Context context, List<Upload> uploadsList) {
        this.context = context;
        this.uploadsList = uploadsList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(
                R.layout.booklist,parent,false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Get data
       final Upload uploadcurrent = uploadsList.get(position);
        String company = uploadcurrent.getMcompany();
        String capacity = uploadcurrent.getMcapacity();
        String price = uploadcurrent.getMprice();
        String plate = uploadcurrent.getMplate();
        String timestamp = uploadcurrent.getMtimestamp();


        // Set data

        holder.company.setText(""+company);
        holder.capacity.setText("Litres"+capacity);
        holder.price.setText("Ksh"+price);
        holder.plate.setText(""+plate);

       // holder.foodItemDate.setText(booking.getPickUpDate());
        //holder.foodItemNameLocation.setText(booking.getPickUpLocation());

        Glide.with(context).load(uploadcurrent.getMimageUrl()).into(holder.imageView);

        holder.approvebooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("APPROVE Booking")
                        .setMessage("Approve current booking?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });


            }
        });


    }

    @Override
    public int getItemCount() {

        return uploadsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        //UI Views

        public TextView company,capacity,plate,price;
        public ImageView imageView;
        public Button approvebooking;


        public ViewHolder( View itemView) {
            super(itemView);
           //initialize views
            company=itemView.findViewById(R.id.descriptionl);
            capacity = itemView.findViewById(R.id.capl);
            plate = itemView.findViewById(R.id.NoPlatel);
            price = itemView.findViewById(R.id.Bpricel);
            approvebooking = itemView.findViewById(R.id.bookspl);
            imageView = itemView.findViewById(R.id.bookImageView);
            //description = itemView.findViewById(R.id.descriPtion);
           // cap = itemView.findViewById(R.id.cap);
           // price = itemView.findViewById(R.id.Bprice);
            //noplate = itemView.findViewById(R.id.NoPlate);
            //foodItemDate = itemView.findViewById(R.id.foodItemNameDate);
           // foodItemNameLocation = itemView.findViewById(R.id.foodItemNameLocation);
            // bookingImage = itemView.findViewById(R.id.bookImageView);
           // approvebooking = itemView.findViewById(R.id.booksp);

        }
    }
}

