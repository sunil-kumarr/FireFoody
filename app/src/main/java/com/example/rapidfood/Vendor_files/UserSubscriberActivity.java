package com.example.rapidfood.Vendor_files;

import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

    private static final String TAG = "UserSubscriberActivity";
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
               // .whereEqualTo("verified", false);
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
                Toast.makeText(this, "verify clicked", Toast.LENGTH_SHORT).show();
                changeVerificationStatus(pSubscriptionTransactionModel.getTransaction_id(), true);
                addToVerifiedSubscriber(pSubscriptionTransactionModel);
                break;

        }
    }

    @Override
    public void onClickFailde(View pView, SubscriptionTransactionModel pSubscriptionTransactionModel) {
        Button vButton = (Button) pView;
        switch (vButton.getId()){
            case R.id.subscriber_btn_failed:
                Toast.makeText(this, "fail clicked", Toast.LENGTH_SHORT).show();
                changeVerificationStatus(pSubscriptionTransactionModel.getTransaction_id(), false);
                break;
        }
    }

    void addToVerifiedSubscriber(SubscriptionTransactionModel pModel) {
        Map<String, Object> mp = new HashMap<>();
        String f_uid = pModel.getUid();
        Log.d(TAG,f_uid);
        Toast.makeText(this, ""+f_uid, Toast.LENGTH_SHORT).show();
        mp.put("start_date", FieldValue.serverTimestamp());
        String[] days = pModel.getDuration().split(" ", 1);
        mp.put("duration", days[0]);
        mp.put("trans_id", pModel.getTransaction_id());
        mp.put("balance", Integer.parseInt(pModel.getSubcost()));
        mFirebaseFirestore.collection("subscribed_user").document(f_uid).set(mp).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> pTask) {
                    if(pTask.isSuccessful()){
                        Toast.makeText(UserSubscriberActivity.this, "Added Subscriber", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(UserSubscriberActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }

    void changeVerificationStatus(String t_string, boolean token) {
        Map<String, Object> mp = new HashMap<>();
        if(token) {
            mp.put("verification_status", "SUCCESS");
        }
        else{
            mp.put("verification_status","FAILURE");
        }
        mp.put("verified",true);
        mFirebaseFirestore.collection("sub_transaction_data").document(t_string).update(mp).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> pTask) {
                        if (pTask.isSuccessful()) {
                            Toast.makeText(UserSubscriberActivity.this, "verified Transaction", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserSubscriberActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
}
