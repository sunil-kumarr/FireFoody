package com.rapdfoods.VendorActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rapdfoods.Activites.Authentication;
import com.rapdfoods.Models.TimeStamp;
import com.rapdfoods.R;
import com.rapdfoods.Utils.FirebaseInstances;
import com.rapdfoods.Utils.UtilClass;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private MaterialCardView mCreateSubBtn;
    private MaterialCardView mCreatePackBtn;
    private MaterialCardView mAddDishBtn;
    private MaterialCardView mDeliveryOrderBtn;
    private MaterialCardView mUserView;
    private MaterialCardView mQRScanner;
    private MaterialCardView mTodayMenu;
    private MaterialCardView mSubsriberBtn;
    private MaterialCardView mCustomOrders;
    private FirebaseInstances mFirebaseInstances;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private LinearLayout mNoInternetLAyout;
    private FirebaseFirestore mFirestore;
    private Button mCheckInternet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mFirebaseInstances = new FirebaseInstances();
        mFirebaseAuth = mFirebaseInstances.getFirebaseAuth();
        mFirestore=mFirebaseInstances.getFirebaseFirestore();

        mCreateSubBtn = findViewById(R.id.vendor_create_subs);
        mCreatePackBtn = findViewById(R.id.vendor_create_packs);
        mAddDishBtn = findViewById(R.id.vendor_add_dish);
        mTodayMenu = findViewById(R.id.vendor_today_menu);
        mSubsriberBtn = findViewById(R.id.vendor_sub_customers);
        mCustomOrders = findViewById(R.id.vendor_orders);
        mCheckInternet=findViewById(R.id.btn_check_internet);
        mNoInternetLAyout=findViewById(R.id.no_internet);
        mQRScanner=findViewById(R.id.vendor_qr_scanner);
        mDeliveryOrderBtn=findViewById(R.id.vendor_default_order);

        mCreatePackBtn.setOnClickListener(this);
        mCustomOrders.setOnClickListener(this);
        mSubsriberBtn.setOnClickListener(this);
        mAddDishBtn.setOnClickListener(this);
        mCreateSubBtn.setOnClickListener(this);
        mTodayMenu.setOnClickListener(this);
        mCheckInternet.setOnClickListener(this);
        mQRScanner.setOnClickListener(this);
        mDeliveryOrderBtn.setOnClickListener(this);

//        mFirestore.collection("subscribed_user")
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                        if(!queryDocumentSnapshots.isEmpty())
//                        {
//                            List<DocumentSnapshot> users=queryDocumentSnapshots.getDocuments();
//                            for(DocumentSnapshot userD:users){
//
//                                String currentDuration=userD.getString("")
//                            }
//                        }
//                    }
//                });
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.vendor_default_order:
                startActivity(new Intent(DashboardActivity.this, VendorDefaultOrderActivity.class));
                break;
            case R.id.vendor_orders:

                startActivity(new Intent(DashboardActivity.this, VendorShowOrderActivity.class));
                break;
            case R.id.vendor_sub_customers:
                startActivity(new Intent(DashboardActivity.this, UserSubscriberActivity.class));
                break;
            case R.id.vendor_create_subs:
                mCreateSubBtn.setEnabled(false);
                startActivity(new Intent(DashboardActivity.this, VendorCreateSubscription.class));
                break;
            case R.id.vendor_create_packs:
                mCreatePackBtn.setEnabled(false);
                startActivity(new Intent(DashboardActivity.this, VendorCreatePackage.class));
                break;
            case R.id.vendor_add_dish:
                mAddDishBtn.setEnabled(false);
                startActivity(new Intent(DashboardActivity.this, VendorAddDish.class));
                break;
            case R.id.vendor_today_menu:
                startActivity(new Intent(DashboardActivity.this, VendorTodayMenuActivity.class));
                break;
            case R.id.vendor_qr_scanner:
                startActivity(new Intent(DashboardActivity.this,VendorQRScannerActivity.class));
                break;
            case R.id.btn_check_internet:
                if(UtilClass.isConnectedToNetwork(this)){
                    mNoInternetLAyout.setVisibility(View.GONE);
                }
                else{
                    mNoInternetLAyout.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    public void logoutFunction(View v){
        AlertDialog vDialog=new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure that you want to logout from admin panel?")
                .setIcon(R.drawable.ic_logout_new_red_24dp)
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ( mFirebaseAuth.getCurrentUser()!= null) {
                            mFirebaseAuth.signOut();
                            startActivity(new Intent(DashboardActivity.this, Authentication.class));
                            finish();
                        }
                    }
                }).create();
        vDialog.show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mCreatePackBtn.setEnabled(true);
        mCreateSubBtn.setEnabled(true);
        mAddDishBtn.setEnabled(true);
        if (!UtilClass.isConnectedToNetwork(this)) {
            findViewById(R.id.no_internet).setVisibility(View.VISIBLE);
        }
        else{
            mNoInternetLAyout.setVisibility(View.GONE);
        }
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Map<String, Object> mp = new HashMap<>();
        mp.put("timestamp", FieldValue.serverTimestamp());
        mFirebaseInstances.getFirebaseFirestore().collection("company_data").document("timestamp").update(mp);


    }
}
