package com.example.rapidfood.VendorActivities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rapidfood.Adapters.DeliverOrderListAdapter;
import com.example.rapidfood.Adapters.OrderListAdapter;
import com.example.rapidfood.Models.CheckoutPlaceOrderModel;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class DeliveryCustomOrderActivity extends AppCompatActivity implements DeliverOrderListAdapter.DeliverOrderListener {
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


        mOrderAdapter = new DeliverOrderListAdapter(mOptions, mORderRecycleView, this);

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
        pView.setVisibility(View.GONE);

    }

    @Override
    public void onClickFailed(View pView, CheckoutPlaceOrderModel pCheckoutPlaceOrderModel) {
        Toast.makeText(this, "Not Allowed call vendor", Toast.LENGTH_SHORT).show();
//        changeVerificationStatus(pCheckoutPlaceOrderModel, false, pCheckoutPlaceOrderModel.getUid());
//        pView.setVisibility(View.GONE);
    }

    void changeVerificationStatus(CheckoutPlaceOrderModel pCheckoutPlaceOrderModel, boolean token, String f_uid) {
        Map<String, Object> notify = new HashMap<>();
        Map<String, Object> deliver = new HashMap<>();
        if (token) {

            deliver.put("deliverystatus", "SUCCESS");
            notify.put("title", "Order Delivered");
            notify.put("description", "Your order was delivered successfully");
            notify.put("status", "true");
        } else {
            deliver.put("deliverystatus", "FAILURE");
            notify.put("status", "false");
            notify.put("title", "Delivery unsuccessful");
            notify.put("description", "We are sorry that your order delivery was unsuccessful.");
        }
        notify.put("note_type", "deliver");
        notify.put("timestamp", FieldValue.serverTimestamp());

        //Add notification to user database
        mFirebaseFirestore.collection("users")
                .document(f_uid).collection("notifications")
                .document().set(notify);


        //Add order in user database
        mFirebaseFirestore.collection("users").document(f_uid)
                .collection("my_orders")
                .document(pCheckoutPlaceOrderModel.getTrans_id())
                .update(deliver);

        // add updated order to orders database
        mFirebaseFirestore.collection("delivery_orders")
                .document(pCheckoutPlaceOrderModel.getTrans_id())
                .update(deliver);
    }
}
