package com.example.rapidfood.Vendor_files;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.rapidfood.Activites.Authentication;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private MaterialCardView mCreateSubBtn;
    private MaterialCardView mCreatePackBtn;
    private MaterialCardView mAddDishBtn;
    private MaterialCardView mDeliveryOrderBtn;
    private MaterialCardView mUserView;
    private MaterialCardView mSubsriberBtn;
    FirebaseInstances mFirebaseInstances;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mFirebaseInstances=new FirebaseInstances();
        mFirebaseAuth=mFirebaseInstances.getFirebaseAuth();

        mToolbar = findViewById(R.id.toolbar_vendor);
        mCreateSubBtn = findViewById(R.id.vendor_create_subs);
        mCreatePackBtn = findViewById(R.id.vendor_create_packs);
        mAddDishBtn = findViewById(R.id.vendor_add_dish);
        mUserView=findViewById(R.id.vendor_user_view);

        mCreatePackBtn.setOnClickListener(this);
        mUserView.setOnClickListener(this);
        mAddDishBtn.setOnClickListener(this);
        mCreateSubBtn.setOnClickListener(this);

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
            case R.id.vendor_user_view:
                startActivity(new Intent(DashboardActivity.this, UserPreviewActivity.class));
                Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
                break;
            case R.id.vendor_orders:
                Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.vendor_sub_customers:
                Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.vendor_create_subs:
                startActivity(new Intent(DashboardActivity.this,VendorCreateSubscription.class));
                break;
            case R.id.vendor_create_packs:
                startActivity(new Intent(DashboardActivity.this,VendorCreatePackage.class));
                break;
            case R.id.vendor_add_dish:
                startActivity(new Intent(DashboardActivity.this, VendorAddDish.class));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater vInflater=getMenuInflater();
        vInflater.inflate(R.menu.dashboard_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.my_profile){
            if(mFirebaseUser!=null){
                mFirebaseAuth.signOut();
                startActivity(new Intent(DashboardActivity.this, Authentication.class));
              finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
       mFirebaseUser= mFirebaseAuth.getCurrentUser();
    }
}
