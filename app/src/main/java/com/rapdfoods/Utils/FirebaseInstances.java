package com.rapdfoods.Utils;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class FirebaseInstances {
    private static final String TAG = "FirebaseInstances";
    private   FirebaseAuth mFirebaseAuth;
    private   FirebaseFirestore mFirebaseFirestore;
    private   FirebaseStorage mFirebaseStorage;



     public FirebaseFirestore getFirebaseFirestore(){
        if(mFirebaseFirestore==null){
            Log.d(TAG,"Instances init");
            mFirebaseFirestore=FirebaseFirestore.getInstance();
        }
        return mFirebaseFirestore;
    }

    public FirebaseStorage getFirebaseStorage() {
         if(mFirebaseStorage==null){
             Log.d(TAG,"Instances Firestorage");
             mFirebaseStorage=FirebaseStorage.getInstance();
         }

        return mFirebaseStorage;
    }

     public  FirebaseAuth getFirebaseAuth(){
        if(mFirebaseAuth==null){
            Log.d(TAG,"Instances Auth");
            mFirebaseAuth=FirebaseAuth.getInstance();
        }
        return mFirebaseAuth;
    }



}
