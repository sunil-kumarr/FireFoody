package com.example.rapidfood.Activites;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rapidfood.Models.PaymentSubDataModel;
import com.example.rapidfood.Models.SubscriptionModel;
import com.example.rapidfood.Models.SubscriptionTransactionModel;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.example.rapidfood.Utils.GenerateUUIDClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class GooglePayActivity extends AppCompatActivity {
    private static final int TEZ_REQUEST_CODE = 123;
    private ConstraintLayout mPAymentSuccess, mPaymentFailure;
    private static final String GOOGLE_TEZ_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    private static final String TAG = "GooglePayActivity";
    private PreferenceManager mPreferenceManager;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseInstances firebaseInstances;
    private PaymentSubDataModel mPayLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_tm);
        firebaseInstances = new FirebaseInstances();
        mFirebaseFirestore = firebaseInstances.getFirebaseFirestore();
        mPAymentSuccess = findViewById(R.id.payment_success);
        mPaymentFailure = findViewById(R.id.payment_failure);
        Intent intent = getIntent();
        if (intent.getExtras()!= null) {
            mPayLoad=(PaymentSubDataModel) Objects.requireNonNull(intent.getExtras()).getSerializable("payload");

            MakePaymentGooglePay();
        }




    }

    private void MakePaymentGooglePay() {

        Uri uri = new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", "casrisaurav@okaxis")
                .appendQueryParameter("pn", "sunil kumar")
                .appendQueryParameter("mc", "12471")
                .appendQueryParameter("tr", mPayLoad.getOrder_id())
                .appendQueryParameter("tn", "Rapidfoods subscription")
                .appendQueryParameter("am", mPayLoad.getAmount())
                .appendQueryParameter("cu", "INR")
                .appendQueryParameter("url", "https://techonogb.com")
                .build();
        PackageManager packageManager = getApplication().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(GOOGLE_TEZ_PACKAGE_NAME);
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, TEZ_REQUEST_CODE);
        } else {
            AlertDialog.Builder vBuilder = new AlertDialog.Builder(GooglePayActivity.this)
                    .setCancelable(true)
                    .setIcon(R.drawable.ic_google_pay_mark_800_gray)
                    .setMessage("Google Pay not installed!!")
                    .setTitle("Google Pay not found!");
            vBuilder.create().show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TEZ_REQUEST_CODE) {

            SubscriptionTransactionModel vModel = new SubscriptionTransactionModel();

            vModel.setSubname(mPayLoad.getSubname());
            vModel.setDuration(mPayLoad.getDuration());
            vModel.setSubcost(mPayLoad.getSubcost());
            vModel.setSubcoupon(mPayLoad.getSubcoupon());

            vModel.setUid(mPayLoad.getCust_id());
            vModel.setMobile(mPayLoad.getMobile());
            vModel.setTotal_paid(mPayLoad.getAmount());
            vModel.setTransaction_id(mPayLoad.getOrder_id());

            vModel.setPayment_status(data.getStringExtra("Status"));
            vModel.setVerified("pending");

            if (data.getStringExtra("Status").equals("SUCCESS")) {

                mPAymentSuccess.setVisibility(View.VISIBLE);

            } else {

                mPaymentFailure.setVisibility(View.VISIBLE);
            }
            mFirebaseFirestore.collection("sub_transaction_data").document(vModel.getTransaction_id()).set(vModel);
        }
    }

    public void goBackHome(View view) {
        startActivity(new Intent(GooglePayActivity.this, MainActivity.class));
        finish();
    }
}

