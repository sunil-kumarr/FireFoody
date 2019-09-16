package com.firefoody.Adapters;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.firefoody.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class TodayViewHolder extends RecyclerView.ViewHolder {
    CheckBox mCheckBox;
    TextView mTextView,typeName;
    TodayViewHolder(@NonNull View itemView) {
        super(itemView);

        mCheckBox = itemView.findViewById(R.id.pack_item_type);

        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v,getAdapterPosition());
            }
        });
        mCheckBox.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }

    TodayViewHolder.ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(TodayViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}