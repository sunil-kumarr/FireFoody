package com.firefoody.VendorActivities;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.firefoody.Adapters.SubscriberListAdapter;
import com.firefoody.Models.SubscribedUserModel;
import com.firefoody.Models.SubscriptionTransactionModel;
import com.firefoody.Models.TimeStamp;
import com.firefoody.Models.UserModel;
import com.firefoody.R;
import com.firefoody.Utils.FirebaseInstances;
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

        mFirebaseFirestore.collection("company_data")
                .document("timestamp")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> pTask) {
                        if (pTask.isSuccessful()) {
                            DocumentSnapshot vSnapshot = pTask.getResult();
                            if (vSnapshot != null) {
                                TimeStamp vTimeStamp = vSnapshot.toObject(TimeStamp.class);
                                assert vTimeStamp != null;
                                String realTimestamp = String.valueOf(vTimeStamp.getTimestamp());
                                //Toast.makeText(UserSubscriberActivity.this, ""+realTimestamp, Toast.LENGTH_SHORT).show();
                              String current=  getCurrentDate(realTimestamp);
                              mFirebaseFirestore.collection("subscribed_user")
                                      .get()
                                      .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                          @Override
                                          public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                              if(!queryDocumentSnapshots.isEmpty())
                                              {
                                                  List<DocumentSnapshot> UserList=queryDocumentSnapshots.getDocuments();
                                                  for(DocumentSnapshot user:UserList){
                                                      Map<String,Object> update=new HashMap<>();
                                                      if(user!=null)
                                                      if(!Objects.equals(user.getString("updateData"), current)){

                                                          int duration=Integer.parseInt(Objects.requireNonNull(user.getString("duration")));
                                                          Toast.makeText(UserSubscriberActivity.this, ""+duration, Toast.LENGTH_SHORT).show();
                                                          if(duration>0){
                                                              duration--;
                                                              update.put("updateData",current);
                                                              update.put("duration",String.valueOf(duration));

                                                          }
                                                          if(Objects.equals(user.getString("duration"),"0")){
                                                              Toast.makeText(UserSubscriberActivity.this, "expired", Toast.LENGTH_SHORT).show();
                                                              update.put("updateData",current);
                                                              update.put("duration",String.valueOf(duration));
                                                              update.put("balance","0");
                                                              sendInAppNotificationExpired(user.getString("uid"));
                                                          }
                                                          mFirebaseFirestore.collection("subscribed_user")
                                                                  .document(Objects.requireNonNull(user.getString("uid")))
                                                                  .update(update);

                                                      }

                                                  }
                                              }

                                          }
                                      }).addOnFailureListener(new OnFailureListener() {
                                  @Override
                                  public void onFailure(@NonNull Exception e) {
                                      Toast.makeText(UserSubscriberActivity.this, "update failed", Toast.LENGTH_SHORT).show();
                                  }
                              });
                            }
                        } else {
                            Toast.makeText(UserSubscriberActivity.this, "Sorry something wrong!", Toast.LENGTH_SHORT).show();
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


    private void sendInAppNotificationExpired(String f_uid) {
        Map<String, Object> notify = new HashMap<>();
        notify.put("note_type", "subscription");
        notify.put("description","Your Subscription is Expired,subscribe again to continue our awesome features.");
        notify.put("timestamp", FieldValue.serverTimestamp());
        notify.put("head","Subscriptions");
        notify.put("status","false");
        notify.put("title", "Subscription Expired");
        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        mFirebaseFirestore.collection("users").document(f_uid).collection("notifications")
                .document().set(notify);
    }


    private String getCurrentDate(String realDate) {

        String[] getDate = realDate.split(" ",6);
        String realMonth = getDate[1];
        String realDay = getDate[2];
        String realYear=getDate[5];
        String currentDAte= realDay + "-" + realMonth + "-" + realYear;
     //   Toast.makeText(this, "Date:"+" "+currentDAte, Toast.LENGTH_SHORT).show();
        return currentDAte;
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
        mFirebaseFirestore.collection("company_data")
                .document("timestamp")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> pTask) {
                        if (pTask.isSuccessful()) {
                            DocumentSnapshot vSnapshot = pTask.getResult();
                            if (vSnapshot != null) {
                                TimeStamp vTimeStamp = vSnapshot.toObject(TimeStamp.class);
                                assert vTimeStamp != null;
                                String realTimestamp = String.valueOf(vTimeStamp.getTimestamp());
                                vModel.setUpdateData(getCurrentDate(realTimestamp));
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
                        } else {
                            Toast.makeText(UserSubscriberActivity.this, "Sorry something wrong!", Toast.LENGTH_SHORT).show();
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
