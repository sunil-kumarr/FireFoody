package com.rapdfoods.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rapdfoods.Activites.SubscriptionCheckoutActivity;
import com.rapdfoods.Models.SubscriptionModel;
import com.rapdfoods.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
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
    protected void onBindViewHolder(@NonNull final SubscriptionViewHolder pSubscriptionViewHolder,
                                    int pI, @NonNull final SubscriptionModel pSubscriptionModel) {
        Picasso.get()
                .load(pSubscriptionModel.getImagesub())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(pSubscriptionViewHolder.mSubImage, new Callback() {
                    @Override
                    public void onSuccess() {
                    pSubscriptionViewHolder.mSubName.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onError(Exception e) {
                        //Try again online if cache failed
                        Picasso.get()
                                .load(pSubscriptionModel.getImagesub())
                                .error(R.drawable.ic_undraw_failure)
                                .into(pSubscriptionViewHolder.mSubImage, new Callback() {
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

        pSubscriptionViewHolder.mSubName.setText(pSubscriptionModel.getType());

        pSubscriptionViewHolder.mSubPrice.setText(pSubscriptionModel.getPrice());
        pSubscriptionViewHolder.mSubImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext, SubscriptionCheckoutActivity.class);
                i.putExtra("sub_name",pSubscriptionModel.getType());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public SubscriptionViewHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.show_subscription_image, group, false);
        int width = mRecyclerview.getWidth();
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = (int) (width * 0.8);
        view.setLayoutParams(params);
        return new SubscriptionViewHolder(view);
    }

    class SubscriptionViewHolder extends RecyclerView.ViewHolder {
        private ImageView mSubImage;
        private TextView mSubName;
        private TextView mSubPrice;
        SubscriptionViewHolder(View itemView) {
            super(itemView);
            mSubImage= itemView.findViewById(R.id.sub_image);
            mSubName=itemView.findViewById(R.id.sub_name);
            mSubPrice=itemView.findViewById(R.id.sub_total_cost);
        }

    }

}

