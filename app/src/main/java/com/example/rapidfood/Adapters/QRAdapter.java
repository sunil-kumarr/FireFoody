package com.example.rapidfood.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rapidfood.Activites.GooglePayActivity;
import com.example.rapidfood.Models.PackageModel;
import com.example.rapidfood.Models.SubscriptionModel;
import com.example.rapidfood.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class QRAdapter extends FirestoreRecyclerAdapter<PackageModel, QRAdapter.QRViewHolder> {

    private RecyclerView mRecyclerview;
    private Context mContext;

    public QRAdapter(@NonNull FirestoreRecyclerOptions<PackageModel> options,
                     RecyclerView pRecylerView, Context pContext) {
        super(options);
        mContext=pContext;
        mRecyclerview=pRecylerView;

    }


    @Override
    protected void onBindViewHolder(@NonNull final QRViewHolder pQRViewHolder,
                                    int pI, @NonNull final PackageModel pPackageModel) {
        pQRViewHolder.mPAckPrice.setText(String.format("Rs.%s", pPackageModel.getPrice()));
        pQRViewHolder.mPAckName.setText(pPackageModel.getName());
        if(pPackageModel.isBreakfast()){
            pQRViewHolder.mPackTypeImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_coffee));
        }
        else{
            pQRViewHolder.mPackTypeImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_lunch));
        }
        pQRViewHolder.mGenerateQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Genrating", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public QRViewHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.layout_qr_code_tab, group, false);
        return new QRViewHolder(view);
    }

    class QRViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView mPackTypeImg;
        private TextView mPAckName;
        private TextView mPAckPrice;
        private Button mGenerateQRCode;
        private
        QRViewHolder(View itemView) {
            super(itemView);
            mPackTypeImg= itemView.findViewById(R.id.qr_pack_type_img);
            mGenerateQRCode=itemView.findViewById(R.id.qr_generate_btn);
            mPAckName=itemView.findViewById(R.id.qr_pack_name);
            mPAckPrice=itemView.findViewById(R.id.qr_pack_price);
        }
//        void showBottomSheetDialog() {
//            View view = LayoutInflater.from(mContext).inflate(R.layout.subscription_payment_bottom_sheet,null);
//            BottomSheetDialog dialog = new BottomSheetDialog(mContext);
//            dialog.setContentView(view);
//            dialog.show();
//        }
    }

}

