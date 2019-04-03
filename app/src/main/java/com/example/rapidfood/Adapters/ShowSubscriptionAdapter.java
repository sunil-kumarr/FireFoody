package com.example.rapidfood.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.rapidfood.Models.SubscriptionModel;
import com.example.rapidfood.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShowSubscriptionAdapter extends FirestoreRecyclerAdapter<SubscriptionModel, ShowSubscriptionAdapter.SubscriptionViewHolder> {

    private RecyclerView mRecyclerview;
    private Context mContext;

    public ShowSubscriptionAdapter(@NonNull FirestoreRecyclerOptions<SubscriptionModel> options,
                               RecyclerView pRecylerView,Context pContext) {
        super(options);
        mContext=pContext;
        mRecyclerview=pRecylerView;

    }


    @Override
    protected void onBindViewHolder(@NonNull SubscriptionViewHolder pSubscriptionViewHolder,
                                    int pI, @NonNull SubscriptionModel pSubscriptionModel) {
        Picasso.get()
                .load(pSubscriptionModel.getImagesub())
                .into(pSubscriptionViewHolder.mSubImage);
    }

    @Override
    public SubscriptionViewHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.subscription_image, group, false);
        int width = mRecyclerview.getWidth();
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = (int) (width * 0.8);
        view.setLayoutParams(params);
        return new SubscriptionViewHolder(view);
    }

    class SubscriptionViewHolder extends RecyclerView.ViewHolder {
        private ImageView mSubImage;
        public SubscriptionViewHolder(View itemView) {
            super(itemView);
            mSubImage= itemView.findViewById(R.id.sub_image);
        }
    }

}

