package com.firefoody.VendorActivities;

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
import android.widget.ListView;

import com.firefoody.Adapters.AddDishPackHelpAdapter;
import com.firefoody.Models.PackageModel;
import com.firefoody.Models.VendorDishModel;
import com.firefoody.R;
import com.firefoody.Utils.FirebaseInstances;
import com.firefoody.Utils.ImageUtil;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class VendorAddDish extends AppCompatActivity implements View.OnClickListener {
    private ImageView mImageView;
    private EditText mName, mDesc;                   // mPrice;
    private Button mCreateBtn;
    private ListView mListView;
    private FrameLayout mFrameLayout;
    private MaterialCardView mMaterialCardView;


    private FirebaseInstances mFirebaseInstances;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseFirestore mFirebaseFirestore;


    private ProgressDialog mProgressDialog;
    private ImageUtil mImageUtil;
    private String image;
    private Uri ImageUri;
    private VendorDishModel mVendorDishModel;
    private boolean mImageSelected = false;
    private AddDishPackHelpAdapter mAddDishPackHelpAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_add_dish);


        Toolbar vToolbar = findViewById(R.id.toolbar_add_menu);
        setSupportActionBar(vToolbar);
        if (getSupportActionBar() != null) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_black_24dp);
        }
        mCreateBtn = findViewById(R.id.item_create_btn);
        mImageView = findViewById(R.id.item_image);
        mMaterialCardView = findViewById(R.id.image_container);
//        mListView = findViewById(R.id.pack_type_list);
        mName = findViewById(R.id.item_name);
        mDesc = findViewById(R.id.item_desc);
        mFrameLayout = findViewById(R.id.frame_layout);
//        mPrice = findViewById(R.id.item_price);

        mFirebaseInstances = new FirebaseInstances();
        mFirebaseAuth = mFirebaseInstances.getFirebaseAuth();
        mFirebaseStorage = mFirebaseInstances.getFirebaseStorage();
        mFirebaseFirestore = mFirebaseInstances.getFirebaseFirestore();
        mImageUtil = new ImageUtil();

        mVendorDishModel = new VendorDishModel();
//        getPackList();

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

    private void createPackItem() {
        if (mImageSelected && EmptyString(mName) && EmptyString(mDesc) ) {
            mVendorDishModel.setName(mName.getText().toString());
            mVendorDishModel.setDescription(mDesc.getText().toString());
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMax(100);
            mProgressDialog.setMessage("Create dish and adding to package....");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.show();
            UploadImageToFirebase(ImageUri, "dishes_main");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frame_layout:
                AlertDialog vBuilder = new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setIcon(R.drawable.ic_photo_library_blue_24dp)
                        .setMessage("SELECT IMAGE FROM YOUR DEVICE.")
                        .setTitle("UPLOAD IMAGE")
                        .setPositiveButton("OPEN GALLERY", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mImageUtil.pickFromGallery(VendorAddDish.this);
                            }
                        }).create();
                vBuilder.show();

                break;
            case R.id.item_create_btn:
                AlertDialog vUpdateDialog = new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setIcon(R.drawable.ic_lunch)
                        .setMessage("ARE YOU SURE YOU WANT TO ADD THIS DISH TO MENU??")
                        .setTitle("ADD DISH ")
                        .setPositiveButton("Yes,Sure.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                createPackItem();
                            }
                        }).create();
                vUpdateDialog.show();
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
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
       // Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show();
        final StorageReference ref = mFirebaseStorage.getReference().child("package_item_image/" + image);
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
                        String imageId = downloadUri.toString();
                        addItemToFireStore(CollectionName, imageId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });
    }

    private void addItemToFireStore(String CollectionName, String imageId) {
        mVendorDishModel.setImage(imageId);
//        if (mAddDishPackHelpAdapter != null) {
//            mVendorDishModel.setPacklist(mAddDishPackHelpAdapter.getSelectedPacks());
//        } else {
//           // Toast.makeText(this, "Adapter NULL", Toast.LENGTH_SHORT).show();
//        }
        mFirebaseFirestore.collection(CollectionName)
                .document(mVendorDishModel.getName())
                .set(mVendorDishModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> pTask) {
                        mProgressDialog.dismiss();
                        finish();
                    }
                });
    }

//    private void getPackList() {
//        mFirebaseFirestore.collection("packages").orderBy("name").get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot pQueryDocumentSnapshots) {
//                        final List<PackageModel> pack = new ArrayList<>();
//                        for (QueryDocumentSnapshot doc : pQueryDocumentSnapshots) {
//                            pack.add(doc.toObject(PackageModel.class));
//                        }
//                        mAddDishPackHelpAdapter = new AddDishPackHelpAdapter(VendorAddDish.this,
//                                R.layout.list_item_checkbox_style, pack);
//                        mListView.setAdapter(mAddDishPackHelpAdapter);
//                    }
//                });
//    }
}
