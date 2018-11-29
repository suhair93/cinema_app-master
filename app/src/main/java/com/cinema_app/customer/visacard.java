package com.cinema_app.customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.view.CardEditText;
import com.braintreepayments.cardform.view.CardForm;
import com.braintreepayments.cardform.view.SupportedCardTypesView;
import com.bumptech.glide.Glide;
import com.cinema_app.Adapter.SeatAdapter;
import com.cinema_app.R;
import com.cinema_app.models.Keys;
import com.cinema_app.models.Reservation;
import com.cinema_app.models.Seat;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class visacard extends AppCompatActivity implements OnCardFormSubmitListener,
        CardEditText.OnCardTypeChangedListener {
    ProgressDialog progressDialog;
    DatabaseReference ref;
    FirebaseDatabase database;
    Bundle bundle;
    int price1 = 0;
    int count =0 ;
    String about= "",place= "",timeShow="" ,length="",id = "",name="";
    TextView price,number;
    List<Seat> list = new ArrayList<>();
    private static final CardType[] SUPPORTED_CARD_TYPES = { CardType.VISA, CardType.MASTERCARD };

    private SupportedCardTypesView mSupportedCardTypesView;

    protected CardForm mCardForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_payment);

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

        mSupportedCardTypesView = findViewById(R.id.supported_card_types);
        mSupportedCardTypesView.setSupportedCardTypes(SUPPORTED_CARD_TYPES);


        mCardForm = findViewById(R.id.card_form);
        mCardForm.cardRequired(true)
                .maskCardNumber(true)
                .maskCvv(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is enabled for this mobile number")
                .actionLabel("purchase")
                .setup(this);
        mCardForm.setOnCardFormSubmitListener(this);
        mCardForm.setOnCardTypeChangedListener(this);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);


        bundle = getIntent().getExtras();
        if (bundle != null) {

            id = bundle.getString("id");



        }
        Query fireQuery = ref.child("movie").orderByChild("id").equalTo(id);
        fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // ازا الحساب غير موجوديظهر مسج
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(getBaseContext(), "Not found", Toast.LENGTH_SHORT).show();

                    // ازا الحساب موجوديقوم بتخزين الحساب المدخل
                } else {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        movies movies = snapshot.getValue(movies.class);
                        about=(movies.getDetails());
                        place=(movies.getScreen());
                        timeShow=(movies.getTime());
                        length=(movies.getDuration());
                        price1= (movies.getPrice());
                        name = movies.getName();



                    }




                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getBaseContext(), "no connected internet", Toast.LENGTH_SHORT).show();
            }

        });

        price = findViewById(R.id.price);
        number = findViewById(R.id.number);

        Query fireQuery1 = ref.child("seat").orderByChild("id_movie").equalTo(id);
        fireQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // ازا الحساب غير موجوديظهر مسج
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(getBaseContext(), "Not found", Toast.LENGTH_SHORT).show();

                    // ازا الحساب موجوديقوم بتخزين الحساب المدخل
                } else {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SeatList seat = snapshot.getValue(SeatList.class);
                        list.addAll(seat.getSeatList());
                    }
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getId_customer().equals(email_customer)) {
                            count++;


                        }

                    }
                    int total = list.size() * price1;
                    price.setText(total + "");
                    number.setText(count + "");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), "no connected internet", Toast.LENGTH_SHORT).show();
            }

        });

        Button payment = findViewById(R.id.payment);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(visacard.this);
                builder.setMessage("Are you sure of the booking?");
                builder.setCancelable(true)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                int uniqueID = generateUniqueId();
                                Reservation reservation = new Reservation();

                                reservation.setDetails(about);
                                reservation.setId(uniqueID);
                                reservation.setLength( length);
                                reservation.setPrice( price1 + "");
                                reservation.setTimeShow( timeShow);
                                reservation.setName( name);

                                reservation.setEmail_customer(email_customer);

                                ref.child("reservation").push().setValue(reservation);
                                Intent i = new Intent(visacard.this,ticket_barcode.class);
                                startActivity(i);

                            }
                        });
                builder.setCancelable(true).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                AlertDialog alertdialog = builder.create();
                alertdialog.show();
            }
        });
    }
    @Override
    public void onCardTypeChanged(CardType cardType) {
        if (cardType == CardType.EMPTY) {
            mSupportedCardTypesView.setSupportedCardTypes(SUPPORTED_CARD_TYPES);
        } else {
            mSupportedCardTypesView.setSelected(cardType);
        }
    }

    @Override
    public void onCardFormSubmit() {
        if (mCardForm.isValid()) {
            Toast.makeText(this, R.string.valid, Toast.LENGTH_SHORT).show();
        } else {
            mCardForm.validate();
            Toast.makeText(this, R.string.invalid, Toast.LENGTH_SHORT).show();
        }
    }
    public static int generateUniqueId() {
        UUID idOne = UUID.randomUUID();
        String str=""+idOne;
        int uid=str.hashCode();
        String filterStr=""+uid;
        str=filterStr.replaceAll("-", "");
        return Integer.parseInt(str);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.card_io_item) {
            mCardForm.scanCard(this);
            return true;
        }

        return false;
    }
}
