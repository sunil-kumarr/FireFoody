package com.example.rapidfood.Activites;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rapidfood.Models.CheckoutOrderDataModel;
import com.example.rapidfood.Models.CheckoutPlaceOrderModel;
import com.example.rapidfood.Models.SubscribedUserModel;
import com.example.rapidfood.Models.UserModel;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.example.rapidfood.Utils.GenerateUUIDClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import static android.view.View.GONE;

public class CheckoutActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CheckoutActivity";

    private static final String GOOGLE_TEZ_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    private static final int TEZ_REQUEST_CODE = 526;
    private static CheckoutPlaceOrderModel sCheckoutPlaceOrderModel;
    private ListView mOrderItems;
    private TextView mDefaultMenu;
    private LinearLayout mBtnRapidFood, mBtnGooglePay;
    private RadioButton mRRadio, mGRadio;
    private Button mPlaceOrderBtn;
    private String tr;
    private GenerateUUIDClass mUUIDGeneration;
    private FirebaseInstances mFirebaseInstances;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseAuth mFirebaseAuth;
    private LinearLayout mAddressContainer;
    private TextView mAddAddressButton;
    private TextView mUserAddress;
    private String mPackOrderName;
    private ImageButton mDeleteAddress;
    private TextView mUserCurrentBal;
    private TextView mCheckoutTotalCost;
    private RelativeLayout mGooglePayCheckout;
    private int OrderCost;
    private List<String> mItems;
    private TextView mPackOrder;
    private String DeliveryAddress;
    private String PackageName;
    private ConstraintLayout mOrderSuccess;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_checkout_layout);
        mFirebaseInstances = new FirebaseInstances();
        mFirebaseAuth = mFirebaseInstances.getFirebaseAuth();
        mFirebaseFirestore = mFirebaseInstances.getFirebaseFirestore();


        mOrderSuccess=findViewById(R.id.order_succes_show);
        mPackOrder = findViewById(R.id.checkout_order_package);
        mCheckoutTotalCost = findViewById(R.id.checkout_order_total_cost);
        mDeleteAddress = findViewById(R.id.delete_address_btn);
        mUserCurrentBal = findViewById(R.id.user_current_balance);
        mAddAddressButton = findViewById(R.id.add_address_profile_button);
        mAddressContainer = findViewById(R.id.address_container);
        mUserAddress = findViewById(R.id.profile_address_first);
        mRRadio = findViewById(R.id.radio_btn_rapidfood);
        mGRadio = findViewById(R.id.radio_btn_google);
        mBtnRapidFood = findViewById(R.id.rapid_btn_container);
        mBtnGooglePay = findViewById(R.id.google_btn_container);
        mPlaceOrderBtn = findViewById(R.id.checkout_order_button);
        mDeleteAddress.setOnClickListener(this);
        mPlaceOrderBtn.setOnClickListener(this);
        mBtnGooglePay.setOnClickListener(this);
        mBtnRapidFood.setOnClickListener(this);
        mAddAddressButton.setOnClickListener(this);
        mUUIDGeneration = new GenerateUUIDClass();

        CheckoutOrderDataModel vModel = (CheckoutOrderDataModel) getIntent().getExtras().getSerializable("orderdata");
        mOrderItems = findViewById(R.id.checkout_dish_list);
        mDefaultMenu = findViewById(R.id.checkout_default_menu);
        mItems = new ArrayList<>();
        if (vModel != null) {
            mItems = vModel.getOrderDishlist();
            mPackOrderName = vModel.getPackageName();
        }
        if (mItems.size() > 0) {
            Toast.makeText(this, "" + mItems.size(), Toast.LENGTH_SHORT).show();
            ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mItems);
            mOrderItems.setAdapter(mAdapter);
        }

        getPackDetails();

        fetchUserData();


    }

    private void getPackDetails() {
        mFirebaseFirestore.collection("today_menu").document(mPackOrderName).get()
                .addOnSuccessListener(pDocumentSnapshot -> {
                    if (pDocumentSnapshot.exists()) {
                        if (mItems.size() == 0) {
                            mOrderItems.setVisibility(GONE);
                            mDefaultMenu.setVisibility(View.VISIBLE);
                            mDefaultMenu.setText(pDocumentSnapshot.getString("description"));
                        }
                        mPackOrder.setText(String.format("Package: %s", pDocumentSnapshot.getString("name")));
                        mCheckoutTotalCost.setText(pDocumentSnapshot.getString("price"));
                    }
                });
    }

    private void fetchUserData() {
        final UserModel[] currecntUser = {new UserModel()};
        if (mFirebaseAuth.getCurrentUser() != null) {
            String uid = mFirebaseAuth.getCurrentUser().getUid();
            DocumentReference vUSerData = mFirebaseFirestore.collection("users").document(uid);
            DocumentReference vSubData = mFirebaseFirestore.collection("subscribed_user").document(mFirebaseAuth.getCurrentUser().getUid());
            vSubData.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> pTask) {
                    if (pTask.isSuccessful() && pTask.getResult().exists()) {
                        DocumentSnapshot vSnapshot=pTask.getResult();
                        SubscribedUserModel vModel=vSnapshot.toObject(SubscribedUserModel.class);
                        String bal=String.valueOf(vModel.getBalance());
                        mUserCurrentBal.setText("₹"+bal);
                    } else {
                        mUserCurrentBal.setTextColor(getResources().getColor(R.color.red_500));
                        mUserCurrentBal.setText("Not Subscribed");
                    }
                }
            });

            vUSerData.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot pDocumentSnapshot) {
                    if (pDocumentSnapshot.exists()) {
                        UserModel vUserModel = pDocumentSnapshot.toObject(UserModel.class);
                        if (vUserModel != null) {
                            if (vUserModel.getAddress_first() != null) {
                                mAddAddressButton.setVisibility(View.GONE);
                                mAddAddressButton.setEnabled(true);
                                mUserAddress.setText(vUserModel.getAddress_first().getAddresscomplete());
                                mAddressContainer.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            });
        }

    }

    private void deleteUserAddress() {
        if (mFirebaseAuth.getCurrentUser() != null) {
            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
            mFirebaseFirestore.collection("users").document(mFirebaseUser.getUid()).update("address_first", null)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void pVoid) {
                            Toast.makeText(CheckoutActivity.this, "Address removed", Toast.LENGTH_SHORT).show();
                            mAddAddressButton.setVisibility(View.VISIBLE);
                            mAddAddressButton.setEnabled(true);
                            mUserAddress.setText(" ");
                            mAddressContainer.setVisibility(View.GONE);

                        }
                    });

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_address_profile_button:
                startActivity(new Intent(this, AddressActivity.class));
                v.setEnabled(false);
                break;
            case R.id.delete_address_btn:
                deleteUserAddress();
                break;
            case R.id.rapid_btn_container:
                if (mGRadio.isChecked()) {
                    mGRadio.setChecked(false);
                }
                mRRadio.setChecked(true);
                break;
            case R.id.google_btn_container:
                if (mRRadio.isChecked()) {
                    mRRadio.setChecked(false);
                }
                mGRadio.setChecked(true);
                break;
            case R.id.checkout_order_button:
               // Toast.makeText(this, "order", Toast.LENGTH_SHORT).show();
                placeMyOrder();
                break;
        }
    }

    private void placeMyOrder() {
        sCheckoutPlaceOrderModel=new CheckoutPlaceOrderModel();
        OrderCost = Integer.parseInt(mCheckoutTotalCost.getText().toString());
        PackageName = mPackOrder.getText().toString();
        sCheckoutPlaceOrderModel.setPackageprice(String.valueOf(OrderCost));
        sCheckoutPlaceOrderModel.setPackageordered(PackageName);
        if(mItems.size()==0){
            sCheckoutPlaceOrderModel.setCustom(false);
            sCheckoutPlaceOrderModel.setSelecteditems(null);
        }
        else{
            sCheckoutPlaceOrderModel.setCustom(true);
            sCheckoutPlaceOrderModel.setSelecteditems(mItems);
        }
        if (!mAddAddressButton.isEnabled() || !(mUserAddress.getText()==null)) {
            DeliveryAddress = mUserAddress.getText().toString();
            sCheckoutPlaceOrderModel.setDeliveryaddress(DeliveryAddress);
            tr = mUUIDGeneration.generateUniqueKeyUsingUUID();
            sCheckoutPlaceOrderModel.setTrans_id(tr);
            sCheckoutPlaceOrderModel.setUid(Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid());
            sCheckoutPlaceOrderModel.setMobile(mFirebaseAuth.getCurrentUser().getPhoneNumber());
            if (mRRadio.isChecked()) {
                sCheckoutPlaceOrderModel.setPaymentmethod("RapidFood Wallet");
                verifySubscription();
            } else if (mGRadio.isChecked()) {
                sCheckoutPlaceOrderModel.setPaymentmethod("Google pay");
                payWithGooglePay();
            } else {
                Toast.makeText(this, "Select payment method", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Add Address", Toast.LENGTH_SHORT).show();
        }

    }

    private void verifySubscription() {
        if (mFirebaseAuth.getCurrentUser() != null) {

            mFirebaseFirestore.collection("subscribed_user").document(mFirebaseAuth.getCurrentUser().getUid())
                    .get().addOnCompleteListener(pTask -> {
                if (pTask.isSuccessful()) {
                    DocumentSnapshot vDocumentSnapshot = pTask.getResult();
                    assert vDocumentSnapshot != null;
                    if (vDocumentSnapshot.exists()) {
                        String curBal = vDocumentSnapshot.getString("balance");
                        int curBalance = Integer.parseInt(curBal);
                        if (curBalance > OrderCost) {
                        Map<String ,Object> mp=new HashMap<>();
                        int orderprice=Integer.parseInt(sCheckoutPlaceOrderModel.getPackageprice());
                        curBalance=curBalance-orderprice;
                        String upprice=String.valueOf(curBalance);
                        mp.put("balance",upprice);
                        mFirebaseFirestore.collection("subscribed_user").document(mFirebaseAuth.getCurrentUser().getUid())
                                .update(mp).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> pTask) {
                            if(pTask.isSuccessful()){
                                mOrderSuccess.setVisibility(View.VISIBLE);
                                sCheckoutPlaceOrderModel.setOrderStatus("pending");
                                sCheckoutPlaceOrderModel.setPaymentstatus("SUCCESS");
                              //  Toast.makeText(CheckoutActivity.this, "Order Placed", Toast.LENGTH_SHORT).show();
                                mFirebaseFirestore.collection("delivery_orders").document(tr).set(sCheckoutPlaceOrderModel)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void pVoid) {
                                                //Toast.makeText(CheckoutActivity.this, "Confirmed order", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                mFirebaseFirestore.collection("users").document(mFirebaseAuth.getCurrentUser().getUid())
                                        .collection("my_orders").document(tr).set(sCheckoutPlaceOrderModel)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void pVoid) {
                                               // Toast.makeText(CheckoutActivity.this, "Order Added", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });
                            }
                            else{
                                Toast.makeText(CheckoutActivity.this, "Order Not Placed", Toast.LENGTH_SHORT).show();
                            }
                            }
                        });
                        }
                        else{
                            Toast.makeText(CheckoutActivity.this, "Insufficient Balance", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CheckoutActivity.this, "Choose other payment method", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(CheckoutActivity.this, "Choose other payment method", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
        }
    }

    private void payWithGooglePay() {
        Log.d(TAG, tr);
        Uri uri = new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", "sunindus.sk@okaxis")
                .appendQueryParameter("pn", "sunil kumar")
                .appendQueryParameter("mc", "1234")
                .appendQueryParameter("tr", tr)
                .appendQueryParameter("tn", "rapidfood subscription")
                .appendQueryParameter("am", sCheckoutPlaceOrderModel.getPackageprice())
                .appendQueryParameter("cu", "INR")
                .appendQueryParameter("url", "https://test.merchant.website")
                .build();
        PackageManager packageManager = getApplication().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(GOOGLE_TEZ_PACKAGE_NAME);
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, TEZ_REQUEST_CODE);
        } else {
            AlertDialog.Builder vBuilder =
                    new AlertDialog.Builder(CheckoutActivity.this)
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
           // Toast.makeText(this, "" + tr, Toast.LENGTH_SHORT).show();
            //Log.d(TAG, "onActivityResult: " + tr);
            if (data.getStringExtra("Status").equals("SUCCESS")) {
                mOrderSuccess.setVisibility(View.VISIBLE);
               // Toast.makeText(this, "Result :" + data.getStringExtra("Status"), Toast.LENGTH_SHORT).show();
                sCheckoutPlaceOrderModel.setPaymentstatus("SUCCESS");
                sCheckoutPlaceOrderModel.setOrderStatus("pending");
             //   Toast.makeText(CheckoutActivity.this, "Order Placed", Toast.LENGTH_SHORT).show();
                mFirebaseFirestore.collection("delivery_orders").document(tr).set(sCheckoutPlaceOrderModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void pVoid) {
                               // Toast.makeText(CheckoutActivity.this, "Confirmed order", Toast.LENGTH_SHORT).show();
                            }
                        });
                mFirebaseFirestore.collection("users").document(mFirebaseAuth.getCurrentUser().getUid())
                        .collection("my_orders").document(tr).set(sCheckoutPlaceOrderModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void pVoid) {
                               // Toast.makeText(CheckoutActivity.this, "Order Added", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
            }
            {
                Toast.makeText(this, "Error Occurred !!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchUserData();
        mAddAddressButton.setEnabled(true);
    }
}
