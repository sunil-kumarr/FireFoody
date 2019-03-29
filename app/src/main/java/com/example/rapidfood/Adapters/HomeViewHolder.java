package com.example.rapidfood.Adapters;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rapidfood.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeViewHolder extends RecyclerView.ViewHolder {

    ImageView mImageView;
    TextView mName, mDesc, mPrice;

    public HomeViewHolder(@NonNull View itemView) {
        super(itemView);
        mImageView = itemView.findViewById(R.id.item_Image);
        mName = itemView.findViewById(R.id.item_Name);
        mDesc = itemView.findViewById(R.id.item_Desc);
        mPrice = itemView.findViewById(R.id.item_Price);
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
