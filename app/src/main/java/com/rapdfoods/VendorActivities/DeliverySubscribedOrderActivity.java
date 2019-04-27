package com.rapdfoods.VendorActivities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rapdfoods.Adapters.OrderDefaultListAdapter;
import com.rapdfoods.Models.SubscribedUserModel;
import com.rapdfoods.Models.SubscriptionTransactionModel;
import com.rapdfoods.R;
import com.rapdfoods.Utils.FirebaseInstances;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DeliverySubscribedOrderActivity extends AppCompatActivity implements OrderDefaultListAdapter.UserDateListener {

    private FirebaseInstances firebaseInstances;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerOptions<SubscribedUserModel> mOptions;
    private FirestoreRecyclerAdapter mOrderListAdapter;
    private RecyclerView orderRecyclerView;
    private TextView mTotalCount;
    private MaterialCardView mStatContainer;
    private Calendar mCalendar;
    private Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_default_order_activity);

        mContext = getApplicationContext();
        firebaseInstances = new FirebaseInstances();
        firebaseFirestore = firebaseInstances.getFirebaseFirestore();
        firebaseAuth = firebaseInstances.getFirebaseAuth();

        mCalendar = Calendar.getInstance();

        mStatContainer = findViewById(R.id.Statistics_Container);
        mStatContainer.setVisibility(View.GONE);
        findViewById(R.id.subscibed_text).setVisibility(View.GONE);
        orderRecyclerView = findViewById(R.id.vendor_default_recycler_view);

        Query query = firebaseFirestore.collection("subscribed_user")
                .orderBy("start_date", Query.Direction.DESCENDING);
        mOptions = new FirestoreRecyclerOptions.Builder<SubscribedUserModel>()
                .setQuery(query, SubscribedUserModel.class).build();

        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        mOrderListAdapter = new OrderDefaultListAdapter(mOptions, orderRecyclerView, DeliverySubscribedOrderActivity.this);

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
        //Toast.makeText(this, ""+monthS+" "+ar[2]+" "+ar.length, Toast.LENGTH_SHORT).show();
        StringBuilder builder = new StringBuilder();
        builder.append(yearS).append("-").append(monthS).append("-").append(day);
        return String.valueOf(builder.toString());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mOrderListAdapter.startListening();

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

        showBottomSheetDialog(getCurrentDate(),userId);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mOrderListAdapter.stopListening();
    }

    private void showBottomSheetDialog(String pDate,String f_uid) {
        View view = LayoutInflater.from(DeliverySubscribedOrderActivity.this).inflate(R.layout.layout_bottom_time_sheet, null);
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


        CalendarBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog vDialog = new AlertDialog.Builder(DeliverySubscribedOrderActivity.this)
                        .setTitle("CONFIRM DELIVERY")
                        .setIcon(R.drawable.ic_check_circlce_button)
                        .setMessage("Are you sure you want to confirm Delivery?")
                        .setPositiveButton("YES,SURE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TextView vTextView = (TextView) v;
                                vTextView.setTextColor(mContext.getResources().getColor(R.color.white));
                                vTextView.setBackgroundColor(mContext.getResources().getColor(R.color.green_500));
                                setUserDeliverTiming("breakfast",f_uid);
                                CalendarBreakfast.setEnabled(false);
                            }
                        }).create();
                vDialog.show();
            }
        });
        CalendarLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog vDialog = new AlertDialog.Builder(DeliverySubscribedOrderActivity.this)
                        .setTitle("CONFIRM DELIVERY")
                        .setIcon(R.drawable.ic_check_circlce_button)
                        .setMessage("Are you sure you want to confirm Delivery?")
                        .setPositiveButton("YES,SURE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TextView vTextView = (TextView) v;
                                vTextView.setTextColor(mContext.getResources().getColor(R.color.white));
                                vTextView.setBackgroundColor(mContext.getResources().getColor(R.color.green_500));
                                setUserDeliverTiming("lunch",f_uid);
                                CalendarLunch.setEnabled(false);
                            }
                        }).create();
                vDialog.show();
            }
        });
        CalendarDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog vDialog = new AlertDialog.Builder(DeliverySubscribedOrderActivity.this)
                        .setTitle("CONFIRM DELIVERY")
                        .setIcon(R.drawable.ic_check_circlce_button)
                        .setMessage("Are you sure you want to confirm Delivery?")
                        .setPositiveButton("YES,SURE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TextView vTextView = (TextView) v;
                                vTextView.setTextColor(mContext.getResources().getColor(R.color.white));
                                vTextView.setBackgroundColor(mContext.getResources().getColor(R.color.green_500));
                                setUserDeliverTiming("dinner",f_uid);
                                CalendarDinner.setEnabled(false);

                            }
                        }).create();
                vDialog.show();
            }
        });
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();
    }
    private void sendInAppNotification(String f_uid, String timing ){
        Map<String, Object> notify = new HashMap<>();
        notify.put("title", "Order Delivered");
        notify.put("description", "Your order for "+timing+" delivered successfully");
        notify.put("status", "true");
        notify.put("note_type", "deliver");
        notify.put("timestamp", FieldValue.serverTimestamp());
       firebaseFirestore.collection("users").document(f_uid).collection("notifications")
                .document().set(notify);
    }

    private void setUserDeliverTiming(String timing,String f_uid){
        Map<String,Object> deliver=new HashMap<>();
        deliver.put(timing,"delivered");
        sendInAppNotification(f_uid,timing);
        firebaseFirestore.collection("subscribed_user")
                .document(f_uid)
                .collection("dates")
                .document(getCurrentDate())
                .update(deliver);

    }

}

