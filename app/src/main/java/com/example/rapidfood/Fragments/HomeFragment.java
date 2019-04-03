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
import com.example.rapidfood.Adapters.ShowSubscriptionAdapter;
import com.example.rapidfood.Models.HomeDataModel;
import com.example.rapidfood.Models.PackageModel;
import com.example.rapidfood.Models.SubscriptionContainerModel;
import com.example.rapidfood.Models.SubscriptionModel;
import com.example.rapidfood.Models.VendorDishModel;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.example.rapidfood.Vendor_files.VendorTodayMenuActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    private RecyclerView mHomeRecycler, mSubscriptionRecycler;
    private FirebaseFirestore mFirebaseFirestore;
    private Context mContext;
    private Toolbar mToolbar;
    private ShowSubscriptionAdapter mShowSubscriptionAdapter;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private FirestoreRecyclerOptions<SubscriptionModel> mSubscriptionModelFirestoreRecyclerOptions;
    private FirestoreRecyclerOptions<PackageModel> mPackageModelFirestoreRecyclerOptions;
    private FirestoreRecyclerAdapter mSubAdapter,mHomeAadapter;

    private static final String TAG = "HomeFragment";


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

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar = view.findViewById(R.id.home_toolbar);
        ActionBar vActionBar = ((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar();
        if (vActionBar != null) {
            vActionBar.setDisplayShowTitleEnabled(false);
        }
        mHomeRecycler = view.findViewById(R.id.home_recyclerview);
        mSubscriptionRecycler = view.findViewById(R.id.subscription_recyclerview);
//        mHomeRecycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        mHomeRecycler.setLayoutManager(llm);

//        mSubscriptionRecycler.setHasFixedSize(true);
        LinearLayoutManager horiziontal = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        mSubscriptionRecycler.setLayoutManager(horiziontal);
        getAllDataFireStore();
    }


    private void getAllDataFireStore() {

        Query query = mFirebaseFirestore
                .collection("subscriptions");

        mSubscriptionModelFirestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<SubscriptionModel>()
                .setQuery(query, SubscriptionModel.class).build();
        mSubAdapter = new ShowSubscriptionAdapter(mSubscriptionModelFirestoreRecyclerOptions, mSubscriptionRecycler, mContext);
        mSubscriptionRecycler.post(new Runnable() {
            @Override
            public void run() {
                mSubscriptionRecycler.setAdapter(mSubAdapter);
                mSubscriptionRecycler.setItemAnimator(new DefaultItemAnimator());
            }
        });

        Query homequery = mFirebaseFirestore
                .collection("today_menu").orderBy("name");

        mPackageModelFirestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<PackageModel>()
                .setQuery(homequery, PackageModel.class).build();

        mHomeAadapter = new HomeAdapter(mPackageModelFirestoreRecyclerOptions);
        mHomeRecycler.post(new Runnable() {
            @Override
            public void run() {
                mHomeRecycler.setAdapter(mHomeAadapter);
                mHomeRecycler.setItemAnimator(new DefaultItemAnimator());
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        mSubAdapter.startListening();
        mHomeAadapter.startListening();
        Log.d(TAG,"FragmentHome: onStart ");
        Toast.makeText(mContext, "Subs: "+mSubAdapter.getItemCount(), Toast.LENGTH_SHORT).show();
        Toast.makeText(mContext, "items: "+mHomeAadapter.getItemCount(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        mSubAdapter.stopListening();
        mHomeAadapter.stopListening();
        Log.d(TAG,"FragmentHome: onStop ");
    }


}
