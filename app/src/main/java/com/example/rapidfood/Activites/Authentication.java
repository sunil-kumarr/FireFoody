package com.example.rapidfood.Activites;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.example.rapidfood.Utils.IdentityUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class Authentication extends AppCompatActivity implements View.OnClickListener {


    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout layout3;
    private StepView stepView;
    private Button sendCodeButton;
    private Button verifyCodeButton;
    private Button signOutButton;
    private Button button3;
    private TextView resendCodeButton;
    private EditText phoneNum;
    private PinView verifyCodeET;
    private TextView phonenumberText;
    private AlertDialog dialog_verifying;
    private AlertDialog profile_dialog;

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private String phoneNumber;
    private static String uniqueIdentifier = null;
    private int currentStep = 0;
    private static final String UNIQUE_ID = "UNIQUE_ID";
    private static final long ONE_HOUR_MILLI = 60 * 60 * 1000;
    private static final String TAG = "Authentication";
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;
    public static boolean user = false;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private IdentityUser mIdentityUser;
    private FirebaseInstances mFirebaseInstances;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseAuth mFirebaseAuth;
    private ProgressDialog mProgressDialog;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        FirebaseInstances vFirebaseInstances = new FirebaseInstances();
        mAuth = vFirebaseInstances.getFirebaseAuth();

        //Get all 3 layouts
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.layout3);

        // get all Buttons
        sendCodeButton = findViewById(R.id.submit1);
        verifyCodeButton = findViewById(R.id.submit2);
        button3 = findViewById(R.id.submit3);
        resendCodeButton = findViewById(R.id.resendCodeBtn);
        phoneNum = findViewById(R.id.phonenumber);
        verifyCodeET = findViewById(R.id.pinView);
        phonenumberText = findViewById(R.id.phonenumberText);
        stepView = findViewById(R.id.step_view);

        stepView.setStepsNumber(3);
        stepView.go(0, true);

        layout1.setVisibility(View.VISIBLE);
        mIdentityUser = new IdentityUser();
        resendCodeButton.setOnClickListener(this);
        sendCodeButton.setOnClickListener(this);
        verifyCodeButton.setOnClickListener(this);
        button3.setOnClickListener(this);
        mFirebaseInstances=new FirebaseInstances();
        mFirebaseFirestore = mFirebaseInstances.getFirebaseFirestore();
        mFirebaseAuth=mFirebaseInstances.getFirebaseAuth();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                mVerificationInProgress = false;
                Toast.makeText(Authentication.this, "onVerification Completed", Toast.LENGTH_SHORT).show();
                showDialogNext();
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                mVerificationInProgress = false;
                Log.d(TAG, "" + e.getMessage());
                Toast.makeText(Authentication.this, "verification Failed:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    verifyCodeET.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // Save verification ID and resending token so we can use them later
                Toast.makeText(Authentication.this, "OnCode Sent", Toast.LENGTH_SHORT).show();
                mVerificationId = verificationId;
                mResendToken = token;
                // ...
            }
        };
    }

    private void showDialog(String title) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(title);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();
    }

    private void goToProfile() {

        if (currentStep < stepView.getStepCount() - 1) {
            currentStep++;
            stepView.go(currentStep, true);
        } else {
            stepView.done(true);
        }
        showDialog("Loading user...");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.dismiss();
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
                                    mFirebaseUser=mFirebaseAuth.getCurrentUser();
                                    if (mFirebaseUser.getUid().equals(vendorId)) {
                                        user = true;
                                        startActivity(new Intent(Authentication.this, VendorActivity.class));
                                        finish();
                                    }
                                }
                                if (!user) {
                                    startActivity(new Intent(Authentication.this, MainActivity.class));
                                    finish();
                                }

                            }
                        });
            }
        }, 3000);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(phoneNum.getText().toString());
        }
        if (currentUser != null) {
            startActivity(new Intent(this, VendorActivity.class));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    private void showDialogNext() {
        showDialog("Verifying number...");
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            mProgressDialog.dismiss();
                            if (currentStep < stepView.getStepCount() - 1) {
                                currentStep++;
                                stepView.go(currentStep, true);
                            } else {
                                stepView.done(true);
                            }
                            layout1.setVisibility(View.GONE);
                            layout2.setVisibility(View.GONE);
                            layout3.setVisibility(View.VISIBLE);
                            // ...
                        } else {
                            mProgressDialog.dismiss();
                            Toast.makeText(Authentication.this, "Something wrong", Toast.LENGTH_SHORT).show();
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                verifyCodeET.setError("Invalid code.");
                            }
                        }
                    }
                });
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = phoneNum.getText().toString();
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 10) {
            phoneNum.setError("Invalid phone number.");
            return false;
        }
        return true;
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        Toast.makeText(this, "Start Phone verification", Toast.LENGTH_SHORT).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]
        mVerificationInProgress = true;
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        Toast.makeText(this, "Request Code Resent", Toast.LENGTH_SHORT).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit1:
                if (validatePhoneNumber()) {
                    String sPhoneNumber = "+91" + phoneNum.getText().toString();
                    if (currentStep < stepView.getStepCount() - 1) {
                        currentStep++;
                        stepView.go(currentStep, true);
                    } else {
                        stepView.done(true);
                    }
                    layout1.setVisibility(View.GONE);
                    layout2.setVisibility(View.VISIBLE);
                    phoneNumber = sPhoneNumber;
                    phonenumberText.setText(phoneNumber);
                    startPhoneNumberVerification(sPhoneNumber);
                }
                break;
            case R.id.submit2:
                String verificationCode = Objects.requireNonNull(verifyCodeET.getText()).toString();
                if (verificationCode.isEmpty()) {
                    Toast.makeText(Authentication.this, "Enter verification code", Toast.LENGTH_SHORT).show();
                } else {
                    showDialogNext();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                    signInWithPhoneAuthCredential(credential);

                }
                break;
            case R.id.resendCodeBtn:
                resendCodeButton.setTextColor(getResources().getColor(R.color.grey_600));
                resendCodeButton.setEnabled(false);
                resendVerificationCode(phoneNum.getText().toString(), mResendToken);
                break;
            case R.id.submit3:
                goToProfile();
                break;

        }
    }

}
