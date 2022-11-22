package com.evelyne.labs.trialapp.customer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.evelyne.labs.trialapp.R;
import com.evelyne.labs.trialapp.fragments.BookFragment;
import com.evelyne.labs.trialapp.listeners.ICartLoadListener;
import com.evelyne.labs.trialapp.model.CartModel;
import com.evelyne.labs.trialapp.serviceprovider.SpLogIn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookNowActivity extends AppCompatActivity  {
    private TextView textview;
    private Button book,back;
    DatePickerDialog picker;
    TimePickerDialog timepicker;
    private FirebaseAuth auth;
    private DatabaseReference mReference;

    //create object of DatabaseReference class to access firebase's Realtime Database
    //FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference myRef = database.getReference("");

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_now);
        final EditText capacity  = findViewById(R.id.capacity);
        final EditText date  = findViewById(R.id.date);
        final EditText time  = findViewById(R.id.time);
        final Button book = findViewById(R.id.book);
        final Button back = findViewById(R.id.back);
        auth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();


        time.setInputType(InputType.TYPE_NULL);
        time.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final Calendar cldr = Calendar.getInstance();
                                        int hour = cldr.get(Calendar.HOUR_OF_DAY);
                                        int minutes = cldr.get(Calendar.MINUTE);
                                        // time picker dialog
                                        timepicker = new TimePickerDialog(BookNowActivity.this,
                                                new TimePickerDialog.OnTimeSetListener() {
                                                    @Override
                                                    public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                                        time.setText(sHour + ":" + sMinute);
                                                    }
                                                }, hour, minutes, true);
                                        timepicker.show();
                                    }
                                });
        date.setInputType(InputType.TYPE_NULL);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(BookNowActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
       // @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final TextView loginBtnsp = findViewById(R.id.login);

        book.setOnClickListener(view -> {

            //get data from edits
            final String capacitytxt = capacity.getText().toString();
           // final String datetxt = date.getText().toString();
            // String timetxt = time.setText("Selected Date");
            date.setText("Selected Date: ");
            time.setText("Selected Time");

            String saveCurrentTime, saveCurrentDate;
            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat currentDate = new SimpleDateFormat("dd,MMM,yyyy");
            saveCurrentDate = currentDate.format(calendar.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTime.format(calendar.getTime());

            //check if user fills all fields before sending data to firebase
            if(capacitytxt.isEmpty() || date.isImportantForAutofill() ||time.isImportantForAutofill()
                   ){
                Toast.makeText(BookNowActivity.this, "Please fill all fields", Toast.LENGTH_SHORT
                ).show();
                // check if passwords are matching
            }
            else {
                String timestamp = ""+System.currentTimeMillis();
                mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                                     //sending data to firebase realtime database
                           HashMap<String,Object>hashMap = new HashMap<>();
                           hashMap.put("uploadId", ""+timestamp);
                           hashMap.put("timestamp", ""+timestamp);
                           hashMap.put("uid", ""+auth.getUid());
                            String capacitytxt = capacity.getText().toString().trim();
                           // final String datetxt = date.getText().toString();
                           String timetxt = time.getText().toString().trim();
                           String datetxt = date.getText().toString().trim();
                           date.setText("Selected Date: ") ;
                           time.setText("Selected Time");

                           HashMap uploadBookNow = new HashMap();
                           // uploadDetails.put("uid", Uid);
                         //  uploadBookNow.put("Company Name", CName);
                           uploadBookNow.put("Tank Capacity", capacitytxt);
                           uploadBookNow.put("Booking date", date);
                           uploadBookNow.put("Booking time", time);
                           uploadBookNow.put("Booking date", datetxt);
                           uploadBookNow.put("Booking time", timetxt);
                           uploadBookNow.put("IsUser", true);
                          //phone number is unique identifier so comes under all other details
                           /*databaseReference.child("book").child("capacity").setValue(capacitytxt);
                           databaseReference.child("book").child("time").setValue(time);
                           databaseReference.child("book").child("date").setValue(date);
                           databaseReference.child("book").child("bookingdate").setValue(currentDate);
                           databaseReference.child("book").child("bookingtime").setValue(currentTime); */
                          // databaseReference.child("Book").child(auth.getCurrentUser().getUid()).setValue(uploadBookNow);

                           mReference.child("Users").child(auth.getCurrentUser().getUid()).child("cart").child("book").child(timestamp).setValue(uploadBookNow);

                         //show success message and finish ativity
                  Toast.makeText(BookNowActivity.this, "User successfully registered", Toast.LENGTH_SHORT).show();
                  startActivity(new Intent(BookNowActivity.this, SpLogIn.class));
       finish();

                   }

         @Override
          public void onCancelled(@NonNull DatabaseError error) {

           }
           })

     //open register


                ;



                }

    });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookNowActivity.this, BookFragment.class);
                startActivity(intent);
            }
        });
    }


}



