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

import org.greenrobot.eventbus.EventBus;

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

    public MyBookAdapter(Context context, List<Upload> uploadModelList) {
        this.context = context;
        this.uploadList = uploadList;
    }

    @NonNull
    @Override
    public MyDrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       return new MyDrinkViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.booklist,parent,false));
        // MyDrinkViewHolder v =LayoutInflater.from(context)
       //.inflate(R.layout.booklist,parent,false);

    }

    @Override
    public void onBindViewHolder(@NonNull MyDrinkViewHolder holder, int position) {

        //Get data
        final Upload uploadcurrent = uploadList.get(position);
        String company = uploadcurrent.getMcompany();
        String capacity = uploadcurrent.getMcapacity();
        String price = uploadcurrent.getMprice();
        String plate = uploadcurrent.getMplate();
        String timestamp = uploadcurrent.getMtimestamp();


        // Set data

        holder.companyname.setText(""+company);
        holder.capacity.setText("Litres"+capacity);
        holder.price.setText("Ksh"+price);
        holder.plate.setText(""+plate);

        // holder.foodItemDate.setText(booking.getPickUpDate());
        //holder.foodItemNameLocation.setText(booking.getPickUpLocation());

        Glide.with(context).load(uploadcurrent.getMimageUrl()).into(holder.imageView);
       /* final Upload uploads = uploadList.get(position);
       // Glide.with(context)
                //.load(uploadList.get(position).getMimageUrl())
                        //.into(holder.imageView);
        Glide.with(context)
                .load(uploads.getMimageUrl())
                .into(holder.imageView);
       // holder.price.setText(new StringBuilder("Ksh").append(uploads.getMprice()));
        holder.price.setText(new StringBuilder("Ksh").append(uploadList.get(position).getMprice()));
        holder.capacity.setText(new StringBuilder("Litres").append(uploadList.get(position).getMcapacity()));
        holder.plate.setText(new StringBuilder().append(uploadList.get(position).getMplate()));
        holder.companyname.setText(new StringBuilder().append(uploadList.get(position).getMcompany()));
       /* holder.capacity.setText(new StringBuilder("Litres").append(uploads.getMcapacity()));
        holder.plate.setText(new StringBuilder().append(uploads.getMplate()));
        holder.companyname.setText(new StringBuilder().append(uploads.getMcompany())); */
        //for cart
        holder.setListener((view, adapterPosition) -> {
            addToCart(uploadList.get(position));
        });
    }

    private void addToCart(Upload upload) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference userCart = FirebaseDatabase
                .getInstance().getReference("Users")
                        .child("UNIQUE_USER_ID").child("Cart");
               // .child("UNIQUE_USER_ID");

       // userCart.child(upload.getMuid())
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
      /*  @BindView(R.id.bookImageView)
        ImageView imageView;
        @BindView(R.id.descriptionl)
        TextView companyname;
        @BindView(R.id.capl)
        TextView capacity;
        @BindView(R.id.NoPlatel)
        TextView plate;
        @BindView(R.id.Bpricel)
        TextView price; */
private TextView  companyname,capacity,plate,price;
private ImageView imageView;
        //for cart
        IRecyclerViewListener listener;

        public void setListener(IRecyclerViewListener listener) {
            this.listener = listener;
        }

        private Unbinder unbinder;
        public MyDrinkViewHolder(@NonNull View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.bookImageView);
            companyname = itemView.findViewById(R.id.descriptionl);
            capacity = itemView.findViewById(R.id.capl);
            plate = itemView.findViewById(R.id.NoPlatel);
            price = itemView.findViewById(R.id.Bpricel);
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
