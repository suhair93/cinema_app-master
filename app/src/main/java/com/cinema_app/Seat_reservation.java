package com.cinema_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cinema_app.Adapter.SeatAdapter;
import com.cinema_app.customer.buyTick;
import com.cinema_app.customer.visacard;
import com.cinema_app.models.Seat;
import com.cinema_app.models.SeatList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Seat_reservation extends AppCompatActivity {


    RecyclerView recyclerView;
    List<Seat> list = new ArrayList<>();
    SeatAdapter seatAdapter;
    ProgressDialog dialog;
    FirebaseDatabase database;
    DatabaseReference ref;
    String id = "";
  Button payment , cancle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_reservation);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id", "");
        }

        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        dialog = new ProgressDialog(Seat_reservation.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("loading...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Query fireQuery1 = ref.child("seat").orderByChild("id_movie").equalTo(id);
        fireQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // ازا الحساب غير موجوديظهر مسج
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(getBaseContext(), "Not found", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    // ازا الحساب موجوديقوم بتخزين الحساب المدخل
                } else {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SeatList seat = snapshot.getValue(SeatList.class);
                        list.addAll(seat.getSeatList());

                        seatAdapter = new SeatAdapter(getApplicationContext(), list, seat.getId_movie());
                        recyclerView.setAdapter(seatAdapter);
                        dialog.dismiss();

                    }


                    dialog.dismiss();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
                Toast.makeText(getBaseContext(), "no connected internet", Toast.LENGTH_SHORT).show();
            }

        });

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 10));

          payment = findViewById(R.id.next);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Seat_reservation.this, visacard.class);
//                i.putExtra("details", about.getText().toString());
//                i.putExtra("place", place.getText().toString());
//                i.putExtra("price", price.getText().toString());
//                i.putExtra("timeShow", timeShow.getText().toString());
//                i.putExtra("length", length.getText().toString());
//                i.putExtra("name", name);
                i.putExtra("id", id);

                startActivity(i);
            }
        });
  cancle = findViewById(R.id.cancel);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
