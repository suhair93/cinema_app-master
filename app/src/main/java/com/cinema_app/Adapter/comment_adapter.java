package com.cinema_app.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.cinema_app.R;
import com.cinema_app.models.Keys;
import com.cinema_app.models.comment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class comment_adapter extends RecyclerView.Adapter<comment_adapter.MyViewHolder>  {
    FirebaseDatabase database;
    DatabaseReference ref;
   Context mContext;
  List<comment> commentList;




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, comment;


        public MyViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.name);
            comment = (TextView) view.findViewById(R.id.comment);

        }
    }


    public comment_adapter(Context mContext, List<comment> commentList) {
        this.mContext = mContext;
        this.commentList = commentList;
        //this.orig=studentsList;
    }

    @Override
    public comment_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_row_item, parent, false);

        return new comment_adapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final comment_adapter.MyViewHolder holder, final int position) {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        SharedPreferences prefs =  mContext.getSharedPreferences(Keys.KEY_ID, MODE_PRIVATE);
        final String  email_customer = prefs.getString(Keys.KEY_CUSTOMER,"");

        final comment comment = commentList.get(position);
        holder.name.setText(comment.getName());
        holder.comment.setText(comment.getComment());
        if(comment.getCustomerId().equals(email_customer)) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Query fireQuery = ref.child("comment").orderByChild("customerId").equalTo(email_customer);
                    fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // ازا الحساب غير موجوديظهر مسج
                            if (dataSnapshot.getValue() != null) {

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    snapshot.getRef().setValue(comment);
                                    notifyDataSetChanged();
                                    Toast.makeText(mContext, "Done", Toast.LENGTH_SHORT).show();


                                }


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                            Toast.makeText(mContext
                                    , "no connected internet", Toast.LENGTH_SHORT).show();
                        }

                    });
                }
            });
        }

    }



        @Override
        public int getItemCount () {
            return commentList.size();


        }

}
