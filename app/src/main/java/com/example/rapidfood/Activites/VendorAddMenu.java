package com.example.rapidfood.Activites;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rapidfood.Models.VendorMenuItem;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class VendorAddMenu extends AppCompatActivity implements View.OnClickListener {

    static final Integer GALLERY_REQUEST_CODE = 2973;
    private EditText mItemname;
    private EditText mItemDesc;
    private EditText mQuantity;
    private EditText mPriority;
    private ImageView mItemImage;
    private Button mSaveBtn;
    private String image;
    private Uri imageUri;
    private VendorMenuItem mItem;
    private ProgressBar mProgressBar;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseInstances mFirebaseInstances;
    private FirebaseStorage mFirebaseStorage;
    private boolean mImageSelected=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_add_menu);
        Toolbar vToolbar = findViewById(R.id.toolbar_add_menu);
        setSupportActionBar(vToolbar);
        if (getSupportActionBar() != null) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_white_black_24dp);
        }
        mFirebaseInstances=new FirebaseInstances();
        mFirebaseFirestore= mFirebaseInstances.getFirebaseFirestore();
        mFirebaseStorage=mFirebaseInstances.getFirebaseStorage();
        mItem=new VendorMenuItem();
        mProgressBar=findViewById(R.id.progressBarUpload);
        mItemname = findViewById(R.id.item_name);
        mItemDesc = findViewById(R.id.item_desc);
        mItemImage = findViewById(R.id.item_image);
        mPriority = findViewById(R.id.item_priority);
        mQuantity = findViewById(R.id.item_quantity);
        mSaveBtn = findViewById(R.id.submitMenu);
        mItemImage.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);
    }

    private void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 2973:
                    //data.getData returns the content URI for the selected Image
                    imageUri = data.getData();
                    if(imageUri!=null)
                     image=FilePathNameExtractor(imageUri);
                    if(image!=null){
                        mItemImage.setImageURI(imageUri);
                        mImageSelected=true;
                    }
                   
                    break;
            }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_image:
                    pickFromGallery();
                    break;
            case R.id.submitMenu:
                Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
                addMenuData();
                break;
        }
    }
    private boolean EmptyString(View v) {
        EditText localEditText = (EditText) v;
        if (localEditText.getText().toString().equals("") || localEditText.getText().toString().equals("null")) {
            localEditText.setError("Field cannot be Empty");
            return false;
        }
        return true;
    }
    private  void addMenuData(){


        String name=mItemname.getText().toString();
        String desc=mItemDesc.getText().toString();
        String quant=mQuantity.getText().toString();
        String pri=mPriority.getText().toString();
        if(EmptyString(mItemname)&&EmptyString(mQuantity)&&EmptyString(mPriority)&&EmptyString(mItemDesc) && mImageSelected){

            mItem.setItemdescription(desc);
            mItem.setItemname(name);
            mItem.setItempriority(pri);
            mItem.setItemquantitiy(quant);
            UploadImageToFirebase(imageUri);
        }
    }

    private void addItemToFireStore() {
        mFirebaseFirestore.collection("menus")
                .document(mItemname.getText().toString())
                .set(mItem)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> pTask) {
                        finish();
                    }
                });
    }

    private void UploadImageToFirebase(Uri pImageUri) {
        Toast.makeText(this, "Upload called", Toast.LENGTH_SHORT).show();
        final StorageReference ref = mFirebaseStorage.getReference().child("images/" + image);
        ref.putFile(pImageUri).
                addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot pTaskSnapshot) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        double progress = (100.0 * pTaskSnapshot.getBytesTransferred()) / pTaskSnapshot.getTotalByteCount();
                        mProgressBar.setProgress((int) progress);
                        String s = new DecimalFormat("##").format(progress);

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

                        mItem.setItemImageid(downloadUri.toString());
                        mProgressBar.setVisibility(View.GONE);
                        showImage(downloadUri.toString());
                        addItemToFireStore();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });
    }
    public void showImage(String url) {
        if (url != null && !url.isEmpty()) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get()
                    .load(url)
                    .resize(width,(width*9)/10)
                    .centerCrop()
                    .into(mItemImage);

        }
    }
    private String FilePathNameExtractor(Uri pImageUri) {
        String path = pImageUri.getLastPathSegment();
        String filename = path.substring(path.lastIndexOf("/") + 1);
        Log.d("DealsPath", filename);
        return filename;

    }
}
