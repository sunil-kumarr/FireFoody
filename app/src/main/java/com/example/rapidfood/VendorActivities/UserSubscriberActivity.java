package com.example.rapidfood.VendorActivities;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.rapidfood.Adapters.SubscriberListAdapter;
import com.example.rapidfood.Models.SubscriptionTransactionModel;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

                                vButton.setEnabled(false);
                                vButton.setText("Verified");
                                Drawable img = getResources().getDrawable(R.drawable.ic_check_white_24dp);
                                vButton.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                                vButton.setBackgroundColor(getResources().getColor(R.color.green_500));
                                changeVerificationStatus(pSubscriptionTransactionModel.getTransaction_id(), true);
                                addToVerifiedSubscriber(pSubscriptionTransactionModel);

                break;

        }
    }

    @Override
    public void onClickFailed(View pView, SubscriptionTransactionModel pSubscriptionTransactionModel) {
        Button vButton = (Button) pView;
        switch (vButton.getId()) {
            case R.id.subscriber_btn_failed:
                vButton.setEnabled(false);
                vButton.setText("Failed");
                Drawable img = getResources().getDrawable(R.drawable.ic_circle_cross_24dp);
                vButton.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                vButton.setBackgroundColor(getResources().getColor(R.color.red_500));
                changeVerificationStatus(pSubscriptionTransactionModel.getTransaction_id(), false);
                break;
        }
    }

    void addToVerifiedSubscriber(SubscriptionTransactionModel pModel) {
        Map<String, Object> mp = new HashMap<>();
        String f_uid = pModel.getUid();
        mp.put("start_date", FieldValue.serverTimestamp());
        mp.put("duration", pModel.getDuration());
        mp.put("trans_id", pModel.getTransaction_id());
        mp.put("balance", pModel.getSubcost());
        mFirebaseFirestore.collection("subscribed_user").document(f_uid).set(mp);


        Map<String, Object> notify = new HashMap<>();
        notify.put("note_type", "subscription");
        notify.put("timestamp", FieldValue.serverTimestamp());
        notify.put("title", "Congratulations,you subscribed customer");
        mFirebaseFirestore.collection("users").document(f_uid).collection("notifications")
                .document().set(notify);
    }

    void changeVerificationStatus(String t_string, boolean token) {
        Map<String, Object> mp = new HashMap<>();
        if (token) {
            mp.put("verification_status", "SUCCESS");
        } else {
            mp.put("verification_status", "FAILURE");
        }
        mp.put("verified", true);
        mFirebaseFirestore.collection("sub_transaction_data").document(t_string).update(mp);
    }
}
