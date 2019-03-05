package com.example.rapidfood.Activites;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Toast;

import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.example.rapidfood.Utils.PermissionUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

import static android.os.SystemClock.sleep;

public class LogoActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_KEY = 48127;
    private PreferenceManager mPreferenceManager;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseFirestore mFirebaseFirestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);
        mFirebaseAuth = FirebaseInstances.getFirebaseAuth();
        FirebaseInstances vFirebaseInstances=new FirebaseInstances();
        mFirebaseFirestore = vFirebaseInstances.getFirebaseFirestore();
        mPreferenceManager = new PreferenceManager(this);

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

    private void closeLogoActivity() {
        //create background thread
        //put it in sleep for 2seconds to show the splash screen to user
        Thread splashThread = new Thread(new Runnable() {
            @Override
            public void run() {
                sleep(2000);
                if (!mPreferenceManager.FirstLaunch()) {
                    launchMain();
                } else {

                    Intent myIntent = new Intent(LogoActivity.this, MainScreenActivity.class);
                    startActivity(myIntent);
                    finish();
                }
            }
        });
        splashThread.start();
    }

    private void launchMain() {
        mPreferenceManager.setFirstTimeLaunch(false);

        if (mFirebaseUser == null) {
            Intent myIntent = new Intent(LogoActivity.this, Authentication.class);
            startActivity(myIntent);
            finish();
        } else {
            DocumentReference vUserId = mFirebaseFirestore.collection("vendors").document("sunil_kumar");
            vUserId.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> pTask) {
                    if (pTask.isSuccessful()) {
                        DocumentSnapshot document = pTask.getResult();
                        if (document.exists()) {
                            Log.d("DATAGET","Id: "+document.get("firebase_id"));
                            String userId = Objects.requireNonNull(document.get("firebase_id")).toString();
                            if(userId.equals(mFirebaseUser.getUid())) {
                                startActivity(new Intent(LogoActivity.this, VendorActivity.class));
                                finish();
                            }
                            else{
                                startActivity(new Intent(LogoActivity.this, MainActivity.class));
                                finish();
                            }
                            Log.d("DATAGET", "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d("DATAGET", "No such document");
                        }
                    } else {
                        Log.d("DATAGET", "get failed with ", pTask.getException());
                    }
                }
            });


        }
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
