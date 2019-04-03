package com.example.rapidfood.Vendor_files;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rapidfood.Models.VendorBreakFastItem;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.example.rapidfood.Utils.ImageUtil;
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

public class VendorAddBreakfast extends AppCompatActivity implements View.OnClickListener {


    static final Integer BREAKFAST = 1;
    static final Integer LUNCH = 2;
    static final Integer DINNER = 3;
    private EditText mItemname;
    private EditText mItemDesc;
    private EditText mS1,mS2,mS3,mS4;
    private ImageView mItemImage;
    private Button mSaveBtn;
    private Spinner mCategory;
    private String image;
    private Uri imageUri;
    private ProgressDialog mProgressDialog;
    private VendorBreakFastItem mItem;
    private ProgressBar mProgressBar;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseInstances mFirebaseInstances;
    private FirebaseStorage mFirebaseStorage;
    private boolean mImageSelected = false;
    private ImageUtil mImageUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_create_breakfast_item);
        Toolbar vToolbar = findViewById(R.id.toolbar_add_menu);
        setSupportActionBar(vToolbar);
        if (getSupportActionBar() != null) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_black_24dp);
        }
        mFirebaseInstances = new FirebaseInstances();
        mFirebaseFirestore = mFirebaseInstances.getFirebaseFirestore();
        mFirebaseStorage = mFirebaseInstances.getFirebaseStorage();
        mItem = new VendorBreakFastItem();
        mImageUtil=new ImageUtil();

        mItemname = findViewById(R.id.item_name);
        mItemDesc = findViewById(R.id.item_desc);
        mItemImage = findViewById(R.id.item_image);
        mS1=findViewById(R.id.item_s1);
        mS2=findViewById(R.id.item_s2);
        mS3=findViewById(R.id.item_s3);
        mS4=findViewById(R.id.item_s4);
        mSaveBtn = findViewById(R.id.submitMenu);
        mCategory = findViewById(R.id.item_category);
        String[] cate = {"Breakfast", "Lunch", "Dinner"};
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, cate);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        mCategory.setAdapter(dataAdapter);

        findViewById(R.id.image_container).setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 2973:
                    //data.getData returns the content URI for the selected Image
                    imageUri = data.getData();
                    if (imageUri != null)
                        image = mImageUtil.FilePathNameExtractor(imageUri);
                    if (image != null) {
                        Picasso.get()
                                .load(imageUri)
                                .centerCrop()
                                .resize(150,230)
                                .placeholder(R.drawable.foodplaceholder)
                                .into(mItemImage);
                        mImageSelected = true;
                    }

                    break;
            }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_container:
                mImageUtil.pickFromGallery(VendorAddBreakfast.this);
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

    private void addMenuData() {
        String name = mItemname.getText().toString();
        String desc = mItemDesc.getText().toString();
        String s1=mS1.getText().toString();
        String s2=mS2.getText().toString();
        String s3=mS3.getText().toString();
        String s4=mS4.getText().toString();
        if (EmptyString(mItemname) && EmptyString(mS1) && EmptyString(mS2)&&EmptyString(mS3)&&EmptyString(mS4)
                && EmptyString(mItemDesc) && mImageSelected) {

            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMax(100);
            mProgressDialog.setTitle("Uploading....");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.show();
            mItem.setItemdescription(desc);
            mItem.setItemname(name);
           mItem.setS1(s1);
           mItem.setS2(s2);
           mItem.setS3(s3);
           mItem.setS4(s4);
            mItem.setItemCategory(mCategory.getSelectedItem().toString());
            UploadImageToFirebase(imageUri,"menus");
        }
    }

    private void addItemToFireStore(String CollectionName) {
        mFirebaseFirestore.collection(CollectionName)
                .document(mItemname.getText().toString())
                .set(mItem)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> pTask) {
                        finish();
                    }
                });
        mProgressDialog.dismiss();
    }

    private void UploadImageToFirebase(Uri pImageUri, final String CollectionName) {
        Toast.makeText(this, "Upload called", Toast.LENGTH_SHORT).show();
        final StorageReference ref = mFirebaseStorage.getReference().child("images/" + image);
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
                        mItem.setItemImageid(downloadUri.toString());
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
