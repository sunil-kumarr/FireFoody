package com.example.rapidfood.Activites;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.rapidfood.R;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class GooglePayActivity extends AppCompatActivity {
    private static final int TEZ_REQUEST_CODE = 123;

    private static final String GOOGLE_TEZ_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_payment_bottom_sheet);

        RelativeLayout payButton = findViewById(R.id.googlepay_button);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri =
                        new Uri.Builder()
                                .scheme("upi")
                                .authority("pay")
                                .appendQueryParameter("pa", "test@axisbank")
                                .appendQueryParameter("pn", "Test Merchant")
                                .appendQueryParameter("mc", "1234")
                                .appendQueryParameter("tr", "123456789")
                                .appendQueryParameter("tn", "test transaction note")
                                .appendQueryParameter("am", "10.01")
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
                    AlertDialog.Builder vBuilder=new AlertDialog.Builder(GooglePayActivity.this)
                            .setCancelable(true)
                            .setIcon(R.drawable.ic_google_pay_mark_800_gray)
                            .setMessage("Google Pay not installed!!")
                            .setTitle("Google Pay not found!");
                    vBuilder.create().show();
                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TEZ_REQUEST_CODE) {
            // Process based on the data in response.
            Log.d("result", data.getStringExtra("Status"));
        }
    }
}
