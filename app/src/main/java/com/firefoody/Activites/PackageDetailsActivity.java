package com.firefoody.Activites;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firefoody.Adapters.PackageDetailShowAdapter;
import com.firefoody.Models.CheckoutOrderDataModel;
import com.firefoody.Models.FoodOffered;
import com.firefoody.Models.PackageContainerModel;
import com.firefoody.Models.PackageModel;
import com.firefoody.Models.VendorDishModel;
import com.firefoody.R;
import com.firefoody.Utils.FirebaseInstances;
import com.firefoody.Utils.GridSpacingItemDecoration;
import com.firefoody.VendorActivities.VendorAddDish;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class PackageDetailsActivity extends AppCompatActivity implements View.OnClickListener , PackageDetailShowAdapter.ShowButtonListener {

    private FirebaseInstances mFirebaseInstances;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mFireAuth;
    private ImageView mPackageImage;
    private TextView mPackageDetails;
    private CircleImageView mPAckTypeImg;
    private RecyclerView mPackageItemRecyclerView;
    private Toolbar mToolbar;
    private FrameLayout mImageContainer;
    private TextView mPackPRice;
    private Context mContext;
    private PackageDetailShowAdapter mShowAdapter;
    private Button mOrderBtn;
    private TextView mPackItemCount;
//    private LinearLayout mLoadingPAge;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.package_details_layout);
        mContext=this;
        mPackageDetails = findViewById(R.id.package_details_text_view);
        mPackageItemRecyclerView = findViewById(R.id.package_details_recycler_view);
        mOrderBtn=findViewById(R.id.order_now_package);
        mPackItemCount=findViewById(R.id.package_detail_item_count);
        mImageContainer=findViewById(R.id.frame_layout_contaienr);
        mPAckTypeImg=findViewById(R.id.pack_detail_type_img);
        mPackPRice=findViewById(R.id.package_total_cost);
        mOrderBtn.setOnClickListener(this);
//        mLoadingPAge=findViewById(R.id.loading_data_page);

        GridLayoutManager vLayoutManager = new GridLayoutManager(this, 2,RecyclerView.VERTICAL,false);
//        vLayoutManager.setAutoMeasureEnabled(false);
        mPackageItemRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        mPackageItemRecyclerView.setLayoutManager(vLayoutManager);
        mPackageItemRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mFirebaseInstances = new FirebaseInstances();
        mFirestore = mFirebaseInstances.getFirebaseFirestore();
        mFireAuth=mFirebaseInstances.getFirebaseAuth();
        mPackageImage = findViewById(R.id.package_image);
        if(mFireAuth.getCurrentUser()!=null){
            mFirestore.collection("subscribed_user")
                    .document(mFireAuth.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            boolean isSubscribed=false;
                            if(task.isSuccessful()){
                                DocumentSnapshot documentSnapshot=task.getResult();
                                assert documentSnapshot != null;
                                if(documentSnapshot.exists()) {
                                    mOrderBtn.setVisibility(View.GONE);
                                    isSubscribed=true;
                                }
                                else{
                                    mOrderBtn.setVisibility(View.VISIBLE);
                                }
                            }
                            else{
                                mOrderBtn.setVisibility(View.VISIBLE);
                            }
                            getPackageDetail(isSubscribed);

                        }
                    });
        }

    }

    void getPackageDetail(boolean isSubscribed) {
        mFirestore.collection("today_menu").document("today_menu")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> pTask) {
                if (pTask.isSuccessful()) {
                    DocumentSnapshot q = pTask.getResult();
                    assert q != null;
                    FoodOffered mFood = q.toObject(FoodOffered.class);
                    PackageModel mPack = mFood.getFoodPack();
                    List<VendorDishModel> mDishList = mFood.getmCurrentMenuList();
                    Picasso.get()
                            .load(mPack.getImage())
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .into(mPackageImage, new Callback() {
                                @Override
                                public void onSuccess() {
//                                    mLoadingPAge.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError(Exception e) {
                                    //Try again online if cache failed
                                    Picasso.get()
                                            .load(mPack.getImage())
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
                    if(mPack.isBreakfast()){
                        mPAckTypeImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_coffee));
                    }
                    else{
                        mPAckTypeImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_lunch));
                    }
                    assert mPack != null;
                    mPackageDetails.setText(mPack.getDescription());
                    mPackItemCount.setText(String.format("**select upto any:%s dish's", mPack.getItem_count()));
                    mPackPRice.setText(mPack.getPrice());
                    int count=0;
                    if(!mDishList.isEmpty())
                        count=Integer.parseInt(String.valueOf(mDishList.size()));
                    mShowAdapter = new PackageDetailShowAdapter(mDishList,PackageDetailsActivity.this,
                            count,mPack.getName(),isSubscribed);
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
                            .setMessage("No Item Selected!Please select atleast 1 item to order.")
                            .setTitle("Empty Food Cart!!")
                            .setIcon(getResources().getDrawable(R.drawable.ic_lunch))
                            .create();
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

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onClickBtn(int s) {
        if(s==0){
           // Toast.makeText(mContext, "size:"+s, Toast.LENGTH_SHORT).show();
            mOrderBtn.setVisibility(View.GONE);
        }
        else if(s==1){
           // Toast.makeText(mContext, "size:"+s, Toast.LENGTH_SHORT).show();
            mOrderBtn.setVisibility(View.VISIBLE);
        }

    }
    public void goBackHome(View view) {
        startActivity(new Intent(PackageDetailsActivity.this, MainActivity.class));
        finish();
    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
