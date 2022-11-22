package com.evelyne.labs.trialapp.Adapter;

import android.app.AlertDialog;
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
import com.evelyne.labs.trialapp.model.CartModel;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyCartAdapter  extends RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder> {

    private Context context;
    private List<CartModel> cartModelList;

    public MyCartAdapter(Context context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
    }

    @NonNull
    @Override
    public MyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyCartViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_cart_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartViewHolder holder, int position) {
        Glide.with(context)
                .load(cartModelList.get(position).getImage())
                .into(holder.imageView);
        holder.txtPrice.setText(new StringBuilder("Ksh").append(cartModelList.get(position).getPrice()));
        holder.txtQuantity.setText(new StringBuilder("").append(cartModelList.get(position).getCustomercapacity()));
        holder.txtName.setText(new StringBuilder().append(cartModelList.get(position).getCompanyname()));

        //event
        holder.btnMinus.setOnClickListener(view -> {
            minusCartItem(holder,cartModelList.get(position));
        });
        holder.btnPlus.setOnClickListener(view -> {
            plusCartItem(holder,cartModelList.get(position));
        });
        holder.btnDelete.setOnClickListener(view -> {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Delete Item")
                    .setMessage("Do you really want to delete item from cart?")
                    .setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.dismiss())
                    .setPositiveButton("OK", (dialogInterface, i) -> {

                        //Temp remove
                        notifyItemRemoved(position);

                        deleteFromFirebase(cartModelList.get(position));
                        dialogInterface.dismiss();
                    }).create();
            dialog.show();
        });
    }

    private void deleteFromFirebase(CartModel cartModel) {
        FirebaseDatabase.getInstance().getReference("Users")
                .child("UNIQUE_USER_ID").child("Cart").child(cartModel.getKey())
                .removeValue()
                .addOnSuccessListener(aVoid-> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));
    }

    private void plusCartItem(MyCartViewHolder holder, CartModel cartModel) {
        if(cartModel.getCustomercapacity()>1) {
            cartModel.setCustomercapacity(cartModel.getCustomercapacity() + 1);
            cartModel.setTotalrice(cartModel.getCustomercapacity() * Float.parseFloat(cartModel.getPrice()));
            //update quantity
            holder.txtQuantity.setText(new StringBuilder().append(cartModel.getCustomercapacity()));
            updateFirebase(cartModel);
        }
        }

    private void minusCartItem(MyCartViewHolder holder, CartModel cartModel) {
        if(cartModel.getCustomercapacity() > 1) {
            cartModel.setCustomercapacity(cartModel.getCustomercapacity() - 1);
            cartModel.setTotalrice(cartModel.getCustomercapacity() * Float.parseFloat(cartModel.getPrice()));

            //update quantity
            holder.txtQuantity.setText(new StringBuilder().append(cartModel.getCustomercapacity()));
            updateFirebase(cartModel);
        }
    }

    private void updateFirebase(CartModel cartModel) {
        String timestamp = ""+System.currentTimeMillis();
        FirebaseDatabase.getInstance().getReference("Users")
                .child("UNIQUE_USER_ID").child("Cart").child(cartModel.getKey())
                .setValue(cartModel)
                .addOnSuccessListener(aVoid-> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class MyCartViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.btnMinus)
        ImageView btnMinus;
        @BindView(R.id.btnDelete)
        ImageView btnDelete;
        @BindView(R.id.btnPlus)
        ImageView btnPlus;
        @BindView(R.id.cartImageView)
        ImageView imageView;
        @BindView(R.id.txtCart)
        TextView txtName;
        @BindView(R.id.txtQuantity)
        TextView txtQuantity;
        @BindView(R.id.txtPrice)
        TextView txtPrice;

        Unbinder unbinder;
        public MyCartViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
        }
    }
}
