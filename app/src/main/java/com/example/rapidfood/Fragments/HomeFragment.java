package com.example.rapidfood.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rapidfood.Adapters.HomeAdapter;
import com.example.rapidfood.Adapters.SelectTodayMenuAdapter;
import com.example.rapidfood.Models.VendorDishModel;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.example.rapidfood.Vendor_files.VendorTodayMenuActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {

    private RecyclerView mHomeRecycler;
    private FirebaseFirestore mFirebaseFirestore;
    private Context mContext;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private FirestoreRecyclerOptions<VendorDishModel> dishOptions;
    private FirestoreRecyclerAdapter mAdapter;



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        FirebaseInstances vFirebaseInstances = new FirebaseInstances();
        mFirebaseFirestore = vFirebaseInstances.getFirebaseFirestore();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_home, container, false);

        return  v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       mToolbar=view.findViewById(R.id.home_toolbar);
        ActionBar vActionBar= ((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar();
        if(vActionBar!=null)
        {
            vActionBar.setDisplayShowTitleEnabled(false);
        }
        mHomeRecycler=view.findViewById(R.id.home_recyclerview);
        getAllDataFireStore();
    }



    private void getAllDataFireStore() {


        mHomeRecycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        mHomeRecycler.setLayoutManager(llm);

        Query query = mFirebaseFirestore
                .collection("dishes_main");

        dishOptions = new FirestoreRecyclerOptions.Builder<VendorDishModel>()
                .setQuery(query, VendorDishModel.class).build();
        mAdapter = new HomeAdapter(dishOptions);
        mHomeRecycler.post(new Runnable() {
            @Override
            public void run() {
                mHomeRecycler.setAdapter(mAdapter);
                mHomeRecycler.setItemAnimator(new DefaultItemAnimator());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public  void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }



}
