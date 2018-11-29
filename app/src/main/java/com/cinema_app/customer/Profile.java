package com.cinema_app.customer;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cinema_app.Login;
import com.cinema_app.R;
import com.cinema_app.models.Keys;
import com.cinema_app.models.Reservation;
import com.cinema_app.models.movies;
import com.cinema_app.models.user;
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

import static android.content.Context.MODE_PRIVATE;

public class Profile extends Fragment {


   EditText name,password,email,phone;
   Button update,login;
   LinearLayout layout1,layout2;
   ImageView barcode;
    ProgressDialog dialog;
    FirebaseDatabase database;
    DatabaseReference ref;
    List<user> userlist;

    SharedPreferences prefs;
    String email_customer = "",id_tickt = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view =inflater.inflate(R.layout.fragment_profile, container, false);

       name = view.findViewById(R.id.user);
        password = view.findViewById(R.id.password);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);
        update = view.findViewById(R.id.update);
        login = view.findViewById(R.id.login);
        barcode = view.findViewById(R.id.barcode);

        dialog = new ProgressDialog(getActivity());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("waiting ....");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);



        layout1 = view.findViewById(R.id.layout);
        layout2 = view.findViewById(R.id.layout2);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        prefs =  getActivity().getSharedPreferences(Keys.KEY_ID, MODE_PRIVATE);
        email_customer = prefs.getString(Keys.KEY_CUSTOMER,"");




        if(!email_customer.equals("")) {
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.GONE);
            Query fireQuery = ref.child("user").orderByChild("email").equalTo(email_customer);
            fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // ازا الحساب غير موجوديظهر مسج
                    if (dataSnapshot.getValue() == null) {
                        Toast.makeText(getActivity(), "Not found", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        // ازا الحساب موجوديقوم بتخزين الحساب المدخل
                    } else {
                        List<user> searchList = new ArrayList<user>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            user user = snapshot.getValue(user.class);
                            searchList.add(user);

                            name.setText(user.getName());
                            password.setText(user.getPassword());
                            phone.setText(user.getPhone());
                            email.setText(user.getEmail());



                        }


                        dialog.dismiss();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "no connected internet", Toast.LENGTH_SHORT).show();
                }

            });
        }else {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);


        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             final user user = new user();
             user.setEmail(email.getText().toString());
             user.setName(name.getText().toString());
             user.setPassword(password.getText().toString());
             user.setPhone(phone.getText().toString());

                final Query query1 = ref.child("user").orderByChild("email").equalTo(user.getEmail());

                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            snapshot.getRef().setValue(user);
                            Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });



        final Query query1 = ref.child("reservation").orderByChild("email_customer").equalTo(email_customer);

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

               Reservation r = snapshot.getValue(Reservation.class);
               id_tickt = r.getId()+"";


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

      login.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent i = new Intent(getActivity(),Login.class);
              startActivity(i);
          }
      });

 barcode.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {

                         AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                         LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                         View views = inflater.inflate(R.layout.dialoge_barcode, null);
                         alertDialog.setView(views);
                         final AlertDialog dialog1 = alertDialog.create();
                         dialog1.show();
                         ImageView imageView = (ImageView) views.findViewById(R.id.imageView);


                          ;// Whatever you need to encode in the QR code
                         MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                         try {
                             BitMatrix bitMatrix = multiFormatWriter.encode(id_tickt, BarcodeFormat.CODABAR, 250, 250);
                             BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                             Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                             imageView.setImageBitmap(bitmap);
                         } catch (WriterException e) {
                             e.printStackTrace();
                         }


                     }
                 });




        return view;
    }

}
