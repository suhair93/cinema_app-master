package com.cinema_app.customer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cinema_app.Adapter.SeatAdapter;
import com.cinema_app.R;
import com.cinema_app.models.Keys;
import com.cinema_app.models.Reservation;
import com.cinema_app.models.SeatList;
import com.cinema_app.models.movies;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.UUID;

public class ticket_barcode extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference ref;
    Bundle bundle;
    int id= 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialoge_barcode);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));

        }
        SharedPreferences prefs = getSharedPreferences(Keys.KEY_ID, MODE_PRIVATE);
        final String email_customer = prefs.getString(Keys.KEY_CUSTOMER, "");

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();


        final ImageView imageView = (ImageView)  findViewById(R.id.imageView);

        Query fireQuery1 = ref.child("reservation").orderByChild("email_customer").equalTo(email_customer);
        fireQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // ازا الحساب غير موجوديظهر مسج
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(getBaseContext(), "Not found", Toast.LENGTH_SHORT).show();

                    // ازا الحساب موجوديقوم بتخزين الحساب المدخل
                } else {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Reservation r = snapshot.getValue(Reservation.class);
                            id = r.getId();
                        // Whatever you need to encode in the QR code
                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                        try {
                            BitMatrix bitMatrix = multiFormatWriter.encode(id+"", BarcodeFormat.CODABAR, 250, 250);
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                            imageView.setImageBitmap(bitmap);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }

                    }



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getBaseContext(), "no connected internet", Toast.LENGTH_SHORT).show();
            }

        });


        Button done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ticket_barcode.this,MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }
}
