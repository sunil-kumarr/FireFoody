package com.firefoody.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.firefoody.Adapters.HomeAdapter;
import com.firefoody.Adapters.ShowSubscriptionAdapter;
import com.firefoody.Models.PackageModel;
import com.firefoody.Models.SubscriptionModel;
import com.firefoody.R;
import com.firefoody.Utils.FirebaseInstances;
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
    private LinearLayout mLoadingPAge;
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

//        mLoadingPAge=view.findViewById(R.id.loading_data_page);
//        mHomeRecycler = view.findViewById(R.id.home_recyclerview);
        mSubscriptionRecycler = view.findViewById(R.id.subscription_recyclerview);
//        mHomeRecycler.setHasFixedSize(true);
//        LinearLayoutManager llm = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        LinearLayoutManager sbm = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
//        mHomeRecycler.setLayoutManager(llm);
        mSubscriptionRecycler.setLayoutManager(sbm);
        getAllDataFireStore();
    }
    private void getAllDataFireStore() {
        Query query = mFirebaseFirestore
                .collection("subscriptions");
        mSubscriptionModelFirestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<SubscriptionModel>()
                .setQuery(query, SubscriptionModel.class).build();
        mSubAdapter = new ShowSubscriptionAdapter(mSubscriptionModelFirestoreRecyclerOptions);
        mSubscriptionRecycler.post(new Runnable() {
            @Override
            public void run() {
                mSubscriptionRecycler.setAdapter(mSubAdapter);
                mSubscriptionRecycler.setItemAnimator(new DefaultItemAnimator());
            }
        });
//        Query homequery = mFirebaseFirestore
//                .collection("today_menu").orderBy("name");
//        mPackageModelFirestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<PackageModel>()
//                .setQuery(homequery, PackageModel.class).build();
//        mHomeAadapter = new HomeAdapter(mPackageModelFirestoreRecyclerOptions,mLoadingPAge);
//        mHomeRecycler.post(new Runnable() {
//            @Override
//            public void run() {
//                mHomeRecycler.setAdapter(mHomeAadapter);
//                mHomeRecycler.setItemAnimator(new DefaultItemAnimator());
//            }
//        });
    }
    @Override
    public void onStart() {
        super.onStart();
        mSubAdapter.startListening();
//        mHomeAadapter.startListening();
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onStop() {
        super.onStop();
        mSubAdapter.stopListening();
//        mHomeAadapter.stopListening();
        //Log.d(TAG,"FragmentHome: onStop ");
    }
}