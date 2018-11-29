package com.cinema_app.Adapter;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.renderscript.Sampler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.cinema_app.R;
import com.cinema_app.admin.add_movie;
import com.cinema_app.models.movies;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ImageView img;
    ImageButton upload;
    FirebaseDatabase database;
    private ArrayAdapter mAdapter;
    private int mSelectedIndex = 0;
    ProgressDialog progressDialog;
    DatabaseReference ref;
    String type_show = "";
    StorageReference storageReference;
    public static final String STORAGE_PATH_UPLOADS = "uploads_movie/";
    public static final String DATABASE_PATH_UPLOADS = "uploads";
    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
    private List<movies> list ;
    private Context context;
    public MovieAdapter(Context context, List<movies> List1) {
        this.context = context;
        this.list = List1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_movies, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        final Holder  Holder = (Holder) holder;
        final movies movies = list.get(position);

         Holder.title.setText(movies.getName());
         Holder.details.setText(movies.getDetails());
         Holder.price.setText(movies.getPrice() +"");
         Holder.duration.setText(movies.getDuration());
         Holder.libirty.setText(movies.getScreen());
         Holder.time.setText(movies.getTime());
    //     Holder.type.setSelection(Integer.valueOf(movies.getType()));
         Glide.with(context).load(movies.getImg()).into(Holder.img);

         Holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                 LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

                 View views = inflater.inflate(R.layout.edit_movie_admin, null);
                 alertDialog.setView(views);
                 final AlertDialog dialog = alertDialog.create();
                 dialog.show();
                 final EditText  title =  views.findViewById(R.id.name);
                 final EditText details =  views.findViewById(R.id.details);
                 final EditText duration =  views.findViewById(R.id.duration);
                 final EditText time =  views.findViewById(R.id.time);
                 getTime(time);
                 final EditText libirty =  views.findViewById(R.id.lib);
                 final EditText price =  views.findViewById(R.id.price);
                 final ImageView img = views.findViewById(R.id.img);
                 final ImageButton  upload = views.findViewById(R.id.upload);
                 final EditText  add = views.findViewById(R.id.add);
                 final Spinner type = views.findViewById(R.id.type);

                 final List<String> typeShow = new ArrayList<>();
                 typeShow.add("select type show film");
                 typeShow.add("Showing now");
                 typeShow.add("Comming soon");
                 // Initialize an array adapter
                 mAdapter = new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item,typeShow){
                     public View getView(int position, View convertView, ViewGroup parent) {
                         TextView tv = (TextView) super.getView(position, convertView, parent);
                         tv.setTextColor(Color.WHITE);
                         return tv;
                     }
                     @Override
                     public View getDropDownView(int position, View convertView, ViewGroup parent){
                         TextView tv = (TextView) super.getDropDownView(position,convertView,parent);
                         tv.setTextColor(Color.BLACK);
                         if(position == mSelectedIndex){
                             tv.setTextColor(Color.RED);
                         }
                         return tv;
                     }
                 };
                 type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                     @Override
                     public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                         mSelectedIndex = i;
                         type_show = typeShow.get(i);
                     }

                     @Override
                     public void onNothingSelected(AdapterView<?> adapterView) {
                     }
                 });
                 type.setAdapter(mAdapter);

                 title.setText(movies.getName());
                 details.setText(movies.getDetails());
                 price.setText(movies.getPrice()+"");
                 duration.setText(movies.getDuration());
                 libirty.setText(movies.getScreen());
                 time.setText(movies.getTime());
                 type.setSelection(Integer.valueOf(movies.getType()));
                 Glide.with(context).load(movies.getImg()).into(img);

             }
         });




    }

    public void getTime(final EditText editTextDate){
        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(context , new TimePickerDialog.OnTimeSetListener() {
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
    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);

    }
    public class Holder extends RecyclerView.ViewHolder {
        TextView title ,details,duration,time,libirty,price,rate;
        ImageView img;
        RatingBar ratingBar;
        Spinner type;
        public Holder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.name);
            details = (TextView) itemView.findViewById(R.id.details);
            duration = (TextView) itemView.findViewById(R.id.duration);
            time = (TextView) itemView.findViewById(R.id.time);
            libirty = (TextView) itemView.findViewById(R.id.lib);
            price = (TextView) itemView.findViewById(R.id.price);
            img = itemView.findViewById(R.id.img);
            type = itemView.findViewById(R.id.type);
      //      ratingBar =itemView.findViewById(R.id.ratingBar);



        }

    }
}






