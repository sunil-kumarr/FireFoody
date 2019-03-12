package com.example.rapidfood.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rapidfood.Activites.VendorAddMenu;
import com.example.rapidfood.Adapters.ShowMenuAdapter;
import com.example.rapidfood.Adapters.ShowMenuLunchAdapter;
import com.example.rapidfood.Adapters.ShowSubscriptionAdapter;
import com.example.rapidfood.Models.PackageModel;
import com.example.rapidfood.Models.SubscriptionModel;
import com.example.rapidfood.Models.VendorBreakFastItem;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.CenterZoomLinearLayoutManager;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

public class VendorMenuDetails extends Fragment {
    private FloatingActionButton mAddMenuBtn;
    private RecyclerView mBreakfastRecycler, mLunchRecycler, mSubRecycler;
    private ArrayList<VendorBreakFastItem> mItems;
    private FirebaseFirestore mFirebaseFirestore;
    private Context mContext;
    private FirestoreRecyclerAdapter adapter,mMenuAdapter,mLunchAdapter;
    private FirestoreRecyclerOptions<SubscriptionModel> options;
    private FirestoreRecyclerOptions<VendorBreakFastItem> mItemOptions;
    private FirestoreRecyclerOptions<PackageModel> mLunchOptions;


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
        return inflater.inflate(R.layout.vendor_menu_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAddMenuBtn = view.findViewById(R.id.addMenuButton);
        mAddMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), VendorAddMenu.class));
            }
        });
        mBreakfastRecycler = view.findViewById(R.id.recyclerMenuBreak);
        mSubRecycler = view.findViewById(R.id.subscription_recycler);
        mLunchRecycler=view.findViewById(R.id.recyclerMenuLunch);
        initSubRecyclerView();
        initBreakfastRecyclerView();
        initLunchRecyclerView();

    }

    private void initLunchRecyclerView() {
        mLunchRecycler.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        mLunchRecycler.setLayoutManager(llm);
        final SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mLunchRecycler);
        getLunch();
    }
    private void getLunch() {
        Query query = mFirebaseFirestore
                .collection("packages");

        mLunchOptions = new FirestoreRecyclerOptions.Builder<PackageModel>()
                .setQuery(query, new SnapshotParser<PackageModel>() {
                    @NonNull
                    @Override
                    public PackageModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        PackageModel vModel = new PackageModel();
                        vModel.setImage(snapshot.getString("image"));
                       // Toast.makeText(mContext, ""+vModel.getImagePackage(), Toast.LENGTH_SHORT).show();
                        return vModel;
                    }
                }).build();

        mLunchAdapter = new ShowMenuLunchAdapter(mLunchOptions, mLunchRecycler);
        mLunchRecycler.post(new Runnable() {
            @Override
            public void run() {
                mLunchRecycler.setAdapter(mLunchAdapter);
                mLunchRecycler.setItemAnimator(new DefaultItemAnimator());

            }
        });
    }
    private void initSubRecyclerView(){
        mSubRecycler.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        mSubRecycler.setLayoutManager(llm);
        final SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mSubRecycler);
        getSubscription();
        adapter = new ShowSubscriptionAdapter(options, mSubRecycler,mContext);
        mSubRecycler.post(new Runnable() {
            @Override
            public void run() {
                mSubRecycler.setAdapter(adapter);
                mSubRecycler.setItemAnimator(new DefaultItemAnimator());

            }
        });
    }

    private void getSubscription(){
        Query query = mFirebaseFirestore
                .collection("subscriptions");

        options = new FirestoreRecyclerOptions.Builder<SubscriptionModel>()
                .setQuery(query, new SnapshotParser<SubscriptionModel>() {
                    @NonNull
                    @Override
                    public SubscriptionModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        SubscriptionModel vModel = new SubscriptionModel();
                        vModel.setImagesub(snapshot.getString("imagesub"));
                        return vModel;
                    }
                }).build();

        adapter = new ShowSubscriptionAdapter(options, mSubRecycler,mContext);
        mSubRecycler.post(new Runnable() {
            @Override
            public void run() {
                mSubRecycler.setAdapter(adapter);
                mSubRecycler.setItemAnimator(new DefaultItemAnimator());

            }
        });

    }

    private void initBreakfastRecyclerView() {
        mBreakfastRecycler.setHasFixedSize(true);
        final LinearLayoutManager llm = new CenterZoomLinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        mBreakfastRecycler.setLayoutManager(llm);
        final SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mBreakfastRecycler);
        getAllMenu();
    }

    private void getAllMenu() {
        Query query = mFirebaseFirestore
                .collection("menus");

        mItemOptions = new FirestoreRecyclerOptions.Builder<VendorBreakFastItem>()
                .setQuery(query, VendorBreakFastItem.class).build();

        mMenuAdapter = new ShowMenuAdapter(mItemOptions,mBreakfastRecycler);
        mBreakfastRecycler.post(new Runnable() {
            @Override
            public void run() {
                mBreakfastRecycler.setAdapter(mMenuAdapter);
                mBreakfastRecycler.setItemAnimator(new DefaultItemAnimator());

            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        mMenuAdapter.startListening();
        mLunchAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        mMenuAdapter.stopListening();
        mLunchAdapter.stopListening();
    }
}
