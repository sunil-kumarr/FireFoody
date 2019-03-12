package com.example.rapidfood.Activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Authentication extends AppCompatActivity {

    private static final int RC_SIGN_IN = 532;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseFirestore mFirebaseFirestore;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        Button vButton=findViewById(R.id.loginBtn);
        FirebaseInstances vFirebaseInstances = new FirebaseInstances();
        mFirebaseAuth = vFirebaseInstances.getFirebaseAuth();
        mFirebaseFirestore = vFirebaseInstances.getFirebaseFirestore();
        vButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLoginAPI();
            }
        });
    }
    private void callLoginAPI(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {

                Toast.makeText(this, "Verification Done..", Toast.LENGTH_SHORT).show();
                mProgressDialog=new ProgressDialog(this);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setMessage("Validating...");
                identifyUserTypeMethod();
                mProgressDialog.show();

                // ...
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
    private void identifyUserTypeMethod() {
        mFirebaseUser=mFirebaseAuth.getCurrentUser();
        mFirebaseFirestore.collection("vendors")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        boolean user=false;
                        if (e != null) {
                            Log.d("DownloadData", "Listen failed.", e);
                            return;
                        }
                        assert value != null;
                        for (QueryDocumentSnapshot doc : value) {
                            String vendorId=doc.getString("firebase_id");
                            assert vendorId != null;
                            if(mFirebaseUser.getUid().equals(vendorId)){
                                user=true;
                                mProgressDialog.dismiss();
                                startActivity(new Intent(Authentication.this, VendorActivity.class));
                                finish();
                            }
                        }
                        if(!user) {
                            mProgressDialog.dismiss();
                            startActivity(new Intent(Authentication.this, MainActivity.class));
                            finish();
                        }

                    }
                });
    }
}
