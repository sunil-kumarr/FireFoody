package com.rapdfoods.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.rapdfoods.Models.CheckoutPlaceOrderModel;
import com.rapdfoods.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class DeliverOrderListAdapter extends FirestoreRecyclerAdapter<CheckoutPlaceOrderModel, DeliverOrderListAdapter.ORDERLISTViewHolder> {

    private RecyclerView mRecyclerview;
    private Context mContext;
    private DeliverOrderListener mOrderListener;
    private static final String TAG = "OrderListAdapter";

    public DeliverOrderListAdapter(@NonNull FirestoreRecyclerOptions<CheckoutPlaceOrderModel> options,
                                   RecyclerView pRecylerView, Context pContext) {
        super(options);
        Log.d(TAG, "OrderListAdapter: ");
        mContext = pContext;
        mRecyclerview = pRecylerView;
        mOrderListener = (DeliverOrderListener) pContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ORDERLISTViewHolder pSubscriptionViewHolder,
                                    int pI, @NonNull final CheckoutPlaceOrderModel pCheckoutPlaceOrderModel) {

        pSubscriptionViewHolder.transactionId.setVisibility(View.GONE);
        Log.d(TAG, "onBindViewHolder: " + pCheckoutPlaceOrderModel.getPackageordered());
        pSubscriptionViewHolder.paymentStatus.setText(pCheckoutPlaceOrderModel.getPaymentstatus());
        pSubscriptionViewHolder.transactiontime.setText(pCheckoutPlaceOrderModel.getOrdertimestamp().toString());
        pSubscriptionViewHolder.userMobile.setText(pCheckoutPlaceOrderModel.getMobile());
        pSubscriptionViewHolder.mOrderName.setText(pCheckoutPlaceOrderModel.getPackageordered());
        pSubscriptionViewHolder.mOrderCost.setText(pCheckoutPlaceOrderModel.getPackageprice());
        pSubscriptionViewHolder.mOrderDeliveryAddress.setText(pCheckoutPlaceOrderModel.getDeliveryaddress());
        pSubscriptionViewHolder.mPaymentMethod.setText(pCheckoutPlaceOrderModel.getPaymentmethod());
        pSubscriptionViewHolder.orderConfirmStatus.setText(pCheckoutPlaceOrderModel.getOrderStatus());
        pSubscriptionViewHolder.mDeliverStatus.setText(pCheckoutPlaceOrderModel.getDeliverystatus());
        if(pCheckoutPlaceOrderModel.isCustom()) {
            pSubscriptionViewHolder.mOrderDetails.setText(String.valueOf(pCheckoutPlaceOrderModel.getSelecteditems()));
        }
        else{
            pSubscriptionViewHolder.mOrderDetails.setText("Default Order");
        }
        if (!pCheckoutPlaceOrderModel.getDeliverystatus().equals("pending")) {
            pSubscriptionViewHolder.confirmBTN.setVisibility(View.GONE);
        } else {
            pSubscriptionViewHolder.confirmBTN.setVisibility(View.VISIBLE);
            pSubscriptionViewHolder.confirmBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog vDialog = new AlertDialog.Builder(mContext)
                            .setTitle("CONFIRM DELIVERY")
                            .setIcon(R.drawable.ic_check_circlce_button)
                            .setMessage("Are you sure you want to confirm Delivery?")
                            .setPositiveButton("CONFIRM Delivery", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mOrderListener.onClickVerify(v, pCheckoutPlaceOrderModel);
                                }
                            })
                            .setNegativeButton("CANCEL Delivery", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mOrderListener.onClickFailed(v, pCheckoutPlaceOrderModel);
                                }
                            })
                            .create();
                    vDialog.show();
                }
            });
        }


    }

    @NonNull
    @Override
    public CheckoutPlaceOrderModel getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ORDERLISTViewHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.vendor_show_order_layout, group, false);
        return new ORDERLISTViewHolder(view);
    }

    class ORDERLISTViewHolder extends RecyclerView.ViewHolder {
        private TextView mOrderName;
        private TextView mOrderCost;
        private LinearLayout transactionId;
        private TextView transactiontime;
        private TextView userMobile;
        private TextView paymentStatus;
        private TextView mOrderDeliveryAddress;
        private TextView mOrderDetails;
        private TextView mPaymentMethod;
        private Button confirmBTN;
        private TextView orderConfirmStatus;
        private TextView mDeliverStatus;

        private ORDERLISTViewHolder(@NonNull View itemView) {
            super(itemView);
            confirmBTN = itemView.findViewById(R.id.order_btn_confirm);
            orderConfirmStatus = itemView.findViewById(R.id.order_confirm_Status);
            userMobile = itemView.findViewById(R.id.order_cust_mob);
            mOrderName = itemView.findViewById(R.id.order_pack_name);
            mOrderCost = itemView.findViewById(R.id.order_cost);
            mPaymentMethod = itemView.findViewById(R.id.order_payment_method);
            transactionId = itemView.findViewById(R.id.trans_id_Container);
            transactiontime = itemView.findViewById(R.id.order_Time);
            paymentStatus = itemView.findViewById(R.id.order_payment_Status);
            mOrderDeliveryAddress = itemView.findViewById(R.id.order_delivery_Address);
            mOrderDetails = itemView.findViewById(R.id.order_details);
            mDeliverStatus=itemView.findViewById(R.id.order_delivery_Status);
        }
    }

    public interface DeliverOrderListener {
        void onClickVerify(View pView, CheckoutPlaceOrderModel pCheckoutPlaceOrderModel);

        void onClickFailed(View pView, CheckoutPlaceOrderModel pCheckoutPlaceOrderModel);
    }

}

