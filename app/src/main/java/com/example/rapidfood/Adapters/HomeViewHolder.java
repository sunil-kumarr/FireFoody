package com.example.rapidfood.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rapidfood.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeViewHolder extends RecyclerView.ViewHolder {

    ImageView mPackageImage;
    TextView mPackageName;
    ImageView mPackTypeImg;

    HomeViewHolder(@NonNull View itemView) {
        super(itemView);
        mPackageImage = itemView.findViewById(R.id.home_pack_image);
        mPackageName=itemView.findViewById(R.id.home_pack_name);
        mPackTypeImg=itemView.findViewById(R.id.package_type_image);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v,getAdapterPosition());
            }
        });
    }

    HomeViewHolder.ClickListener mClickListener;

    public interface ClickListener {

        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);

    }

    public void setOnClickListener(HomeViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}
