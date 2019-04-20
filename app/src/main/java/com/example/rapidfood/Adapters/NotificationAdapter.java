package com.example.rapidfood.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rapidfood.Activites.GooglePayActivity;
import com.example.rapidfood.Models.NotificationModel;
import com.example.rapidfood.Models.SubscriptionModel;
import com.example.rapidfood.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationAdapter extends FirestoreRecyclerAdapter<NotificationModel, NotificationAdapter.NoteHolder> {

    private RecyclerView mRecyclerview;
    private Context mContext;

    public NotificationAdapter(@NonNull FirestoreRecyclerOptions<NotificationModel> options,
                               RecyclerView pRecylerView, Context pContext) {
        super(options);
        mContext=pContext;
        mRecyclerview=pRecylerView;

    }


    @Override
    protected void onBindViewHolder(@NonNull final NoteHolder pNoteHolder,
                                    int pI, @NonNull final NotificationModel pNotificationModel) {
       pNoteHolder.mNoteTitle.setText(pNotificationModel.getTitle());
       if(pNotificationModel.getNote_type().equals("subscription")){
           if(pNotificationModel.isStatus()) {
               pNoteHolder.mNoteImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_undraw_subscriber));
           }
           else{
               pNoteHolder.mNoteImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_undraw_failure));
           }
       }
       else if(pNotificationModel.getNote_type().equals("order")){
           if(pNotificationModel.isStatus()) {
               pNoteHolder.mNoteImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_undraw_confirmation));
           }
           else{
               pNoteHolder.mNoteImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_undraw_failure));
           }
       }

    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.layout_notification_tab, group, false);
        return new NoteHolder(view);
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        private ImageView mNoteImage;
        private TextView mNoteTitle;
        NoteHolder(View itemView) {
            super(itemView);
            mNoteImage= itemView.findViewById(R.id.notify_image);
            mNoteTitle=itemView.findViewById(R.id.notification_title);
        }
    }

}

