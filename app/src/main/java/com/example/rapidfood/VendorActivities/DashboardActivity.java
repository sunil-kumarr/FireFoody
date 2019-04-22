package com.example.rapidfood.VendorActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.rapidfood.Activites.Authentication;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.example.rapidfood.Utils.UtilClass;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
    private Button mCheckInternet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mFirebaseInstances = new FirebaseInstances();
        mFirebaseAuth = mFirebaseInstances.getFirebaseAuth();

        mToolbar = findViewById(R.id.toolbar_vendor);
        mCreateSubBtn = findViewById(R.id.vendor_create_subs);
        mCreatePackBtn = findViewById(R.id.vendor_create_packs);
        mAddDishBtn = findViewById(R.id.vendor_add_dish);
        mTodayMenu = findViewById(R.id.vendor_today_menu);
        mSubsriberBtn = findViewById(R.id.vendor_sub_customers);
        mCustomOrders = findViewById(R.id.vendor_orders);
        mCheckInternet=findViewById(R.id.btn_check_internet);
        mNoInternetLAyout=findViewById(R.id.no_internet);
        mQRScanner=findViewById(R.id.vendor_qr_scanner);

        mCreatePackBtn.setOnClickListener(this);
        mCustomOrders.setOnClickListener(this);
        mSubsriberBtn.setOnClickListener(this);
        mAddDishBtn.setOnClickListener(this);
        mCreateSubBtn.setOnClickListener(this);
        mTodayMenu.setOnClickListener(this);
        mCheckInternet.setOnClickListener(this);
        mQRScanner.setOnClickListener(this);

        setSupportActionBar(mToolbar);
        ActionBar vActionBar = getSupportActionBar();
        if (vActionBar != null) {
            vActionBar.setDisplayShowHomeEnabled(true);
            vActionBar.setDisplayShowTitleEnabled(true);
            vActionBar.setDisplayHomeAsUpEnabled(true);
            vActionBar.setHomeAsUpIndicator(R.drawable.ic_apps_black_24dp);
        }

    }


    @Override
    public void onClick(View v) {
        MaterialCardView mTapped = (MaterialCardView) v;
        switch (mTapped.getId()) {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater vInflater = getMenuInflater();
        vInflater.inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.shutdown_dashboard) {
            AlertDialog vDialog=new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure that you want to logout from admin panel?")
                    .setIcon(R.drawable.ic_logout_new_red_24dp)
                    .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mFirebaseUser != null) {
                                mFirebaseAuth.signOut();
                                startActivity(new Intent(DashboardActivity.this, Authentication.class));
                                finish();
                            }
                        }
                    }).create();
             vDialog.show();
        }
        return super.onOptionsItemSelected(item);
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

}
