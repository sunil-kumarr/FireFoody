package com.example.rapidfood.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rapidfood.Models.SubscribedUserModel;
import com.example.rapidfood.Models.SubscriptionTransactionModel;
import com.example.rapidfood.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SubscriberListAdapter extends FirestoreRecyclerAdapter<SubscribedUserModel, SubscriberListAdapter.SubscriberViewHolder> {

    private RecyclerView mRecyclerview;
    private Context mContext;

    public SubscriberListAdapter(@NonNull FirestoreRecyclerOptions<SubscribedUserModel> options,
                                 RecyclerView pRecylerView, Context pContext) {
        super(options);
        mContext = pContext;
        mRecyclerview = pRecylerView;

    }

    @Override
    protected void onBindViewHolder(@NonNull final SubscriberViewHolder pSubscriptionViewHolder,
                                    int pI, @NonNull final SubscribedUserModel pSubscriptionModel) {

        pSubscriptionViewHolder.googleStatus.setText("SUCCESS");
        pSubscriptionViewHolder.transactiontime.setText(pSubscriptionModel.getStart_date().toString());
        pSubscriptionViewHolder.transactionId.setText(pSubscriptionModel.getTrans_id());
        pSubscriptionViewHolder.userMobile.setText(pSubscriptionModel.getMobile());
        pSubscriptionViewHolder.mSubName.setText(pSubscriptionModel.getSubscriptionType());

    }


    @NonNull
    @Override
    public SubscribedUserModel getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public SubscriberViewHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.subscribed_user_list_item, group, false);
        return new SubscriberListAdapter.SubscriberViewHolder(view);
    }

    class SubscriberViewHolder extends RecyclerView.ViewHolder {
        private TextView mSubName;
        private TextView transactionId;
        private TextView transactiontime;
        private TextView userMobile;
        private TextView googleStatus;

        private SubscriberViewHolder(@NonNull View itemView) {
            super(itemView);

            userMobile = itemView.findViewById(R.id.subscriber_mobile);
            mSubName = itemView.findViewById(R.id.subscriber_type);
            transactionId = itemView.findViewById(R.id.subscriber_trans_id);
            transactiontime = itemView.findViewById(R.id.subscriber_date);
            googleStatus = itemView.findViewById(R.id.subscriber_google_Status);

        }

    }

}

