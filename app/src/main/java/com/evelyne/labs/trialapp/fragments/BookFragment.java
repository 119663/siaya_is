package com.evelyne.labs.trialapp.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evelyne.labs.trialapp.Adapter.BookAdapter;
import com.evelyne.labs.trialapp.Adapter.MyBookAdapter;
import com.evelyne.labs.trialapp.CartActivity;
import com.evelyne.labs.trialapp.R;
import com.evelyne.labs.trialapp.customer.BookNowActivity;
import com.evelyne.labs.trialapp.eventbus.MyUpdateCartEvent;
import com.evelyne.labs.trialapp.listeners.IBookLoadListener;
import com.evelyne.labs.trialapp.listeners.ICartLoadListener;
import com.evelyne.labs.trialapp.model.CartModel;
import com.evelyne.labs.trialapp.model.Upload;
import com.evelyne.labs.trialapp.utilis.SpaceItemDecoration;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BookFragment extends Fragment implements IBookLoadListener, ICartLoadListener {
 /*@BindView(R.id.recview)
 RecyclerView recview;
 @BindView(R.id.bookLayout)
 RelativeLayout booklayout;
 @BindView(R.id.badge)
 NotificationBadge badge;
 @BindView(R.id.btnCart)
 FrameLayout btnCart; */
    private RecyclerView recview;
    private RelativeLayout booklayout;
    private NotificationBadge badge;
    private FrameLayout btnCart;

    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    private MyBookAdapter adapter;
    private ArrayList<Upload> uploadList;

 ICartLoadListener cartLoadListener;
 IBookLoadListener bookLoadListener;

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
            public void onUpdateCart(MyUpdateCartEvent event)
    {
        countCartItem();
    }

    // creating a variable for our Firebase Database.
   // FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database Reference for Firebase.
    //DatabaseReference databaseReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_book,null);
       // return inflater.inflate(R.layout.fragment_mybookings, container, false);
        // Inflate the layout for this fragment
        recview = (RecyclerView) root.findViewById(R.id.recview);
        booklayout = (RelativeLayout) root.findViewById(R.id.bookLayout);
        badge = (NotificationBadge) root.findViewById(R.id.badge);
        btnCart = (FrameLayout) root.findViewById(R.id.btnCart);
        // of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        uploadList = new ArrayList<>();
        // init();
         loadUploadFromFirebase();
         countCartItem();
         initializ();
          return root;
        //return null;
    }


    private void loadUploadFromFirebase() {
        //List<Upload> uploadss = new ArrayList<>();
       DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Providers");
        reference.child(mAuth.getUid()).child("uploads")
       // FirebaseDatabase.getInstance()
                //getReference("uploads")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                      if(snapshot.exists())
                      {
                          for(DataSnapshot uploadSnapshot: snapshot.getChildren())
                          {
                              Upload uploads = uploadSnapshot.getValue(Upload.class);
                              //uploads.setKey(uploadSnapshot.getKey());
                              uploadList.add(uploads);
                          }

                          adapter = new MyBookAdapter(getContext(),uploadList);

                          recview.setAdapter(adapter);
                          bookLoadListener.onUploadLoadSuccess(uploadList);
                      }
                      else
                          bookLoadListener.onUploadLoadFailure("Cant find uploads");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                       bookLoadListener.onUploadLoadFailure(error.getMessage());
                    }
                });
    }
    private void initializ() {
            ButterKnife.bind(this, requireActivity());

            bookLoadListener = this;
            cartLoadListener = this;
            GridLayoutManager gridLayoutManager= new GridLayoutManager(this.getContext(),2);
            recview.setLayoutManager(gridLayoutManager);
            recview.addItemDecoration(new SpaceItemDecoration());

            btnCart.setOnClickListener(view ->
                    startActivity(new Intent(this.getContext(), CartActivity.class)));
    }
  /*  private void init(){
       ButterKnife.bind(this, requireActivity());

       bookLoadListener = this;
        cartLoadListener = this;
       GridLayoutManager gridLayoutManager= new GridLayoutManager(this.getContext(),2);
       recview.setLayoutManager(gridLayoutManager);
       recview.addItemDecoration(new SpaceItemDecoration());

       btnCart.setOnClickListener(view ->
           startActivity(new Intent(this.getContext(), CartActivity.class)));



   } */

    @Override
    public void onUploadLoadSuccess(List<Upload> uploadModelList) {
        MyBookAdapter adapter = new MyBookAdapter(this.getContext(),uploadModelList,cartLoadListener);
        recview.setAdapter(adapter);
        loadUploadFromFirebase();
    }

    @Override
    public void onUploadLoadFailure(String Message) {
        Snackbar.make(booklayout,"Upload load failure",Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
             int cartsum = 0;
             for(CartModel cartModel:cartModelList)
                 cartsum = Integer.parseInt(cartsum + cartModel.getPrice());
             badge.setNumber(cartsum);

    }

    @Override
    public void onCartLoadFailure(String Message) {
            Snackbar.make(booklayout,Message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        countCartItem();
    }

    private void countCartItem() {
          List<CartModel> cartModels = new ArrayList<>();
                    FirebaseDatabase.getInstance().getReference("Cart")
                            .child("UNIQUE_USER_ID")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot cartSnapshot: snapshot.getChildren())
                                    {
                                        CartModel cartModel = cartSnapshot.getValue(CartModel.class);
                                        cartModel.setKey(cartSnapshot.getKey());
                                        cartModels.add(cartModel);
                                    }
                                    cartLoadListener.onCartLoadSuccess(cartModels);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
cartLoadListener.onCartLoadFailure(error.getMessage());
                                }
                            });
    }
}