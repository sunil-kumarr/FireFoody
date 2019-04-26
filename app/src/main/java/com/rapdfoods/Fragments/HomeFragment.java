package com.rapdfoods.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rapdfoods.Adapters.HomeAdapter;
import com.rapdfoods.Adapters.ShowSubscriptionAdapter;
import com.rapdfoods.Models.PackageModel;
import com.rapdfoods.Models.SubscriptionModel;
import com.rapdfoods.R;
import com.rapdfoods.Utils.FirebaseInstances;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {


    private Context mContext;

    private RecyclerView mHomeRecycler, mSubscriptionRecycler;
    private FirebaseFirestore mFirebaseFirestore;
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
       // Log.d(TAG,"FragmentHome: onStart ");
       // Toast.makeText(mContext, "Subs: "+mSubAdapter.getItemCount(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(mContext, "items: "+mHomeAadapter.getItemCount(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        mSubAdapter.stopListening();
        mHomeAadapter.stopListening();
        //Log.d(TAG,"FragmentHome: onStop ");
    }


}
