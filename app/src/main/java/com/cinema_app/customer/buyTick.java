package com.cinema_app.customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cinema_app.Adapter.SeatAdapter;
import com.cinema_app.Adapter.comment_adapter;
import com.cinema_app.R;
import com.cinema_app.Seat_reservation;
import com.cinema_app.models.Keys;
import com.cinema_app.models.Seat;
import com.cinema_app.models.SeatList;
import com.cinema_app.models.comment;
import com.cinema_app.models.movies;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.dynamicbox.DynamicBox;


public class buyTick extends AppCompatActivity {
    String id = "", name = "";
    TextView about, place, timeShow, length, price;
    ImageView img;
    Button next;
    ProgressDialog dialog;
    FirebaseDatabase database;
    DatabaseReference ref;
    private Button add_btn;
    private List<comment> searchList = new ArrayList<comment>();
    private EditText add_comment;
    DynamicBox box;
    private List<comment> commentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private comment_adapter Adapter;
    SharedPreferences prefs;
    String id_customer = "", name_customer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_home);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button rate = findViewById(R.id.Rating);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater)  getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                View alertLayout = inflater.inflate(R.layout.rating_dialog, null);
                final AlertDialog.Builder alert = new AlertDialog.Builder(buyTick.this);
                alert.setView(alertLayout);
                final AlertDialog dialog1 = alert.create();
                dialog1.show();
                final   RatingBar ratingBar = (RatingBar) alertLayout.findViewById(R.id.ratingBar);
                final  TextView txtRatingValue= (TextView) alertLayout.findViewById(R.id.txtRatingValue);
                final   Button btnSubmit = (Button) alertLayout.findViewById(R.id.btnSubmit);


                ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
                    public void onRatingChanged(RatingBar ratingBar, float rating,
                                                boolean fromUser) {

                        txtRatingValue.setText(String.valueOf(rating));

                    }
                });
                //if click on me, then display the current rating value.
                btnSubmit.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Toast.makeText(buyTick.this,
                                String.valueOf(ratingBar.getRating()),
                                Toast.LENGTH_SHORT).show();

                        dialog1.cancel();

                    }

                });



            }
        });

        prefs = getSharedPreferences(Keys.KEY_ID, MODE_PRIVATE);
        id_customer = prefs.getString(Keys.KEY_CUSTOMER, "");
        name_customer = prefs.getString(Keys.KEY_NAME, "");


        about = findViewById(R.id.about);
        place = findViewById(R.id.lab);
        timeShow = findViewById(R.id.showTime);
        length = findViewById(R.id.length);
        img = findViewById(R.id.img);
        next = findViewById(R.id.next);
        about = findViewById(R.id.about);

        price = findViewById(R.id.price);

        dialog = new ProgressDialog(buyTick.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("loading...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        add_btn = (Button) findViewById(R.id.comment_btn);
        add_comment = (EditText) findViewById(R.id.add_comment);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id", "");
        }
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleview_comment);
        Adapter = new comment_adapter(buyTick.this, commentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(buyTick.this));
        recyclerView.setAdapter(Adapter);

        box = new DynamicBox(buyTick.this, recyclerView);
        box.showLoadingLayout();

        Query fireQuery = ref.child("movie").orderByChild("id").equalTo(id);
        fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // ازا الحساب غير موجوديظهر مسج
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(getBaseContext(), "Not found", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    // ازا الحساب موجوديقوم بتخزين الحساب المدخل
                } else {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        movies movies = snapshot.getValue(movies.class);
                        about.setText(movies.getDetails());
                        place.setText(movies.getScreen());
                        timeShow.setText(movies.getTime());
                        length.setText(movies.getDuration());
                        price.setText(movies.getPrice() + "");
                        name = movies.getName();
                        Glide.with(getBaseContext()).load(movies.getImg()).into(img);


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

        Query comment = ref.child("comment").orderByChild("movieId").equalTo(id);
        comment.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(buyTick.this, "Not found", Toast.LENGTH_SHORT).show();
                    box.hideAll();
                } else {
                    box.hideAll();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        comment c = snapshot.getValue(comment.class);
                        searchList.add(c);
                        Adapter = new comment_adapter(buyTick.this, searchList);
                        recyclerView.setAdapter(Adapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                box.hideAll();
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!id_customer.equals("")){
                comment comment = new comment();
                comment.setMovieId(id);
                comment.setComment(add_comment.getText().toString());
                comment.setName(name_customer);
                comment.setCustomerId(id_customer);
                ref.child("comment").push().setValue(comment);
                searchList.add(comment);
                    Adapter.notifyDataSetChanged();

                Toast.makeText(buyTick.this, "done", Toast.LENGTH_LONG).show();}
                else{
                    Toast.makeText(buyTick.this, "sing in please", Toast.LENGTH_LONG).show();
                }
            }
        });




        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(buyTick.this, Seat_reservation.class);
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


    }
}
