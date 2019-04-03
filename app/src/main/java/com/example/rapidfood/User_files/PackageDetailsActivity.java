package com.example.rapidfood.User_files;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rapidfood.Adapters.PackageDetailShowAdapter;
import com.example.rapidfood.Models.PackageModel;
import com.example.rapidfood.Models.VendorDishModel;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PackageDetailsActivity extends AppCompatActivity {

    private FirebaseInstances mFirebaseInstances;
    private FirebaseFirestore mFirestore;
    private ImageView mPackageImage;
    private TextView mPackageDetails;
    private RecyclerView mPackageItemRecyclerView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.package_details_layout);

        mPackageDetails = findViewById(R.id.package_details_text_view);
        mPackageItemRecyclerView = findViewById(R.id.package_details_recycler_view);
        LinearLayoutManager vLayoutManager =new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
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
                    PackageModel pack = q.toObject(PackageModel.class);
                    Picasso.get().load(q.getString("image")).fit().into(mPackageImage);
                    List<VendorDishModel> items = pack.getDishlist();
                    mPackageDetails.setText(pack.getDescription());
                    PackageDetailShowAdapter vShowAdapter = new PackageDetailShowAdapter(items, PackageDetailsActivity.this);
                    mPackageItemRecyclerView.setAdapter(vShowAdapter);
                } else {
                    Toast.makeText(PackageDetailsActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
