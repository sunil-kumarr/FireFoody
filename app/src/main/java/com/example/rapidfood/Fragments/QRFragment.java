package com.example.rapidfood.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rapidfood.Adapters.QRAdapter;
import com.example.rapidfood.Models.PackageModel;
import com.example.rapidfood.Models.SubscriptionModel;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
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

public class QRFragment extends Fragment {
    private RecyclerView mQRRecyceler;
    private FirebaseFirestore mFirebaseFirestore;
    private FirestoreRecyclerOptions<PackageModel> mOptions;
    private FirestoreRecyclerAdapter mQRAdapter;
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mQRRecyceler = view.findViewById(R.id.qr_recycler_view);
        FirebaseInstances vInstances = new FirebaseInstances();
        mFirebaseFirestore = vInstances.getFirebaseFirestore();
        LinearLayoutManager llm = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        mQRRecyceler.setLayoutManager(llm);
        mQRRecyceler.setItemAnimator(new DefaultItemAnimator());
        Query qrQuery = mFirebaseFirestore
                .collection("packages");

        mOptions = new FirestoreRecyclerOptions.Builder<PackageModel>()
                .setQuery(qrQuery, PackageModel.class).build();

        mQRAdapter = new QRAdapter(mOptions,mQRRecyceler,mContext);
        mQRRecyceler.post(new Runnable() {
            @Override
            public void run() {
                mQRRecyceler.setAdapter(mQRAdapter);
                mQRRecyceler.setItemAnimator(new DefaultItemAnimator());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qrcode, container, false);
    }

    @Override
    public void onStop() {
        super.onStop();
        mQRAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        mQRAdapter.startListening();
    }
}
