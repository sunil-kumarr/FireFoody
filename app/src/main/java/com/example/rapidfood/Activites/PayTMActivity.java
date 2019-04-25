package com.example.rapidfood.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.rapidfood.BuildConfig;
import com.example.rapidfood.Models.ChecksumResponse;
import com.example.rapidfood.Models.PaymentSubDataModel;
import com.example.rapidfood.Networking.ApiClient;
import com.example.rapidfood.Networking.ApiService;
import com.example.rapidfood.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PayTMActivity extends AppCompatActivity {

    private static final String TAG = "PayTMActivity";
    private static ApiClient mApi;


    private ConstraintLayout mSuccesORDER;
    private ConstraintLayout mERRORORder;
    private Button mGoBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_tm);


        mERRORORder=findViewById(R.id.payment_failure);
        mSuccesORDER=findViewById(R.id.payment_success);
        mGoBackHome=findViewById(R.id.goback_home);

        mGoBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PayTMActivity.this, MainActivity.class));
                finish();
            }
        });

        if(getIntent().getExtras()!=null){
            PaymentSubDataModel model=(PaymentSubDataModel)getIntent().getExtras().getSerializable("payload");
            FirebaseFirestore.getInstance().collection("company_data")
                    .document("paytm")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String mid=documentSnapshot.getString("mid");
                            Toast.makeText(PayTMActivity.this, ""+model.getAmount(), Toast.LENGTH_SHORT).show();
                            //
                            getChecksum(model,mid);
                        }
                    });
        }
        else{
            mERRORORder.setVisibility(View.VISIBLE);
        }
    }

    void getChecksum( PaymentSubDataModel paymentSubDataModel,String Mid) {


        Map<String, String> paramMap = preparePayTmParams(paymentSubDataModel,Mid);

        getApi().getCheckSum(paramMap).enqueue(new Callback<ChecksumResponse>() {
            @Override
            public void onResponse(Call<ChecksumResponse> call, Response<ChecksumResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(PayTMActivity.this, "Network  Failed", Toast.LENGTH_SHORT).show();
                    mERRORORder.setVisibility(View.VISIBLE);
                    return;
                }

                assert response.body() != null;

                paramMap.put("CHECKSUMHASH", response.body().CHECKSUMHASH);
                Log.d(TAG, "onResponse: "+response.body().CHECKSUMHASH);
               placeOrder(paramMap);
            }

            @Override
            public void onFailure(Call<ChecksumResponse> call, Throwable t) {
//                showOrderStatus(false);
                mERRORORder.setVisibility(View.VISIBLE);
                Toast.makeText(PayTMActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Map<String, String> preparePayTmParams( PaymentSubDataModel paymentSubDataModel,String Mid) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID="+ paymentSubDataModel.getOrder_id());
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("CUST_ID", "CUSTOMER_" + paymentSubDataModel.getCust_id());
        paramMap.put("INDUSTRY_TYPE_ID","Retail");
        paramMap.put("MID", "QsZUBo16768499332071");
        paramMap.put("WEBSITE", "DEFAULT");
        paramMap.put("ORDER_ID", paymentSubDataModel.getOrder_id());
        paramMap.put("TXN_AMOUNT", "1.0");
        return paramMap;
    }


    public void placeOrder(Map<String, String> params) {

        // choosing between PayTM staging and production
        PaytmPGService pgService = PaytmPGService.getProductionService();

        PaytmOrder Order = new PaytmOrder((HashMap<String, String>) params);

        pgService.initialize(Order, null);

        pgService.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {

                        mERRORORder.setVisibility(View.VISIBLE);
                        Toast.makeText(PayTMActivity.this, "UI ERROR", Toast.LENGTH_SHORT).show();
                        finish();
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                    }

                    @Override
                    public void onTransactionResponse(Bundle inResponse) {

//                        mSuccesORDER.setVisibility(View.VISIBLE);
                        String orderId = inResponse.getString("ORDERID");
                        Toast.makeText(PayTMActivity.this, ""+orderId, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void networkNotAvailable() { // If network is not

                        mERRORORder.setVisibility(View.VISIBLE);
                        Toast.makeText(PayTMActivity.this, "newtork", Toast.LENGTH_SHORT).show();
                        finish();
                        // available, then this
                        // method gets called.
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {

                        mERRORORder.setVisibility(View.VISIBLE);
                        Toast.makeText(PayTMActivity.this, "erroeeee", Toast.LENGTH_SHORT).show();
                        finish();
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {

                        mERRORORder.setVisibility(View.VISIBLE);
                        finish();
                    }

                    @Override
                    public void onBackPressedCancelTransaction() {
                        mERRORORder.setVisibility(View.VISIBLE);
                        Toast.makeText(PayTMActivity.this,
                                "Back pressed. Transaction cancelled", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {

                        mERRORORder.setVisibility(View.VISIBLE);
                        Toast.makeText(PayTMActivity.this, "cancel", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    public ApiClient getApi() {
        if (mApi == null) {
            mApi = ApiService.getClient().create(ApiClient.class);
        }
        return mApi;
    }

}
