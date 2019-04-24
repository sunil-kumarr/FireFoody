package com.example.rapidfood.Activites;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AboutUsActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        TextView vTextView=findViewById(R.id.about_us_text);
        FirebaseInstances vFirebaseInstances=new FirebaseInstances();
        FirebaseFirestore vFirebaseFirestore=vFirebaseInstances.getFirebaseFirestore();
        vFirebaseFirestore.collection("company_data")
                .document("about")
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot pDocumentSnapshot) {
                if(pDocumentSnapshot.exists()){
                    vTextView.setText(pDocumentSnapshot.getString("about"));
                }
            }
        });

    }
}
