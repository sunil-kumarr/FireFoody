package com.firefoody.VendorActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.firefoody.Models.SubscriptionModel;
import com.firefoody.R;
import com.firefoody.Utils.FirebaseInstances;
import com.firefoody.Utils.ImageUtil;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Objects;

public class VendorCreateSubscription extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageView;
    private EditText mSubType, mSubPrice, mSubDuration, mSubDetail,mSubCoupon;
    private Button mCreateBtn;
    private FrameLayout mFrameLayout;
    private SubscriptionModel mSubscriptionModel;
    private FirebaseInstances mFirebaseInstances;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseFirestore mFirebaseFirestore;
    private ProgressDialog mProgressDialog;
    private ImageUtil mImageUtil;
    private String image;
    private Uri ImageUri;
    private boolean mImageSelected=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_create_subscription);

        Toolbar vToolbar = findViewById(R.id.toolbar_add_menu);
        setSupportActionBar(vToolbar);
        if (getSupportActionBar() != null) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_black_24dp);
        }
        mSubDuration = findViewById(R.id.sub_duration);
        mSubDetail = findViewById(R.id.sub_details);
        mSubPrice = findViewById(R.id.sub_price);
        mSubType = findViewById(R.id.sub_packtype);
        mCreateBtn = findViewById(R.id.sub_create_btn);
        mImageView=findViewById(R.id.sub_image);
        mSubCoupon=findViewById(R.id.sub_coupon);


        mFirebaseInstances = new FirebaseInstances();
        mFirebaseAuth = mFirebaseInstances.getFirebaseAuth();
        mFirebaseStorage = mFirebaseInstances.getFirebaseStorage();
        mFirebaseFirestore = mFirebaseInstances.getFirebaseFirestore();
        mImageUtil=new ImageUtil();
        mSubscriptionModel=new SubscriptionModel();
        mFrameLayout=findViewById(R.id.container_frame);
        mFrameLayout.setOnClickListener(this);
        mCreateBtn.setOnClickListener(this);


    }


    private boolean EmptyString(View v) {
        EditText localEditText = (EditText) v;
        if (localEditText.getText().toString().equals("") || localEditText.getText().toString().equals("null")) {
            localEditText.setError("Field cannot be Empty");
            return false;
        }
        return true;
    }

    private void createSubscription() {

        if (EmptyString(mSubDuration) && EmptyString(mSubType) && EmptyString(mSubPrice) && EmptyString(mSubDetail) && mImageSelected)
        {
            String duration = mSubDuration.getText().toString();
            String packType = mSubType.getText().toString();
            String price = mSubPrice.getText().toString();
            String detail = mSubDetail.getText().toString();
            String Coupon=mSubCoupon.getText().toString();
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMax(100);
            mProgressDialog.setMessage("Creating Subscription");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.show();
            mSubscriptionModel.setDetails(detail);
            mSubscriptionModel.setDuration(duration);
            mSubscriptionModel.setPrice(price);
            mSubscriptionModel.setType(packType);
            mSubscriptionModel.setCoupon(Coupon);
            UploadImageToFirebase(ImageUri, "subscriptions");
        }
    }

    private void addItemToFireStore(String CollectionName) {
        mFirebaseFirestore.collection(CollectionName)
                .document(mSubscriptionModel.getType())
                .set(mSubscriptionModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> pTask) {
                        finish();
                    }
                });
        mProgressDialog.dismiss();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_frame:
                AlertDialog vBuilder = new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setIcon(R.drawable.ic_photo_library_blue_24dp)
                        .setMessage("SELECT IMAGE FROM YOUR DEVICE.")
                        .setTitle("UPLOAD IMAGE")
                        .setPositiveButton("OPEN GALLERY", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mImageUtil.pickFromGallery(VendorCreateSubscription.this);
                            }
                        }).create();
                vBuilder.show();

                break;
            case R.id.sub_create_btn:
                AlertDialog vUpdateDialog = new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setIcon(R.drawable.ic_lunch)
                        .setMessage("ARE YOU SURE YOU WANT TO CREATE THIS SUBSCRIPTION??")
                        .setTitle("CREATE SUBSCRIPTION ")
                        .setPositiveButton("Yes,Sure.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                createSubscription();
                            }
                        }).create();
                vUpdateDialog.show();

                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 2973:
                    //data.getData returns the content URI for the selected Image
                    ImageUri = data.getData();
                    if (ImageUri != null)
                        image = mImageUtil.FilePathNameExtractor(ImageUri);
                    if (image != null) {
                        Picasso.get()
                                .load(ImageUri)
                                .fit()
                                .into(mImageView);
                        mImageSelected = true;
                    }

                    break;
            }
    }

    private void UploadImageToFirebase(Uri pImageUri, final String CollectionName) {
     //   Toast.makeText(this, "Upload called", Toast.LENGTH_SHORT).show();
        final StorageReference ref = mFirebaseStorage.getReference().child("subscription_image/" + image);
        ref.putFile(pImageUri).
                addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot pTaskSnapshot) {

                        double progress = (100.0 * pTaskSnapshot.getBytesTransferred()) / pTaskSnapshot.getTotalByteCount();

                        String s = new DecimalFormat("##").format(progress);
                        mProgressDialog.setProgressNumberFormat(s);
                        mProgressDialog.setProgress((int) progress);
                    }
                })
                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        // Forward any exceptions
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        return ref.getDownloadUrl();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(@NonNull Uri downloadUri) {
                        mSubscriptionModel.setImagesub(downloadUri.toString());
                        addItemToFireStore(CollectionName);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });
    }

}
