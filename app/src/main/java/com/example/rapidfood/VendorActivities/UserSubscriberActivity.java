package com.example.rapidfood.VendorActivities;

import android.os.Bundle;
import android.view.View;

import com.example.rapidfood.Adapters.SubscriberListAdapter;
import com.example.rapidfood.Models.SubscribedUserModel;
import com.example.rapidfood.Models.SubscriptionTransactionModel;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserSubscriberActivity extends AppCompatActivity  {

    private static final String TAG = "UserSubscriberActivity";
    private FirestoreRecyclerOptions<SubscribedUserModel> mOptions;
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
        mFirebaseFirestore.collection("sub_transaction_data")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        QuerySnapshot queryDocumentSnapshots=task.getResult();
                        List<DocumentSnapshot> transList=queryDocumentSnapshots.getDocuments();

                        for(DocumentSnapshot x:transList){
                            if(x.exists()){
                                SubscriptionTransactionModel vModel=x.toObject(SubscriptionTransactionModel.class);
                                assert vModel != null;
                                if(vModel.getGooglePay_status().equals("SUCCESS")){
                                    addToVerifiedSubscriber(vModel);
                                }
                            }
                        }

                    }
            }
        });


        Query query=  mFirebaseFirestore.collection("subscribed_user")
                .orderBy("start_date", Query.Direction.DESCENDING);

        mOptions = new FirestoreRecyclerOptions.Builder<SubscribedUserModel>()
                .setQuery(query, SubscribedUserModel.class).build();
        mSubListAdapter = new SubscriberListAdapter(mOptions, mSubscriberRecycler, this);
        mSubscriberRecycler.post(new Runnable() {
            @Override
            public void run() {
                mSubscriberRecycler.setAdapter(mSubListAdapter);
                mSubscriberRecycler.setItemAnimator(new DefaultItemAnimator());
            }
        });
    }
    void addToVerifiedSubscriber(SubscriptionTransactionModel  pModel ) {
        Map<String, Object> mp = new HashMap<>();
        String f_uid = pModel.getUid();
        mp.put("start_date", FieldValue.serverTimestamp());
        mp.put("duration", pModel.getDuration());
        mp.put("trans_id", pModel.getTransaction_id());
        mp.put("balance", pModel.getSubcost());
        mp.put("subscription_type",pModel.getSubname());
        mp.put("mobile",pModel.getMobile());
        mFirebaseFirestore.collection("subscribed_user").document(f_uid).set(mp);

        sendInAppNotification(f_uid);
    }

    private void sendInAppNotification(String f_uid) {
        Map<String, Object> notify = new HashMap<>();
        notify.put("note_type", "subscription");
        notify.put("timestamp", FieldValue.serverTimestamp());
        notify.put("head","Subscriptions");
        notify.put("title", "Congratulations,you are now a subscribed customer of RapidFoods.");
        mFirebaseFirestore.collection("users").document(f_uid).collection("notifications")
                .document().set(notify);
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





}
