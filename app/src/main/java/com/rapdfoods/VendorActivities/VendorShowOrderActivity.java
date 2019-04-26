package com.rapdfoods.VendorActivities;

import android.os.Bundle;
import android.view.View;

import com.rapdfoods.Adapters.OrderListAdapter;
import com.rapdfoods.Models.CheckoutPlaceOrderModel;
import com.rapdfoods.R;
import com.rapdfoods.Utils.FirebaseInstances;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

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
        changeVerificationStatus(pCheckoutPlaceOrderModel, true, pCheckoutPlaceOrderModel.getUid());
    }

    @Override
    public void onClickFailed(View pView, CheckoutPlaceOrderModel pCheckoutPlaceOrderModel) {

        changeVerificationStatus(pCheckoutPlaceOrderModel, false, pCheckoutPlaceOrderModel.getUid());
    }

    void changeVerificationStatus(CheckoutPlaceOrderModel pCheckoutPlaceOrderModel, boolean token, String f_uid) {
        Map<String, Object> notify = new HashMap<>();

        if (token) {

            pCheckoutPlaceOrderModel.setOrderStatus("SUCCESS");
            pCheckoutPlaceOrderModel.setVerified(true);
            notify.put("title", "Order confirmed");
            notify.put("description","Your is confirmed by the vendor and will be on its way."+System.getProperty("line.separator")+"OTP: "+pCheckoutPlaceOrderModel.getOtp());
            notify.put("status", "true");
        } else {
            pCheckoutPlaceOrderModel.setOrderStatus("FAILURE");
            pCheckoutPlaceOrderModel.setVerified(true);
            notify.put("status", "false");
            notify.put("title", "Order Cancelled");
            notify.put("description","We are sorry but the vendor have cancelled your order.Money will be refunded to wallet within 24 hrs");
        }
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
    }
}
