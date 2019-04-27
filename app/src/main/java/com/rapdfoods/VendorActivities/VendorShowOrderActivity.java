package com.rapdfoods.VendorActivities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.rapdfoods.Adapters.OrderListAdapter;
import com.rapdfoods.Models.CheckoutPlaceOrderModel;
import com.rapdfoods.Models.PaymentSubDataModel;
import com.rapdfoods.Models.SubscribedUserModel;
import com.rapdfoods.Models.SubscriptionTransactionModel;
import com.rapdfoods.Models.UserModel;
import com.rapdfoods.R;
import com.rapdfoods.Utils.FirebaseInstances;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VendorShowOrderActivity extends AppCompatActivity implements OrderListAdapter.OrderListener {
    private FirestoreRecyclerOptions<CheckoutPlaceOrderModel> mOptions;
    private FirebaseInstances mFirebaseInstances;
    private FirebaseFirestore mFirebaseFirestore;
    private RecyclerView mORderRecycleView;
    private ProgressDialog dialog;
    private FirestoreRecyclerAdapter mOrderAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list_layout);

        mFirebaseInstances = new FirebaseInstances();
        mFirebaseFirestore = mFirebaseInstances.getFirebaseFirestore();


        mORderRecycleView = findViewById(R.id.order_recycler_view);
        mORderRecycleView.setItemAnimator(new DefaultItemAnimator());
        mORderRecycleView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        Query query = mFirebaseFirestore
                .collection("delivery_orders")
                .orderBy("ordertimestamp", Query.Direction.DESCENDING);
        mOptions = new FirestoreRecyclerOptions.Builder<CheckoutPlaceOrderModel>()
                .setQuery(query, CheckoutPlaceOrderModel.class).build();
        mOrderAdapter = new OrderListAdapter(mOptions, mORderRecycleView, this);
        mORderRecycleView.post(new Runnable() {
            @Override
            public void run() {
                mORderRecycleView.setAdapter(mOrderAdapter);
                mORderRecycleView.setItemAnimator(new DefaultItemAnimator());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mOrderAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mOrderAdapter.stopListening();
    }

    @Override
    public void onClickVerify(View pView, CheckoutPlaceOrderModel pCheckoutPlaceOrderModel) {
        showDialog();
        changeVerificationStatusSUCCESS(pCheckoutPlaceOrderModel, true, pCheckoutPlaceOrderModel.getUid());
    }

    @Override
    public void onClickFailed(View pView, CheckoutPlaceOrderModel pCheckoutPlaceOrderModel) {
        showDialog();
        changeVerificationStatusFAILURE(pCheckoutPlaceOrderModel, false, pCheckoutPlaceOrderModel.getUid());
    }

    private void showDialog() {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Processing..");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void changeVerificationStatusSUCCESS(CheckoutPlaceOrderModel pCheckoutPlaceOrderModel, boolean token, String f_uid) {
        Map<String, Object> notify = new HashMap<>();

        pCheckoutPlaceOrderModel.setOrderStatus("SUCCESS");
        pCheckoutPlaceOrderModel.setVerified(true);
        notify.put("title", "Order confirmed");
        notify.put("description", "Your is confirmed by the vendor for " + pCheckoutPlaceOrderModel.getPackageordered() + "and will be on its way.");
        notify.put("status", "true");
        notify.put("note_type", "order");
        notify.put("timestamp", FieldValue.serverTimestamp());

        //Add notification to user database
        mFirebaseFirestore.collection("users").document(f_uid).collection("notifications")
                .document().set(notify);


        //Add order in user database
        mFirebaseFirestore.collection("users").document(f_uid)
                .collection("my_orders").document(pCheckoutPlaceOrderModel.getTrans_id())
                .set(pCheckoutPlaceOrderModel);

        // add updated order to orders database
        mFirebaseFirestore.collection("delivery_orders").document(pCheckoutPlaceOrderModel.getTrans_id()).set(pCheckoutPlaceOrderModel);
        dialog.dismiss();
    }

    private void changeVerificationStatusFAILURE(CheckoutPlaceOrderModel pCheckoutPlaceOrderModel, boolean token, String f_uid) {

        Map<String, Object> notify = new HashMap<>();


        pCheckoutPlaceOrderModel.setOrderStatus("FAILURE");
        pCheckoutPlaceOrderModel.setVerified(true);
        notify.put("status", "false");
        notify.put("title", "Order Cancelled");
        notify.put("description", "We are sorry but the vendor have cancelled your order for "
                + pCheckoutPlaceOrderModel.getPackageordered() + ".Money will be refunded to you within 24 hrs");
        notify.put("note_type", "order");
        notify.put("timestamp", FieldValue.serverTimestamp());

        //Add notification to user database
        mFirebaseFirestore.collection("users").document(f_uid).collection("notifications")
                .document().set(notify);


        //Add order in user database
        mFirebaseFirestore.collection("users").document(f_uid)
                .collection("my_orders").document(pCheckoutPlaceOrderModel.getTrans_id())
                .set(pCheckoutPlaceOrderModel);

        // add updated order to orders database
        mFirebaseFirestore.collection("delivery_orders").document(pCheckoutPlaceOrderModel.getTrans_id()).set(pCheckoutPlaceOrderModel);

       dialog.dismiss();


    }
//    void addToVerifiedSubscriber(SubscriptionTransactionModel pModel ) {
//        Map<String, Object> mp = new HashMap<>();
//        SubscribedUserModel vModel=new SubscribedUserModel();
//        vModel.setBalance(pModel.getSubcost());
//        vModel.setDuration(pModel.getDuration());
//        vModel.setTrans_id(pModel.getTransaction_id());
//        vModel.setMobile(pModel.getMobile());
//        vModel.setSubscriptionType(pModel.getSubname());
//        vModel.setUid(pModel.getUid());
//        mFirebaseFirestore.collection("users")
//                .document(pModel.getUid())
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot pDocumentSnapshot) {
//                        if (pDocumentSnapshot.exists()) {
//
//                            UserModel vUserModel = pDocumentSnapshot.toObject(UserModel.class);
//                            if (vUserModel != null) {
//                                vModel.setAddress_first(vUserModel.getAddress_first());
//                                mFirebaseFirestore.collection("subscribed_user").document(pModel.getUid()).set(vModel);
//                                sendInAppNotification(pModel.getUid());
//                            }
//                        }
//                    }
//                });
//    }
//
//
//
//    private void sendInAppNotification(String f_uid) {
//        Map<String, Object> notify = new HashMap<>();
//        notify.put("note_type", "subscription");
//        notify.put("description","Congratulations,you are now a subscribed customer of RapidFoods.");
//        notify.put("timestamp", FieldValue.serverTimestamp());
//        notify.put("head","Subscriptions");
//        notify.put("status","true");
//        notify.put("title", "Subscription Confirmed");
//        mFirebaseFirestore.collection("users").document(f_uid).collection("notifications")
//                .document().set(notify);
//    }
}
