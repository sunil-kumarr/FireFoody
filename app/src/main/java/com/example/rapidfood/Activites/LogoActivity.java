package com.example.rapidfood.Activites;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.widget.Toast;

import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.example.rapidfood.Utils.PermissionUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LogoActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_KEY = 48127;
    private PreferenceManager mPreferenceManager;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseFirestore mFirebaseFirestore;

    private static final String TAG = "LogoActivity";
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);
        FirebaseInstances vFirebaseInstances = new FirebaseInstances();

        mFirebaseAuth = vFirebaseInstances.getFirebaseAuth();
        mFirebaseFirestore = vFirebaseInstances.getFirebaseFirestore();
        mPreferenceManager = new PreferenceManager(this);
        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] permissionsG = {
                Manifest.permission.READ_SMS};

        if (PermissionUtils.shouldAskForPermission(LogoActivity.this, permissionsG[0])) {
            PermissionUtils.requestActivityPermissions(LogoActivity.this, permissionsG, REQUEST_PERMISSION_KEY);
        } else {
            closeLogoActivity();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mProgressDialog.dismiss();
    }

    private void closeLogoActivity() {


        Handler myHandler=new Handler();
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mPreferenceManager.FirstLaunch()) {
                    launchMain();
                } else {
                    Intent myIntent = new Intent(LogoActivity.this, MainScreenActivity.class);
                    startActivity(myIntent);
                    finish();
                }
            }
        },2000);

    }

    private void launchMain() {
        mPreferenceManager.setFirstTimeLaunch(false);

        if (mFirebaseUser == null) {
            Intent myIntent = new Intent(LogoActivity.this, Authentication.class);
            startActivity(myIntent);
            finish();
        } else {
            identifyUserTypeMethod();

        }
    }

    private void identifyUserTypeMethod() {
        mProgressDialog.setMessage("Validating...");
        mProgressDialog.show();
        mFirebaseFirestore.collection("vendors")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        boolean user=false;

                        assert value != null;
                        for (QueryDocumentSnapshot doc : value) {
                            String vendorId=doc.getString("firebase_id");
                            assert vendorId != null;
                            if(mFirebaseUser.getUid().equals(vendorId)){
                               user=true;
                                startActivity(new Intent(LogoActivity.this, VendorActivity.class));
                                finish();
                            }
                        }
                        if(!user) {
                            startActivity(new Intent(LogoActivity.this, MainActivity.class));
                            finish();
                        }

                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissionsG, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissionsG, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_KEY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    for (String x : permissionsG)
                        PermissionUtils.markedPermissionAsAsked(this, x);
                } else {
                    Toast.makeText(LogoActivity.this, "You must accept permissions.", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

}
