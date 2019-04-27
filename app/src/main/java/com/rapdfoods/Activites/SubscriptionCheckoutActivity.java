package com.rapdfoods.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.rapdfoods.Models.PaymentSubDataModel;
import com.rapdfoods.Models.SubscribedUserModel;
import com.rapdfoods.Models.SubscriptionModel;
import com.rapdfoods.R;
import com.rapdfoods.Utils.FirebaseInstances;
import com.rapdfoods.Utils.GenerateUUIDClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SubscriptionCheckoutActivity extends AppCompatActivity {


    private static final int USE_GOOGLE_PAY = 0;
    private static final int USE_PAYTM = 1;


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
                checkSubscriptionStatus(USE_GOOGLE_PAY);
            }
        });
        paytmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSubscriptionStatus(USE_PAYTM);
            }
        });
    }

    private void checkSubscriptionStatus(int paymentOption) {
        if (mFirebaseAuth.getCurrentUser() != null) {
            mFirebaseFirestore.collection("subscribed_user")
                    .document(mFirebaseAuth.getCurrentUser().getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> pTask) {
                    if (pTask.isSuccessful()) {
                        DocumentSnapshot userSubModel=pTask.getResult();
                        assert userSubModel != null;
                        if (userSubModel.exists()) {
                            SubscribedUserModel mSubUSer=userSubModel.toObject(SubscribedUserModel.class);
                            upgradeCustomerSubscription(mSubUSer,paymentOption);
                        } else {
                            newCustomerSubscription(paymentOption);
                        }
                    } else {
                        Snackbar.make(mOrder, "Error has occurred!", Snackbar.LENGTH_LONG).show();

                    }
                }
            });

        }
    }

    private void upgradeCustomerSubscription(SubscribedUserModel pSubscribedUser,int paymentOption) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_subscription_difference, null);
        TextView pCurrentBal=view.findViewById(R.id.upgrade_sub_current_price);
        TextView pCurrentVal=view.findViewById(R.id.upgrade_sub_current_time);
        TextView pUpgradeBal=view.findViewById(R.id.upgrade_sub_new_price);
        TextView pUpgradeVal=view.findViewById(R.id.upgrade_sub_new_time);
        TextView pTotalBal=view.findViewById(R.id.upgrade_total_price);
        TextView pTotalVal=view.findViewById(R.id.upgrade_total_time);

        pCurrentBal.setText(String.format("₹%s", pSubscribedUser.getBalance()));
        pCurrentVal.setText(String.format("%s Days", pSubscribedUser.getDuration()));

        pUpgradeBal.setText(String.format("₹%s", String.valueOf(msubcost.getText())));
        pUpgradeVal.setText(String.format("%s Days", String.valueOf(msubval.getText())));


        final int total=Integer.parseInt(String.valueOf(msubcost.getText()))+Integer.parseInt(pSubscribedUser.getBalance());

        String newBal="₹"+String.valueOf(total);
        String payment=String.valueOf(total);
        pTotalBal.setText(newBal);
        pTotalVal.setText(String.format("%s Days", msubval.getText()));
        view.findViewById(R.id.upgrade_sub_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFirebaseAuth.getCurrentUser() != null) {
                    mPaymentSubDataModel = new PaymentSubDataModel();
                    mPaymentSubDataModel.setCust_id(mFirebaseAuth.getCurrentUser().getUid());
                    mPaymentSubDataModel.setDuration(String.valueOf(msubval.getText()));
                    mPaymentSubDataModel.setMobile(mFirebaseAuth.getCurrentUser().getPhoneNumber());
                    mPaymentSubDataModel.setSubcost(newBal);
                    mPaymentSubDataModel.setSubcoupon(String.valueOf(msubcoupon_Value.getText()));
                    mPaymentSubDataModel.setSubname(sub_name);
                }

                switch (paymentOption) {
                    case USE_GOOGLE_PAY:
                        payUsingGooglePay(getPaymentAmount());
                        break;
                    case USE_PAYTM:
                        payUsingPayTM(getPaymentAmount());
                        break;
                    default:
                        payUsingGooglePay(getPaymentAmount());
                        break;
                }



            }
        });


        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();


//        Snackbar.make(mOrder, "ALREADY SUBSCRIBED USER!", Snackbar.LENGTH_LONG).show();
    }

    private void newCustomerSubscription(int paymentOption) {
        if (mFirebaseAuth.getCurrentUser() != null) {
            mPaymentSubDataModel = new PaymentSubDataModel();
            mPaymentSubDataModel.setCust_id(mFirebaseAuth.getCurrentUser().getUid());
            mPaymentSubDataModel.setDuration(String.valueOf(msubval.getText()));
            mPaymentSubDataModel.setMobile(mFirebaseAuth.getCurrentUser().getPhoneNumber());
            mPaymentSubDataModel.setSubcost(String.valueOf(msubcost.getText()));
            mPaymentSubDataModel.setSubcoupon(String.valueOf(msubcoupon_Value.getText()));
            mPaymentSubDataModel.setSubname(sub_name);
        }
        switch (paymentOption) {
            case USE_GOOGLE_PAY:
                payUsingGooglePay(getPaymentAmount());
                break;
            case USE_PAYTM:
                payUsingPayTM(getPaymentAmount());
                break;
            default:
                payUsingGooglePay(getPaymentAmount());
                break;
        }
    }

    private void payUsingGooglePay(String payment) {
        tr = vGenerateUUIDClass.generateUniqueKeyUsingUUID();
        mPaymentSubDataModel.setOrder_id(tr);
        mPaymentSubDataModel.setAmount(payment);


        Intent intent = new Intent(SubscriptionCheckoutActivity.this, GooglePayActivity.class);
        intent.putExtra("payload", mPaymentSubDataModel);

        startActivity(intent);
    }

    private void payUsingPayTM(String payment) {
        tr = vGenerateUUIDClass.generateUniqueKeyUsingUUID();
        mPaymentSubDataModel.setOrder_id(tr);
        mPaymentSubDataModel.setAmount(payment);

        Intent intent = new Intent(SubscriptionCheckoutActivity.this, PayTMActivity.class);
        intent.putExtra("payload", mPaymentSubDataModel);

        startActivity(intent);
    }


    private String getPaymentAmount() {
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


    void getSubDetails(String pSub_name) {
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



