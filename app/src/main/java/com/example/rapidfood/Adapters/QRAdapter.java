package com.example.rapidfood.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rapidfood.Models.PackageModel;
import com.example.rapidfood.Models.QRorderModel;
import com.example.rapidfood.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class QRAdapter extends FirestoreRecyclerAdapter<PackageModel, QRAdapter.QRViewHolder> {

    private RecyclerView mRecyclerview;
    private Context mContext;
    private qrListener mQrListener;

    public QRAdapter(@NonNull FirestoreRecyclerOptions<PackageModel> options,
                     RecyclerView pRecylerView, qrListener act, Context pContext) {
        super(options);
        mContext = pContext;
        mRecyclerview = pRecylerView;
        mQrListener = act;
    }


    @Override
    protected void onBindViewHolder(@NonNull final QRViewHolder pQRViewHolder,
                                    int pI, @NonNull final PackageModel pPackageModel) {
        pQRViewHolder.mPAckPrice.setText(String.format("Rs.%s", pPackageModel.getPrice()));
        pQRViewHolder.mPAckName.setText(pPackageModel.getName());
        if (pPackageModel.isBreakfast()) {
            pQRViewHolder.mPackTypeImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_coffee));
        } else {
            pQRViewHolder.mPackTypeImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_lunch));
        }
        pQRViewHolder.mGenerateQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pPackageModel != null) {
                    String packname = pPackageModel.getName();
                    String packprice = pPackageModel.getPrice();
                    QRorderModel vModel = new QRorderModel();
                    vModel.setPack_name(packname);
                    vModel.setPack_price(packprice);
                    mQrListener.onCLickQRGen(v, vModel);
                }

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

        private QRViewHolder(View itemView) {
            super(itemView);
            mPackTypeImg = itemView.findViewById(R.id.qr_pack_type_img);
            mGenerateQRCode = itemView.findViewById(R.id.qr_generate_btn);
            mPAckName = itemView.findViewById(R.id.qr_pack_name);
            mPAckPrice = itemView.findViewById(R.id.qr_pack_price);
        }

    }

    public interface qrListener {
        void onCLickQRGen(View v, QRorderModel pQRorderModel);
    }

}

