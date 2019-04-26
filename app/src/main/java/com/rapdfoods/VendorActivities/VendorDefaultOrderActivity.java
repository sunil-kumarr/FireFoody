package com.rapdfoods.VendorActivities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rapdfoods.Adapters.OrderDefaultListAdapter;
import com.rapdfoods.Models.SubscribedUserModel;
import com.rapdfoods.R;
import com.rapdfoods.Utils.FirebaseInstances;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.HashMap;
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
    private TextView mDinnerCount, mBreakfastCount, mLunchCount;
    private Calendar mCalendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_default_order_activity);

        firebaseInstances = new FirebaseInstances();
        firebaseFirestore = firebaseInstances.getFirebaseFirestore();
        firebaseAuth = firebaseInstances.getFirebaseAuth();

        mCalendar = Calendar.getInstance();

        mTotalCount = findViewById(R.id.deliver_total_count);
        mDinnerCount = findViewById(R.id.deliver_dinner_count);
        mBreakfastCount = findViewById(R.id.deliver_breakfast_count);
        mLunchCount = findViewById(R.id.deliver_lunch_count);
        orderRecyclerView = findViewById(R.id.vendor_default_recycler_view);

        Query query = firebaseFirestore.collection("subscribed_user")
                .orderBy("start_date", Query.Direction.DESCENDING);
        mOptions = new FirestoreRecyclerOptions.Builder<SubscribedUserModel>()
                .setQuery(query, SubscribedUserModel.class).build();

        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        mOrderListAdapter = new OrderDefaultListAdapter(mOptions, orderRecyclerView, VendorDefaultOrderActivity.this);

        orderRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                orderRecyclerView.setAdapter(mOrderListAdapter);
                orderRecyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        });


    }

    private String getMonth(String month) {
        switch (month) {
            case "Jan":
                return "01";
            case "Feb":
                return "02";
            case "Mar":
                return "03";

            case "Apr":
                return "04";

            case "May":
                return "05";
            case "Jun":
                return "06";
            case "Jul":
                return "07";
            case "Aug":
                return "08";
            case "Sept":
                return "09";
            case "Oct":
                return "10";
            case "Nov":
                return "11";

            case "Dec":
                return "12";

        }
        return "00";
    }

    private String getCurrentDate() {
        String[] ar = String.valueOf(mCalendar.getTime()).split(" ");
        String monthS = getMonth(ar[1]);
        String yearS = ar[5];
        String day = ar[2];
        // Toast.makeText(this, ""+monthS+" "+ar[2]+" "+ar.length, Toast.LENGTH_SHORT).show();
        StringBuilder builder = new StringBuilder();
        builder.append(yearS).append("-").append(monthS).append("-").append(day);
        return String.valueOf(builder.toString());
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


                int count = queryDocumentSnapshots.getDocuments().size();
                mTotalCount.setText(String.valueOf(count));


                firebaseFirestore.collection("delivery_cancelled")
                        .document(getCurrentDate())
                        .collection("breakfast")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

                                int bre = queryDocumentSnapshots.size();
                                bre = count - bre;
                                mBreakfastCount.setText(String.valueOf(bre));


                            }
                        });
                firebaseFirestore.collection("delivery_cancelled")
                        .document(getCurrentDate())
                        .collection("lunch")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                                int lun = queryDocumentSnapshots.size();
                                lun = count - lun;
                                mLunchCount.setText(String.valueOf(lun));
                            }
                        });
                firebaseFirestore.collection("delivery_cancelled")
                        .document(getCurrentDate())
                        .collection("dinner")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

                                int dinner=queryDocumentSnapshots.size();
                                dinner=count-dinner;
                                mDinnerCount.setText(String.valueOf(dinner));
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
        showBottomSheetDialog(userId);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mOrderListAdapter.stopListening();
    }

    private void showBottomSheetDialog(String f_uid) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_bottom_time_sheet, null);
        TextView dateHead = view.findViewById(R.id.calendar_Choice_date);
        dateHead.setText(getCurrentDate());
        TextView CalendarBreakfast = view.findViewById(R.id.calendar_Choice_breakfast);
        TextView CalendarLunch = view.findViewById(R.id.calendar_Choice_lunch);
        TextView CalendarDinner = view.findViewById(R.id.calendar_Choice_dinner);
        firebaseFirestore.collection("subscribed_user")
                .document(f_uid)
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
                    if (Objects.equals(pDocumentSnapshot.getString("breakfast"), "delivered")) {
                        CalendarBreakfast.setEnabled(false);
                        CalendarBreakfast.setTextColor(getResources().getColor(R.color.white));
                        CalendarBreakfast.setBackgroundColor(getResources().getColor(R.color.green_500));
                    }
                    if (Objects.equals(pDocumentSnapshot.getString("lunch"), "delivered")) {
                        CalendarLunch.setEnabled(false);
                        CalendarLunch.setTextColor(getResources().getColor(R.color.white));
                        CalendarLunch.setBackgroundColor(getResources().getColor(R.color.green_500));
                    }
                    if (Objects.equals(pDocumentSnapshot.getString("dinner"), "delivered")) {
                        CalendarDinner.setEnabled(false);
                        CalendarDinner.setTextColor(getResources().getColor(R.color.white));
                        CalendarDinner.setBackgroundColor(getResources().getColor(R.color.green_500));
                    }
                }
            }
        });

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();
    }

}

