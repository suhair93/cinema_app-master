package com.cinema_app.admin;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cinema_app.R;
import com.cinema_app.customer.MainActivity;
import com.cinema_app.models.Seat;
import com.cinema_app.models.SeatList;
import com.cinema_app.models.movies;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class add_movie extends AppCompatActivity {
  EditText  title ,details,duration,time,libirty,price;
  Spinner type;
  ImageView img;
  ImageButton upload;
    FirebaseDatabase database;
    private ArrayAdapter mAdapter;
    private int mSelectedIndex = 0;
    List<Seat> seatList = new ArrayList<>();
    ProgressDialog progressDialog;
    DatabaseReference ref;
    StorageReference storageReference;
    public static final String STORAGE_PATH_UPLOADS = "uploads_movie/";

    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
  Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_movie_admin);

       Button back = findViewById(R.id.back);
       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });
     //   getPIC();
       //////////////////////////.
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        title =  findViewById(R.id.name);
        details =  findViewById(R.id.details);
        duration =  findViewById(R.id.duration);
        time =  findViewById(R.id.time);
        getTime(time);
        libirty =  findViewById(R.id.lib);
        price =  findViewById(R.id.price);
        img = findViewById(R.id.img);
        upload = findViewById(R.id.upload);
        add = findViewById(R.id.add);
      type = findViewById(R.id.type);


        for (int i =0 ; i<= 59 ; i++){
            seatList.add(new Seat(i,false+"",""));
        }


        // Initialize a new list
        List<String> typeFilme = new ArrayList<>();
        typeFilme.add("select type show film");
        typeFilme.add("Showing now");
        typeFilme.add("Comming soon");
        // Initialize an array adapter
        mAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,typeFilme){
            public View getView(int position, View convertView, ViewGroup parent) {
                // Cast the spinner collapsed item (non-popup item) as a text view
                TextView tv = (TextView) super.getView(position, convertView, parent);
                // Set the text color of spinner item
                tv.setTextColor(Color.WHITE);
                // Return the view
                return tv;
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                // Cast the drop down items (popup items) as text view
                TextView tv = (TextView) super.getDropDownView(position,convertView,parent);

                // Set the text color of drop down items
                tv.setTextColor(Color.BLACK);
                // If this item is selected item
                if(position == mSelectedIndex){
                    // Set spinner selected popup item's text color
                    tv.setTextColor(Color.RED);
                }

                // Return the modified view
                return tv;
            }
        };

        // Set an item selection listener for spinner widget
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Set the value for selected index variable
                mSelectedIndex = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // Data bind the spinner with array adapter items
        type.setAdapter(mAdapter);

 /////////////////////////////////////////
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            uploadFile();
            }
        });
        /////////////////////////////////////////
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        /////////////////////////////////////////
    }
    public void getTime(final EditText editTextDate){
        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(add_movie.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(add_movie.this , new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        String myFormat = "dd/MM/yyyy HH:mm";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        editTextDate.setText(sdf.format(date.getTime()));
                        // editTextDate.setText(new SimpleDateFormat("dd-MMM-yyyy h:mm").format(date.getTime()));
                    }
                },currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();

            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
        //   datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
        editTextDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                datePickerDialog.show();
            }});
    }
    /////////////////////////////////////////
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    /////////////////////////////////////////
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{}
    }
    /////////////////////////////////////////
    public String getFileExtension(Uri uri) {
        ContentResolver cR =  getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    /////////////////////////////////////////
    private void uploadFile() {
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(add_movie.this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            //getting the storage reference
            final StorageReference sRef = storageReference.child(STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));
            //adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            String uniqueID = UUID.randomUUID().toString();
                            movies movies = new movies();
                            movies.setName(title.getText().toString());
                            movies.setDetails(details.getText().toString());
                            movies.setDuration(duration.getText().toString());
                            movies.setTime(time.getText().toString());
                            movies.setScreen(libirty.getText().toString());
                            int price1 = Integer.parseInt(price.getText().toString());
                            movies.setPrice(price1);
                            movies.setId(uniqueID);
                            movies.setType(mSelectedIndex+"");
                            movies.setSeatList(seatList);
                            movies.setImg(taskSnapshot.getDownloadUrl().toString());

                            SeatList seat = new SeatList();
                            seat.setId_movie(uniqueID);
                            seat.setSeatList(seatList);

                            ref.child("seat").push().setValue(seat);
                            ref.child("movie").push().setValue(movies);

                            Toast.makeText(getBaseContext(), "image Uploaded ", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getBaseContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                            //   Toast.makeText(getBaseContext(),"Done",Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }


    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] Result) {

        switch (RC) {

            case 1:

                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getBaseContext(),"Permission Granted", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getBaseContext() ,"Permission Canceled", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
}
