package com.example.rapidfood.Adapters;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.rapidfood.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class TodayViewHolder extends RecyclerView.ViewHolder {
    CheckBox mCheckBox;
    TextView mTextView,typeName;
    TodayViewHolder(@NonNull View itemView) {
        super(itemView);
        mTextView=itemView.findViewById(R.id.item_price_today);
        mCheckBox = itemView.findViewById(R.id.pack_item_type);
        typeName =itemView.findViewById(R.id.item_type_name);
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