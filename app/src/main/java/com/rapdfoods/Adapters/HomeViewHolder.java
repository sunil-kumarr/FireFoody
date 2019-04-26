package com.rapdfoods.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rapdfoods.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeViewHolder extends RecyclerView.ViewHolder {

    ImageView mPackageImage;
    TextView mPackageName;
    ImageView mPackTypeImg;
    TextView mPackCost;

    HomeViewHolder(@NonNull View itemView) {
        super(itemView);
        mPackageImage = itemView.findViewById(R.id.home_pack_image);
        mPackageName=itemView.findViewById(R.id.home_pack_name);
        mPackTypeImg=itemView.findViewById(R.id.package_type_image);
        mPackCost=itemView.findViewById(R.id.package_total_cost);
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

    }

    public void setOnClickListener(HomeViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}
