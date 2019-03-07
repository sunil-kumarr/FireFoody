package com.example.rapidfood.Utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseInstances {
    private  FirebaseAuth mFirebaseAuth;
    private  FirebaseFirestore mFirebaseFirestore;
    private FirebaseStorage mFirebaseStorage;



     public FirebaseFirestore getFirebaseFirestore(){
        if(mFirebaseFirestore==null){
            mFirebaseFirestore=FirebaseFirestore.getInstance();
        }
        return mFirebaseFirestore;
    }

    public FirebaseStorage getFirebaseStorage() {
         if(mFirebaseStorage==null){
             mFirebaseStorage=FirebaseStorage.getInstance();
         }

        return mFirebaseStorage;
    }

     public  FirebaseAuth getFirebaseAuth(){
        if(mFirebaseAuth==null){
            mFirebaseAuth=FirebaseAuth.getInstance();
        }
        return mFirebaseAuth;
    }



}
