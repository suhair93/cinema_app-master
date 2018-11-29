package com.cinema_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cinema_app.Login;
import com.cinema_app.R;
import com.cinema_app.customer.buyTick;
import com.cinema_app.models.Keys;
import com.cinema_app.models.movies;


import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class NowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<movies> list ;
    private Context context;
    public NowAdapter(Context context, List<movies> List1) {
        this.context = context;
        this.list = List1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_now, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

         SharedPreferences prefs =  context.getSharedPreferences(Keys.KEY_ID, MODE_PRIVATE);
        final String  email_customer = prefs.getString(Keys.KEY_CUSTOMER,"");
        Holder holder1 = (Holder) holder;
        final movies movies = list.get(position);

        holder1.title.setText(movies.getName());
        Glide.with(context).load(movies.getImg()).into(holder1.img);
        holder1.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email_customer.equals("")){
                    Intent i = new Intent(context,Login.class);
                    context.startActivity(i);
               }
                else{
                    Intent i = new Intent(context,buyTick.class);
                    i.putExtra("id",movies.getId());
                    context.startActivity(i);

                }
            }
        });



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
        TextView title ;
        ImageView img;
        Button book;
        public Holder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.name);
            img = itemView.findViewById(R.id.img);
            book = itemView.findViewById(R.id.book);


        }

    }
}






