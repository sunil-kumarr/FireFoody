package com.firefoody.VendorActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.firefoody.Activites.Authentication;
import com.firefoody.R;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

public class DeliveryBoyActivity extends AppCompatActivity implements View.OnClickListener {

    MaterialCardView mSubscribedOrders;
    FirebaseAuth mFirebaseAuth;
    MaterialCardView mCustomOrders;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_dashboard);

        mFirebaseAuth= FirebaseAuth.getInstance();
        mSubscribedOrders=findViewById(R.id.delivery_default_order);
        mCustomOrders=findViewById(R.id.delivery_custom_orders);

        mSubscribedOrders.setOnClickListener(this);
        mCustomOrders.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.delivery_custom_orders:
                startActivity(new Intent(this,DeliveryCustomOrderActivity.class));
                v.setEnabled(false);
                break;

            case  R.id.delivery_default_order:
                startActivity(new Intent(this,DeliverySubscribedOrderActivity.class));
                v.setEnabled(false);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCustomOrders.setEnabled(true);
        mSubscribedOrders.setEnabled(true);
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
                            startActivity(new Intent(DeliveryBoyActivity.this, Authentication.class));
                            finish();
                        }
                    }
                }).create();
        vDialog.show();
    }

}
