package com.example.rapidfood.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rapidfood.Adapters.NotificationAdapter;
import com.example.rapidfood.Adapters.OrderListAdapter;
import com.example.rapidfood.Models.CheckoutPlaceOrderModel;
import com.example.rapidfood.Models.NotificationModel;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationFragment  extends Fragment {

    private RecyclerView mNotifyRecyclerView;
    private Context mContext;
    private FirestoreRecyclerOptions<NotificationModel> mOptions;
    private FirebaseInstances mFirebaseInstances;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseAuth mFirebaseAuth;
    private FirestoreRecyclerAdapter mNoteAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_notification,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNotifyRecyclerView=view.findViewById(R.id.notification_recycler_view);
        mFirebaseInstances = new FirebaseInstances();
        mFirebaseFirestore = mFirebaseInstances.getFirebaseFirestore();
        mFirebaseAuth=mFirebaseInstances.getFirebaseAuth();

        mNotifyRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mNotifyRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        if(mFirebaseAuth.getCurrentUser()!=null){
        Query query = mFirebaseFirestore
                .collection("users")
                .document(mFirebaseAuth.getCurrentUser().getUid())
                .collection("notifications")
                .orderBy("timestamp", Query.Direction.DESCENDING);
               // .orderBy("timestamp", Query.Direction.DESCENDING);
        mOptions = new FirestoreRecyclerOptions.Builder<NotificationModel>()
                .setQuery(query, NotificationModel.class).build();
        mNoteAdapter = new NotificationAdapter(mOptions, mNotifyRecyclerView, mContext);
        mNotifyRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mNotifyRecyclerView.setAdapter(mNoteAdapter);
                mNotifyRecyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mNoteAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mNoteAdapter.stopListening();
    }
}
