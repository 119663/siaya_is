package com.evelyne.labs.trialapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.evelyne.labs.trialapp.MapsActivity;
import com.evelyne.labs.trialapp.R;
import com.evelyne.labs.trialapp.ShowUploads;
import com.evelyne.labs.trialapp.eventbus.MyUpdateCartEvent;
import com.evelyne.labs.trialapp.listeners.ICartLoadListener;
import com.evelyne.labs.trialapp.listeners.IRecyclerViewListener;
import com.evelyne.labs.trialapp.model.CartModel;
import com.evelyne.labs.trialapp.model.Upload;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyBookAdapter extends RecyclerView.Adapter<MyBookAdapter.MyDrinkViewHolder> {
private Context context;
private List<Upload> uploadList;
private ICartLoadListener iCartLoadListener;

    public MyBookAdapter(Context context, List<Upload> uploadList, ICartLoadListener iCartLoadListener) {
        this.context = context;
        this.uploadList = uploadList;
        this.iCartLoadListener = iCartLoadListener;
    }

    public MyBookAdapter(Context context, List<Upload> uploadList) {
        this.context = context;
        this.uploadList = uploadList;
    }

    @NonNull
    @Override
    public MyDrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       return new MyDrinkViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.booklist,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyDrinkViewHolder holder, int position) {

        //Get data
        Upload uploadcurrent = uploadList.get(position);
        String CompanyName = uploadcurrent.getMcompany();
        String TankCapacity = uploadcurrent.getMcapacity();
        String ServicePrice = uploadcurrent.getMprice();
        String NumberPlate = uploadcurrent.getMplate();
        String timestamp = uploadcurrent.getMtimestamp();
//        String imageUri = uploadcurrent.getMimageUrl();


        // Set data

        holder.CompanyName.setText(""+CompanyName);
        holder.capacity.setText("Litres"+TankCapacity);
        holder.price.setText("Ksh"+ServicePrice);
        holder.plate.setText(""+NumberPlate);
        Glide.with(context).load(uploadcurrent.getMimageUrl()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MapsActivity.class);
                context.startActivity(intent);
            }
        });


        //Glide.with(context).load(uploadcurrent.getMimageUrl()).into(holder.imageView);

//        holder.setListener((view, adapterPosition) -> {
//            addToCart(uploadList.get(position));
//        });
    }

    private void addToCart(Upload upload) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference userCart = FirebaseDatabase
                .getInstance().getReference("Users")
                        .child("UNIQUE_USER_ID").child("Cart");

        userCart.child("Cart").equalTo(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                     if(snapshot.exists()) //if item is already in cart
                     {
                         //update company,capacity,plate and total price
                         CartModel cartModel = snapshot.getValue(CartModel.class);
                         cartModel.setCompanyname(cartModel.getCompanyname()+1);
                         cartModel.setCustomercapacity(cartModel.getCustomercapacity()+1);
                         cartModel.setCompanyname(cartModel.getCompanyname()+1);
                         cartModel.setPrice(cartModel.getPrice()+1);

                         Map<String,Object> updateData = new HashMap<>();
                         updateData.put("company",cartModel.getCompanyname()+1);
                         updateData.put("capacity",cartModel.getCustomercapacity()+1);
                         updateData.put("plate",cartModel.getPlate()+1);
                         updateData.put("price",cartModel.getPrice()+1);

                         userCart.child(upload.getKey())
                                 .updateChildren(updateData)
                                 .addOnSuccessListener(unused -> {
                                     iCartLoadListener.onCartLoadFailure("Add to Cart success");
                                 })
                                 .addOnFailureListener(e -> iCartLoadListener.onCartLoadFailure(e.getMessage()));
                     }else //iff item is not in cart add new
                     {
                       CartModel cartModel = new CartModel();
                       cartModel.setCompanyname(upload.getMcompany());
                         cartModel.setImage(upload.getMimageUrl());
                         cartModel.setPrice(upload.getMprice());
                         cartModel.setPlate(upload.getMplate());
                         cartModel.setKey(upload.getKey());
                         cartModel.setCapacity(upload.getMcapacity());
                         cartModel.setTotalrice(Float.parseFloat(upload.getMprice()));

                         userCart.child(upload.getKey())
                                 .setValue(cartModel).addOnSuccessListener(unused -> {
                                 }).addOnFailureListener(new OnFailureListener() {
                                     @Override
                                     public void onFailure(@NonNull Exception e) {
                                         iCartLoadListener.onCartLoadFailure(e.getMessage());
                                     }
                                 });
                         EventBus.getDefault().postSticky(new MyUpdateCartEvent());
                     }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
iCartLoadListener.onCartLoadFailure(error.getMessage());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return uploadList.size();
    }

    public class MyDrinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView  CompanyName,capacity,plate,price;
    private ImageView imageView;
    private Button bookspl;
        //for cart
        IRecyclerViewListener listener;

        //public void setListener(IRecyclerViewListener listener) {
            //this.listener = listener;
       // }

        private Unbinder unbinder;
        public MyDrinkViewHolder(@NonNull View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.bookImageView);
            CompanyName = itemView.findViewById(R.id.descriptionl);
            capacity = itemView.findViewById(R.id.capl);
            plate = itemView.findViewById(R.id.NoPlatel);
            price = itemView.findViewById(R.id.Bpricel);
            bookspl = itemView.findViewById(R.id.bookspl);
            unbinder = ButterKnife.bind(this,itemView);
            //for cart
            itemView.setOnClickListener(this);
        }

        //for cart
        @Override
        public void onClick(View view) {
            listener.onRecyclerClick(view,getAdapterPosition());
        }

    }
}
