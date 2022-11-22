package com.evelyne.labs.trialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.evelyne.labs.trialapp.Adapter.MyCartAdapter;
import com.evelyne.labs.trialapp.eventbus.MyUpdateCartEvent;
import com.evelyne.labs.trialapp.listeners.ICartLoadListener;
import com.evelyne.labs.trialapp.model.CartModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends AppCompatActivity implements ICartLoadListener {

    @BindView(R.id.recycler_cart)
    RecyclerView recyclerCart;
    @BindView(R.id.cartLayout)
    RelativeLayout mainLayout;
    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.txtTotal)
    TextView txtTotal;
    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    // Database Reference for Firebase.
    DatabaseReference databaseReference;
    ICartLoadListener cartLoadListener;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        if(EventBus.getDefault().hasSubscriberForEvent(MyUpdateCartEvent.class))
            EventBus.getDefault().removeStickyEvent(MyUpdateCartEvent.class);
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true )
    public void onUpdateCart(MyUpdateCartEvent event){
        loadCartFromFirebase();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        btnBack = findViewById(R.id.btnBack);
        txtTotal = findViewById(R.id.txtTotal);
        mainLayout = findViewById(R.id.cartLayout);
        recyclerCart = findViewById(R.id.recycler_cart);
        // below line is used to get the instance
        // of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        init();
        loadCartFromFirebase();
    }

    private void loadCartFromFirebase() {
        List<CartModel>cartModels = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Cart")
                .child("UNIQUE_USER_ID").child("Cart")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
if(snapshot.exists())
{
  for(DataSnapshot cartSnapshot : snapshot.getChildren())
  {
      CartModel cartModel= cartSnapshot.getValue(CartModel.class);
      cartModel.setKey(cartSnapshot.getKey());
      cartModels.add(cartModel);
  }
  cartLoadListener.onCartLoadSuccess(cartModels);
}else
    cartLoadListener.onCartLoadFailure("Cart empty");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
cartLoadListener.onCartLoadFailure(error.getMessage());
                    }
                });
    }

    private void init(){
        ButterKnife.bind(this);

        cartLoadListener=this;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerCart.setLayoutManager(layoutManager);
        recyclerCart.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));

        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
        double sum = 0;
        for (CartModel cartModel: cartModelList){
            sum+=cartModel.getTotalrice();
        }
        txtTotal.setText(new StringBuilder("Ksh").append(sum));
        MyCartAdapter adapter = new MyCartAdapter(this, cartModelList);
        recyclerCart.setAdapter(adapter);
    }

    @Override
    public void onCartLoadFailure(String Message) {
        Snackbar.make(mainLayout,Message,Snackbar.LENGTH_LONG).show();
    }
}