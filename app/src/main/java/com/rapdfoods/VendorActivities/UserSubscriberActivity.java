package com.rapdfoods.VendorActivities;

import android.os.Bundle;

import com.rapdfoods.Adapters.SubscriberListAdapter;
import com.rapdfoods.Models.SubscribedUserModel;
import com.rapdfoods.Models.SubscriptionTransactionModel;
import com.rapdfoods.Models.UserModel;
import com.rapdfoods.R;
import com.rapdfoods.Utils.FirebaseInstances;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
                                if(vModel.getVerified().equals("pending")) {
                                    if (vModel.getPayment_status().equals("SUCCESS")) {
                                        addToVerifiedSubscriber(vModel);
                                    }
                                    Map<String,Object> mp=new HashMap<>();
                                    mp.put("verified","SUCCESS");
                                    mFirebaseFirestore.collection("sub_transaction_data")
                                            .document(Objects.requireNonNull(x.getString("transaction_id")))
                                            .update(mp);
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
        SubscribedUserModel vModel=new SubscribedUserModel();
        vModel.setBalance(pModel.getSubcost());
        vModel.setDuration(pModel.getDuration());
        vModel.setTrans_id(pModel.getTransaction_id());
        vModel.setMobile(pModel.getMobile());
        vModel.setSubscriptionType(pModel.getSubname());
        vModel.setUid(pModel.getUid());
        mFirebaseFirestore.collection("users")
                .document(pModel.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot pDocumentSnapshot) {
                        if (pDocumentSnapshot.exists()) {

                            UserModel vUserModel = pDocumentSnapshot.toObject(UserModel.class);
                            if (vUserModel != null) {
                                vModel.setAddress_first(vUserModel.getAddress_first());
                                mFirebaseFirestore.collection("subscribed_user").document(pModel.getUid()).set(vModel);
                                sendInAppNotification(pModel.getUid(),pModel);
                            }
                        }
                    }
                });
    }



    private void sendInAppNotification(String f_uid,SubscriptionTransactionModel mpdel) {
        Map<String, Object> notify = new HashMap<>();
        notify.put("note_type", "subscription");
        notify.put("description","Congratulations,you are now "+mpdel.getSubname()+" customer of RapidFoods.");
        notify.put("timestamp", FieldValue.serverTimestamp());
        notify.put("head","Subscriptions");
        notify.put("status","true");
        notify.put("title", "Subscription Confirmed");
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
