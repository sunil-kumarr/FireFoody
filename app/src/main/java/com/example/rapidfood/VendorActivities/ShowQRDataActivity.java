package com.example.rapidfood.VendorActivities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rapidfood.Models.CheckoutPlaceOrderModel;
import com.example.rapidfood.Models.QRorderModel;
import com.example.rapidfood.Models.SubscribedUserModel;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.EncryptionHelper;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ShowQRDataActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mUserMobile;
    private TextView mPAckName;
    private TextView mPackPrice;
    private TextView mTransId;
    private TextView mTransDate;

    private TextView mPayStatus;
    private TextView mORderStatus;

    private Button mORderConfirm;

    private FirebaseInstances mFirebaseInstances;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseAuth mFirebaseAuth;
    private QRorderModel vModel;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_show_qr_data);

        mUserMobile = findViewById(R.id.qr_order_user_mobile);
        mPackPrice = findViewById(R.id.qr_order_pack_price);
        mPAckName = findViewById(R.id.qr_order_pack_name);

        mPayStatus = findViewById(R.id.qr_order_pay_status);
        mTransId = findViewById(R.id.qr_order_pack_trans_id);
        mTransDate = findViewById(R.id.qr_order_pack_date);
        mORderStatus = findViewById(R.id.qr_order_status);

        mORderConfirm = findViewById(R.id.order_btn_confirm);


        mORderConfirm.setOnClickListener(this);

        mFirebaseInstances = new FirebaseInstances();
        mFirebaseFirestore = mFirebaseInstances.getFirebaseFirestore();
        mFirebaseAuth = mFirebaseInstances.getFirebaseAuth();

        if (getIntent().getSerializableExtra("qr_data") != null) {
            String text = EncryptionHelper.getInstance().getDecryptionString(getIntent().getStringExtra("qr_data"));
            Gson vGson = new Gson();
            vModel = vGson.fromJson(text, QRorderModel.class);

            mFirebaseFirestore.collection("generated_qr_code")
                    .document(vModel.getQr_id()).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot pDocumentSnapshot) {
                            mTransDate.setText(pDocumentSnapshot.toObject(QRorderModel.class).getTimestamp().toString());
                            mPAckName.setText(vModel.getPack_name());
                            vModel.setTimestamp(pDocumentSnapshot.toObject(QRorderModel.class).getTimestamp());
                            mPackPrice.setText(vModel.getPack_price());
                            mUserMobile.setText(vModel.getUser_mobile());
                            mTransId.setText(vModel.getQr_id());
                            mPayStatus.setText(vModel.getPayment_status());
                            mORderStatus.setText(vModel.getOrder_Status());
                        }
                    });

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_btn_confirm:
                AlertDialog vDialog = new AlertDialog.Builder(this)
                        .setTitle("Place order")
                        .setMessage("Are you sure that you want to place order?")
                        .setIcon(R.drawable.ic_check_circlce_button)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                confirmOrderMethod();
                            }
                        }).create();
                vDialog.show();
                break;
            default:
                Toast.makeText(this, "Unknown option", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmOrderMethod() {
        mFirebaseFirestore.collection("generated_qr_code")
                .document(vModel.getQr_id())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> pTask) {
                        if (pTask.isSuccessful()) {
                            DocumentSnapshot vDocumentSnapshot = pTask.getResult();
                            if (vDocumentSnapshot != null) {
                                if (!Objects.requireNonNull(vDocumentSnapshot.toObject(QRorderModel.class)).isUsed_qr()) {
                                    mFirebaseFirestore.collection("subscribed_user").document(vModel.getUser_UID())
                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> pTask) {
                                            if (pTask.isSuccessful()) {
                                                DocumentSnapshot vDocumentSnapshot = pTask.getResult();
                                                if (vDocumentSnapshot.exists()) {
                                                    SubscribedUserModel vUserModel = vDocumentSnapshot.toObject(SubscribedUserModel.class);
                                                    int pack_price = Integer.parseInt(vModel.getPack_price());
                                                    assert vUserModel != null;
                                                    int user_bal = Integer.parseInt(vUserModel.getBalance());
                                                    if (user_bal >= pack_price) {
                                                        int bal = user_bal - pack_price;
                                                        Map<String, Object> vMap = new HashMap<>();
                                                        if (bal < user_bal) {
                                                            vMap.put("balance", String.valueOf(bal));
                                                            mFirebaseFirestore.collection("subscribed_user").document(vModel.getUser_UID())
                                                                    .update(vMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> pTask) {
                                                                    if (pTask.isSuccessful()) {
                                                                        findViewById(R.id.qr_order_details_Card).setVisibility(View.GONE);
                                                                        findViewById(R.id.show_order_Success).setVisibility(View.VISIBLE);
                                                                        CheckoutPlaceOrderModel vOrderModel = new CheckoutPlaceOrderModel();
                                                                        vOrderModel.setPackageordered(vModel.getPack_name());
                                                                        vOrderModel.setPackageprice(vModel.getPack_price());
                                                                        vOrderModel.setPaymentmethod("wallet + QR");
                                                                        vOrderModel.setPaymentstatus("SUCCESS");
                                                                        vOrderModel.setMobile(vModel.getUser_mobile());
                                                                        vOrderModel.setUid(vModel.getUser_UID());
                                                                        vOrderModel.setTrans_id(vModel.getQr_id());
                                                                        vOrderModel.setOrderStatus("pending");
                                                                        vOrderModel.setCustom(false);
                                                                        vOrderModel.setDeliveryaddress("nil");
                                                                        vOrderModel.setDeliverystatus("pending");
                                                                        vOrderModel.setSelecteditems(null);
                                                                        mFirebaseFirestore.collection("delivery_orders")
                                                                                .document(vOrderModel.getTrans_id()).set(vOrderModel)
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void pVoid) {
                                                                                        Map<String, Object> qr_Code_map = new HashMap<>();
                                                                                        qr_Code_map.put("used_qr", true);
                                                                                        qr_Code_map.put("payment_status","SUCCESS");
                                                                                        mFirebaseFirestore.collection("generated_qr_code")
                                                                                                .document(vModel.getQr_id())
                                                                                                .update(qr_Code_map);
                                                                                        finish();
                                                                                        //Toast.makeText(CheckoutActivity.this, "Confirmed order", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                });
                                                                    } else {
                                                                        Snackbar.make(findViewById(R.id.main_layout_holder), "Failed", Snackbar.LENGTH_LONG).show();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    } else {
                                                        Snackbar.make(findViewById(R.id.main_layout_holder), "Wallet balance low!!", Snackbar.LENGTH_LONG).show();
                                                    }
                                                }
                                            } else {
                                                Snackbar.make(findViewById(R.id.main_layout_holder), "User Not Subscribed", Snackbar.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                                else{
                                    Snackbar.make(findViewById(R.id.main_layout_holder), "QR code used", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        } else {
                            Snackbar.make(findViewById(R.id.main_layout_holder), "QR code Invalid", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
