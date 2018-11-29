package com.cinema_app.customer;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.cinema_app.R;
import com.cinema_app.models.Keys;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static android.content.Context.MODE_PRIVATE;


public class barcodeFragment extends Fragment {
    View view;
    String Select;
    FirebaseDatabase database;
    DatabaseReference ref;
    SharedPreferences prefs;
    String email = "" ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


         ///////////////////////////////

         view= inflater.inflate(R.layout.barcode, container, false);
      //  title.setText(getResources().getString(R.string.home));
        ////////////////////////////

        prefs =  getActivity().getSharedPreferences(Keys.KEY_ID, MODE_PRIVATE);
        email = prefs.getString(Keys.KEY_EMPLOYEE, "");

        ///////////////////////////////
        final ImageView barcode_btn=(ImageView)view.findViewById(R.id.barcode_btn);
        barcode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        //////////////////////////////

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

//        Query fireQuery = ref.child("employee").orderByChild("email").equalTo(email);
//        fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // ازا الحساب غير موجوديظهر مسج
//                if (dataSnapshot.getValue() != null) {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        employee e = snapshot.getValue(employee.class);
//                        String  car_number = e.getCar_no();
//                        String name = e.getName();
//
//                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
//                        try {
//                            BitMatrix bitMatrix = multiFormatWriter.encode(car_number, BarcodeFormat.CODABAR,300,300);
//                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
//                            barcode_btn.setImageBitmap(bitmap);
//                        } catch (WriterException e1) {
//                            e1.printStackTrace();
//                        }
//
//                    }
//                }
//                else {
//
//                        Toast.makeText(getActivity(), "no result", Toast.LENGTH_LONG).show();
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(getActivity(), "no connected internet", Toast.LENGTH_SHORT).show();}
//
//
//        });








        return view;
    }


}
