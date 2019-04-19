package com.example.rapidfood.Activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rapidfood.R;
import com.example.rapidfood.User_files.MainActivity;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.example.rapidfood.Utils.UtilClass;
import com.example.rapidfood.Vendor_files.DashboardActivity;
import com.example.rapidfood.Vendor_files.TermConActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Authentication extends AppCompatActivity {

    private static final int RC_SIGN_IN = 532;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private TextView mTerms;
    private FirebaseFirestore mFirebaseFirestore;
    private ConstraintLayout mMainConatainer;
    private ProgressDialog mProgressDialog;
    private Snackbar mSnackbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Button vButton = findViewById(R.id.loginBtn);
        mMainConatainer = findViewById(R.id.main_layout_holder);
        FirebaseInstances vFirebaseInstances = new FirebaseInstances();
        mFirebaseAuth = vFirebaseInstances.getFirebaseAuth();
        mFirebaseFirestore = vFirebaseInstances.getFirebaseFirestore();
        mTerms = findViewById(R.id.goToTerms);
        mTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UtilClass.isConnectedToNetwork(Authentication.this)) {
                    startActivity(new Intent(Authentication.this, TermConActivity.class));
                } else {

                    final Snackbar vSnackbar = Snackbar.make(mMainConatainer, "Internet connection unavailable", Snackbar.LENGTH_INDEFINITE);
                    vSnackbar.setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (UtilClass.isConnectedToNetwork(Authentication.this)) {
                                startActivity(new Intent(Authentication.this, TermConActivity.class));
                                vSnackbar.dismiss();
                            }
                        }
                    }).show();
                }
            }
        });
        vButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UtilClass.isConnectedToNetwork(Authentication.this)) {
                    callLoginAPI();

                } else {
                    final Snackbar vSnackbar = Snackbar.make(mMainConatainer, "Internet connection unavailable", Snackbar.LENGTH_INDEFINITE);
                    vSnackbar.setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (UtilClass.isConnectedToNetwork(Authentication.this)) {
                                callLoginAPI();
                                vSnackbar.dismiss();
                            }
                        }
                    }).show();

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!UtilClass.isConnectedToNetwork(this)) {
            final Snackbar vSnackbar = Snackbar.make(mMainConatainer, "Internet connection unavailable", Snackbar.LENGTH_INDEFINITE);
            vSnackbar.setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (UtilClass.isConnectedToNetwork(Authentication.this)) {
                        vSnackbar.dismiss();
                    }
                }
            }).show();
        }
    }

    private void callLoginAPI() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.applogo_small)
                        .setTheme(R.style.AppTheme)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                mFirebaseFirestore.collection("users").document(mFirebaseAuth.getCurrentUser().getUid())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot pDocumentSnapshot) {
                        if (!pDocumentSnapshot.exists()) {
                            Map<String, Object> mp = new HashMap<>();
                            mp.put("balance", "0");
                            mp.put("subscribed", false);
                            mp.put("firebase_id", mFirebaseAuth.getCurrentUser().getUid());
                            mp.put("mobile", Objects.requireNonNull(mFirebaseAuth.getCurrentUser().getPhoneNumber()));
                            mFirebaseFirestore.collection("users").document(mFirebaseAuth.getCurrentUser().getUid())
                                    .set(mp);
                        }
                    }
                });

                //Toast.makeText(this, "Verification Done..", Toast.LENGTH_SHORT).show();
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setMessage("Validating...");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setCancelable(false);
                identifyUserTypeMethod();
                mProgressDialog.show();

                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                if (response == null) {
                    Snackbar.make(mMainConatainer, "Login Failed ", Snackbar.LENGTH_SHORT);
                }
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    private void identifyUserTypeMethod() {
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mFirebaseFirestore.collection("vendors")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        boolean user = false;
                        if (e != null) {
                            Log.d("DownloadData", "Listen failed.", e);
                            return;
                        }
                        assert value != null;
                        for (QueryDocumentSnapshot doc : value) {
                            String vendorId = doc.getString("firebase_id");
                            assert vendorId != null;
                            if (mFirebaseUser.getUid().equals(vendorId)) {
                                user = true;
                                mProgressDialog.dismiss();
                                startActivity(new Intent(Authentication.this, DashboardActivity.class));
                                finish();
                            }
                        }
                        if (!user) {
                            mProgressDialog.dismiss();
                            startActivity(new Intent(Authentication.this, MainActivity.class));
                            finish();
                        }

                    }
                });
    }
}
