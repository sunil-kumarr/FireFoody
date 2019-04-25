package com.example.rapidfood.Activites;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.rapidfood.Models.PaymentSubDataModel;
import com.example.rapidfood.Models.SubscriptionModel;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.example.rapidfood.Utils.GenerateUUIDClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class SubscriptionCheckoutActivity extends AppCompatActivity {


    private ConstraintLayout mOrder;
    private String tr;
    private TextView msubcost, msubval, mdetails, msubname, msubcoupon_Value, mTotalCost;
    private FirebaseInstances mFirebaseInstances;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirebaseFirestore;
    private GenerateUUIDClass vGenerateUUIDClass;
    private String sub_name;
    private PaymentSubDataModel mPaymentSubDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_checkout_layout);


        mFirebaseInstances = new FirebaseInstances();
        mFirebaseAuth = mFirebaseInstances.getFirebaseAuth();
        mFirebaseFirestore = mFirebaseInstances.getFirebaseFirestore();


        sub_name = getIntent().getStringExtra("sub_name");

        mOrder = findViewById(R.id.main_layout_holder);
        RelativeLayout googlepaybtn = findViewById(R.id.googlepay_button);
        RelativeLayout paytmButton = findViewById(R.id.paytm_btn);
        msubcost = findViewById(R.id.subscription_cost);
        msubval = findViewById(R.id.subscription_validity);
        mdetails = findViewById(R.id.subscription_detail);
        msubcoupon_Value = findViewById(R.id.subscription_coupon_offer);
        msubname = findViewById(R.id.subscription_type);
        mTotalCost = findViewById(R.id.subscription_total_cost);

        getSubDetails(sub_name);
        vGenerateUUIDClass = new GenerateUUIDClass();


        googlepaybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFirebaseAuth.getCurrentUser() != null) {
                    mFirebaseFirestore.collection("subscribed_user")
                            .document(mFirebaseAuth.getCurrentUser().getUid())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> pTask) {
                            if (pTask.isSuccessful()) {
                                if (pTask.getResult().exists()) {
                                    Snackbar.make(mOrder, "ALREADY SUBSCRIBED USER!", Snackbar.LENGTH_LONG).show();
                                } else {
                                    if (mFirebaseAuth.getCurrentUser() != null) {
                                        mPaymentSubDataModel = new PaymentSubDataModel();
                                        mPaymentSubDataModel.setCust_id(mFirebaseAuth.getCurrentUser().getUid());
                                        mPaymentSubDataModel.setDuration(String.valueOf(msubval.getText()));
                                        mPaymentSubDataModel.setMobile(mFirebaseAuth.getCurrentUser().getPhoneNumber());
                                        mPaymentSubDataModel.setSubcost(String.valueOf(msubcost.getText()));
                                        mPaymentSubDataModel.setSubcoupon(String.valueOf(msubcoupon_Value.getText()));
                                        mPaymentSubDataModel.setSubname(sub_name);
                                    }
                                    payUsingGooglePay();
                                }
                            } else {
                                Snackbar.make(mOrder, "Error has occurred!", Snackbar.LENGTH_LONG).show();

                            }
                        }
                    });

                }
            }
        });
        paytmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFirebaseAuth.getCurrentUser() != null) {
                    mFirebaseFirestore.collection("subscribed_user")
                            .document(mFirebaseAuth.getCurrentUser().getUid())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> pTask) {
                            if (pTask.isSuccessful()) {
                                if (pTask.getResult().exists()) {
                                    Snackbar.make(mOrder, "ALREADY SUBSCRIBED USER!", Snackbar.LENGTH_LONG).show();
                                } else {
                                    if (mFirebaseAuth.getCurrentUser() != null) {
                                        mPaymentSubDataModel = new PaymentSubDataModel();
                                        mPaymentSubDataModel.setCust_id(mFirebaseAuth.getCurrentUser().getUid());
                                        mPaymentSubDataModel.setDuration(String.valueOf(msubval.getText()));
                                        mPaymentSubDataModel.setMobile(mFirebaseAuth.getCurrentUser().getPhoneNumber());
                                        mPaymentSubDataModel.setSubcost(String.valueOf(msubcost.getText()));
                                        mPaymentSubDataModel.setSubcoupon(String.valueOf(msubcoupon_Value.getText()));
                                        mPaymentSubDataModel.setSubname(sub_name);
                                    }
                                    payUsingPayTM();
                                }
                            } else {
                                Snackbar.make(mOrder, "Error has occurred!", Snackbar.LENGTH_LONG).show();

                            }
                        }
                    });

                }
            }
        });
    }
        private void payUsingGooglePay () {
            tr = vGenerateUUIDClass.generateUniqueKeyUsingUUID();
            mPaymentSubDataModel.setOrder_id(tr);
            mPaymentSubDataModel.setAmount(getPaymentAmount());


            Intent intent = new Intent(SubscriptionCheckoutActivity.this, GooglePayActivity.class);
            intent.putExtra("payload", mPaymentSubDataModel);

            startActivity(intent);
        }

        private void payUsingPayTM () {
            tr = vGenerateUUIDClass.generateUniqueKeyUsingUUID();
            mPaymentSubDataModel.setOrder_id(tr);
            mPaymentSubDataModel.setAmount(getPaymentAmount());


            Intent intent = new Intent(SubscriptionCheckoutActivity.this, PayTMActivity.class);
            intent.putExtra("payload", mPaymentSubDataModel);

            startActivity(intent);
        }


        private String getPaymentAmount () {
            String amountt = String.valueOf(mTotalCost.getText());
            String transAmount = null;
            if (amountt != null) {
                StringBuilder vStringBuilder = new StringBuilder();
                vStringBuilder.append(amountt)
                        .append(".00");
                transAmount = vStringBuilder.toString();
            }
            return transAmount;
        }


        void getSubDetails (String pSub_name){
            mFirebaseFirestore.collection("subscriptions").document(pSub_name).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> pTask) {
                            if (pTask.isSuccessful()) {
                                DocumentSnapshot vSnapshot = pTask.getResult();
                                SubscriptionModel vModel = vSnapshot.toObject(SubscriptionModel.class);
                                if (vModel != null) {
                                    msubcost.setText(String.format("%s", vModel.getPrice()));
                                    mdetails.setText(String.format("**%s", vModel.getDetails()));
                                    msubval.setText(vModel.getDuration());
                                    msubname.setText(vModel.getType());
//                                Picasso.get().load(vModel.getImagesub())
//                                        .fit()
//                                        .into((ImageView) findViewById(R.id.sub_image));
                                    msubcoupon_Value.setText(String.format("%s", vModel.getCoupon()));
                                    long total_bill = (Long.parseLong(vModel.getPrice()) - Long.parseLong(vModel.getCoupon()));
                                    // Toast.makeText(GooglePayActivity.this, "" + Long.toString(total_bill), Toast.LENGTH_SHORT).show();
                                    mTotalCost.setText(String.format("%s", Long.toString(total_bill)));
                                }
                            }
                        }
                    });
        }
    }



