package com.example.rapidfood.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rapidfood.Models.PackageModel;
import com.example.rapidfood.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShowMenuLunchAdapter extends FirestoreRecyclerAdapter<PackageModel, ShowMenuLunchAdapter.MenuViewHolder> {

    private RecyclerView mRecyclerView;
    public ShowMenuLunchAdapter(@NonNull FirestoreRecyclerOptions<PackageModel> options, RecyclerView pRecyclerView) {
        super(options);
        mRecyclerView=pRecyclerView;
    }
    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_package_layout,parent,false);
        int width = mRecyclerView.getWidth();
        ViewGroup.LayoutParams params = itemView.getLayoutParams();
        params.width = (int) (width * 0.8);
        itemView.setLayoutParams(params);
          return new MenuViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull MenuViewHolder holder, int pI, @NonNull PackageModel vItem) {
        Picasso.get()
                .load(vItem.getImagePackage())
                .fit()
                .into(holder.mView);
    }


    class MenuViewHolder extends RecyclerView.ViewHolder{
        ImageView mView;
        MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView.findViewById(R.id.item_Image_package);

        }
    }
}
