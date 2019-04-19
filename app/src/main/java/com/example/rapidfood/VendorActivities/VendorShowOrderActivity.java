package com.example.rapidfood.VendorActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rapidfood.Adapters.OrderListAdapter;
import com.example.rapidfood.Models.CheckoutPlaceOrderModel;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
                .collection("delivery_orders");
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
                changeVerificationStatus(pCheckoutPlaceOrderModel.getTrans_id(), true,pCheckoutPlaceOrderModel.getUid());
                break;

        }
    }

    @Override
    public void onClickFailde(View pView, CheckoutPlaceOrderModel pCheckoutPlaceOrderModel) {
        Button vButton = (Button) pView;
        switch (vButton.getId()){
            case R.id.order_btn_cancel:
                changeVerificationStatus(pCheckoutPlaceOrderModel.getTrans_id(), false,pCheckoutPlaceOrderModel.getUid());
                break;
        }
    }

    void changeVerificationStatus(String t_string, boolean token,String f_uid) {
        Map<String, Object> mp = new HashMap<>();
        if(token) {
            mp.put("orderStatus", "SUCCESS");
            Map<String,Object> notify=new HashMap<>();
            notify.put("note_type","order");
            notify.put("title","Your order is confirmed");
            notify.put("timestamp", FieldValue.serverTimestamp());
            mFirebaseFirestore.collection("users").document(f_uid).collection("notifications")
                    .document().set(notify);
        }
        else{
            mp.put("orderStatus","FAILURE");
        }
        mFirebaseFirestore.collection("delivery_orders").document(t_string).update(mp).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> pTask) {
                        if (pTask.isSuccessful()) {
                            Toast.makeText(VendorShowOrderActivity.this, "Order Confirmed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(VendorShowOrderActivity.this, "Order Cancel", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
}
