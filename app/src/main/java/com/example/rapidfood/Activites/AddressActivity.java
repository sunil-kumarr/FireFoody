package com.example.rapidfood.Activites;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rapidfood.Models.UserAddressModal;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddressActivity extends AppCompatActivity {

    ImageView mSaveButton;
    TextInputEditText mAddName, mAddComplete, mAddInstruct;
    UserAddressModal mUserAddressModal;
    FirebaseInstances mFirebaseInstances;
    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore mFirebaseFirestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_address);
        mFirebaseInstances=new FirebaseInstances();
        mFirebaseAuth=mFirebaseInstances.getFirebaseAuth();
        mFirebaseFirestore=mFirebaseInstances.getFirebaseFirestore();


        mAddName = findViewById(R.id.address_name);
        mAddComplete = findViewById(R.id.address_complete);
        mAddInstruct = findViewById(R.id.address_delivery_instructions);
        mSaveButton = findViewById(R.id.save_address);
        mUserAddressModal = new UserAddressModal();
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getAddressData()) {

                    saveAddressToFirebase();

                }

            }
        });
    }

    private void saveAddressToFirebase() {
        AlertDialog vDialog=new AlertDialog.Builder(AddressActivity.this)
                .setTitle("Update Address")
                .setMessage("Are you sure to add or update address!")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mFirebaseAuth.getCurrentUser()!=null)
                        {
                            mFirebaseUser=mFirebaseAuth.getCurrentUser();
                            String uid=mFirebaseUser.getUid();
                            DocumentReference userData= mFirebaseFirestore.collection("users").document(uid);

                            userData.update("address_first", mUserAddressModal);
                            mFirebaseFirestore.collection("subscribed_user").document(uid)
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    if(documentSnapshot.exists()){
                                        mFirebaseFirestore.collection("subscribed_user")
                                                .document(uid).update("address_first",mUserAddressModal);

                                        Toast.makeText(AddressActivity.this, "Address Updated", Toast.LENGTH_SHORT).show();
                                            finish();

                                    }
                                }
                            });

                        }
                    }
                }).create();
        vDialog.show();
    }

    private boolean MinLengthString(View v,int len) {
        EditText localEditText = (EditText) v;
        if (localEditText.getText().toString().length()< len ) {
            localEditText.setError("Please enter valid address");
            return false;
        }
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseUser=mFirebaseAuth.getCurrentUser();
    }

    private boolean getAddressData() {

        if (mAddName.getText() != null && MinLengthString(mAddName,4)) {
            mUserAddressModal.setAddressname(mAddName.getText().toString());
        } else {
            mAddName.setError("Length greater then 4");
            return  false;
        }
        if (mAddComplete.getText() != null && MinLengthString(mAddComplete,15)) {
            mUserAddressModal.setAddresscomplete(Objects.requireNonNull(mAddComplete.getText()).toString());
        } else {
            mAddComplete.setError("Length greater than 15");
            return  false;
        }
        if (mAddInstruct.getText() != null)
            mUserAddressModal.setAddressinstructions(mAddInstruct.getText().toString());
        else {
            mUserAddressModal.setAddressinstructions("no instruction");
        }
        return true;
    }

}
