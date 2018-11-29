package com.cinema_app.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.cinema_app.R;
import com.cinema_app.models.comment;

import java.util.List;


public class comment_adapter extends RecyclerView.Adapter<comment_adapter.MyViewHolder>  {

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
        comment comment = commentList.get(position);
        holder.name.setText(comment.getName());
        holder.comment.setText(comment.getComment());


    }



        @Override
        public int getItemCount () {
            return commentList.size();


        }

}
