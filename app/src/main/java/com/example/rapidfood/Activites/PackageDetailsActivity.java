package com.example.rapidfood.Activites;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rapidfood.Adapters.PackageDetailShowAdapter;
import com.example.rapidfood.Models.CheckoutOrderDataModel;
import com.example.rapidfood.Models.PackageContainerModel;
import com.example.rapidfood.Models.VendorDishModel;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PackageDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseInstances mFirebaseInstances;
    private FirebaseFirestore mFirestore;
    private ImageView mPackageImage;
    private TextView mPackageDetails;
    private ImageView mPAckTypeImg;
    private RecyclerView mPackageItemRecyclerView;
    private Toolbar mToolbar;
    private FrameLayout mImageContainer;
    private Context mContext;
    private PackageDetailShowAdapter mShowAdapter;
    private Button mOrderBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.package_details_layout);
        mContext=this;
        mPackageDetails = findViewById(R.id.package_details_text_view);
        mPackageItemRecyclerView = findViewById(R.id.package_details_recycler_view);
        mOrderBtn=findViewById(R.id.order_now_package);
        mImageContainer=findViewById(R.id.frame_layout_contaienr);
        mPAckTypeImg=findViewById(R.id.pack_detail_type_img);
        mOrderBtn.setOnClickListener(this);

        LinearLayoutManager vLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        vLayoutManager.setAutoMeasureEnabled(false);
        mPackageItemRecyclerView.setLayoutManager(vLayoutManager);
        mPackageItemRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mFirebaseInstances = new FirebaseInstances();
        mFirestore = mFirebaseInstances.getFirebaseFirestore();
        mPackageImage = findViewById(R.id.package_image);

        String s = getIntent().getStringExtra("package_name");
        getPackageDetail(s);
        Toast.makeText(this, "" + s, Toast.LENGTH_SHORT).show();
    }

    void getPackageDetail(String package_name) {
        mFirestore.collection("today_menu").document(package_name).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> pTask) {
                if (pTask.isSuccessful()) {
                    DocumentSnapshot q = pTask.getResult();
                    assert q != null;
                    PackageContainerModel pack = q.toObject(PackageContainerModel.class);
                    Picasso.get()
                            .load(q.getString("image"))
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .into(mPackageImage, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {
                                    //Try again online if cache failed
                                    Picasso.get()
                                            .load(q.getString("image"))
                                            .error(R.drawable.ic_undraw_failure)
                                            .into(mPackageImage, new Callback() {
                                                @Override
                                                public void onSuccess() {
                                                }

                                                @Override
                                                public void onError(Exception e) {
                                                    Log.v("Picasso", "Could not fetch image");
                                                }
                                            });
                                }
                            });
                    if(pack.isBreakfast()){
                        mPAckTypeImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_coffee));
                    }
                    else{
                        mPAckTypeImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_lunch));
                    }
                    assert pack != null;
                    mPackageDetails.setText(pack.getDescription());
                    List<VendorDishModel> items=pack.getDishlist();
                    int count=0;
                    try {
                        count=Integer.parseInt(pack.getItem_count());
                    }
                    catch (Exception e){

                    }
                    mShowAdapter = new PackageDetailShowAdapter(items, PackageDetailsActivity.this,
                            count,pack.getName());
                    mPackageItemRecyclerView.setAdapter(mShowAdapter);
                } else {
                    Toast.makeText(PackageDetailsActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.order_now_package:

                List<String> vList=mShowAdapter.getSelectedItems();
                CheckoutOrderDataModel vModel=new CheckoutOrderDataModel();
                vModel.setOrderDishlist(vList);
                vModel.setPackageName(mShowAdapter.getPackName());
                if(vList.size()==0){
                    AlertDialog vDialog=new AlertDialog.Builder(this)
                            .setMessage("No Dish selected,order default Menu!")
                            .setTitle("Order Default Menu")
                            .setIcon(getResources().getDrawable(R.drawable.ic_lunch))
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent vIntent=new Intent(PackageDetailsActivity.this, CheckoutActivity.class);
                                    vIntent.putExtra("orderdata",vModel);
                                    startActivity(vIntent);
                                }
                            }).create();
                    vDialog.show();
                }
                else{
                    Intent vIntent=new Intent(PackageDetailsActivity.this, CheckoutActivity.class);
                    vIntent.putExtra("orderdata",vModel);
                    startActivity(vIntent);
                }
                break;
        }
    }
}