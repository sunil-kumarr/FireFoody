package com.example.rapidfood.Vendor_files;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rapidfood.Adapters.ShowSubscriptionAdapter;
import com.example.rapidfood.Adapters.SubscriberListAdapter;
import com.example.rapidfood.Models.PackageModel;
import com.example.rapidfood.Models.SubscriptionModel;
import com.example.rapidfood.Models.SubscriptionTransactionModel;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserSubscriberActivity extends AppCompatActivity implements SubscriberListAdapter.SubscriberListener {

    private FirestoreRecyclerOptions<SubscriptionTransactionModel> mOptions;
    private FirebaseInstances mFirebaseInstances;
    private FirebaseFirestore mFirebaseFirestore;
    private RecyclerView mSubscriberRecycler;
    private FirestoreRecyclerAdapter mSubListAdapter;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber_list_layout);
        mFirebaseInstances = new FirebaseInstances();
        mFirebaseFirestore = mFirebaseInstances.getFirebaseFirestore();

        mSubscriberRecycler = findViewById(R.id.subscriber_recycler_View_list);
        mSubscriberRecycler.setItemAnimator(new DefaultItemAnimator());
        mSubscriberRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        Query query = mFirebaseFirestore
                .collection("sub_transaction_data")
                .orderBy("transaction_time", Query.Direction.DESCENDING);
        mOptions = new FirestoreRecyclerOptions.Builder<SubscriptionTransactionModel>()
                .setQuery(query, SubscriptionTransactionModel.class).build();
        mSubListAdapter = new SubscriberListAdapter(mOptions, mSubscriberRecycler, this);
        mSubscriberRecycler.post(new Runnable() {
            @Override
            public void run() {
                mSubscriberRecycler.setAdapter(mSubListAdapter);
                mSubscriberRecycler.setItemAnimator(new DefaultItemAnimator());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSubListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSubListAdapter.stopListening();
    }

    @Override
    public void onClickVerify(View pView, SubscriptionTransactionModel pSubscriptionTransactionModel) {
        Button vButton = (Button) pView;
        switch (vButton.getId()) {
            case R.id.subscriber_btn_verify:
                break;
            case R.id.subscriber_btn_failed:
                break;
        }
    }
}
