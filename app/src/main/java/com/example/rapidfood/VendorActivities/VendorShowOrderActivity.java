package com.example.rapidfood.VendorActivities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.rapidfood.Adapters.OrderListAdapter;
import com.example.rapidfood.Models.CheckoutPlaceOrderModel;
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
        Button vButton = (Button) pView;
        switch (vButton.getId()) {
            case R.id.order_btn_confirm:

                vButton.setEnabled(false);
                vButton.setText("Confirmed");
                Drawable img = getResources().getDrawable(R.drawable.ic_check_white_24dp);
                vButton.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                vButton.setBackgroundColor(getResources().getColor(R.color.green_500));
                changeVerificationStatus(pCheckoutPlaceOrderModel, true, pCheckoutPlaceOrderModel.getUid());

                break;

        }
    }

    @Override
    public void onClickFailed(View pView, CheckoutPlaceOrderModel pCheckoutPlaceOrderModel) {
        Button vButton = (Button) pView;
        switch (vButton.getId()) {
            case R.id.order_btn_cancel:

                vButton.setEnabled(false);
                vButton.setText("Canceled");
                Drawable img = getResources().getDrawable(R.drawable.ic_circle_cross_24dp);
                vButton.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                vButton.setBackgroundColor(getResources().getColor(R.color.red_500));
                changeVerificationStatus(pCheckoutPlaceOrderModel, false, pCheckoutPlaceOrderModel.getUid());


                break;
        }
    }

    void changeVerificationStatus(CheckoutPlaceOrderModel pCheckoutPlaceOrderModel, boolean token, String f_uid) {
        Map<String, Object> notify = new HashMap<>();
        if (token) {

            pCheckoutPlaceOrderModel.setOrderStatus("SUCCESS");
            notify.put("title", "Your order is confirmed");
            notify.put("status", true);
        } else {
            pCheckoutPlaceOrderModel.setOrderStatus("FAILURE");
            notify.put("status", false);
            notify.put("title", "Your order is cancelled");
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