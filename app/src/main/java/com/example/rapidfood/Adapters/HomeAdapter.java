package com.example.rapidfood.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rapidfood.Models.PackageModel;
import com.example.rapidfood.R;
import com.example.rapidfood.User_files.PackageDetailsActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
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
         if(pPackageModel.isBreakfast()){
             pHomeViewHolder.mPackTypeImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_coffee));
         }
         else{
             pHomeViewHolder.mPackTypeImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_lunch));
         }
        Picasso.get()
                .load(pPackageModel.getImage())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(pHomeViewHolder.mPackageImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }
                    @Override
                    public void onError(Exception e) {
                        //Try again online if cache failed
                        Picasso.get()
                                .load(pPackageModel.getImage())
                                .error(R.drawable.ic_undraw_failure)
                                .into(pHomeViewHolder.mPackageImage, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });
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
