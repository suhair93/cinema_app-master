package com.cinema_app.admin;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cinema_app.Adapter.MovieAdapter;
import com.cinema_app.Adapter.SoonAdapter;
import com.cinema_app.R;
import com.cinema_app.models.movies;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class all_movies extends Fragment {
    FirebaseDatabase database;
    DatabaseReference ref;
    StorageReference storageReference;
    RecyclerView recyclerView;
    List<movies> lList = new ArrayList<movies>();
    FloatingActionButton add;
    MovieAdapter movieAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movies, container, false);



          LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
         mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(mLayoutManager);
         movieAdapter = new MovieAdapter(getContext(), lList);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        ref.child("movie").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    movies movies = snapshot.getValue(movies.class);

                    lList.add(movies);
                        movieAdapter.notifyDataSetChanged();}


                Collections.reverse(lList);
                // box.hideAll();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        add = view.findViewById(R.id.fab);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getActivity(),add_movie.class);
                startActivity(i);

            }
        });
        return  view;
    }

}
