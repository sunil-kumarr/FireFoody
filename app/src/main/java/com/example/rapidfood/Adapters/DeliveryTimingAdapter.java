package com.example.rapidfood.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rapidfood.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DeliveryTimingAdapter extends RecyclerView.Adapter<DeliveryTimingAdapter.TimingHolder> {


    @NonNull
    @Override
    public TimingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.timing_bar_layout,parent,false);
        return new TimingHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TimingHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class TimingHolder extends RecyclerView.ViewHolder{

        public TimingHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
