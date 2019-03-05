package com.example.rapidfood.Activites;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import com.shuhart.stepview.StepView;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
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
    private static final long ONE_HOUR_MILLI = 60*60*1000;
    private static final String TAG = "FirebasePhoneNumAuth";
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        mAuth = FirebaseInstances.getFirebaseAuth();

        //Get all 3 layouts
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.layout3);

        // get all Buttons
        sendCodeButton = findViewById(R.id.submit1);
        verifyCodeButton = findViewById(R.id.submit2);
        button3 = findViewById(R.id.submit3);
        resendCodeButton=findViewById(R.id.resendCodeBtn);
        phoneNum = findViewById(R.id.phonenumber);
        verifyCodeET = findViewById(R.id.pinView);
        phonenumberText = findViewById(R.id.phonenumberText);
        stepView = findViewById(R.id.step_view);

        stepView.setStepsNumber(3);
        stepView.go(0, true);

        layout1.setVisibility(View.VISIBLE);

        resendCodeButton.setOnClickListener(this);
        sendCodeButton.setOnClickListener(this);
        verifyCodeButton.setOnClickListener(this);
        button3.setOnClickListener(this);

        mCallbacks =new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                mVerificationInProgress = false;
                showDialogNext();
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }
            @Override
            public void onVerificationFailed(FirebaseException e) {
                mVerificationInProgress = false;
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
                mVerificationId = verificationId;
                mResendToken = token;
                // ...
            }
        };
    }

    private void goToProfile() {

                if (currentStep < stepView.getStepCount() - 1) {
                    currentStep++;
                    stepView.go(currentStep, true);
                } else {
                    stepView.done(true);
                }
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout= inflater.inflate(R.layout.profile_create_dialog,null);
                AlertDialog.Builder show = new AlertDialog.Builder(Authentication.this);
                show.setView(alertLayout);
                show.setCancelable(false);
                profile_dialog = show.create();
                profile_dialog.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        profile_dialog.dismiss();
                        startActivity(new Intent(Authentication.this, VendorActivity.class));
                        finish();
                    }
                },3000);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(phoneNum.getText().toString());
        }
        if(currentUser!=null){
            startActivity(new Intent(this,VendorActivity.class));
        }
    } @Override
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
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.processing_dialog, null);
        AlertDialog.Builder show = new AlertDialog.Builder(Authentication.this);
        show.setView(alertLayout);
        show.setCancelable(false);
        dialog_verifying = show.create();
        dialog_verifying.show();
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            dialog_verifying.dismiss();
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
                            dialog_verifying.dismiss();
                            Toast.makeText(Authentication.this,"Something wrong", Toast.LENGTH_SHORT).show();
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
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length()<10) {
            phoneNum.setError("Invalid phone number.");
            return false;
        }
        return true;
    }
    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
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
                    String sPhoneNumber="+91"+phoneNum.getText().toString();
                    if (currentStep < stepView.getStepCount() - 1) {
                        currentStep++;
                        stepView.go(currentStep, true);
                    } else {
                        stepView.done(true);
                    }
                    layout1.setVisibility(View.GONE);
                    layout2.setVisibility(View.VISIBLE);
                    phoneNumber=sPhoneNumber;
                    phonenumberText.setText(phoneNumber);
                    startPhoneNumberVerification(sPhoneNumber);
                }
                break;
            case R.id.submit2:
                String verificationCode = Objects.requireNonNull(verifyCodeET.getText()).toString();
                if(verificationCode.isEmpty()){
                    Toast.makeText(Authentication.this,"Enter verification code", Toast.LENGTH_SHORT).show();
                }else {
                    showDialogNext();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                    signInWithPhoneAuthCredential(credential);

                }
                break;
            case R.id.resendCodeBtn:
                resendCodeButton.setTextColor(getResources().getColor(R.color.grey_600));
                resendVerificationCode(phoneNum.getText().toString(), mResendToken);
                break;
            case  R.id.submit3:
                goToProfile();
                break;

        }
    }

}
