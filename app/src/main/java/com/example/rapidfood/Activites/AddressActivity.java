package com.example.rapidfood.Activites;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rapidfood.Models.UserAddressModal;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
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
                getAddressData();

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
                                    userData.update("address_first", mUserAddressModal).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void pVoid) {
                                            Toast.makeText(AddressActivity.this, "Address updated successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            }
                        }).create();
                vDialog.show();


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseUser=mFirebaseAuth.getCurrentUser();
    }

    private void getAddressData() {
        if (mAddName.getText() != null) {
            mUserAddressModal.setAddressname(mAddName.getText().toString());
        } else {
            mAddName.setError("required");
        }
        if (mAddComplete.getText() != null) {
            mUserAddressModal.setAddresscomplete(Objects.requireNonNull(mAddComplete.getText()).toString());
        } else {
            mAddComplete.setError("required");
        }
        if (mAddInstruct.getText() != null)
            mUserAddressModal.setAddressinstructions(mAddInstruct.getText().toString());
        else {
            mUserAddressModal.setAddressinstructions("no instruction");
        }
    }

}
