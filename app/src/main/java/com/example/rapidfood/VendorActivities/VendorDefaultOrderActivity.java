package com.example.rapidfood.VendorActivities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rapidfood.Adapters.OrderDefaultListAdapter;
import com.example.rapidfood.Adapters.SubscriberListAdapter;
import com.example.rapidfood.Models.SubscribedUserModel;
import com.example.rapidfood.Models.SubscriptionTransactionModel;
import com.example.rapidfood.Models.UserModel;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class VendorDefaultOrderActivity extends AppCompatActivity implements OrderDefaultListAdapter.UserDateListener {

    private FirebaseInstances firebaseInstances;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerOptions<SubscribedUserModel> mOptions;
    private FirestoreRecyclerAdapter mOrderListAdapter;
    private RecyclerView orderRecyclerView;
    private TextView mTotalCount;
    private TextView mDinnerCount,mBreakfastCount,mLunchCount;
    private final static String CURRENT_DATE = String.valueOf(CalendarDay.today());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_default_order_activity);

        firebaseInstances = new FirebaseInstances();
        firebaseFirestore = firebaseInstances.getFirebaseFirestore();
        firebaseAuth = firebaseInstances.getFirebaseAuth();


        mTotalCount=findViewById(R.id.deliver_total_count);
        mDinnerCount=findViewById(R.id.deliver_dinner_count);
        mBreakfastCount=findViewById(R.id.deliver_breakfast_count);
        mLunchCount=findViewById(R.id.deliver_lunch_count);
        orderRecyclerView = findViewById(R.id.vendor_default_recycler_view);

        Query query = firebaseFirestore.collection("subscribed_user")
                .orderBy("start_date", Query.Direction.DESCENDING);
        mOptions = new FirestoreRecyclerOptions.Builder<SubscribedUserModel>()
                .setQuery(query, SubscribedUserModel.class).build();

        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));

        mOrderListAdapter = new OrderDefaultListAdapter(mOptions, orderRecyclerView, VendorDefaultOrderActivity.this);

        orderRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                orderRecyclerView.setAdapter(mOrderListAdapter);
                orderRecyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        });


    }

    private String getCurrentDate(){
        String[] ar=CURRENT_DATE.split("-");
        StringBuilder builder=new StringBuilder();
        builder.append(ar[0]);
        int month=Integer.parseInt(ar[1]);
        if(month<10){
            builder.append("-").append("0"+ar[1]);
            builder.append("-").append(ar[2]);
            return builder.toString();
        }
        return CURRENT_DATE;

    }
    @Override
    protected void onStart() {
        super.onStart();
        mOrderListAdapter.startListening();
        //Toast.makeText(this, ""+CalendarDay.today(), Toast.LENGTH_SHORT).show();
        firebaseFirestore.collection("subscribed_user")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int count=queryDocumentSnapshots.getDocuments().size();
                mTotalCount.setText(String.valueOf(count));
                firebaseFirestore.collection("delivery_cancelled")
                        .document(getCurrentDate())
                        .collection("breakfast")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            QuerySnapshot queryDocumentSnapshots=task.getResult();
                            int bre = queryDocumentSnapshots.getDocuments().size();
                            mBreakfastCount.setText(String.valueOf(bre));
                        }
                        else{
                            Toast.makeText(VendorDefaultOrderActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                firebaseFirestore.collection("delivery_cancelled")
                        .document(getCurrentDate())
                        .collection("lunch")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            int bre = queryDocumentSnapshots.getDocuments().size();
                            int d = count - bre;
                            mLunchCount.setText(String.valueOf(d));
                        }
                        else{
                           // Toast.makeText(VendorDefaultOrderActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                firebaseFirestore.collection("delivery_cancelled")
                        .document(getCurrentDate())
                        .collection("dinner")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            int bre = queryDocumentSnapshots.getDocuments().size();
                            int d = count - bre;
                            mDinnerCount.setText(String.valueOf(d));
                        }
                        else{
                            //Toast.makeText(VendorDefaultOrderActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

        Map<String, Object> mp = new HashMap<>();
        mp.put("timestamp", FieldValue.serverTimestamp());
        firebaseFirestore.collection("company_data").document("timestamp").update(mp);
    }

    @Override
    public void getUserDates(String userId) {
        showBottomSheetDialog(getCurrentDate());
    }

    @Override
    protected void onStop() {
        super.onStop();
        mOrderListAdapter.stopListening();
    }

    private void showBottomSheetDialog(String pDate) {
        View view = LayoutInflater.from(VendorDefaultOrderActivity.this).inflate(R.layout.layout_bottom_time_sheet, null);
        TextView dateHead = view.findViewById(R.id.calendar_Choice_date);
        dateHead.setText(getCurrentDate());
        TextView CalendarBreakfast = view.findViewById(R.id.calendar_Choice_breakfast);
        TextView CalendarLunch = view.findViewById(R.id.calendar_Choice_lunch);
        TextView CalendarDinner = view.findViewById(R.id.calendar_Choice_dinner);
        firebaseFirestore.collection("subscribed_user")
                .document()
                .collection("dates")
                .document(getCurrentDate()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot pDocumentSnapshot) {
                if (pDocumentSnapshot.exists() && pDocumentSnapshot != null) {
                    if (Objects.equals(pDocumentSnapshot.getString("breakfast"), "true")) {
                        CalendarBreakfast.setEnabled(false);
                        CalendarBreakfast.setTextColor(getResources().getColor(R.color.white));
                        CalendarBreakfast.setBackgroundColor(getResources().getColor(R.color.red_500));
                    }
                    if (Objects.equals(pDocumentSnapshot.getString("lunch"), "true")) {
                        CalendarLunch.setEnabled(false);
                        CalendarLunch.setTextColor(getResources().getColor(R.color.white));
                        CalendarLunch.setBackgroundColor(getResources().getColor(R.color.red_500));
                    }
                    if (Objects.equals(pDocumentSnapshot.getString("dinner"), "true")) {
                        CalendarDinner.setEnabled(false);
                        CalendarDinner.setTextColor(getResources().getColor(R.color.white));
                        CalendarDinner.setBackgroundColor(getResources().getColor(R.color.red_500));
                    }
                }
            }
        });
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();
    }

}

