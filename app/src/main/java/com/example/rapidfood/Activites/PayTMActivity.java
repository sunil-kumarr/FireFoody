//package com.example.rapidfood.Activites;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.rapidfood.BuildConfig;
//import com.example.rapidfood.Models.ChecksumResponse;
//import com.example.rapidfood.Networking.ApiClient;
//import com.example.rapidfood.Networking.ApiService;
//import com.example.rapidfood.R;
//import com.paytm.pgsdk.PaytmOrder;
//import com.paytm.pgsdk.PaytmPGService;
//import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//
//public class PayTMActivity extends AppCompatActivity {
//
//    private static ApiClient mApi;
//    public ApiClient getApi() {
//        if (mApi == null) {
//            mApi = ApiService.getClient().create(ApiClient.class);
//        }
//        return mApi;
//    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_pay_tm);
//    }
//
//    void getChecksum( ) {
//
//
//        Map<String, String> paramMap = preparePayTmParams();
//
//        getApi().getCheckSum(paramMap).enqueue(new Callback<ChecksumResponse>() {
//            @Override
//            public void onResponse(Call<ChecksumResponse> call, Response<ChecksumResponse> response) {
//                if (!response.isSuccessful()) {
//                    Timber.e("Network call failed");
//                    handleUnknownError();
//                    showOrderStatus(false);
//                    return;
//                }
//
//                Timber.d("Checksum Received: " + response.body().checksum);
//
//                // Add the checksum to existing params list and send them to PayTM
//                paramMap.put("CHECKSUMHASH", response.body().checksum);
//                placeOrder(paramMap);
//            }
//
//            @Override
//            public void onFailure(Call<ChecksumResponse> call, Throwable t) {
//                handleError(t);
//                showOrderStatus(false);
//            }
//        });
//    }
//
//    public Map<String, String> preparePayTmParams(PrepareOrderResponse response) {
//        Map<String, String> paramMap = new HashMap<String, String>();
//        paramMap.put("CALLBACK_URL", String.format(BuildConfig.PAYTM_CALLBACK_URL, response.orderGatewayId));
//        paramMap.put("CHANNEL_ID", appConfig.getChannel());
//        paramMap.put("CUST_ID", "CUSTOMER_" + user.id);
//        paramMap.put("INDUSTRY_TYPE_ID", appConfig.getIndustryType());
//        paramMap.put("MID", appConfig.getMerchantId());
//        paramMap.put("WEBSITE", appConfig.getWebsite());
//        paramMap.put("ORDER_ID", response.orderGatewayId);
//        paramMap.put("TXN_AMOUNT", response.amount);
//        return paramMap;
//    }
//
//
//    /**
//     * STEP 3: Redirecting to PayTM gateway with necessary params along with checksum
//     * This will redirect to PayTM gateway and gives us the PayTM transaction response
//     */
//    public void placeOrder(Map<String, String> params) {
//
//        // choosing between PayTM staging and production
//        PaytmPGService pgService = BuildConfig.IS_PATM_STAGIN ? PaytmPGService.getStagingService() : PaytmPGService.getProductionService();
//
//        PaytmOrder Order = new PaytmOrder(params);
//
//        pgService.initialize(Order, null);
//
//        pgService.startPaymentTransaction(this, true, true,
//                new PaytmPaymentTransactionCallback() {
//                    @Override
//                    public void someUIErrorOccurred(String inErrorMessage) {
//
//                        finish();
//                        // Some UI Error Occurred in Payment Gateway Activity.
//                        // // This may be due to initialization of views in
//                        // Payment Gateway Activity or may be due to //
//                        // initialization of webview. // Error Message details
//                        // the error occurred.
//                    }
//
//                    @Override
//                    public void onTransactionResponse(Bundle inResponse) {
//
//                        String orderId = inResponse.getString("ORDERID");
//
//                    }
//
//                    @Override
//                    public void networkNotAvailable() { // If network is not
//
//                        finish();
//                        // available, then this
//                        // method gets called.
//                    }
//
//                    @Override
//                    public void clientAuthenticationFailed(String inErrorMessage) {
//
//                        finish();
//                        // This method gets called if client authentication
//                        // failed. // Failure may be due to following reasons //
//                        // 1. Server error or downtime. // 2. Server unable to
//                        // generate checksum or checksum response is not in
//                        // proper format. // 3. Server failed to authenticate
//                        // that client. That is value of payt_STATUS is 2. //
//                        // Error Message describes the reason for failure.
//                    }
//
//                    @Override
//                    public void onErrorLoadingWebPage(int iniErrorCode,
//                                                      String inErrorMessage, String inFailingUrl) {
//
//                        finish();
//                    }
//
//                    @Override
//                    public void onBackPressedCancelTransaction() {
//                        Toast.makeText(PayTMActivity.this, "Back pressed. Transaction cancelled", Toast.LENGTH_LONG).show();
//                        finish();
//                    }
//
//                    @Override
//                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
//
//                        finish();
//                    }
//                });
//    }
//
//}
