package com.example.rapidfood.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rapidfood.Models.PackageModel;
import com.example.rapidfood.R;
import com.example.rapidfood.User_files.PackageDetailsActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class HomeAdapter extends FirestoreRecyclerAdapter<PackageModel,HomeViewHolder> {


    private Context mContext;
    public HomeAdapter(@NonNull FirestoreRecyclerOptions<PackageModel> options) {
        super(options);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onBindViewHolder(@NonNull HomeViewHolder pHomeViewHolder, int pI, @NonNull final PackageModel pPackageModel) {
        pHomeViewHolder.mPackageName.setTypeface(mContext.getResources().getFont(R.font.iran_sans_mobile));
        pHomeViewHolder.mPackageName.setText(pPackageModel.getName());
        Picasso.get().load(pPackageModel.getImage())
                .into(pHomeViewHolder.mPackageImage);
        pHomeViewHolder.setOnClickListener(new HomeViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(mContext, ""+position, Toast.LENGTH_SHORT).show();
                Intent i=new Intent(mContext, PackageDetailsActivity.class);
                i.putExtra("package_name",pPackageModel.getName());
                mContext.startActivity(i);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_package_layout,parent,false);
        return new HomeViewHolder(layout);
    }
}
