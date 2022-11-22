package com.evelyne.labs.trialapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evelyne.labs.trialapp.R;
import com.evelyne.labs.trialapp.model.Upload;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterUploadSeller extends RecyclerView.Adapter<AdapterUploadSeller.HolderUploadSeller>{

    private Context context;
    private ArrayList<Upload> uploadList;

    public AdapterUploadSeller(Context context, ArrayList<Upload> uploadList) {
        this.context = context;
        this.uploadList = uploadList;
    }

    @NonNull
    @Override
    public HolderUploadSeller onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_show_uploads,parent,false);

        return new HolderUploadSeller(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderUploadSeller holder, int position) {

        //get data
        Upload upload= uploadList.get(position);
        String id = upload.getUploadId();
        String uid= upload.getMuid();
        String companyname = upload.getMcompany();
        String capacity = upload.getMcapacity();
        String plate = upload.getMplate();
        String price = upload.getMprice();
        String Key = upload.getKey();
        String image = upload.getMimageUrl();
        String timestamp = upload.getMtimestamp();

        //set data
        holder.titlePrice.setText(price);
        holder.titleCompany.setText(companyname);
        holder.titleNoPlate.setText(plate);
        holder.titleCapacity.setText(capacity);
       try {
           Picasso.with(context).load(image).placeholder(R.drawable.image_donate).into(holder.uploadIconIv);
       }
       catch (Exception e){
           holder.uploadIconIv.setImageResource(R.drawable.image_donate);
       }
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        //handle item clicks show item details
    }
});
    }

    @Override
    public int getItemCount() {
        return uploadList.size();
    }

    class HolderUploadSeller extends RecyclerView.ViewHolder{

        //holds views of recycler view
        private ImageView uploadIconIv,nextIv;
        private TextView titleCompany,titleCapacity,titleNoPlate, titlePrice;

        public HolderUploadSeller(@NonNull View itemView) {
            super(itemView);
            uploadIconIv = itemView.findViewById(R.id.uploadIconIv);
            nextIv = itemView.findViewById(R.id.nextIv);
            titleCompany = itemView.findViewById(R.id.titleCompany);
            titleCapacity = itemView.findViewById(R.id.titleCapacity);
            titleNoPlate = itemView.findViewById(R.id.titleNoPlate);
            titlePrice = itemView.findViewById(R.id.titlePrice);
        }
    }
}
