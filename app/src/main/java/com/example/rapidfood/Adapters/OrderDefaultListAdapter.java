package com.example.rapidfood.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rapidfood.Models.SubscribedUserModel;
import com.example.rapidfood.R;
import com.example.rapidfood.VendorActivities.VendorDefaultOrderActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.card.MaterialCardView;

public class OrderDefaultListAdapter extends FirestoreRecyclerAdapter<SubscribedUserModel, OrderDefaultListAdapter.SubscriberViewHolder> {

    private RecyclerView mRecyclerview;
    private Context mContext;
     private UserDateListener mListener;

    public OrderDefaultListAdapter(  @NonNull FirestoreRecyclerOptions<SubscribedUserModel> options,
                                     RecyclerView pRecylerView, Context pContext) {
        super(options);
        mContext = pContext;
        mRecyclerview = pRecylerView;

        mListener=(VendorDefaultOrderActivity) pContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull final SubscriberViewHolder pSubscriptionViewHolder,
                                    int pI, @NonNull final SubscribedUserModel pSubscriptionModel) {

        Toast.makeText(mContext, ""+pSubscriptionModel.getUid(), Toast.LENGTH_SHORT).show();
            pSubscriptionViewHolder.mUserMobile.setText(pSubscriptionModel.getMobile());
            pSubscriptionViewHolder.mSubscriptionType.setText(pSubscriptionModel.getSubscriptionType());
            if(pSubscriptionModel.getAddress_first()!=null) {
                pSubscriptionViewHolder.mUserAddress.setText(String.valueOf(pSubscriptionModel.getAddress_first().getAddresscomplete()));
                pSubscriptionViewHolder.mUserDeliveryInstruction.setText(String.valueOf(pSubscriptionModel.getAddress_first().getAddressinstructions()));
            }

            pSubscriptionViewHolder.mDateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(mContext, "clicked", Toast.LENGTH_SHORT).show();
                    mListener.getUserDates(pSubscriptionModel.getUid());
                }
            });
    }


    @NonNull
    @Override
    public SubscribedUserModel getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public SubscriberViewHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.default_order_tab_layout, group, false);
        return new OrderDefaultListAdapter.SubscriberViewHolder(view);
    }

    class SubscriberViewHolder extends RecyclerView.ViewHolder {
        private TextView mUserMobile;
        private TextView mUserAddress;
        private TextView mUserDeliveryInstruction;
        private TextView mSubscriptionType;
        private LinearLayout mDateBtn;

        private SubscriberViewHolder(@NonNull View itemView) {
            super(itemView);

            mUserAddress = itemView.findViewById(R.id.default_delivery_Address);
            mUserDeliveryInstruction = itemView.findViewById(R.id.default_delivery_instruction);
            mUserMobile = itemView.findViewById(R.id.defualt_subscriber_mobile);
            mSubscriptionType = itemView.findViewById(R.id.default_subp_type);
            mDateBtn = itemView.findViewById(R.id.getUserDataLayout);

        }

    }
    public interface UserDateListener{
        void getUserDates(String userId);
    }

}

