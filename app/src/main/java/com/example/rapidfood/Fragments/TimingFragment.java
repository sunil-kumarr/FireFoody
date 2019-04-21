package com.example.rapidfood.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;

import com.applikeysolutions.cosmocalendar.settings.lists.DisabledDaysCriteria;

import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skyhope.eventcalenderlibrary.CalenderEvent;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TimingFragment extends Fragment {

    private FirebaseInstances mFirebaseInstances;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseAuth mFirebaseAuth;
    private Context mContext;
    private ImageView mNotSubscribedLayout;
    private CalenderEvent mTimeSheetCalendar;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNotSubscribedLayout=view.findViewById(R.id.not_subscribed);

        mFirebaseInstances = new FirebaseInstances();
        mFirebaseAuth = mFirebaseInstances.getFirebaseAuth();
        mFirebaseFirestore = mFirebaseInstances.getFirebaseFirestore();

    }

    @Override
    public void onStart() {
        super.onStart();
//        if (mFirebaseAuth.getCurrentUser() != null) {
//            mFirebaseFirestore.collection("subscribed_user")
//                    .document(mFirebaseAuth.getCurrentUser().getUid())
//                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> pTask) {
//                        if(pTask.isSuccessful()){
//                            if(pTask.getResult().exists()){
//                                mNotSubscribedLayout.setVisibility(View.GONE);
//                            }
//                            else {
//                                mNotSubscribedLayout.setVisibility(View.VISIBLE);
//                            }
//                        }
//                        else {
//                            mNotSubscribedLayout.setVisibility(View.VISIBLE);
//                        }
//                }
//            });
//
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Map<String, Object> mp = new HashMap<>();
        mp.put("timestamp", FieldValue.serverTimestamp());
        mFirebaseFirestore.collection("company_data").document("timestamp").update(mp);
    }
}
