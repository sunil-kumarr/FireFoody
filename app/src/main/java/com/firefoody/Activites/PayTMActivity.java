package com.firefoody.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.firefoody.Models.ChecksumResponse;
import com.firefoody.Models.PaymentSubDataModel;
import com.firefoody.Models.SubscriptionTransactionModel;
import com.firefoody.Networking.ApiClient;
import com.firefoody.Networking.ApiService;
import com.firefoody.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PayTMActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PayTMActivity";
    private static ApiClient mApi;


    private ConstraintLayout mSuccesORDER;
    private ConstraintLayout mERRORORder;
    private Button mGoBackHome;
    private LinearLayout mProcessing;
    private MaterialCardView mPAytmScreenLogin;
    private TextInputEditText mPaytmNumber;
    private Button mProceedPAytmBtn;
    private TextView mSkipPaytm;
    private PaymentSubDataModel mPayLoad;
    private FirebaseFirestore mFireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_tm);


        mFireStore=FirebaseFirestore.getInstance();
        mERRORORder=findViewById(R.id.payment_failure);
        mSuccesORDER=findViewById(R.id.payment_success);
        mGoBackHome=findViewById(R.id.goback_home);
        mProcessing=findViewById(R.id.payment_processing);
//        mPaytmNumber=findViewById(R.id.paytm_edt_number);
//        mProceedPAytmBtn=findViewById(R.id.paytm_login_button);
//        mPAytmScreenLogin=findViewById(R.id.paytm_login_page);
//        mSkipPaytm=findViewById(R.id.paytm_skip_login);
//        mProcessing.setVisibility(View.GONE);
//
//        mSkipPaytm.setOnClickListener(this);
//        mProceedPAytmBtn.setOnClickListener(this);
        mGoBackHome.setOnClickListener(this);



        if(getIntent().getExtras()!=null) {
            mPayLoad = (PaymentSubDataModel) getIntent().getExtras().getSerializable("payload");
            callFirebase(false);
        }
        else{
            mERRORORder.setVisibility(View.VISIBLE);
        }
    }






    void getChecksum( PaymentSubDataModel paymentSubDataModel,String Mid,boolean mNumberAvail) {


            Map<String, String> paramMap = preparePayTmParams(paymentSubDataModel, Mid, mNumberAvail);

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
                    Log.d(TAG, "onResponse: " + response.body().CHECKSUMHASH);
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

    public Map<String, String> preparePayTmParams( PaymentSubDataModel paymentSubDataModel,String Mid,boolean mNumberAvail) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID="+ paymentSubDataModel.getOrder_id());
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("CUST_ID", "CUSTOMER_" + paymentSubDataModel.getCust_id());
        paramMap.put("INDUSTRY_TYPE_ID","Retail");
        paramMap.put("MID", Mid);
        paramMap.put("WEBSITE", "DEFAULT");
        paramMap.put("ORDER_ID", paymentSubDataModel.getOrder_id());
        paramMap.put("TXN_AMOUNT", paymentSubDataModel.getAmount());

//        Toast.makeText(this, ""+mNumberAvail, Toast.LENGTH_SHORT).show();
//        if(mNumberAvail){
//            Toast.makeText(this, "num", Toast.LENGTH_SHORT).show();
//                String num=String.valueOf(mPaytmNumber.getText());
//                paramMap.put("MOBILE_NO", num);
//        }
            return paramMap;

    }

    private boolean EmptyString(View v) {
        EditText localEditText = (EditText) v;
        if (localEditText.getText().toString().equals("") || localEditText.getText().toString().equals("null")) {
            localEditText.setError("Field cannot be Empty");
            return false;
        }
        return true;
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

                       updateStatusUI(false);
                        Toast.makeText(PayTMActivity.this, "UI ERROR", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onTransactionResponse(Bundle inResponse) {

                        updateStatusUI(true);

                    }

                    @Override
                    public void networkNotAvailable() { // If network is not

                        updateStatusUI(false);
                        Toast.makeText(PayTMActivity.this, "Network problem", Toast.LENGTH_SHORT).show();

                        // available, then this
                        // method gets called.
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {

                        updateStatusUI(false);
                        Toast.makeText(PayTMActivity.this, "Error", Toast.LENGTH_SHORT).show();

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

                        Toast.makeText(PayTMActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        updateStatusUI(false);

                    }

                    @Override
                    public void onBackPressedCancelTransaction() {
                        updateStatusUI(false);
                        Toast.makeText(PayTMActivity.this,
                                "Transaction cancelled", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {

                        updateStatusUI(false);
                        Toast.makeText(PayTMActivity.this, "Transaction cancelled", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void updateStatusUI(boolean Status){

        if(Status){

            SubscriptionTransactionModel vModel = new SubscriptionTransactionModel();

            vModel.setSubname(mPayLoad.getSubname());
            vModel.setDuration(mPayLoad.getDuration());
            vModel.setSubcost(mPayLoad.getSubcost());
            vModel.setSubcoupon(mPayLoad.getSubcoupon());

            vModel.setUid(mPayLoad.getCust_id());
            vModel.setMobile(mPayLoad.getMobile());
            vModel.setTotal_paid(mPayLoad.getAmount());
            vModel.setTransaction_id(mPayLoad.getOrder_id());

            vModel.setPayment_status("SUCCESS");
            vModel.setVerified("pending");
            mFireStore.collection("sub_transaction_data").document(vModel.getTransaction_id()).set(vModel);
            mSuccesORDER.setVisibility(View.VISIBLE);
            mProcessing.setVisibility(View.GONE);
            mERRORORder.setVisibility(View.GONE);

        }
        else{
            SubscriptionTransactionModel vModel = new SubscriptionTransactionModel();

            vModel.setSubname(mPayLoad.getSubname());
            vModel.setDuration(mPayLoad.getDuration());
            vModel.setSubcost(mPayLoad.getSubcost());
            vModel.setSubcoupon(mPayLoad.getSubcoupon());

            vModel.setUid(mPayLoad.getCust_id());
            vModel.setMobile(mPayLoad.getMobile());
            vModel.setTotal_paid(mPayLoad.getAmount());
            vModel.setTransaction_id(mPayLoad.getOrder_id());

            vModel.setPayment_status("FAILURE");
            vModel.setVerified("pending");
            mFireStore.collection("sub_transaction_data").document(vModel.getTransaction_id()).set(vModel);
            mSuccesORDER.setVisibility(View.GONE);
            mProcessing.setVisibility(View.GONE);
            mERRORORder.setVisibility(View.VISIBLE);
        }


    }
    public ApiClient getApi() {
        if (mApi == null) {
            mApi = ApiService.getClient().create(ApiClient.class);
        }
        return mApi;
    }

    private void callFirebase(boolean numAvail){
        mProcessing.setVisibility(View.VISIBLE);
//        mPAytmScreenLogin.setVisibility(View.GONE);
        mFireStore.collection("company_data")
                .document("paytm")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String mid=documentSnapshot.getString("mid");
                        getChecksum(mPayLoad,mid,numAvail);
                    }
                });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

//            case R.id.paytm_login_button:
//                if(EmptyString(mPaytmNumber) ) {
//                    String numB=String.valueOf(mPaytmNumber.getText());
//                    if(numB.length()==10){
//                        callFirebase(true);
//                    }
//                    else{
//                        Toast.makeText(this, "Enter Valid Number", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                else{
//                    Toast.makeText(this, "Enter Valid Number", Toast.LENGTH_SHORT).show();
//                }
//                break;
//
//            case R.id.paytm_skip_login:
//                callFirebase(false);
//                break;
            case R.id.goback_home:

                goBackHome(v);
                break;


        }

    }
    public void goBackHome(View view) {
        startActivity(new Intent(PayTMActivity.this, MainActivity.class));
        finish();
    }
}
