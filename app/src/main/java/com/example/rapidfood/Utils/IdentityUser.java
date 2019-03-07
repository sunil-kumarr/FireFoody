package com.example.rapidfood.Utils;

import android.util.Log;

import com.example.rapidfood.Activites.Authentication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class IdentityUser {
    private FirebaseInstances mFirebaseInstances;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirebaseFirestore;
    static final String TAG = "IdentityUser";
    private List<String> mUsers;
    private   boolean user;


   public IdentityUser(){
        mFirebaseInstances=new FirebaseInstances();
        mFirebaseAuth=mFirebaseInstances.getFirebaseAuth();
        mFirebaseFirestore=mFirebaseInstances.getFirebaseFirestore();
    }

    public  void userIsVendor(final String userId){
        final boolean[] matched = {false};

    }
    public boolean getUserType(){
       return user;
    }
    public List<String> getUserDetails(String userId){
       mUsers=new ArrayList<>();
        mFirebaseFirestore.collection("users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d("DownloadData", "Listen failed.", e);
                            return;
                        }
                        assert value != null;
                        for (QueryDocumentSnapshot doc : value) {

                        }
                    }
                });
       return mUsers;
    }

}
