package com.cinema_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cinema_app.Login;
import com.cinema_app.R;
import com.cinema_app.customer.buyTick;
import com.cinema_app.models.Keys;
import com.cinema_app.models.Seat;
import com.cinema_app.models.movies;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class SeatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    FirebaseDatabase database;
    DatabaseReference ref;
    private List<Seat> list ;
    private Context context;
    String idMovie="";
    private boolean status = false;
    public SeatAdapter(Context context, List<Seat> List1,String idMovie) {
        this.context = context;
        this.list = List1;
        this.idMovie =idMovie;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_seat, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        SharedPreferences prefs =  context.getSharedPreferences(Keys.KEY_ID, MODE_PRIVATE);
        final String  email_customer = prefs.getString(Keys.KEY_CUSTOMER,"");

       final Seat seat = list.get(position);
        final Holder holder1 = (Holder) holder;
        if(seat.isStatus().equals(true+"")){
            holder1.itemView.setClickable(false);
            holder1.imageView.setBackgroundResource(R.drawable.select);
        }else {
            holder1.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (status == false) {
                                        holder1.imageView.setBackgroundResource(R.drawable.select);
                                        status = true;
                                        seat.setStatus(true + "");
                                        seat.setId_customer(email_customer);

                                        Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();

                                    } else {
                                        holder1.imageView.setBackgroundResource(R.drawable.seat);
                                        status = false;
                                        seat.setStatus(false + "");
                                        seat.setId_customer("");

                                        Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                                    }
                    final Query query1 = ref.child("seat").orderByChild("id_movie").equalTo(idMovie);

                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){


                                        snapshot.getRef().child("seatList").child(position+"").child("status").setValue(seat.isStatus());
                                        snapshot.getRef().child("seatList").child(position+"").child("id_customer").setValue(seat.getId_customer());
                                        Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();


                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });




                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);

    }
    public class Holder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public Holder(View itemView) {
            super(itemView);
           imageView = itemView.findViewById(R.id.img);

        }

    }
}






