package com.example.rapidfood.VendorActivities;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rapidfood.Models.PackageModel;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.example.rapidfood.Utils.ImageUtil;
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

public class VendorCreatePackage extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageView;
    private EditText mPackName,mDesc,mItemCount,mPrice;
    private Button mCreateBtn;
    private FrameLayout mFrameLayout;
    private FirebaseInstances mFirebaseInstances;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseFirestore mFirebaseFirestore;
    private ProgressDialog mProgressDialog;
    private ImageUtil mImageUtil;
    private String image;
    private Uri ImageUri;
    private PackageModel mPackageModel;
    private CheckBox mBreakfast;
    private boolean mImageSelected=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_create_package);
        
        Toolbar vToolbar = findViewById(R.id.toolbar_add_menu);
        setSupportActionBar(vToolbar);
        if (getSupportActionBar() != null) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_black_24dp);
        }

        mBreakfast=findViewById(R.id.isBreakfastPackage);
        mCreateBtn=findViewById(R.id.sub_create_btn);
        mImageView=findViewById(R.id.sub_image);
        mPackName=findViewById(R.id.pack_name);
        mFrameLayout=findViewById(R.id.container_frame);
        mDesc=findViewById(R.id.pack_details);
        mItemCount=findViewById(R.id.pack_item_Count);
        mPrice=findViewById(R.id.pack_price);
        
        mFirebaseInstances = new FirebaseInstances();
        mFirebaseAuth = mFirebaseInstances.getFirebaseAuth();
        mFirebaseStorage = mFirebaseInstances.getFirebaseStorage();
        mFirebaseFirestore = mFirebaseInstances.getFirebaseFirestore();
        mImageUtil=new ImageUtil();
        mPackageModel=new PackageModel();
        
      
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

    private void createPackage() {
        if (mImageSelected&& EmptyString(mPackName) &&EmptyString(mPrice)&& EmptyString(mItemCount)&& EmptyString(mDesc))
        {
            if(mBreakfast.isChecked()){
                mPackageModel.setBreakfast(true);
            }
            else{
                mPackageModel.setBreakfast(false);
            }
            mPackageModel.setName(mPackName.getText().toString());
            mPackageModel.setItem_count(mItemCount.getText().toString());
            mPackageModel.setDescription(mDesc.getText().toString());
            mPackageModel.setPrice(mPrice.getText().toString());

            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMax(100);
            mProgressDialog.setTitle("Uploading....");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.show();
            UploadImageToFirebase(ImageUri, "packages");
        }
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
                                mImageUtil.pickFromGallery(VendorCreatePackage.this);
                            }
                        }).create();
                vBuilder.show();

                break;
            case R.id.sub_create_btn:
                AlertDialog vUpdateDialog = new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setIcon(R.drawable.ic_shopping_basket_black_24dp)
                        .setMessage("ARE YOU SURE YOU WANT TO CREATE THIS PACKAGE??")
                        .setTitle("CREATE PACKAGE ")
                        .setPositiveButton("Yes,Sure.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                createPackage();
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
        Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show();
        final StorageReference ref = mFirebaseStorage.getReference().child("package_image/" + image);
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
                        String imageId=downloadUri.toString();
                        addItemToFireStore(CollectionName,imageId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });
    }
    private void addItemToFireStore(String CollectionName,String imageId) {
        mPackageModel.setImage(imageId);
        mFirebaseFirestore.collection(CollectionName)
                .document(mPackageModel.getName())
                .set(mPackageModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> pTask) {
                        finish();
                    }
                });
        mProgressDialog.dismiss();
    }
}
