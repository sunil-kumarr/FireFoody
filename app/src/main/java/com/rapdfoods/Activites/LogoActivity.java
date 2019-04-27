package com.rapdfoods.Activites;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.ListenerRegistration;
import com.rapdfoods.R;
import com.rapdfoods.Utils.FirebaseInstances;
import com.rapdfoods.Utils.PermissionUtils;
import com.rapdfoods.Utils.UtilClass;
import com.rapdfoods.VendorActivities.DashboardActivity;
import com.rapdfoods.VendorActivities.DeliveryBoyActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class LogoActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_KEY = 48127;
    private PreferenceManager mPreferenceManager;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseFirestore mFirebaseFirestore;
    private ConstraintLayout mMAinContainter;
    private static final String TAG = "LogoActivity";
    ProgressDialog mProgressDialog;
    private CollectionReference mVendorCol,mBoyCol;
    private ListenerRegistration mVendorListner,mBotListener;
    private Snackbar mSnackbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);
        //Log.d(TAG, "onCreate: ");
        FirebaseInstances vFirebaseInstances = new FirebaseInstances();
        mFirebaseAuth = vFirebaseInstances.getFirebaseAuth();
        mFirebaseFirestore = vFirebaseInstances.getFirebaseFirestore();
        mPreferenceManager = new PreferenceManager(this);
        mMAinContainter = findViewById(R.id.main_layout_holder);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //  Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();

        MultiplePermissionsListener vListener = new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    if (UtilClass.isConnectedToNetwork(LogoActivity.this)) {
                        closeLogoActivity();
                    }
                    else{
                        if(!UtilClass.isConnectedToNetwork(LogoActivity.this)){
                            final Snackbar vSnackbar=Snackbar.make(mMAinContainter,"Internet connection unavailable",Snackbar.LENGTH_INDEFINITE);
                            vSnackbar.setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(UtilClass.isConnectedToNetwork(LogoActivity.this)){
                                        closeLogoActivity();
                                        vSnackbar.dismiss();
                                    }
                                    else{
                                        vSnackbar.show();
                                    }
                                }
                            }).show();
                        }     }
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        };
        SnackbarOnAnyDeniedMultiplePermissionsListener vSnackbar =
                SnackbarOnAnyDeniedMultiplePermissionsListener.Builder
                        .with(mMAinContainter, "All permissions are required!")
                        .withOpenSettingsButton("Open settings")
                        .build();
        CompositeMultiplePermissionsListener vCompositeMultiplePermissionsListener =
                new CompositeMultiplePermissionsListener(vListener, vSnackbar);

        Dexter.withActivity(LogoActivity.this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.CHANGE_NETWORK_STATE,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(vCompositeMultiplePermissionsListener).check();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

    }

    @Override
    protected void onStop() {
        super.onStop();
        //  Log.d(TAG, "onStop: ");
        mProgressDialog.dismiss();

    }

    private void closeLogoActivity() {
        //  Log.d(TAG, "closeLogoActivity: ");
        Handler myHandler = new Handler();
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
        }, 2000);

    }

    private void launchMain() {
        //  Log.d(TAG, "launchMain: ");
        mPreferenceManager.setFirstTimeLaunch(false);
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser != null) {
            mFirebaseFirestore.collection("users").document(mFirebaseUser.getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> pTask) {
                    if (pTask.isSuccessful()) {
                        identifyUserTypeMethod();
                    } else {
                        Intent myIntent = new Intent(LogoActivity.this, Authentication.class);
                        startActivity(myIntent);
                        finish();
                    }
                }
            });
        } else {
            Intent myIntent = new Intent(LogoActivity.this, Authentication.class);
            startActivity(myIntent);
            finish();
        }

    }

    private void identifyUserTypeMethod() {
        // Log.d(TAG, "identifyUserTypeMethod: ");
        mProgressDialog.setMessage("Validating...");
        mProgressDialog.show();

        CollectionReference vCollectionReference = mFirebaseFirestore.collection("vendors");
     vCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> pTask) {
                if (pTask.isSuccessful()) {
                    boolean user = false;
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(pTask.getResult())) {
                        String vendorId = document.getString("firebase_id");
                        assert vendorId != null;
                        if (mFirebaseUser.getUid().equals(vendorId)) {
                            user = true;
                            startActivity(new Intent(LogoActivity.this, DashboardActivity.class));
                            mProgressDialog.dismiss();
                            finish();
                        }
                    }
                    if (!user) {
                        mFirebaseFirestore.collection("delivery_boy")
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {

                                        boolean boy = false;
                                        if (e != null) {
                                            Log.d("DownloadData", "Listen failed.", e);
                                            return;
                                        }
                                        assert value != null;
                                        for (QueryDocumentSnapshot doc : value) {
                                            String vendorId = doc.getString("firebase_id");
                                            assert vendorId != null;
                                            if (mFirebaseUser.getUid().equals(vendorId)) {
                                                boy = true;
                                                mProgressDialog.dismiss();
                                                startActivity(new Intent(LogoActivity.this, DeliveryBoyActivity.class));
                                                finish();
                                            }
                                        }
                                        if(!boy){

                                            mProgressDialog.dismiss();
                                            startActivity(new Intent(LogoActivity.this, MainActivity.class));
                                            finish();
                                        }

                                    }
                                });

                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", pTask.getException());
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissionsG, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissionsG, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: ");
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
